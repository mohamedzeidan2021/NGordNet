package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {

        Graph g = new Graph();
        NGramMap ngm = new NGramMap(wordFile,countFile);
        WordNet wordNet = new WordNet(synsetFile, hyponymFile, wordFile, countFile);

        return new HyponymsHandler(wordNet);
    }
}
