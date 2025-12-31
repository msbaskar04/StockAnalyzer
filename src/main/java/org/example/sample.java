package org.example;

import java.util.Date;

public class sample {
    public static void main(String args[]) {
        String exp = "1762840068";
        System.out.println(Long.parseLong(exp));
        Date d = new Date(Long.parseLong(exp)*1000);
        System.out.println(d);
        if(d.getTime() < System.currentTimeMillis()) {
            System.out.println("Token expired");
        } else {
            System.out.println("Token not expired");
        }
    }
}
