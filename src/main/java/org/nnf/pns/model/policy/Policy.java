package org.nnf.pns.model.policy;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.*;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.nnf.pns.util.Net.stringifyArray;

public abstract class Policy {
    protected static final Logger log = Logger.getLogger(Policy.class);
    protected int[] fires = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    protected static Policy instance;

    public abstract int choose(List<Integer> transitions);
    public abstract boolean canExecute(int transition);

    public void increaseCounter(int transition) {
        fires[transition]++;
        log.debug("Segment One: L[" + fires[1] + "] R[" + fires[2] + "]");
        log.debug("Segment Two: L[" + fires[5] + "] R[" + fires[6] + "]");
        log.debug("Segment Three: L[" + fires[9] + "] R[" + fires[10] + "]");
        log.debug("Segments: " + stringifyArray(fires));
    }

    protected int which(int one, int two) {
        return fires[one] >= fires[two] ? two : one;
    }
}
