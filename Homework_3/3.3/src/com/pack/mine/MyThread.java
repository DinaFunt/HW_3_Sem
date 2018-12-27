package com.pack.mine;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    private int startX;
    private int endX;

    private int[] A;
    private int[] B;
    private int[][] c;
    private int[] phases;

    private CyclicBarrier barrier;

    private int id;

    MyThread(int id, int[] a, int[] b, int x1, int x2, CyclicBarrier barrier, int[][] c, int[] phases) {

        this.id = id;
        this.A = a;
        this.B = b;
        startX = x1;
        endX = x2;

        this.c = c;
        this.phases = phases;

        this.barrier = barrier;
    }

    @Override
    public void run() {

        prepare();
        waiting();

        collectPhase();
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

    private void prepare() {
        int a = A[startX];
        int b = B[startX];

        for (int i = startX + 1; i <= endX; i++) {
            a *= A[i];
            b = A[i] * b + B[i];
        }

        c[0][id] = a;
        c[1][id] = b;
    }

    private void collectPhase() {
        int dist = 1;

        while (id - dist >= 0) {

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

            function(id, dist);
            phases[id] ++;

            dist *= 2;
        }
    }

    private void function(int id, int dist) {
        c[1][id] = c[0][id] * c[1][id - dist] + c[1][id];
        c[0][id] *= c[0][id - dist];
    }
}
