package com.red.processing;

import java.util.ArrayList;
import java.util.List;

//creates prep file
public class Pre_file {
	
	List<Input_for_prep> inputs;
	

	public List<Input_for_prep> getInputs() {
		return inputs;
	}

	public void setInputs(List<Input_for_prep> inputs) {
		this.inputs = inputs;
	}





	//form input options for file
	public List<Input_for_prep> create_Pre_file(int pre_file_Type, int max){
		List<Input_for_prep> inputs = new ArrayList<Input_for_prep>();
		int lCounter = 0;
		Double tmp = 0.0;
		SNLP snlp = new SNLP();
		Additional_func additionalfunc = new Additional_func();
		List < List < String > > lines = readIn();
		int i = 0;
		// calculates the size of the corpus
		int max_s = 0;
		int min_s = 0;
		tmp = lines.size()*0.66;
		int [] train = {0, tmp.intValue()-1};
		int [] test = {tmp.intValue(), lines.size()-1};
		if (pre_file_Type == 1){
			// create trainingscorpus
			min_s = train[0]; max_s = train[1];
		}
		else if (pre_file_Type == 0){
			// create testcorpus
			min_s = test[0]; max_s = test[1];
		}
		else{
			// corpusType as corpusSize
			int pre_file_Size = pre_file_Type;
			min_s = max; max_s = pre_file_Size+max;
		}


        for(List<String> line: lines) {
        	if (lCounter > min_s && lCounter <= max_s) {
        		Input_for_prep inputforprep = new Input_for_prep();
        		inputforprep.setId(lCounter);
        		inputforprep.setAuthor(line.get(0));
        		inputforprep.setDate(line.get(1));
        		inputforprep.setTitle(line.get(2));
        		inputforprep.setUrl(line.get(3));
        		inputforprep.setSum(line.get(4));
        		inputforprep.setContent(line.get(5));
        		List<List<List<String>>> stanfordText = snlp.stanford_lemma_token(inputforprep.getContent());
        		inputforprep.setTextTokens(stanfordText.get(0));
        		inputforprep.setTextLemma(stanfordText.get(1));
				List<List<List<String>>> stanfordSummary = snlp.stanford_lemma_token(inputforprep.getSum());
        		inputforprep.setSummaryTokens(stanfordSummary.get(0));
        		inputforprep.setSummaryLemma(stanfordSummary.get(1));
				List<List<List<String>>> stanfordHeadline = snlp.stanford_lemma_token(inputforprep.getTitle());
				inputforprep.setHeadlineTokens(stanfordHeadline.get(0));
				inputforprep.setHeadlineLemma(stanfordHeadline.get(1));
				Words wf = new Words();
				inputforprep.setWords_text(wf.get_top(inputforprep.getTextLemma()));
				inputforprep.setWords_title(wf.getList(inputforprep.getHeadlineLemma()));

				Label label = new Label(inputforprep);
				inputforprep.setDistances(label.getDistances());
				inputforprep.setMeanDistance(label.getMean(inputforprep.getDistances()));
				inputforprep.setLabels(label.getLabels(inputforprep.getDistances(), inputforprep.getMeanDistance()));
				List<Vectors> vectors = additionalfunc.create_vectors(inputforprep);
				inputforprep.setFeatureVectors(vectors);
        		inputs.add(inputforprep);
        		i++;
        	}
			lCounter++;
        }
		return inputs;
	}

	//read csv file
	public static List < List < String > > readIn () {
		Additional_func additionalfunc = new Additional_func();
		String s = System.getProperty("user.dir");
		String path = s+"src/main/resources/pre_file.csv";
		List < List < String > > lines = additionalfunc.readCSVFile(path);

		return lines;
	}

	//set borders for the file
	public Pre_file(int pre_file_Type, int max) {
		List<Input_for_prep> entries = this.create_Pre_file(pre_file_Type, max);
		this.setInputs(entries);
	}

	//save to file
	public void savePre_file(String path){
		StringBuilder stringBuilder = new StringBuilder();
		List<String> res = new ArrayList<>();
		for (Input_for_prep inputforprep : this.getInputs()){
			int index = 0;
			for (Vectors vectors : inputforprep.getFeatureVectors()){
				stringBuilder.append(inputforprep.getLabels().get(index)+",");
				int cnt = 0;
				for (double aDouble : vectors.getVector()){
					if (cnt < vectors.getVector().length-1){
						stringBuilder.append(aDouble+",");
					}
					else{
						stringBuilder.append(aDouble);
					}
					cnt++;
				}
				stringBuilder.append("\n");
				index++;
			}
			res.add(stringBuilder.toString().trim());
		}

		Additional_func additionalfunc = new Additional_func();
		additionalfunc.write_b_file(path, res);
	}
}
