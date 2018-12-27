package com.pack.mine;

import java.util.concurrent.CyclicBarrier;

public class Sum {
    private int numOfThread;
    private char[] x;
    private int arraySize;


    Sum(String x,  int numOfThread) {
        this.x = x.toCharArray();
        arraySize = x.length();
        this.numOfThread = numOfThread;
    }

    public void goodStringConcurrently() {

        MyThread[] threads = new MyThread[numOfThread];

        int length = arraySize / numOfThread;
        int[] phases = new int[numOfThread];
        CyclicBarrier barrier = new CyclicBarrier(numOfThread);
        int[][] extra = new int[3][numOfThread];

        for (int i = 0; i < numOfThread; i ++) {
            if (i != numOfThread - 1) {
                threads[i] = new MyThread(i, x, i * length, (i + 1) * length - 1, barrier, phases, extra);
            } else {
                threads[i] = new MyThread(i, x, i * length, arraySize - 1, barrier, phases, extra);
            }
            threads[i].start();
        }

        for (int i = 0; i < numOfThread; i ++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (extra[0][numOfThread - 1] == 0 && extra[1][numOfThread - 1] == 0) {
            System.out.println("Good string");
        } else {
            System.out.println("Not a good string");
        }
    }

    public void goodString() {
        int right = 0;
        int left = 0;
        for (int i = 0; i < arraySize; i++) {
            if (x[i] == ')' && right == 0) {
                left++;
            } else if (x[i] == ')') {
                right--;
            } else {
                right++;
            }
        }

        if (left == 0 && right == 0) {
            System.out.println("Good string");
        } else {
            System.out.println("Not a good string");
        }
    }

}
