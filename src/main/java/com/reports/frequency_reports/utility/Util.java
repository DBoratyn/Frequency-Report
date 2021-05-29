package com.reports.frequency_reports.utility;

import com.reports.frequency_reports.models.Instance;
import com.reports.frequency_reports.models.InstanceGroup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

@Slf4j
public class Util implements IUtil{
    //properties
    private List<String> characterRules = new ArrayList();
    List<String> arr;
    private int sentenceLengthWithoutSpecialChars = 0;
    private int usageCount = 0;
    private int totalUsageCount = 0;

    private InstanceComparator comparator = new InstanceComparator();
    private NumberFormat nf= NumberFormat.getInstance();
    private StringBuilder report = new StringBuilder();
    ClassLoader classLoader = getClass().getClassLoader();

    private String sentence;// = "I love to work in global logic!";

    public Util(String sentence, String keyword) {
        this.sentence = sentence;

        for (int i = 0; i < keyword.toLowerCase().length(); i++){
            characterRules.add(String.valueOf(keyword.toLowerCase().charAt(i)));
        }
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.CEILING);
    }

    public Util() {
        InputStream keywordFile = classLoader.getResourceAsStream("keywordFile.txt");
        InputStream sentenceFile = classLoader.getResourceAsStream("sentenceFile.txt");

        String keywordFromFile = readFromInputStream(keywordFile).trim();
        this.sentence = readFromInputStream(sentenceFile).trim();

        for (int i = 0; i < keywordFromFile.toLowerCase().length(); i++){
            characterRules.add(String.valueOf(keywordFromFile.toLowerCase().charAt(i)));
        }

        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.CEILING);
    }

    private String readFromInputStream(InputStream inputStream)  {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException exception) {
            log.error("IOException occured.");
        }
        return resultStringBuilder.toString();
    }

    public String generateReportTest() {
        //Set up properties and remove special characters //TODO: remove square brackets
        setUpReportGeneration();

        //remove words that are not within the letters of the rule
        List<String> setArr = new ArrayList();
        removeUnwantedWords(setArr);
        List<Instance> arrayModels = new ArrayList<>();
        for (String letter :setArr) {
            arrayModels.add(new Instance(letter, this.characterRules));
        }

        //apply count and frequencies to instances
        for (Instance instance: arrayModels) {
            instance.updateStats(this.characterRules, this.usageCount);
        }

        //Order by the amount of letters that appear from the letters that was searched
        sortByFrequency(arrayModels);

        //section out each instances to their own groups
        List<InstanceGroup> instanceGroups = new ArrayList();
        createInstanceGroups(arrayModels, instanceGroups);


        //sort by number of letters in a word for each groups
        //then if they are same amount, sort alphabetically
        comparator.setType("length");
        for (InstanceGroup group: instanceGroups) {
            group.getInstances().sort(comparator);
        }

        //print out stats
        for (InstanceGroup group: instanceGroups) {
            printInstanceStatus(group);
        }
        printTotalStats();

        //write to output.txt
        writeToOutputTextFile();

        return this.report.toString();
    }

    private void writeToOutputTextFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(this.report.toString());
            writer.close();
        } catch (IOException exception) {
            log.error("Encountered IOException: {}", exception);
        }
    }

    private void printTotalStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("TOTAL Frequency: ");
        sb.append(getTotalFrequency());
        sb.append(" (");
        sb.append(this.totalUsageCount);
        sb.append("/");
        sb.append(this.sentenceLengthWithoutSpecialChars);
        sb.append(")");
        log.info(sb.toString());
        this.report.append("<br>");
        this.report.append(sb.toString());
    }

    private String getTotalFrequency() {
        return nf.format((double)this.totalUsageCount/this.sentenceLengthWithoutSpecialChars);
    }

    private void printInstanceStatus(InstanceGroup group) {
        for (Instance instance: group.getInstances()) {
            StringBuilder sb = new StringBuilder();
            sb.append("{(");
            sb.append(instance.getLettersUsed());
            sb.append("), ");
            sb.append(instance.getLengthOfWordPlain());
            sb.append("} = ");
            sb.append(instance.getFrequency());
            sb.append(" (");
            sb.append(instance.getSimilarityCount());
            this.totalUsageCount += instance.getSimilarityCount();
            sb.append("/");
            sb.append(this.usageCount);
            sb.append(")");
            log.info(sb.toString());
            this.report.append(sb.toString());
            this.report.append("\n");
            this.report.append("<br>");
        }
    }

    private void createInstanceGroups(List<Instance> arrayModels, List<InstanceGroup> instanceGroups) {
        for(Iterator<Instance> it = arrayModels.iterator(); it.hasNext();) {
            Instance instance = it.next();
            if(instanceGroups.size() == 0 ) {
                createNewGroupAndAddInstance(instanceGroups, instance);
            } else {
                for(Iterator<InstanceGroup> groupIt = instanceGroups.iterator(); groupIt.hasNext();){
                    InstanceGroup group = groupIt.next();
                    if(group.getGroupNumber() == instance.getSimilarityCount()) {
                        group.addInstance(instance);
                    } else {
                        createNewGroupAndAddInstance(instanceGroups, instance);
                        break;
                    }
                }
            }
        }
    }

    private void createNewGroupAndAddInstance(List<InstanceGroup> instanceGroups, Instance instance) {
        instanceGroups.add(new InstanceGroup(instance, instance.getSimilarityCount()));
    }

    private void sortByFrequency(List<Instance> arrayModels) {
        Collections.sort(arrayModels,comparator);
    }

    private void setUpReportGeneration() {
        setSentenceLengthWithoutSpecialChars();
        sentence = sentence.replaceAll("[!\"#$%&'()*+,-./:;<=>?@^_`{|}~]","");
        arr = new LinkedList<>(Arrays.asList(sentence.toLowerCase().split(" ")));
        this.getRuleLetterUsageCount();
    }

    private void setSentenceLengthWithoutSpecialChars() {
        this.sentenceLengthWithoutSpecialChars =
                sentence.replaceAll("[!\"#$%&'()*+,-./:;<=>?@^_`{|}~ ]","").length();
    }

    private void removeUnwantedWords(List<String> setArr) {
        for (String instance: arr) {
            for (String letter: characterRules) {
                if(instance.contains(letter)){
                    setArr.add(instance);
                    break;
                }
            }
        }
    }

    public void getRuleLetterUsageCount() {
        this.usageCount++;
        for (char instanceLetter: this.sentence.toCharArray()) {
            for (String ruleLetter: characterRules) {
                if(instanceLetter == ruleLetter.charAt(0)) {
                    this.usageCount++;
                }
            }
        }
    }
}
