package com.red.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import edu.stanford.nlp.util.CoreMap;

//stanford nlp functions
public class SNLP {

	public SNLP() {
		
	}


//
//	public List<String> stanford_lemma(String inputText){
//		Properties properties = new Properties();
//		properties.put("annotators", "tokenize, ssplit, pos, lemma");
//		List<String> res = new ArrayList<>();
//		StanfordCoreNLP coreNLP = new StanfordCoreNLP(properties);
//		Annotation document = new Annotation(inputText);
//		coreNLP.annotate(document);
//		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
//		for(CoreMap sentence: sentences) {
//			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
//				res.add(lemma);
//			}
//		}
//		return res;
//	}


	//creates tokenize lemma res from input
	public List<List<List<String>>> stanford_lemma_token(String input){
		Properties properties = new Properties();
		List<List<List<String>>> res = new ArrayList<>();
		properties.put("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP coreNLP = new StanfordCoreNLP(properties);
		Annotation document = new Annotation(input);
		coreNLP.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<List<String>> tokens = new ArrayList<>();
		List<List<String>> lemmat = new ArrayList<>();

		for(CoreMap sentence: sentences) {
			List<String> TokenList = new ArrayList<>();
			List<String> LemmaList = new ArrayList<>();

			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
				TokenList.add(word);
				LemmaList.add(lemma);
			}
			tokens.add(TokenList);
			lemmat.add(LemmaList);
		}
		res.add(tokens);
		res.add(lemmat);
		return res;
	}


}

