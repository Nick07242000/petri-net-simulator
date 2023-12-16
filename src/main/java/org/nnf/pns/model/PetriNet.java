package org.nnf.pns.model;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.stream;
import static org.nnf.pns.util.Constants.*;
import static org.nnf.pns.util.Net.createSequence;
import static org.nnf.pns.util.Net.stringifyArray;

@AllArgsConstructor
public class PetriNet {
    private static final Logger log = Logger.getLogger(PetriNet.class);

    private final RealMatrix incidenceMatrix;
    private RealMatrix currentMarking;

    public synchronized void fire(int... transitions) {
        log.debug("Current Marking: " + stringifyArray(currentMarking.getRow(0)));
        log.debug("Firing transitions: " + Arrays.toString(transitions));

        if (!areSensitized(transitions)) {
            log.error("Transitions not sensitized: fire cannot be performed");
            return;
        }

        RealMatrix sequenceMatrix = new Array2DRowRealMatrix(createSequence(transitions));

        currentMarking = currentMarking
                .transpose()
                .add(incidenceMatrix.multiply(sequenceMatrix))
                .getColumnMatrix(0)
                .transpose();

        log.debug("New Marking: " + stringifyArray(currentMarking.getRow(0)));

        log.info("T" + Arrays.toString(transitions));
    }

    public boolean areSensitized(int... transitions) {
        return stream(transitions)
                .mapToObj(this::isSensitized)
                .allMatch(b -> b.equals(TRUE));
    }

    public boolean isSensitized(int transition) {
        for (int i = 0; i < PLACES_COUNT; i++) {
            if (incidenceMatrix.getEntry(i, transition) == -1 && currentMarking.getEntry(0, i) < 1)
                return false;
        }
        return true;
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

        log.debug("Sensitized Transitions: " + indexTransitions);
        return indexTransitions;
    }


    public boolean isFinished() {
        return deepEquals(currentMarking.getData(), INITIAL_MARKING);
    }

}
