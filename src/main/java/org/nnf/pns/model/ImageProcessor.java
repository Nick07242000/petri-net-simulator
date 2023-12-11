package org.nnf.pns.model;

public class ImageProcessor implements Runnable {
    Monitor monitor;
    int[] transitions;

    public ImageProcessor(int[] transitions){
        this.transitions=transitions;
        monitor=Monitor.getInstance();
    }
    @Override
    public void run() {
        for(int i=0; i<transitions.length;i++){
            monitor.fireTransition(transitions[i],false);
        }
    }
}
