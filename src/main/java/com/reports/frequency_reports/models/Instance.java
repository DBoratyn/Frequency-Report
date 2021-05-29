package com.reports.frequency_reports.models;

import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.util.*;

@Slf4j
public class Instance {

    private final String word;
    private final int lengthOfWord;
    private int similarityCount = 0;
    private double frequency = 0;
    private List<String> lettersUsed = new ArrayList();
    private NumberFormat nf= NumberFormat.getInstance();

    private Map<Integer, String> tree = new TreeMap();
    private List<String> listOrder;


    public Instance( String word, List<String> listOrder) {
        this.listOrder = listOrder;
        this.word = word;
        this.lengthOfWord = word.length();

        nf.setMaximumFractionDigits(2);
    }

    public void updateStats(List<String> characterRules, int usageCount) {
        updateCount(characterRules, this.word);
        updateFreq(usageCount);
    }

    private void updateFreq(int lengthOfSentence) {
        this.frequency = Double.parseDouble(nf.format((double)this.similarityCount/(double)lengthOfSentence));
    }

    private void updateCount(List<String> characterRules, String instance) {
        for (char instanceLetter: instance.toCharArray()) {
            for (String ruleLetter: characterRules) {
                if(instanceLetter == ruleLetter.charAt(0)) {
                    this.lettersUsed.add(ruleLetter);
                    this.similarityCount++;
                }
            }
        }
    }

    private Collection<String> updateLettersOrder(List<String> currentList) {
        for (String letter : currentList) {
            tree.put(listOrder.indexOf(letter), letter);
        }
        return tree.values();
    }

    public String getLettersUsed() {
        List<String> hashedAndSortedList =
                new ArrayList(updateLettersOrder(new ArrayList(new HashSet(this.lettersUsed))));
        if(hashedAndSortedList.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for(Iterator<String> it = hashedAndSortedList.iterator(); it.hasNext();) {
                String letter = it.next();
                sb.append(letter);
                if(it.hasNext()) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return lettersUsed.get(0);
    }

    public String getWord() {
        return word;
    }

    public int getSimilarityCount() {
        return similarityCount;
    }

    public Integer getSimilarityCountObject() {
        return (Integer)similarityCount;
    }

    public double getFrequency() {
        return frequency;
    }

    public int getLengthOfWordPlain() {
        return lengthOfWord;
    }

    public Integer getLengthOfWord() {
        return (Integer)lengthOfWord;
    }
}
