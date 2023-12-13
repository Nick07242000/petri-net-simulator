package org.nnf.pns;

import lombok.AllArgsConstructor;
import org.nnf.pns.service.Monitor;
import org.nnf.pns.util.Constants;

@AllArgsConstructor
public class Worker implements Runnable {
    Monitor monitor;
    int[] transition;

    @Override
    public void run() {
        for(int i=0; i< Constants.TRANSITIONS_COUNT; i++) {
            if(transition[i]==1) {
                monitor.fireTransition(i, false);
            }
        }
    }
}
