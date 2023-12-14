package org.nnf.pns;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;

import static org.nnf.pns.util.Constants.INCIDENCE_MATRIX;
import static org.nnf.pns.util.Constants.INITIAL_MARKING;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        PetriNet pn = new PetriNet(
                new Array2DRowRealMatrix(INCIDENCE_MATRIX),
                new Array2DRowRealMatrix(INITIAL_MARKING)
        );

        pn.getSensitizedTransitions();

        pn.fire(0);

        pn.getSensitizedTransitions();

        pn.fire(1);

        pn.getSensitizedTransitions();
    }
}
