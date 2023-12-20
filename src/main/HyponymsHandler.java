package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    NGramMap gramMap;
    WordNet wordNet;



    public HyponymsHandler(WordNet wordNet) {
        this.wordNet = wordNet;
        gramMap = wordNet.createNGM();
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String res;
        Set<String> hyposList;
        TimeSeries ts;
        Map<Double, List<String>> frequency = new TreeMap<>();

        if (words.size() == 1 && Objects.equals(words.get(0), "")) {
            return "[]";
        }

        if (q.k() != 0) {
            hyposList = wordNet.compareHyponyms(words);

            for (String hypo : hyposList) {
                double sum = 0;

                ts = gramMap.countHistory(hypo, startYear, endYear);
                for (int year : ts.keySet()) {
                    sum += ts.get(year);
                }

                if (sum != 0.0) {
                    if (frequency.containsKey(sum)) {
                        frequency.get(sum).add(hypo);
                    } else {
                        List<String> newList = new ArrayList<>();
                        newList.add(hypo);
                        frequency.put(sum, newList);
                    }
                }

            }
            // sort the frequency map in descending order
            List<Map.Entry<Double, List<String>>> sortedEntries = new ArrayList<>(frequency.entrySet());
            sortedEntries.sort(Collections.reverseOrder(Map.Entry.comparingByKey()));
            List<String> topHyponyms = new ArrayList<>();
            int k = q.k(); // Get the value of k
            int count = 1;

            for (Map.Entry<Double, List<String>> hypo : sortedEntries) {
                if (count > k) {
                    break;
                }
                List<String> values = new ArrayList<>();
                values = hypo.getValue();

                for (String value: values) {
                    if (count > k) {
                        break;
                    }
                    topHyponyms.add(value);
                    count++;
                }
            }

            // Sort the top k hyponyms alphabetically
            Collections.sort(topHyponyms);

            StringBuilder result = new StringBuilder("[");

            for (String hyponym : topHyponyms) {
                result.append(hyponym);
                result.append(", ");
            }

            if (result.length() > 1) {
                result.setLength(result.length() - 2); // Remove the trailing ", "
            }

            result.append("]");
            res = result.toString();
        } else {
            res = wordNet.printAllHyponyms(words);
        }

        return res;
    }
}
