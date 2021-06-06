package com.red.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//creates labels of sentences
public class Label {

    Input_for_prep inputforprep;

    public Input_for_prep getInput() {
        return inputforprep;
    }

    public void setInput(Input_for_prep inputforprep) {
        this.inputforprep = inputforprep;
    }


    public Label(Input_for_prep inputforprep){
        this.setInput(inputforprep);
    }


   //get avg sum
    public double getMean(List<Double> distances){
        double sum = 0;
        for (Double aDouble : distances){
            sum += aDouble;
        }
        double avg = sum / distances.size();
        return avg;
    }

    //create output labels
    public List<Integer> getLabels (List<Double> distances, double mean){
        List<Integer> res = new ArrayList<>();
        for (Double aDouble : distances){
            if (aDouble <= mean){
                res.add(1);
            }
            else{
                res.add(0);
            }
        }
        return res;
    }

   // gets distances from file
    public List<Double> getDistances (){
        Input_for_prep inputforprep = this.getInput();
        List<Double> result = new ArrayList<>();
        Words words = new Words();
        HashMap<String, Integer> lemma_sum = words.createFrequencyList(inputforprep.getSummaryLemma());
        List<List<String>> sentences = inputforprep.getTextLemma();
        for (List<String> sentence : sentences){
            List<List<String>> tmp = new ArrayList<>();
            tmp.add(sentence);
            HashMap<String, Integer> lemmaSentence = words.createFrequencyList(tmp);
            Euclid_D euclidD = new Euclid_D(lemma_sum, lemmaSentence);
            result.add(euclidD.get_dist());
        }

        return result;
    }

}
