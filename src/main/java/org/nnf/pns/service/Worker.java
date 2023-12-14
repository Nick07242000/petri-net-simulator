package org.nnf.pns.service;

import lombok.AllArgsConstructor;

import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@AllArgsConstructor
public class Worker implements Runnable {
    private final Monitor monitor;
    private final int[] transition;

    @Override
    public void run() {
        for (int i = 0; i < TRANSITIONS_COUNT; i++) {
            if (transition[i] == 1) {
                monitor.fireTransition(i, false);
            }
        }
    }
}
