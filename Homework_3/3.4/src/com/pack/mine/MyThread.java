package com.pack.mine;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    private int startX;
    private int endX;
    private char[] x;
    private int[] phases;
    private int[][] extras;
    private int right = 0;
    private int left = 0;

    private final int L = 0;
    private final int R = 1;

    private CyclicBarrier barrier;

    private int id;

    MyThread(int id, char[] x, int x1, int x2, CyclicBarrier barrier, int[] phases, int[][] ex) {

        this.id = id;
        this.x = x;
        startX = x1;
        endX = x2;
        this.phases = phases;

        this.barrier = barrier;
        extras = ex;
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
        for (int i = startX; i <= endX; i++) {
            if (x[i] == ')' && left == 0) {
                right++;
            } else if (x[i] == ')' && left != 0) {
                left--;
            } else {
                left++;
            }
        }

        extras[L][id] = left;
        extras[R][id] = right;
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

            funcForCollectPhase(id - dist, id);
            phases[id] ++;

            dist *= 2;
        }
    }

    private void funcForCollectPhase(int id1, int id2) {
        right = 0;
        left = 0;
        if (extras[L][id1] >= extras[R][id2]) {
            left = extras[L][id1] - extras[R][id2] + extras[L][id2];
            right = extras[R][id1];
        } else {
            right = extras[R][id2] - extras[L][id1] + extras[R][id1];
            left = extras[L][id2];
        }
        extras[L][id2] = left;
        extras[R][id2] = right;
    }

}
