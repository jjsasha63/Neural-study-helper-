package com.red.processing;

/**
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains functions for creating the feature vector for the project TextSummarization
 */
public class Vectors {


    public double getSentence_rel() {
        return sentence_rel;
    }

    public void setSentence_rel(double sentence_rel) {
        this.sentence_rel = sentence_rel;
    }

    public double getSentence_lenght() {
        return sentence_lenght;
    }

    public void setSentence_lenght(double sentence_lenght) {
        this.sentence_lenght = sentence_lenght;
    }

    public double getIsFirstsentence() {
        return isFirstsentence;
    }

    public void setIsFirstsentence(double isFirstsentence) {
        this.isFirstsentence = isFirstsentence;
    }

    public double getTheme_word() {
        return Theme_word;
    }

    public void setTheme_word(double theme_word) {
        this.Theme_word = theme_word;
    }

    public double getTitle_word() {
        return Title_word;
    }

    public void setTitle_word(double title_word) {
        this.Title_word = title_word;
    }
    double sentence_rel;
    double sentence_lenght;
    double isFirstsentence;
    double Theme_word;
    double Title_word;


    /**
     * This method returns an array of double values which represents a feature vector
     * @return double []
     */
    public double[] getVector() {
        double []vector = new double[5];
        vector[0] = this.getSentence_rel();
        vector[1] = this.getSentence_lenght();
        vector[2] = this.getIsFirstsentence();
        vector[3] = this.getTheme_word();
        vector[4] = this.getTitle_word();
        return vector;
    }
    /**
     * This constructor takes five double values as argument and set these values as properties
     * for a feature vector.
     * @param sentencePos
     * @param sentence_lenght
     * @param isFirst
     * @param nrThematic
     * @param nrHead
     */
    public Vectors(double sentencePos, double sentence_lenght, double isFirst, double nrThematic, double nrHead){
        this.setSentence_rel(sentencePos);
        this.setSentence_lenght(sentence_lenght);
        this.setIsFirstsentence(isFirst);
        double temp = nrHead / sentence_lenght;
        this.setTitle_word(temp);
        temp = nrThematic / sentence_lenght;
        this.setTheme_word(temp);
    }

    /**
     * This method returns a string from a feature vector
     * @return String
     */
    public String toString (){
        double [] vector = this.getVector();
        StringBuilder sb = new StringBuilder();
        for (double v : vector){
            sb.append(v + ", ");
        }
        return sb.toString().trim();
    }

}
