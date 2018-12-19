package com.pack.mine;

import java.util.concurrent.CyclicBarrier;

public class Sum {
    private int numOfThread;
    private double[] ang;
    private double[] len;
    private int arraySize;

    Sum(double[] l, double[] a,  int numOfThread) {
        ang = a;
        len = l;
        this.numOfThread = numOfThread;
        arraySize = ang.length;
    }

    public double[] getLocationConcurrently() {

        MyThread[] threads = new MyThread[numOfThread];

        int length = arraySize / numOfThread;
        double[] c = new double[numOfThread];
        double[][] coordForThr = new double[2][numOfThread];

        int[] phases = new int[numOfThread];
        boolean readiness[] = new boolean[numOfThread];
        CyclicBarrier barrier = new CyclicBarrier(numOfThread);

        for (int i = 0; i < numOfThread; i ++) {
            if (i != numOfThread - 1) {
                threads[i] = new MyThread(i, numOfThread, ang, len, i * length, (i + 1) * length - 1,
                        barrier, phases, readiness, c, coordForThr);
            } else {
                threads[i] = new MyThread(i, numOfThread, ang, len,i * length, arraySize - 1,
                        barrier, phases, readiness, c, coordForThr);
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

        return new double[]{coordForThr[0][numOfThread - 1], coordForThr[1][numOfThread - 1]};
    }

    public double[] getLocation() {
        double x = Math.round(len[0] * Math.cos(Math.toRadians(ang[0])));
        double y = Math.round(len[0] * Math.sin(Math.toRadians(ang[0])));
        double a = ang[0];

        for (int i = 1; i < arraySize; i++) {
            a = (a + ang[i]) % 360;
            x +=  Math.round(len[i] * Math.cos(Math.toRadians(a)));
            y += Math.round(len[i] * Math.sin(Math.toRadians(a)));
        }

        return new double[]{x, y};
    }

}
