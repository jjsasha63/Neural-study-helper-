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

//additional func
public class Additional_func {



	//write text to file
	public static void write_file_byte(String name, String text) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(name), "utf-8"));
			writer.write(text);
		} catch (IOException ex) {
		} finally {
			try {writer.close();} catch (Exception ex) {}
		}
	}

	//get list from file
	public static List<String> read_s_file(String name) {
		try {
			return Files.readAllLines(
					FileSystems.getDefault().getPath(".", name),
					Charset.defaultCharset() );
		}
		catch ( IOException ioe ) { ioe.printStackTrace(); }
		return null;
	}

	//read CSV file for network
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

	//write list to file
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

	public String [] clear_quotes(String [] line) {
		String [] res = new String [line.length];
		for (int i = 0; i < line.length; i++) {
			res[i] = line[i].replace("\"", "");
		}
		
		return res;
	}




	// check the amount of equal words
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


	public File select_dir(String type,String type1,String name){
//		DirectoryChooser directoryChooser = new DirectoryChooser();
//		directoryChooser.setInitialDirectory(new File("C:\\Users\\jjsas\\OneDrive\\Рабочий стол\\test"));
		Stage stage = new Stage();
//		File selectedDirectory = directoryChooser.showDialog(stage);
//		return selectedDirectory;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(name);
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(type,type1);
		fileChooser.getExtensionFilters().add(extensionFilter);
		File file = fileChooser.showSaveDialog(stage);
		return file;
	}

	//form list of parameters
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
