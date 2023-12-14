package org.nnf.pns.service;

import lombok.AllArgsConstructor;
import org.nnf.pns.util.Constants;

import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@AllArgsConstructor
public class Worker implements Runnable {
    private final Monitor monitor;
    private final int[] transition;

    @Override
    public void run() {

        while(true) {
            System.out.println("Hilo " + Thread.currentThread().getName() + " iniciando");
            for(int i=0; i< Constants.TRANSITIONS_COUNT; i++) {
                if(transition[i]==1) {
                    monitor.fireTransition(i, false);
                }
            }
        }



    }
}
