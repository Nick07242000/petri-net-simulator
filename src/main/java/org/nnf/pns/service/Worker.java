package org.nnf.pns.service;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.util.Constants;

import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@AllArgsConstructor
public class Worker implements Runnable {
    private final Monitor monitor;
    private final int[] transition;
    private static final Logger log = Logger.getLogger(PetriNet.class);


    @Override
    public void run() {

        while(true) {
            for(int i=0; i< Constants.TRANSITIONS_COUNT; i++) {
                if(transition[i]==1) {
                    log.debug("Hilo " + Thread.currentThread().getName() + " disparando transicion " + i);
                    monitor.fireTransition(i, false);
                }
            }
        }



    }
}
