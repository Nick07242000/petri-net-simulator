package org.nnf.pns;

import org.nnf.pns.model.policy.BalancedPolicy;
import org.nnf.pns.model.policy.Policy;
import org.nnf.pns.service.Monitor;

import static org.nnf.pns.util.Concurrency.createGenerator;
import static org.nnf.pns.util.Concurrency.createWorker;

public class Main {
    public static void main(String[] args) {
        Policy policy = BalancedPolicy.getInstance();
        Monitor monitor = Monitor.getInstance(policy);

        createGenerator(monitor);
        createWorker("PURPLE", monitor, 1, 3);
        createWorker("PINK", monitor, 2, 4);
        createWorker("ORANGE", monitor);
        createWorker("YELLOW", monitor, 5, 7);
        createWorker("GREEN", monitor, 6, 8);
        createWorker("RED", monitor);
        createWorker("BROWN", monitor, 9, 11);
        createWorker("LIGHT-BLUE", monitor, 10, 12);
        createWorker("MAGENTA", monitor, 13, 14);
    }
}
