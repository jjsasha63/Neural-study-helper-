package com.red.processing;

import java.util.List;

/**
 * 
 * @author Tobias Ziegelmayer
 * @version 1.0.0
 * This class constructs an entry of a corpus.
 * The entry contains id, author, date, source url,
 * headlines, summary and text a.s.o.
 * It also use the class StanfordNLP for annotating 
 * the summary and the text.
 *
 */
public class Input_for_prep {
	


	List<String> words_text;
	List<String> words_title;
	List<List<String>> summaryLemma;
	List<List<String>> headlineLemma;
	List<List<String>> textTokens;
	List<List<String>> summaryTokens;
	List<List<String>> headlineTokens;
	List<Vectors> vectors;
	List<Double> distances;
	List<Integer> labels;
	List<List<String>> textLemma;
	double meanDistance;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getWords_text() { return words_text; }
	public void setWords_text(List<String> words_text) { this.words_text = words_text; }
	public List<String> getWords_title() { return words_title; }
	public void setWords_title(List<String> words_title) { this.words_title = words_title; }
	public List<Vectors> getFeatureVectors() {
		return vectors;
	}
	public void setFeatureVectors(List<Vectors> vectors) {
		this.vectors = vectors;
	}
	public List<List<String>> getTextTokens() {
		return textTokens;
	}
	public void setTextTokens(List<List<String>> textTokens) {
		this.textTokens = textTokens;
	}
	public List<List<String>> getSummaryTokens() {
		return summaryTokens;
	}
	public void setSummaryTokens(List<List<String>> summaryTokens) {
		this.summaryTokens = summaryTokens;
	}
	public List<List<String>> getTextLemma() {
		return textLemma;
	}
	public void setTextLemma(List<List<String>> textLemma) {
		this.textLemma = textLemma;
	}
	public List<List<String>> getSummaryLemma() {
		return summaryLemma;
	}
	public void setSummaryLemma(List<List<String>> summaryLemma) {
		this.summaryLemma = summaryLemma;
	}
	public List<List<String>> getHeadlineTokens() {
		return headlineTokens;
	}
	public void setHeadlineTokens(List<List<String>> headlineTokens) {
		this.headlineTokens = headlineTokens;
	}
	public List<List<String>> getHeadlineLemma() {
		return headlineLemma;
	}
	public void setHeadlineLemma(List<List<String>> headlineLemma) {
		this.headlineLemma = headlineLemma;
	}
	public List<Double> getDistances() {
		return distances;
	}
	public void setDistances(List<Double> distances) {
		this.distances = distances;
	}
	public List<Integer> getLabels() {
		return labels;
	}
	public void setLabels(List<Integer> labels) {
		this.labels = labels;
	}
	public double getMeanDistance() {
		return meanDistance;
	}
	public void setMeanDistance(double meanDistance) {
		this.meanDistance = meanDistance;
	}

	int id;
	String title;
	String sum;
	String author;
	String content;
	String date;
	String url;

	/**
	 * This method create a string from some getter method of an entry
	 * @return String
	 */

	/**
	 * Emtpy constructor of an entry
	 */
	public Input_for_prep() {}

	/**
	 * Constructor of an entry
	 * @param content
	 * @param title
	 */
	public Input_for_prep(String content, String title){
		Additional_func additionalfunc = new Additional_func();
		SNLP snlp = new SNLP();
		this.setContent(content);
		List<List<List<String>>> S_Text = snlp.stanford_lemma_token(this.getContent());
		this.setTextTokens(S_Text.get(0));
		this.setTextLemma(S_Text.get(1));
		this.setTitle(title);
		List<List<List<String>>> stanfordTitle = snlp.stanford_lemma_token(this.getTitle());
		this.setHeadlineTokens(stanfordTitle.get(0));
		this.setHeadlineLemma(stanfordTitle.get(1));
		Words words = new Words();
		this.setWords_text(words.getTop10(this.getTextLemma()));
		this.setWords_title(words.getList(this.getHeadlineLemma()));

		List<Vectors> vectors = additionalfunc.create_vectors(this);
		this.setFeatureVectors(vectors);
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId()+", ");
		sb.append(this.getAuthor()+", ");
		sb.append(this.getTitle()+", ");
		sb.append(this.getUrl()+", ");
		sb.append(this.getDate()+", ");
		sb.append(this.getSum()+", ");
		sb.append(this.getContent()+", ");


		return sb.toString();
	}

}
