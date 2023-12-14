package org.nnf.pns.service;

import lombok.AllArgsConstructor;

import static java.lang.Thread.sleep;

@AllArgsConstructor
public class Generator implements Runnable{
    private final Monitor monitor;


    @Override
    public void run() {
        while(true) {
            monitor.fireTransition(0, false);
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
