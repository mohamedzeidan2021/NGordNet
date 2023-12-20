package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap map;

    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        String response = "";

        for (int word = 0; word < q.words().size(); word++) {
            TimeSeries ts = map.weightHistory(q.words().get(word), q.startYear(), q.endYear());
            response += q.words().get(word) + ": " + ts.toString() + "\n";
        }

        return response;
    }
}
