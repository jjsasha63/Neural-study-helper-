package com.red.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//euclid distance creation
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


   //euclid distance
    public Euclid_D(HashMap<String, Integer> sum, HashMap<String, Integer> ex_sum){
        this.set_ex_sum(ex_sum);
        this.set_sum(sum);
        int [] [] inputLists = this.createInputLists(this.get_sum(), this.get_ex_sum());
        this.set_dist(this.distance(inputLists[0], inputLists[1]));
    }




    //recreates ress into list of frequency of content words
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


   //creates list of results to input
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
   //calculate euclid distance
    private double distance(int[] a, int[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }




}


