package org.example;

public class Stock implements Comparable<Stock>{
    String symbol;
    double open;
    double high;
    double low;
    double prevClose;
    double ltp;
    double change;
    double percentageChange;
    double volume;
    double valueInCr;
    double high52W;
    double low52W;
    double change30Days;
    double change365Days;

    double thresholdDiff;
    double diffFrom52WHigh;
    double diffFrom52WLow;

    public double getDiffFrom52WLow() {
        return diffFrom52WLow;
    }

    public void setDiffFrom52WLow(double diffFrom52WLow) {
        this.diffFrom52WLow = diffFrom52WLow;
    }

    public double getDiffFrom52WHigh() {
        return diffFrom52WHigh;
    }

    public void setDiffFrom52WHigh(double diffFrom52WHigh) {
        this.diffFrom52WHigh = diffFrom52WHigh;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(double prevClose) {
        this.prevClose = prevClose;
    }

    public double getLtp() {
        return ltp;
    }

    public void setLtp(double ltp) {
        this.ltp = ltp;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getValueInCr() {
        return valueInCr;
    }

    public void setValueInCr(double valueInCr) {
        this.valueInCr = valueInCr;
    }

    public double getHigh52W() {
        return high52W;
    }

    public void setHigh52W(double high52W) {
        this.high52W = high52W;
    }

    public double getLow52W() {
        return low52W;
    }

    public void setLow52W(double low52W) {
        this.low52W = low52W;
    }

    public double getChange30Days() {
        return change30Days;
    }

    public void setChange30Days(double change30Days) {
        this.change30Days = change30Days;
    }

    public double getChange365Days() {
        return change365Days;
    }

    public void setChange365Days(double change365Days) {
        this.change365Days = change365Days;
    }

    @Override
    public int compareTo(Stock stock) {
        if(this.getThresholdDiff() >= stock.getThresholdDiff()) {
            return -1;
        }
        return 1;
    }

    public double getThresholdDiff() {
        return thresholdDiff;
    }

    public void setThresholdDiff(double thresholdDiff) {
        this.thresholdDiff = thresholdDiff;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", prevClose=" + prevClose +
                ", ltp=" + ltp +
                ", change=" + change +
                ", percentageChange=" + percentageChange +
                ", volume=" + volume +
                ", valueInCr=" + valueInCr +
                ", high52W=" + high52W +
                ", low52W=" + low52W +
                ", change30Days=" + change30Days +
                ", change365Days=" + change365Days +
                ", thresholdDiff=" + thresholdDiff +
                ", diffFrom52WHigh=" + diffFrom52WHigh +
                ", diffFrom52WLow=" + diffFrom52WLow +
                '}';
    }
}
