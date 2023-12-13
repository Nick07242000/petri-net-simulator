package org.nnf.pns;

import org.nnf.pns.model.policy.Policy;
import org.nnf.pns.model.policy.impl.BalancedPolicy;
import org.nnf.pns.service.Monitor;

public class Main {

    public static void main(String[] args) {
        Policy policy = BalancedPolicy.getInstance();
        Monitor monitor = Monitor.getInstance(policy);
        System.out.println("First image");
        monitor.fireTransition(0,false);
    }
}
