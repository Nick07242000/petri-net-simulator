package org.nnf.pns;

import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;

import static org.nnf.pns.util.Constants.BACKWARDS_INCIDENCE_MATRIX;
import static org.nnf.pns.util.Constants.FORWARD_INCIDENCE_MATRIX;

public class Main {

    public static void main(String[] args) {
        SimpleMatrix forwardMatrix = new SimpleMatrix(FORWARD_INCIDENCE_MATRIX);
        SimpleMatrix backwardMatrix = new SimpleMatrix(BACKWARDS_INCIDENCE_MATRIX);

        SimpleMatrix transposed = forwardMatrix.transpose();

        System.out.println(Arrays.deepToString(forwardMatrix.toArray2()));
        System.out.println(backwardMatrix);
        System.out.println(transposed);
    }
}
