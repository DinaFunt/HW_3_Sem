package com.pack.mine;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread {
    private int startX;
    private int endX;
    private static final int C = 2;
    private static final int M = 1;
    private static final int N = 0;

    private int[] a;                //the first number
    private int[] b;                //the second number
    private int[] c;                //array for work of threads in collect and distribute phases
    private int[] carries;          //carries
    private int[] sum;              //array for result
    private int[] phases;           //array for array for the correct working order of threads in collect phase
    private boolean[] readiness;    //array for telling other threads about readiness to work in distribute phase

    private CyclicBarrier barrier;

    private int id;
    private int numOfThreads;

    MyThread(int id, int numOfThreads, int[] ys, int[] xs, int x1, int x2,
             int[] carries, int[] sum, CyclicBarrier barrier, int[] c, int[] phases, boolean[] d) {

        this.id = id;
        this.numOfThreads = numOfThreads;
        a = xs;
        b = ys;
        startX = x1;
        endX = x2;

        this.carries = carries;
        this.sum = sum;
        this.c = c;
        this.phases = phases;

        this.barrier = barrier;
        this.readiness = d;
    }

    @Override
    public void run() {

        prepare();
        waiting();

        collectPhase();
        waiting();

        distributePhase();
        waiting();

        finalCarries();
        waiting();

        result();
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

//calculate the values for the array of the sum
// and summarize the section of the array for work of thread in collect and distribute phases
    private void prepare() {
        int s = M; // M - neutral element by addition
        int carr;

        for (int i = startX; i <= endX; i++) {
            carr = intToCarry(a[i] + b[i]);
            sum[i] = (a[i] + b[i]) % 10;
            s = sumCarries(s, carr);
            c[id] = s;
            carries[i] = s;
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

            c[id] = sumCarries(c[id - dist], c[id]);
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
            c[id] = M;
            phases[id]--;
            dist = (int)Math.pow(2, phases[id]);
            readiness[id] = true;
        } else {
            dist = (int)Math.pow(2, phases[id] - 1);
            readiness[id] = true;
        }

        while (phases[id] >= 0) {

            if (phases[id - dist] == phases[id] && readiness[id - dist]) {
                int temp = c[id - dist];
                c[id - dist] = c[id];
                c[id] = sumCarries(temp, c[id]);

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

//add to the array carries[] the amounts obtained in the previous phases
    private void finalCarries() {
        int num = c[id];

        for (int i = startX; i <= endX; i++) {
            carries[i] = sumCarries(num, carries[i]);
        }
    }

//update the result according to the carries[]
    private void result() {

        for (int i = startX; i <= endX; i++){
            if (i >= 0 && carries[i] == C) {
                sum[i + 1] = (sum[i + 1] + 1) % 10;
            }
        }
    }

//addition function in {M, N, C}
    private int sumCarries(int x, int y) {
        if (x + y * 3 >= 5) {
            return C;
        } else if (x + y * 3 < 4) {
            return N;
        }
        return M;
    }

//transfer of sum (= a[i] + b[i]) to carries
    private int intToCarry(int sum) {
        if (sum == 9) {
            return M;
        } else if (sum < 9) {
            return N;
        } else {
            return C;
        }
    }
}
