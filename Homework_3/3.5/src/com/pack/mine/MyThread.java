package com.pack.mine;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    private int startX;
    private int endX;

    private double[] ang;              //angles
    private double[] len;              //lengths
    private double[] c;                //for collect and distribute phases of summarize angles
    private int[] phases;
    private boolean[] readiness;
    private double[][] coordForThr;    //for collect phase of summarize coordinates

    private CyclicBarrier barrier;

    private int id;
    private int numOfThreads;

    MyThread(int id, int numOfThreads, double[] a, double[] l, int x1, int x2, CyclicBarrier barrier, int[] phases,
             boolean[] readiness,  double[] c, double[][] coordForTr) {

        this.id = id;
        this.numOfThreads = numOfThreads;
        ang = a;
        len = l;
        startX = x1;
        endX = x2;

        this.c = c;
        this.phases = phases;

        this.barrier = barrier;
        this.readiness = readiness;
        this.coordForThr = coordForTr;
    }

    @Override
    public void run() {
//use an algorithm to find the full angles of rotations
        prepareForAng();
        waiting();

        collectPhase();
        waiting();

        distributePhase();
        waiting();

        updAngles();
        waiting();

//translate displacement vectors from the polar coordinate system to Cartesian system of coordinates and summarize
        prepareForCoord();
        waiting();

        collectPhaseForCoord();
        waiting();
    }

    private void waiting() {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void prepareForAng() {
        for (int i = startX; i <= endX; i++) {
            c[id] += ang[i];
            ang[i] = c[id];
        }
    }

    private void collectPhase() {
        int dist = 1;

        while(id - dist >= 0) {

            if ((id + 1) % (dist * 2) != 0) {
                return;
            }

            while (phases[id] != phases[id - dist]) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            c[id] = (c[id] + c[id - dist]) % 360;
            phases[id] ++;

            dist *= 2;
        }
    }

    private void distributePhase() {
        int dist;

        if (id % 2 == 0) {
            readiness[id] = true;
            return;
        }

        if (id == numOfThreads - 1) {
            c[id] = 0;
            phases[id]--;
            dist = (int)Math.pow(2, phases[id]);
            readiness[id] = true;
        } else {
            dist = (int)Math.pow(2, phases[id] - 1);
            readiness[id] = true;
        }

        while (phases[id] >= 0) {

            if (phases[id - dist] == phases[id] && readiness[id - dist]) {
                double temp = c[id - dist];
                c[id - dist] = c[id];
                c[id] += temp;

                phases[id]--;
                phases[id - dist]--;

                dist /= 2;
            } else {
                try{
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updAngles() {
        for (int i = startX; i <= endX; i++) {
            ang[i] = (ang[i] + c[id]) % 360;
        }
    }

    private void prepareForCoord() {
        for (int i = startX; i <= endX; i++) {
            coordForThr[0][id] += Math.round(len[i] * Math.cos(Math.toRadians(ang[i])));
            coordForThr[1][id] += Math.round(len[i] * Math.sin(Math.toRadians(ang[i])));
        }
    }

    private void collectPhaseForCoord() {
        phases[id] = 0;
        int dist = 1;

        while(id - dist >= 0) {

            if ((id + 1) % (dist * 2) != 0) {
                return;
            }

            while (phases[id] != phases[id - dist]) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            coordForThr[0][id] += coordForThr[0][id - dist];
            coordForThr[1][id] += coordForThr[1][id - dist];
            phases[id] ++;

            dist *= 2;
        }
    }
}
