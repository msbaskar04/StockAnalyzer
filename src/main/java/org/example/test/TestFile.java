package org.example.test;

import java.util.Arrays;
import java.util.List;

public class TestFile {


    public static void main(String args[]) {
        List<String> list = Arrays.asList("".split(","));
        String a="a", b=null;
        System.out.println(list.contains(a));
        System.out.println(list.contains(b));
    }
}
