package org.nnf.pns.model;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;
import org.nnf.pns.util.Constants;

import java.util.*;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.stream;
import static org.nnf.pns.util.Concurrency.delay;
import static org.nnf.pns.util.Constants.*;
import static org.nnf.pns.util.Net.createSequence;
import static org.nnf.pns.util.Net.stringifyArray;

@AllArgsConstructor
public class PetriNet {
    private static final Logger log = Logger.getLogger(PetriNet.class);

    private final RealMatrix incidenceMatrix;
    private RealMatrix currentMarking;
    private long[] timeStamp;
    private long currentPeriod;

    public synchronized boolean fire(int... transitions) {
        log.debug("Current Marking: " + stringifyArray(currentMarking.getRow(0)));
        log.debug("Firing transitions: " + Arrays.toString(transitions));

        if (!areSensitized(transitions)) {
            log.debug("Transitions not sensitized: fire cannot be performed");
            return false;
        }
        int[] sensitizedBeforeFire=getSensitizedTransitions();
        RealMatrix sequenceMatrix = new Array2DRowRealMatrix(createSequence(transitions));

        currentMarking = currentMarking
                .transpose()
                .add(incidenceMatrix.multiply(sequenceMatrix))
                .getColumnMatrix(0)
                .transpose();
        int[] sensitizedAfterFire=getSensitizedTransitions();
        resetSensitizedTime(sensitizedBeforeFire, sensitizedAfterFire);
        log.debug("New Marking: " + stringifyArray(currentMarking.getRow(0)));
        return true;
    }

    private void resetSensitizedTime(int[] sensitizedBeforeFire, int[] sensitizedAfterFire) {
        for(int i=0; i<TRANSITIONS_COUNT; i++){
            if(sensitizedBeforeFire[i]==0&&sensitizedAfterFire[i]==1){
                setSenzitizedTransition(i, true);
            }else if(sensitizedBeforeFire[i]==1&&sensitizedAfterFire[i]==0){
                setSenzitizedTransition(i, false);
            }
        }
    }

    public boolean areSensitized(int... transitions) {
        return stream(transitions)
                .mapToObj(this::isSensitized)
                .allMatch(b -> b.equals(TRUE));
    }

    public synchronized long timeWindowTest(int transition) {
        if(!isTemporary(transition))return 0;
        currentPeriod=getCurrentPeriod(transition);
        log.debug("CurrentPeriod of transition "+transition+" : " + currentPeriod);
        if(currentPeriod<ALFA) return ALFA-currentPeriod;
        if(currentPeriod<BETA)return 0;
        return -1;
    }

    public boolean isSensitized(int transition) {
        for (int i = 0; i < PLACES_COUNT; i++) {
            if (incidenceMatrix.getEntry(i, transition) == -1 && currentMarking.getEntry(0, i) < 1)
                return false;
        }
        return true;
    }

    public boolean isTemporary(int transition){
        for (int temporaryTransition : TEMPORARY_TRANSITIONS) {
            if(temporaryTransition==transition) return true;
        }
        return false;
    }

    public int[] getSensitizedTransitions() {
        int[] sensitizedTransitions = new int[TRANSITIONS_COUNT];

        for (int i = 0; i < TRANSITIONS_COUNT; i++) {
            sensitizedTransitions[i] = isSensitized(i) ? 1 : 0;
        }

        log.debug("Sensitized Transitions: " + stringifyArray(sensitizedTransitions));

        return sensitizedTransitions;
    }

    public List<Integer> getSensitizedTransitionNumbers() {
        int[] sensitizedTransitions = getSensitizedTransitions();
        List<Integer> indexTransitions = new ArrayList<>();

        for (int i = 1; i < TRANSITIONS_COUNT; i++) {
            if (sensitizedTransitions[i] == 1) {
                indexTransitions.add(i);
            }
        }

        return indexTransitions;
    }
     public long getTimeStamp(int transition){
        return timeStamp[transition];
     }
    public boolean hasInitialState() {
        return deepEquals(currentMarking.getData(), INITIAL_MARKING);
    }

    public void setSenzitizedTransition(int transition, boolean sensitizedNow) {
       if(sensitizedNow) timeStamp[transition]=System.currentTimeMillis();
       else timeStamp[transition]=0;
    }
    public long getCurrentPeriod(int transition){
        return System.currentTimeMillis()-timeStamp[transition];
    }

}
