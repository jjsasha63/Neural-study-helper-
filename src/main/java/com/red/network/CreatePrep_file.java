package com.red.network;

import com.red.processing.Pre_file;

import java.io.IOException;


public class CreatePrep_file {
    public static void main(String[] args) throws IOException, InterruptedException {

        String basePath = "\\src\\main\\resources";
        Pre_file training = new Pre_file(1,0);
        training.savePre_file(basePath+ "trainSet.csv");
        Pre_file test = new Pre_file(0,0);
        test.savePre_file(basePath+ "testSet.csv");

    }
}
