package com.pack.mine;

import java.util.concurrent.CyclicBarrier;

public class Sum {
    private int numOfThread;
    private int[] a;
    private int[] b;
    private int[][] pairs;
    private int xn;
    private int res;

    Sum(String  a, String b,  int numOfThread, int xn) {
        this.a = stringToArray(a);
        this.b = stringToArray(b);
        this.numOfThread = numOfThread;
        this.xn = xn;
        res = 0;
    }

    public int getXnConcurrently() {

        MyThread[] threads = new MyThread[numOfThread];
        CyclicBarrier barrier = new CyclicBarrier(numOfThread);
        pairs = new int[2][numOfThread];
        int length = xn / numOfThread;
        int[] phases = new int[numOfThread];

        for (int i = 0; i < numOfThread; i ++) {
            if (i != numOfThread - 1) {
                threads[i] = new MyThread(i, a, b, i * length, (i + 1) * length - 1, barrier, pairs, phases);
            } else {
                threads[i] = new MyThread(i, a, b,i * length, xn - 1, barrier, pairs, phases);
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

        return pairs[1][numOfThread - 1];
    }

    public int getXn() {
        res = b[0];
        for (int i = 1; i < xn; i++) {
            res = a[i] * res + b[i];
        }
        return res;
    }

    private int[] stringToArray(String a) {
        int i = 0;
        int num = a.split(" ").length;
        int[] x = new int[num];
        for (String s : a.split(" ")){
            x[i] = Integer.parseInt(s);
            i++;
        }
        return x;
    }
}
