package org.nnf.pns.service;

import lombok.AllArgsConstructor;
import org.nnf.pns.util.Constants;

import static java.lang.Thread.sleep;

@AllArgsConstructor
public class Generator implements Runnable{
    private final Monitor monitor;


    @Override
    public void run() {
        int index=0;
        while (index< Constants.LIMIT_FIRING){
            monitor.fireTransition(0, false);
            /*try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            index++;
        }
    }
}
