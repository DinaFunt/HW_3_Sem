package com.pack.mine;

import java.util.concurrent.CyclicBarrier;

public class Sum {
    private int numOfThread;
    private int[] xs;
    private int[] ys;
    private int[] sum;
    private int arraySize;

    Sum(String x, String y,  int numOfThread) {
        int l1 = x.length();
        int l2 = y.length();
        if (l1 < l2) {
            arraySize = l2;
        } else {
            arraySize = l1;
        }
        xs = new int[arraySize + 1];
        ys = new int[arraySize + 1];
        getArrays(x, y);
        this.numOfThread = numOfThread;
    }

    private void getArrays(String s1, String s2) {
        String b = new StringBuffer(s1).reverse().toString();

        char[] digits1 = b.toCharArray();
        for (int i = 0; i < b.length(); i++) {
            digits1[i] -= '0';
            xs[i] = (digits1[i]);
        }

        String a = new StringBuffer(s2).reverse().toString();

        char[] digits2 = a.toCharArray();
        for (int i = 0; i < a.length(); i++) {
            digits2[i] -= '0';
            ys[i] = digits2[i];
        }
    }

    public String getSumConcurrently() {

        sum = new int[arraySize + 1];
        sum[arraySize] = 0;

        MyThread[] threads = new MyThread[numOfThread];

        int length = arraySize / numOfThread;
        int[] carries = new int[arraySize];
        int[] c = new int[numOfThread];
        int[] phases = new int[numOfThread];
        CyclicBarrier barrier = new CyclicBarrier(numOfThread);

        boolean readiness[] = new boolean[numOfThread];

        for (int i = 0; i < numOfThread; i ++) {
            if (i != numOfThread - 1) {
                threads[i] = new MyThread(i, numOfThread, ys, xs, i * length, (i + 1) * length - 1, carries, sum, barrier, c, phases, readiness);
            } else {
                threads[i] = new MyThread(i, numOfThread, ys, xs,i * length, arraySize - 1, carries, sum, barrier, c, phases, readiness);
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

        return resToString();
    }

    public String getSum() {
        sum = new int[arraySize + 1];
        sum[arraySize] = 0;

        int carry = 0;
        for(int i = 0; i < arraySize; i++) {
            sum[i] = ((xs[i] + ys[i] + carry) % 10);
            carry = ((xs[i] + ys[i] + carry) / 10);
        }
        sum[arraySize] = carry;
        return resToString();
    }

    private String resToString() {
        String s;
        if (sum[arraySize] == 0) {
            s = "";
        } else {
            s = String.valueOf(sum[arraySize]);
        }

        for (int i = arraySize - 1; i >=  0; i --) {
            s = s.concat(String.valueOf(sum[i]));
        }
        return s;
    }

}
