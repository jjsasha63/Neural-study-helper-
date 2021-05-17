package com.red.network;

import com.red.processing.Pre_file;

import java.io.IOException;

/**
 * This Class creates FeatureVectors from a corpus.
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 */
public class CreatePrep_file {
    public static void main(String[] args) throws IOException, InterruptedException {

        String basePath = "src/data/";

        Pre_file training = new Pre_file(1,0);
        training.savePre_file(basePath+ "trainingsSet.csv");

        Pre_file test = new Pre_file(0,0);
        test.savePre_file(basePath+ "testSet.csv");

    }
}
