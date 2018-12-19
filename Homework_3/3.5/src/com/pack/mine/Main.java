package com.pack.mine;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int numOfThread = 2;

        double[] len =  {40, 50, 40, 20};
        double[] ang = {45, 30, 105, 90};

        Sum s = new Sum(len, ang, numOfThread);

        System.out.println(Arrays.toString(s.getLocation()));
        System.out.println(Arrays.toString(s.getLocationConcurrently()));
    }
}
