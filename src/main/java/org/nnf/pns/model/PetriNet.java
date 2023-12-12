package org.nnf.pns.model;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;

import static org.nnf.pns.util.Constants.PLACES_COUNT;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@AllArgsConstructor
public class PetriNet {
    private final SimpleMatrix incidenceMatrix;
    private final SimpleMatrix backwardMatrix;
    private final double[] currentMarking;

    public boolean isSensitized(int transition) {
        return getSensitizedTransitions()[transition] == 1;
    }

    public void fire(int[] transition) {
        //TODO: Complete
    }

    public int[] getSensitizedTransitions(){
        int[] sensitizedTransitions = new int[TRANSITIONS_COUNT];

        for(int i = 0; i < TRANSITIONS_COUNT; i++) {
            for(int j = 0; j < PLACES_COUNT; j++) {
                if (backwardMatrix.get(j,i) > 0 && currentMarking[i] < backwardMatrix.get(j,i)) {
                    sensitizedTransitions[i] = 0;
                    break;
                } else {
                    sensitizedTransitions[i] = 1;
                }
            }
        }
        return sensitizedTransitions;
    }

}
