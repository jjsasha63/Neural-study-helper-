package com.red.network;

import com.red.processing.Additional_func;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Network--config--train

public class Network extends CreatePrep_file {
    private static final int PROPERTIES_NUMBER = 5;
    private static final int HIDDEN_SIZE = 10;
    private static final int HIDDEN_LAYER_SIZE = 10;
    private static final int ITERATIONS = 10000;
    private static final int OUTPUT_SIZE = 2;

    private static final String HOME_PATH = "\\src\\main\\resources\\";


    static HashMap<String, List<Double>> results = new HashMap<>();


    public static void main(String[] args) throws IOException, InterruptedException {
        WeightInit [] weightInits = {WeightInit.DISTRIBUTION, WeightInit.ZERO, WeightInit.SIGMOID_UNIFORM,
                WeightInit.UNIFORM, WeightInit.XAVIER, WeightInit.XAVIER_UNIFORM, WeightInit.XAVIER_FAN_IN,
                WeightInit.XAVIER_LEGACY, WeightInit.RELU, WeightInit.RELU_UNIFORM};

        String [] activations = {"CUBE", "ELU", "HARDSIGMOID", "HARDTANH", "IDENTITY", "LEAKYRELU",
                "RATIONALTANH", "RELU", "RRELU", "SIGMOID", "SOFTMAX", "SOFTPLUS", "SOFTSIGN",
                "TANH", "RECTIFIEDTANH", "SELU"};

        for (String activation : activations){
            for (WeightInit weightInit : weightInits){
                long startTime = System.currentTimeMillis();
                runNetwork(activation, weightInit);
                long estimatedTime = System.currentTimeMillis() - startTime;
                double minTime = (estimatedTime / 1000 ) / 60;
                System.out.println("===================\n"+estimatedTime+"\n=================");
            }
        }
        writeResultsToFile(results);

    }

    private static void writeResultsToFile (HashMap<String, List<Double>> input){
        StringBuilder sb = new StringBuilder();
        sb.append("Activation,WeightInit,F1,precision,recall\n");
        for (String key : input.keySet()){
            String [] temp = key.split("  ");
            sb.append(temp[0]+","+temp[1]+",");
            for (Double aDouble : input.get(key)){
                sb.append(aDouble+",");
            }
            sb.append("\n");
        }
        Additional_func additionalfunc = new Additional_func();
        additionalfunc.write_file_byte(HOME_PATH+"stats.csv", sb.toString());
    }

    public static void runNetwork (String activation, WeightInit weightInit) throws IOException, InterruptedException {
        int batchSize = 1000;
        final String filenameTrain  = new ClassPathResource("trainSet.csv").getFile().getPath();
        final String filenameTest  = new ClassPathResource("testSet.csv").getFile().getPath();
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator trainIter = new RecordReaderDataSetIterator(rr,batchSize,0,OUTPUT_SIZE);
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(filenameTest)));
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest,batchSize,0,+OUTPUT_SIZE);
        MultiLayerNetwork net = new MultiLayerNetwork(getRecurrentConf(activation, weightInit));
        net.init();
        net.setListeners(new ScoreIterationListener(ITERATIONS / 10));
       //get layer info
        Layer[] layers = net.getLayers();
        int totalNumParams = 0;
        for (int i = 0; i < layers.length; i++) {
            int nParams = layers[i].numParams();
            System.out.println("Number of parameters in layer " + i + ": " + nParams);
            totalNumParams += nParams;
        }
        System.out.println("Total number of network parameters: " + totalNumParams);
        net.fit(trainIter); // train init
        System.out.println("Evaluate model....");
        Evaluation eval = new Evaluation(2);
        while(testIter.hasNext()){ //hold info for eval
            DataSet t = testIter.next();
            INDArray features = t.getFeatureMatrix();
            INDArray lables = t.getLabels();
            INDArray predicted = net.output(features,false);
            eval.eval(lables, predicted); // eval
        }
        System.out.println(eval.stats());
        Additional_func additionalfunc = new Additional_func();
        additionalfunc.write_file_byte(HOME_PATH+"model/stats/"+weightInit.name()+"_"+activation+"_RNN.txt", eval.stats());
        if (eval.f1() > 0.8){
            List<Double> temp = new ArrayList<>();
            temp.add(eval.f1());
            temp.add(eval.precision());
            temp.add(eval.recall());
            results.put(activation+"  "+weightInit.name(), temp);
        }
        File saveLocation = new File(HOME_PATH+"Network.zip"); //save
        boolean saveUpdater = true;
        ModelSerializer.writeModel(net,saveLocation,saveUpdater);
    }

    private static void init_train (MultiLayerNetwork net){
        UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        int listenerFrequency = 1;
        net.setListeners(new StatsListener(statsStorage, listenerFrequency));
        uiServer.attach(statsStorage);
    }

    private static MultiLayerConfiguration getRecurrentConf (String activation, WeightInit weightInit){
        NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
        builder.iterations(1000);
        builder.learningRate(0.01);
        builder.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
        builder.seed(123);
        builder.biasInit(0);
        builder.miniBatch(false);
        builder.updater(Updater.RMSPROP);
        builder.weightInit(weightInit);

        ListBuilder listBuilder = builder.list();

      //hidden layers
        for (int i = 0; i < HIDDEN_LAYER_SIZE; i++) {
            GravesLSTM.Builder hiddenLayerBuilder = new GravesLSTM.Builder();
            hiddenLayerBuilder.nIn(i == 0 ? PROPERTIES_NUMBER : HIDDEN_SIZE);
            hiddenLayerBuilder.nOut(HIDDEN_SIZE);
            hiddenLayerBuilder.activation(Activation.fromString(activation));
            listBuilder.layer(i, hiddenLayerBuilder.build());
        }

        RnnOutputLayer.Builder outputLayerBuilder = new RnnOutputLayer.Builder(LossFunctions.LossFunction.MCXENT);
        // normalization all sums to 1 on outp
        outputLayerBuilder.activation(Activation.SOFTMAX);
        outputLayerBuilder.nIn(HIDDEN_SIZE);
        outputLayerBuilder.nOut(OUTPUT_SIZE);
        listBuilder.layer(HIDDEN_LAYER_SIZE, outputLayerBuilder.build());
        listBuilder.pretrain(false);
        listBuilder.backprop(true);
        //end builder
        MultiLayerConfiguration conf = listBuilder.build(); //save conf

        return conf;
    }
}