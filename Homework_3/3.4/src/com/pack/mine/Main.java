package com.pack.mine;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int numOfThread = 8;

        Scanner in = new Scanner(System.in);
        System.out.print("Input string: \n");

        String s = in.nextLine();

        Sum x = new Sum(s, numOfThread);

        x.goodString();
        x.goodStringConcurrently();
    }
}
