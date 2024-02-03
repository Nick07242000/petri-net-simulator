package org.nnf.pns.model;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.fill;
import static java.util.stream.IntStream.of;
import static org.nnf.pns.util.Constants.*;
import static org.nnf.pns.util.Net.createSequence;
import static org.nnf.pns.util.Net.stringifyArray;

public class PetriNet {
    private static final Logger log = Logger.getLogger(PetriNet.class);
    private final RealMatrix incidenceMatrix;
    private RealMatrix currentMarking;
    private final long[] timeStamps;

    public PetriNet() {
        this.incidenceMatrix = new Array2DRowRealMatrix(INCIDENCE_MATRIX);
        this.currentMarking = new Array2DRowRealMatrix(INITIAL_MARKING);
        this.timeStamps = new long[TRANSITIONS_COUNT];
        fill(this.timeStamps, 0);
        timeStamps[0] = currentTimeMillis();
    }

    public void fire(int transition) {
        log.debug("Current Marking: " + stringifyArray(currentMarking.getRow(0)));
        log.debug("Firing transition: " + transition);

        RealMatrix sequenceMatrix = new Array2DRowRealMatrix(createSequence(transition));

        int[] previouslySensitized = getSensitizedTransitions();

        currentMarking = currentMarking
                .transpose()
                .add(incidenceMatrix.multiply(sequenceMatrix))
                .getColumnMatrix(0)
                .transpose();

        log.debug("New Marking: " + stringifyArray(currentMarking.getRow(0)));

        setTimeStamps(previouslySensitized, getSensitizedTransitions());

        if (!arePInvariantValid())
            log.error("Fire error: P Invariants violated");
    }

    public boolean isSensitized(int transition) {
        for (int i = 0; i < PLACES_COUNT; i++)
            if (incidenceMatrix.getEntry(i, transition) == -1 && currentMarking.getEntry(0, i) < 1)
                return false;

        return true;
    }

    public boolean isTimed(int transition) {
        return TIMED_TRANSITIONS.keySet()
                .stream()
                .anyMatch(t -> t == transition);
    }

    public boolean isInWindow(int transition) {
        return currentTimeMillis() - timeStamps[transition] > TIMED_TRANSITIONS.get(transition);
    }

    public int getTimeDelay(int transition) {
        int time = (int) (currentTimeMillis() - timeStamps[transition]);

        return time > TIMED_TRANSITIONS.get(transition) ?
                0 : TIMED_TRANSITIONS.get(transition) - time;
    }

    private void setTimeStamps(int[] previouslySensitized, int[] currentlySensitized) {
        for (int i = 0; i < TRANSITIONS_COUNT; i++) {
            if (previouslySensitized[i] == 0 && currentlySensitized[i] == 1)
                timeStamps[i] = currentTimeMillis();
            else if (previouslySensitized[i] == 1 && currentlySensitized[i] == 0)
                timeStamps[i] = 0;
            else if (previouslySensitized[i] == 1 && currentlySensitized[i] == 1)
                timeStamps[i] = 0;
        }
    }

    public int[] getSensitizedTransitions() {
        int[] sensitizedTransitions = new int[TRANSITIONS_COUNT];

        for (int i = 0; i < TRANSITIONS_COUNT; i++)
            sensitizedTransitions[i] = isSensitized(i) ? 1 : 0;

        log.debug("Sensitized Transitions: " + stringifyArray(sensitizedTransitions));

        return sensitizedTransitions;
    }

    public List<Integer> getSensitizedTransitionNumbers() {
        int[] sensitizedTransitions = getSensitizedTransitions();
        List<Integer> indexTransitions = new ArrayList<>();

        for (int i = 1; i < TRANSITIONS_COUNT; i++)
            if (sensitizedTransitions[i] == 1)
                indexTransitions.add(i);

        return indexTransitions;
    }

    public boolean hasInitialState() {
        return deepEquals(currentMarking.getData(), INITIAL_MARKING);
    }

    public boolean arePInvariantValid() {
        BiPredicate<int[], Integer> evaluator = (marks, sum) -> of(marks)
                .map(m -> (int) currentMarking.getEntry(0,m))
                .sum() == sum;

        boolean ec1 = evaluator.test(new int[] {1,3},1);
        boolean ec2 = evaluator.test(new int[] {2,5},1);
        boolean ec4 = evaluator.test(new int[] {7,9},1);
        boolean ec5 = evaluator.test(new int[] {8,11},1);
        boolean ec7 = evaluator.test(new int[] {17,18},1);
        boolean ec3 = evaluator.test(new int[] {13,14,15},1);
        boolean ec6 = evaluator.test(new int[] {9,10,11},2);
        boolean ec8 = evaluator.test(new int[] {3,4,5,17},3);

        return ec1 && ec2 && ec3 && ec4 && ec5 && ec6 && ec7 && ec8;
    }
}
