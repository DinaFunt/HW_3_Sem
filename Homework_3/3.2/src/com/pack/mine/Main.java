package com.pack.mine;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int numOfThread = 8;

        Scanner in = new Scanner(System.in);
        System.out.print("Input numbers: \n");

        String num1 = in.nextLine();
        String num2 = in.nextLine();

        Sum s = new Sum(num1, num2, numOfThread);

        System.out.println(s.getSum());
        System.out.println(s.getSumConcurrently());
    }
}
