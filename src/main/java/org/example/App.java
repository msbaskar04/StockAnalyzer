package org.example;

import java.awt.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String args[]) throws IOException, AWTException, InterruptedException {
        /*StockProcessor stockProcessor = new StockProcessor();
        stockProcessor.execute(6, "02-Jul-2024");*/
        //stockProcessor.execute(6, null);
        Robot robot = new Robot();
        Random random = new Random();
        while (true) {
            robot.mouseMove(random.nextInt(400)+500, random.nextInt(400)+300);
            Thread.sleep(20000);
        }

    }
}
