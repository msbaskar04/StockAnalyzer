package org.example;

import java.util.Comparator;

public class StockSorter implements Comparator<Stock> {

    @Override
    public int compare(Stock stock1, Stock stock2) {
        if(stock1.getLtp() >= stock2.getLtp()) {
            return -1;
        }
        return 1;
    }
}
