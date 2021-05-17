package com.red.processing;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Tobias Ziegelmayer & Simon Werner
 * @version 1.0.0
 * This class contains functions for Classification of Sentences, based on the trained Neural Network
 */
public class Classifier {

    /**
     * This method takes as parameter a string and creates a MultiLayerNetwork from a stored network.
     * @param path
     * @return MultiLayerNetwork
     * @throws IOException
     */
    public MultiLayerNetwork loadNN(String path) throws IOException {
        return ModelSerializer.restoreMultiLayerNetwork(path);
    }



    /**
     * This method takes two strings as parameter and creates from the input
     * strings a filepath.
     * @param headline
     * @param type
     * @return String
     */
    private static String createPath (String headline, String type){
        headline = headline.replaceAll("[^A-Za-z0-9]", " ");
        headline = headline.replaceAll(" +", " ");
        String [] result = headline.split(" ");
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (String entry : result){
            if (counter < result.length-1){
                sb.append(entry+"_");
            }
            else{
                sb.append(entry+"."+type);
            }
            counter++;
        }
        return sb.toString();
    }

    /**
     * This method takes as parameter an integer, a string and an entry and creates a
     * 2-dimensional array of double values.
     * The double values are the result of the output layer of the use of the trained neuroal
     * network.
     * @param b_size
     * @param Vectors_path
     * @param inputforprep
     * @return double [][]
     * @throws IOException
     * @throws InterruptedException
     */
    public static double [][] Create_marks(int b_size, String Vectors_path, Input_for_prep inputforprep) throws IOException, InterruptedException{
        double [][] res = new double[b_size][2];
        int skip_num = 0;
        char del = ',';
        Classifier classifier = new Classifier();
        MultiLayerNetwork network = classifier.loadNN("src/main/resources/model/Network.zip");

        RecordReader recordReader = new CSVRecordReader(skip_num,del);
        recordReader.initialize(new FileSplit(new ClassPathResource(Vectors_path).getFile()));
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,b_size);
        DataSet next = iterator.next();

        INDArray output = network.output(next.getFeatureMatrix());
        for (int i = 0; i < output.size(0); i++){
            res[i][0] = output.getDouble(i, 0);
            res[i][1] = output.getDouble(i, 1);
        }

        return res;
    }

    /**
     * This method takes a path to a folder and a filename
     * to save the created feature vectors of a corpus as csv file
     * @param path
     */
    public void save_Pre_file(String path, Input_for_prep inputforprep){

        List<String> res = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (Vectors vectors : inputforprep.getFeatureVectors()){
            int cntr = 0;
            for (double aDouble : vectors.getVector()){
                if (cntr < vectors.getVector().length-1){
                    stringBuilder.append(aDouble+",");
                }
                else{
                    stringBuilder.append(aDouble);
                }
                cntr++;
            }
            stringBuilder.append("\n");
        }
        res.add(stringBuilder.toString().trim());


        Additional_func additionalfunc = new Additional_func();
        additionalfunc.write_b_file(path, res);
    }

    /**
     * This method takes as parameter two string and creates a string as summary from
     * the input strings.
     * The output will generated from a trained neural network.
     * @param text
     * @param title
     * @return String
     * @throws IOException
     * @throws InterruptedException
     */
    public String sum(String text, String title) throws IOException, InterruptedException {
        Input_for_prep inputforprep = new Input_for_prep(text, title);
        String File_path = "target/classes/temp/"+createPath(title, "csv");
        save_Pre_file(File_path, inputforprep);
        File_path = "src/main/resources/temp/"+createPath(title, "csv");
        StringBuilder builder = new StringBuilder();

        save_Pre_file(File_path, inputforprep);
        File f = new File (File_path);
        if (!f.exists()){
            System.exit(0);
        }
        File_path = "/temp/"+createPath(title,"csv");
        double[][] marks = Create_marks(inputforprep.getFeatureVectors().size(), File_path, inputforprep);
        List<List<String>> tokens = inputforprep.getTextTokens();
        int count = 0;
        for (int i = 0; i < marks.length; i++){
            if (marks[i][0] >= 0.45){
                count++;
                StringBuilder builder1 = new StringBuilder();
                List<String> temp = tokens.get(i);
                for (String s : temp){builder.append(s+" ");}
                builder.append(builder1.toString()+"\n");
            }
        }
        System.out.println("Total amount:\t"+tokens.size()+"\tSummary amount:\t"+count);
        return builder.toString();
    }

}
