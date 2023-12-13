package org.nnf.pns;

import org.nnf.pns.model.policy.Policy;
import org.nnf.pns.model.policy.impl.BalancedPolicy;
import org.nnf.pns.service.Monitor;

public class Main {

    public static void main(String[] args) {
        Policy policy = BalancedPolicy.getInstance();
        Monitor monitor = Monitor.getInstance(policy);

        int[] t0 = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] t1 = {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] t2 = {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] t3_4 = {0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] t5 = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] t6 = {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] t7_8 = {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0};
        int[] t9 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        int[] t10 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
        int[] t11 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
        int[] t12 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
        int[] t13 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
        int[] t14 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};



        Thread thread0= new Thread(new Worker(monitor, t0));
        Thread thread1= new Thread(new Worker(monitor, t1));
        Thread thread2= new Thread(new Worker(monitor, t2));
        thread1.start();
        thread2.start();




    }
}
