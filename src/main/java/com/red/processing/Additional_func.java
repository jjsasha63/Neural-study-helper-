package com.red.processing;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import play.libs.F;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class contains helper functions for the project TextSummarization
 *
 */
public class Additional_func {

	/**
	 * This method reads a filename or filepath and returns a list of strings
	 * with the content of the file
	 * @param name
	 * @return List<String>
	 */


	/**
	 * This method takes a filename or filepath and string, which has to write
	 * to the file
	 * @param name
	 * @param text
	 */
	public static void write_file_byte(String name, String text) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(name), "utf-8"));
			writer.write(text);
		} catch (IOException ex) {
			// Report
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
	}

	public static List<String> read_s_file(String name) {
		try {
			return Files.readAllLines(
					FileSystems.getDefault().getPath(".", name),
					Charset.defaultCharset() );
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}

	/**
	 * This method takes a filename or filepath and a string as separator and 
	 * an optional String to delete in a line as arguments. The method 
	 * returns a list of list of string as row and columns of a csv file.
	 * @param name
	 * @return List<List<String>> 
	 */
	public List<List<String>> readCSVFile (String name) {
		List<List<String>> lines = new ArrayList<>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                columns = clear_quotes(columns);
                lines.add(Arrays.asList(columns));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
	}

	/**
	 * This method takes a filename or filepath and a list of string, which has to write
	 * to the file
	 * @param name, content
	 */
	public static void write_b_file(String name, List<String> lines) {
		try ( BufferedWriter bufferedWriter =
					  Files.newBufferedWriter(
							  FileSystems.getDefault().getPath(".", name),
							  Charset.forName("UTF-8"),
							  StandardOpenOption.TRUNCATE_EXISTING,
							  StandardOpenOption.CREATE)

		){
			for ( String line : lines ) {
				bufferedWriter.write(line, 0, line.length());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
	}
	/**
	 * This method takes a array of strings. In each string the textquotecharacter '"' 
	 * will be replaced through an empty string. 
	 * @param line
	 * @return String [] 
	 */
	public String [] clear_quotes(String [] line) {
		String [] res = new String [line.length];
		for (int i = 0; i < line.length; i++) {
			res[i] = line[i].replace("\"", "");
		}
		
		return res;
	}




	/**
	 * This method takes two lists of strings as parameter and counts how many words
	 * from the input list are in the words list
	 * @param input
	 * @param words
	 * @return double
	 */
	public static double words_per_sentence(List<String> input, List<String> words){
		double res = 0;

		for (String token : input){
			if (words.contains(token)){
				res++;
			}
		}
		return res;
	}
	public File select_file(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\jjsas\\OneDrive\\Рабочий стол\\test"));
		Stage stage = new Stage();
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}


	public File select_dir(){
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File("C:\\Users\\jjsas\\OneDrive\\Рабочий стол\\test"));
		Stage stage = new Stage();
		File selectedDirectory = directoryChooser.showDialog(stage);
		return selectedDirectory;
	}
	/**
	 * This method takes an entry as parameter and creates a list of feature vectors
	 * for all sentences in the text from the entry
	 * @param inputforprep
	 * @return List<FeatureVector>
	 */
	public List<Vectors> create_vectors(Input_for_prep inputforprep) {
		int cntr = 0;
		List<Vectors> vectors = new ArrayList<>();
		List<List<String>> sentences = inputforprep.getTextTokens();
		for (List<String> sentence : sentences){
			Preprocessing preprocessing = new Preprocessing();
			double text_pos = preprocessing.position_textrel(sentence, sentences);
			double count_word = preprocessing.words_count(sentence,sentences);
			double isFirst = preprocessing.isFirst(sentence, sentences);
			double word_sentence = words_per_sentence(inputforprep.getTextLemma().get(cntr), inputforprep.getWords_text());
			double word_title = words_per_sentence(inputforprep.getTextLemma().get(cntr), inputforprep.getWords_title());
			Vectors vector = new Vectors(text_pos, count_word, isFirst, word_sentence, word_title);
			vectors.add(vector);
			cntr++;
		}
		return vectors;
	}


}
