package org.example;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StockProcessor {
    //private final String filePath = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\Sample\\excelfiles\\500\\MW-NIFTY-500-";
    private final String filePath = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\Sample\\excelfiles\\200\\MW-NIFTY-200-";

    //private final String filePath = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\Sample\\excelfiles\\100\\MW-NIFTY-100-";

    //private final String filePath = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\Sample\\excelfiles\\50\\MW-NIFTY-50-";

    private final static Map<String, Integer> months = new HashMap<>();

    static{
        months.put("Jan", 1);
        months.put("Feb", 2);
        months.put("Mar", 3);
        months.put("Apr", 4);
        months.put("May", 5);
        months.put("Jun", 6);
        months.put("Jul", 7);
        months.put("Aug", 8);
        months.put("Sep", 9);
        months.put("Oct", 10);
        months.put("Nov", 11);
        months.put("Dec", 12);
    }


    /**
     *
     * @param daysToMinus
     * @param date //date in dd-MMM-yyyy format
     * @throws IOException
     */
    public void execute(int daysToMinus, String date) throws IOException {
        getStocksAboveThreashold(date, daysToMinus, 5);
    }

    public Map getStockInformationForMinusDay(String filePath, String date, int daysToMinus) throws IOException {
        if(StringUtil.isNotBlank(date)) {
            String arr[] = date.split("-");
            int day = Integer.parseInt(arr[0]);
            int month = months.get(arr[1]);
            int year = Integer.parseInt(arr[2]);
            return readExcel(filePath + LocalDate.of(year, month, day)
                    .minusDays(daysToMinus).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))+".xlsx");
        }
        return readExcel(filePath + LocalDate.now().minusDays(daysToMinus).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))+".xlsx");
    }

    public Map getStockInformationForGivenDate(String filePath, String date) throws IOException {
        if(StringUtil.isBlank(date)) {
            return readExcel(filePath + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))+".xlsx");
        } else {
            return readExcel(filePath + date+".xlsx");
        }
    }

    public Map readExcel(String filePath) throws IOException {
        Map<String, Stock> map = new HashMap<>();
        FileInputStream fis = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        for(int i = 2; i<=lastRow; i++) {
            Stock stock = getStockFromRow(sheet.getRow(i));
            map.put(stock.getSymbol(), stock);
        }
        return map;
    }

    public LocalDate getDate() {
        LocalDate date = LocalDate.now();
        if(date.getDayOfWeek().name().equalsIgnoreCase("FRIDAY")) {
            return date.minusDays(5);
        } else {
            return date.minusDays(7);
        }
    }

    /**
     * Create and return a stock from the given excel row
     * @param row
     * @return
     */
    private Stock getStockFromRow(Row row) {
        Stock stock = new Stock();
        int index = 0;
        stock.setSymbol(row.getCell(index++).getStringCellValue());
        stock.setOpen(getNumeric(row.getCell(index++)));
        stock.setHigh(getNumeric(row.getCell(index++)));
        stock.setLow(getNumeric(row.getCell(index++)));
        stock.setPrevClose(getNumeric(row.getCell(index++)));
        stock.setLtp(getNumeric(row.getCell(index++)));
        stock.setChange(getNumeric(row.getCell(index++)));
        stock.setPercentageChange(getNumeric(row.getCell(index++)));
        stock.setVolume(getNumeric(row.getCell(index++)));
        stock.setValueInCr(getNumeric(row.getCell(index++)));
        stock.setHigh52W(getNumeric(row.getCell(index++)));
        stock.setLow52W(getNumeric(row.getCell(index++)));
        stock.setChange30Days(getNumeric(row.getCell(index++)));
        stock.setChange365Days(getNumeric(row.getCell(index++)));
        return stock;
    }

    /**
     * Return the given cell value as a double value
     * @param cell
     * @return
     */
    private double getNumeric(Cell cell) {
        if(cell.getCellType().name().equalsIgnoreCase("NUMERIC")) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType().name().equalsIgnoreCase("STRING")) {
            if(cell.getStringCellValue().equalsIgnoreCase("-")) {
                return 0;
            }
            return Double.parseDouble(cell.getStringCellValue().replaceAll(",",""));
        }
        return 0;
    }

    /**
     * Reurn the stocks that are above the given threshold for the given time period
     * @param date
     * @param daysToMinus
     * @throws IOException
     */
    private void getStocksAboveThreashold(String date, int daysToMinus, int threshold) throws IOException {
        Map oldMap = getStockInformationForMinusDay(filePath, date, daysToMinus);
        Map currentMap = getStockInformationForGivenDate(filePath, date);
        compareStocks(currentMap, oldMap, threshold);
    }

    private TreeSet<Stock> compareStocks(Map currentMap, Map oldMap, int threshold) {
        TreeSet<Stock> stockAboveThreshold = new TreeSet<>();
        if(MapUtils.isNotEmpty(currentMap) && MapUtils.isNotEmpty(oldMap)) {
            currentMap.keySet().stream().forEach(stockSymbol -> {
                Stock currentDateStock= (Stock)currentMap.get(stockSymbol);
                Stock oldDateStock = (Stock)oldMap.get(stockSymbol);
                if(oldDateStock != null) {
                    double oldLtp = oldDateStock.getLtp();
                    double currentLtp = currentDateStock. getLtp();
                    double diff = currentLtp - oldLtp;
                    if(diff > 0) {
                        double onePercent = oldLtp/100;
                        double diffInPercentage = diff/onePercent;
                        if(diffInPercentage >= threshold) {
                            currentDateStock.setThresholdDiff(diffInPercentage);
                            stockAboveThreshold.add(currentDateStock);
                        }
                    }
                }
            });
        }
        displayStockThreshold(stockAboveThreshold);
        return stockAboveThreshold;
    }

    private void displayStockThreshold(TreeSet set) {
        if(CollectionUtils.isNotEmpty(set)) {
            set.forEach(stock -> {
                System.out.println(((Stock)stock).getSymbol()+"=="+((Stock)stock).getThresholdDiff());
            });
        }
        System.out.println("Collected number of stocks above threshold==="+set.size());
    }
}
