package org.example;

import java.io.*;

public class JSReader {

    public static void readFile() throws IOException {
        File f = new File("C:\\Users\\s484618\\Documents\\RESConnect\\ResConnectUI\\resconnect-webapp\\ssui-core-web\\app\\scripts\\layout\\navigation\\services\\nav.service.js");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String s = null;
        String url = "masterdata/accesscontrol/pageGroups";
        while((s=br.readLine())!=null) {
            if(s.contains(url)) {
                System.out.println(s);
            }
        }
    }

    public static void main(String args[]) throws IOException {
        //readFile();
        int a =0;

    }

}

