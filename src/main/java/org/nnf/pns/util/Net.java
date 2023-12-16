package org.nnf.pns.util;

import lombok.NoArgsConstructor;

import java.util.Arrays;

import static java.util.Arrays.fill;
import static java.util.Arrays.stream;
import static lombok.AccessLevel.PRIVATE;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@NoArgsConstructor(access = PRIVATE)
public final class Net {
    public static double[] createSequence(int... transitions) {
        double[] sequence = new double[TRANSITIONS_COUNT];

        fill(sequence, 0);

        stream(transitions)
                .forEach(t -> sequence[t] = 1);

        return sequence;
    }

    public static int[] castToInt(double[] array) {
        return stream(array)
                .mapToInt(n -> (int) n)
                .toArray();
    }

    public static String stringifyArray(double[] array) {
        return Arrays.toString(castToInt(array));
    }

    public static String stringifyArray(int[] array) {
        return Arrays.toString(array);
    }
}
