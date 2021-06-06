package com.red.processing;

import java.util.*;

//words frequency calculation
public class Words {


    public Words(){}


    //creates list of stop words
    public List<String> getWordList (){
        Additional_func h = new Additional_func();
        List<String> tmp = h.read_s_file("src/main/resources/stopwords-en.txt");
        List<String> result = new ArrayList<>();
        for (String entry : tmp){
            String ts = clear_entry(entry);
            result.add(ts);
        }
        return result;
    }


    public static String clear_entry (String entry){
        return entry.trim();
    }

    //check words is equal to stop ones
    public boolean isLemmaInWordList (String lemma){
        List<String> wordList = this.getWordList();
        if (wordList.contains(lemma)){return true;}
        return false;
    }

   //form list to proper list of sentences
    private static List<String> createSimpleLemmaList (List<List<String>> lemmata){
        List<String> result = new ArrayList<>();
        for (List<String> entries : lemmata){
            for (String lemma : entries){
                result.add(lemma);
            }
        }
        return result;
    }

   //create frequency list
    public HashMap<String, Integer> createFrequencyList (List<List<String>> lemmata){
        HashMap<String, Integer> result = new HashMap<>();
        List<String> lemmaList = createSimpleLemmaList(lemmata);
        for (String lemma : lemmaList){
            if (this.isLemmaInWordList(lemma) == false){
                if (result.containsKey(lemma)){ result.put(lemma, result.get(lemma) + 1); }
                else{ result.put(lemma, 1); }
            }
        }
        result = sortByValues(result);
        return result;
    }

    //get 10 most frequent words from the text
    public List<String> get_top(List<List<String>> lemmata){
        List<String> result = new ArrayList<>();
        HashMap<String, Integer> freqList = this.createFrequencyList(lemmata);
        Set set2 = freqList.entrySet();
        Iterator iterator2 = set2.iterator();
        int counter = 0;
        while(iterator2.hasNext() && counter < 10) {
                Map.Entry me2 = (Map.Entry)iterator2.next();
                String tmp = me2.getKey().toString();
                result.add(tmp);
            counter++;
        }

        return result;
    }

    public List<String> getList (List<List<String>> lemmata){
        List<String> result = new ArrayList<>();
        HashMap<String, Integer> freqList = this.createFrequencyList(lemmata);
        Set set2 = freqList.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            String tmp = me2.getKey().toString();
            result.add(tmp);
        }

        return result;
    }


  //sort parameters
    private static HashMap sortByValues(HashMap map) {
        List list = new ArrayList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
