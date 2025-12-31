package org.example;

public class ExcelFileReader {

    public static void main(String args[]) {
        String s = "\"hello world\"";
        System.out.println(s);
        if(s.startsWith("\"")) {
            s = s.substring(1);
        }
        if(s.endsWith("\"")) {
            s = s.substring(0,s.length()-1);
        }
        System.out.println(s);
    }

}
