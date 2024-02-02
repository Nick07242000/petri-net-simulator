package org.nnf.pns;

import org.apache.log4j.Logger;
import org.nnf.pns.model.policy.BalancedPolicy;
import org.nnf.pns.model.policy.Policy;
import org.nnf.pns.model.policy.WeightedPolicy;
import org.nnf.pns.service.Monitor;

import java.util.Scanner;

import static java.lang.System.currentTimeMillis;
import static org.nnf.pns.util.Concurrency.createGenerator;
import static org.nnf.pns.util.Concurrency.createWorker;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Monitor monitor = Monitor.getInstance(getPolicy());

        log.debug("Application starting...");
        log.debug("Start time: "+currentTimeMillis());
        createWorker("PURPLE", monitor, 1, 3);
        createWorker("PINK", monitor, 2, 4);
        createWorker("YELLOW", monitor, 5, 7);
        createWorker("GREEN", monitor, 6, 8);
        createWorker("BROWN", monitor, 9, 11);
        createWorker("LIGHT-BLUE", monitor, 10, 12);
        createWorker("MAGENTA", monitor, 13, 14);

        createGenerator(monitor);
    }

    private static Policy getPolicy() {
        Scanner scanner = new Scanner(System.in);
        log.debug("=================================");
        log.debug("Please select the desired policy:");
        log.debug("1 : Balanced 50 - 50");
        log.debug("2 : Weighted 80 - 20");
        log.debug("=================================");
        return scanner.nextInt() == 1 ?
                BalancedPolicy.getInstance() :
                WeightedPolicy.getInstance();
    }
}
