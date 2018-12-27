package com.pack.mine;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int numOfThread = 4;
        int xn;

        Scanner in = new Scanner(System.in);
        System.out.print("Input two arrays of  numbers: \n");

        String num1 = in.nextLine();
        String num2 = in.nextLine();
        xn = in.nextInt();

        Sum s = new Sum(num1, num2, numOfThread, xn);

        System.out.println(s.getXn());
        System.out.println(s.getXnConcurrently());
    }
}
