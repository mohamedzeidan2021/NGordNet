package main;

import edu.princeton.cs.algs4.In;
import ngrams.NGramMap;

import java.util.*;

public class WordNet {

    //gives ID's and word
    //can only send ID possibly
    //call addNode and parse the data a lil bit to pass in ID and Set of ID's
    HashMap<Integer, Set<String>> idToString;
    HashMap<String, Set<Integer>> stringToID;
    HashMap<String, Set<Integer>> sameNode;
    private Graph adjList = new Graph();

    private NGramMap ngm;

    public WordNet(String synsetsFile, String hyponymsFile,  String wordFile, String countFile) {
        In synsets = new In(synsetsFile);
        In hyponyms = new In(hyponymsFile);

        idToString = new HashMap<>();
        stringToID = new HashMap<>();
        sameNode = new HashMap<>();

        ngm = new NGramMap(wordFile, countFile);

        //first put it into the ID to string and string to ID
        while (synsets.hasNextLine()) {
            String[] line = synsets.readLine().split(",");
            Set<String> wordSet = new HashSet<>();

            if (line[1].contains(" ")) {
                String[] wordsWithSpaces = line[1].split(" ");
                wordSet.addAll(Arrays.asList(wordsWithSpaces));

                for (String word : wordsWithSpaces) {
                    if (sameNode.containsKey(word)) {
                        sameNode.get(word).add(Integer.valueOf(line[0]));
                    } else {
                        Set<Integer> idSet = new HashSet<>();
                        idSet.add(Integer.valueOf(line[0]));
                        sameNode.put(word, idSet);
                    }
                    if (stringToID.containsKey(word)) {
                        stringToID.get(word).add(Integer.valueOf(line[0]));
                    } else {
                        Set<Integer> idSet = new HashSet<>();
                        idSet.add(Integer.valueOf(line[0]));
                        stringToID.put(word, idSet);
                    }
                }

            } else {
                wordSet.add(line[1]);
            }
            idToString.put(Integer.valueOf(line[0]), wordSet);

            if (stringToID.containsKey(line[1])) {
                stringToID.get(line[1]).add(Integer.valueOf(line[0]));
            } else {
                Set<Integer> idSet = new HashSet<>();
                idSet.add(Integer.valueOf(line[0]));
                stringToID.put(line[1], idSet);
            }
        }

        //now implement the graph!
        while (hyponyms.hasNextLine()) {
            String[] line = hyponyms.readLine().split(",");

            int hypernym = Integer.parseInt(line[0]);
            TreeSet<Integer> hyponymsList = new TreeSet<>();

            for (int i = 1; i < line.length; i++) {
                hyponymsList.add(Integer.parseInt(line[i]));
            }

            adjList.addNode(hypernym, hyponymsList);
        }
    }

    public Set<String> getHyponyms(String word) {

        TreeSet<Integer> allChildren = new TreeSet<>();

        if (!sameNode.containsKey(word) && !stringToID.containsKey(word)) {
            return Collections.emptySet();
        }

        if (sameNode.containsKey(word)) {
            Set<Integer> idForWord = stringToID.get(word);

            allChildren.addAll(adjList.getChildren(idForWord));
        }
        if (stringToID.containsKey(word)) {
            Set<Integer> idForWord = stringToID.get(word);

            //go to adj list and print all the adj
            //create func that will take list of ID's then go thru the graph and print all words
            allChildren.addAll(adjList.getChildren(idForWord));
        }

        TreeSet<String> sortedChildren = new TreeSet<>();

        for (int id : allChildren) {
            TreeSet<String> correspondingWords = new TreeSet<>(idToString.get(id));

            sortedChildren.addAll(correspondingWords);
        }

        return sortedChildren;
    }

    public NGramMap createNGM() {
        return ngm;
    }

    public Set<String> compareHyponyms(List<String> words) {

        TreeSet<String> res = new TreeSet<>();
        String w = words.get(0);
        TreeSet<String> commonHyponyms = new TreeSet<>(getHyponyms(w));

        for (String word : words) {
            Set<String> wordHyponyms = getHyponyms(word);

            commonHyponyms.retainAll(wordHyponyms);
        }

        return commonHyponyms;
    }

    public String printAllHyponyms(List<String> words) {
        StringBuilder res = new StringBuilder("[");

        Set<String> hyponyms = compareHyponyms(words);

        for (String hypo : hyponyms) {
            res.append(hypo).append(", ");
        }

        if (hyponyms.isEmpty()) {
            res.append("]");
            return res.toString();
        }
        res.deleteCharAt(res.length() - 1);
        res.deleteCharAt(res.length() - 1);

        res.append("]");

        return res.toString();
    }


}
