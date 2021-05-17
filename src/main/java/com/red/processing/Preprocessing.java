package com.red.processing;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Simon Werner
 * @version 1.0.0
 * This class contains functions for extracting features from Sentences
 */
public class Preprocessing {

    //relative Position des Satzes im Text
    public double position_textrel(List<String> sentence, List<List<String>> content) {
        double temp = (content.indexOf(sentence) +1)/ (double) content.size();
        return temp;
    }


    //Prüfung ob erster Satz
    public double isFirst (List<String> sentence, List<List<String>> content) {
        if (content.indexOf(sentence) == 0) {
            return 1;
        } else {
            return 0;
        }
    }
    //Anzahl Wörter in Satz
    public double words_count(List<String> sentence, List<List<String>> sentences) {
        double part = 0.0;
        int sum = 0;
        for (List<String> sent : sentences){sum += sent.size();}
        part = sentence.size() / (double) sum;
        return part;
    }


}
