package org.example;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class NewStockSorter {


    public static void main(String args[]) throws IOException {
        String nifty50File = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-50-31-Dec-2025.xlsx";
        String niftyNext50File = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-NEXT-50-31-Dec-2025.xlsx";
        String niftMidcap50File = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-MIDCAP-50-31-Dec-2025.xlsx";
        String niftMidcap150File = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-MIDCAP-150-31-Dec-2025.xlsx";

        String nifty50DestinationFile = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-50-31-Dec-2025.xlsx";
        String niftyNext50DestinationFile = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-NEXT-50-31-Dec-2025.xlsx";
        String niftMidcap50DestinationFile = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-MIDCAP-50-31-Dec-2025.xlsx";
        String niftMidcap150DestinationFile = "C:\\Users\\s484618\\Documents\\Xavier\\MyFolder\\JAVA\\StockAnalyzer\\excelfiles\\MW-NIFTY-MIDCAP-150-31-Dec-2025.xlsx";

        NewStockSorter newStockSorter = new NewStockSorter();
        newStockSorter.compute(nifty50File, nifty50DestinationFile);
        newStockSorter.compute(niftyNext50File, niftyNext50DestinationFile);
        newStockSorter.compute(niftMidcap50File, niftMidcap50DestinationFile);
        newStockSorter.compute(niftMidcap150File, niftMidcap150DestinationFile);
    }

    public void compute(String filePath, String destinationFile) throws IOException {
        List<Stock> stockList = readExcel(filePath);
        stockSorter(stockList);
        writeToExcel(stockList, filePath, destinationFile);
    }

    public List readExcel(String filePath) throws IOException {
        List<Stock> stockList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        for(int i = 2; i<=lastRow; i++) {
            Stock stock = getStockFromRow(sheet.getRow(i));
            stockList.add(stock);
        }
        return stockList;
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
        index++;
        stock.setChange(getNumeric(row.getCell(index++)));
        stock.setPercentageChange(getNumeric(row.getCell(index++)));
        stock.setVolume(getNumeric(row.getCell(index++)));
        stock.setValueInCr(getNumeric(row.getCell(index++)));
        stock.setHigh52W(getNumeric(row.getCell(index++)));
        stock.setLow52W(getNumeric(row.getCell(index++)));
        stock.setChange30Days(getNumeric(row.getCell(index++)));
        stock.setChange365Days(getNumeric(row.getCell(index++)));
        stock.setDiffFrom52WHigh(calculate52WeekDownPercentage(stock.getHigh52W(), stock.getLtp()));
        stock.setDiffFrom52WLow(calculate52WeekLowPercentage(stock.getLow52W(), stock.getLtp()));
        return stock;
    }

    private double calculate52WeekDownPercentage(double high52W, double ltp) {
        double onePercent = high52W/100;
        return ltp/onePercent;
    }

    private double calculate52WeekLowPercentage(double low52W, double ltp) {
        double onePercent = low52W/100;
        return ltp/onePercent;
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

    private void stockSorter(List<Stock> stockList) {
        stockList.sort(Comparator.comparing(Stock::getDiffFrom52WHigh));
    }

    private void writeToExcel(List<Stock> stockList, String filePath, String destinationFile) throws FileNotFoundException {

        try(FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis)
        ) {
            // Create a new sheet
            Sheet sheet = workbook.createSheet("Result1");
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Symbol");
            headerRow.createCell(1).setCellValue("LTP");
            headerRow.createCell(2).setCellValue("High52W");
            headerRow.createCell(3).setCellValue("DiffFrom52WHigh");
            headerRow.createCell(4).setCellValue("Low52W");
            headerRow.createCell(4).setCellValue("DiffFrom52WLow");


            int rowNum =1;
            for(Stock stock:stockList) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                row.createCell(colNum++).setCellValue(stock.getSymbol());

                row.createCell(colNum++).setCellValue(stock.getLtp());

                row.createCell(colNum++).setCellValue(stock.getHigh52W());

                row.createCell(colNum++).setCellValue(stock.getDiffFrom52WHigh());

                row.createCell(colNum++).setCellValue(stock.getLow52W());

                row.createCell(colNum++).setCellValue(stock.getDiffFrom52WLow());

            }

            // Write back to the same file
            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                workbook.write(fos);
            }

        } catch(IOException ex)  {
            ex.printStackTrace();
        }

    }
}
