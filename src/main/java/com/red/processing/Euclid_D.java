package com.red.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains functions for calculating the EuclideanDistance for the project TextSummarization
 */
public class Euclid_D {


    public HashMap<String, Integer> get_sum() {
        return sum;
    }

    public void set_sum(HashMap<String, Integer> sum) {
        this.sum = sum;
    }

    public HashMap<String, Integer> get_ex_sum() {
        return ex_sum;
    }

    public void set_ex_sum(HashMap<String, Integer> ex_sum) {
        this.ex_sum = ex_sum;
    }

    public double get_dist() {
        return distance;
    }

    public void set_dist(double distance) {
        this.distance = distance;
    }

    HashMap<String, Integer> sum;
    HashMap<String, Integer> ex_sum;
    double distance;


    /**
     * This constructor takes two hashmaps as parameter. A hashmap represents a frequency list of
     * content words from a summary. The hashmaps will be maped as a 2-dimensional integer array.
     * From the array the euclidean distance will be calculated.
     * @param sum
     * @param ex_sum
     */
    public Euclid_D(HashMap<String, Integer> sum, HashMap<String, Integer> ex_sum){
        this.set_ex_sum(ex_sum);
        this.set_sum(sum);
        int [] [] inputLists = this.createInputLists(this.get_sum(), this.get_ex_sum());
        this.set_dist(this.distance(inputLists[0], inputLists[1]));
    }




    /**
     * This method takes two hashmaps as parameter. The method generates for each hashmap
     * an int array which only contains the frequency of the content words.
     * The arrays have the same length and the index of each entry represents the same
     * content word.
     * @param sum
     * @param ex_sum
     * @return int [][]
     */
    private int [][] createInputLists (HashMap<String, Integer> sum, HashMap<String, Integer> ex_sum){
        List<String> input = get_input(sum, ex_sum);
        int [] [] res = new int[2][input.size()];
        int cntr = 0;
        for (String inp : input){
            if (sum.keySet().contains(inp)){
                res[0][cntr] = sum.get(inp);
            }
            else{
                res[0][cntr] = 0;
            }
            if (ex_sum.keySet().contains(inp)){
                res[1][cntr] = ex_sum.get(inp);
            }
            else{
                res[1][cntr] = 0;
            }
            cntr++;
        }
        return res;
    }


    /**
     * This method takes two hashmaps as parameter. The method generates a commom list of all
     * content words.
     * @param sum
     * @param ex_sum
     * @return List<String>
     */
    private static List<String> get_input(HashMap<String, Integer> sum, HashMap<String, Integer> ex_sum){
        List<String> input = new ArrayList<>();
        for (String lemma : sum.keySet()){
            if (input.contains(lemma) == false){
                input.add(lemma);
            }
        }

        for (String lemma : ex_sum.keySet()){
            if (input.contains(lemma) == false){
                input.add(lemma);
            }
        }
        return input;
    }
    /**
     * This method takes two integer arrays as parameter and
     * calculates the euclidean distance between them.
     * @param a
     * @param b
     * @return double
     */
    private double distance(int[] a, int[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }




}


