package org.nnf.pns.model;

import lombok.AllArgsConstructor;
import org.ejml.simple.SimpleMatrix;


import java.util.Arrays;

import static org.ejml.dense.fixed.CommonOps_FDF2.mult;
import static org.ejml.dense.fixed.CommonOps_FDF2.transpose;
import static org.nnf.pns.util.Constants.PLACES_COUNT;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@AllArgsConstructor
public class PetriNet {
    private final SimpleMatrix incidenceMatrix;
    private final SimpleMatrix backwardMatrix;
    private double[][] currentMarking;
    public boolean isSensitized(int transition) {
        return getSensitizedTransitions()[transition] == 1;
    }

    public void fire(double[] transition) {
        SimpleMatrix marking = new SimpleMatrix(currentMarking);
        SimpleMatrix transitionSequence=new SimpleMatrix(transition);
        currentMarking=marking.plus(incidenceMatrix.mult(transitionSequence).transpose()).toArray2();
        System.out.println(Arrays.deepToString(currentMarking));
    }

    public int[] getSensitizedTransitions(){
        int[] sensitizedTransitions = new int[TRANSITIONS_COUNT];

        for(int i = 0; i < TRANSITIONS_COUNT; i++) {
            for(int j = 0; j < PLACES_COUNT; j++) {
                if (backwardMatrix.get(j,i) > 0 && currentMarking[0][i] < backwardMatrix.get(j,i)) {
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