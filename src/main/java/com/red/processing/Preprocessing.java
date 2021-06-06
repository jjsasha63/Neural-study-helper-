package com.red.processing;

import java.util.ArrayList;
import java.util.List;

//preprocess sentences
public class Preprocessing {

    public double position_textrel(List<String> sentence, List<List<String>> content) {
        double temp = (content.indexOf(sentence) +1)/ (double) content.size();
        return temp;
    }


    public double isFirst (List<String> sentence, List<List<String>> content) {
        if (content.indexOf(sentence) == 0) {
            return 1;
        } else {
            return 0;
        }
    }
    public double words_count(List<String> sentence, List<List<String>> sentences) {
        double part = 0.0;
        int sum = 0;
        for (List<String> sent : sentences){sum += sent.size();}
        part = sentence.size() / (double) sum;
        return part;
    }


}
