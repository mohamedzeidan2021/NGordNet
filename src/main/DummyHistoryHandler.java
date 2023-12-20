package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;

public class DummyHistoryHandler extends NgordnetQueryHandler {
    NGramMap map;

    public DummyHistoryHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        //initialize labels and graphs
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<TimeSeries> lts = new ArrayList<>();

        for (int word = 0; word < q.words().size(); word++) {
            labels.add(q.words().get(word));
            TimeSeries ts = map.weightHistory(q.words().get(word), q.startYear(), q.endYear());
            lts.add(ts);
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
