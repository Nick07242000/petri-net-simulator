package org.nnf.pns.model;

public class Policy {
    private static Policy instance;
    public Policy(){
    }

    public Policy getInstance(){
        if (instance == null) {
            instance = new Policy();
        }
        return instance;
    }

    public int whichChoose(int[] transitions){
        return 0;
    }
}
