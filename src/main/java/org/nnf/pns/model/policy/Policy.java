package org.nnf.pns.model.policy;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

public abstract class Policy {
    protected static final Logger log = Logger.getLogger(Policy.class);
    protected static final Random random = new Random();
    protected static Policy instance;

    public abstract int choose(List<Integer> transitions, List<String> firedTransitions);

    protected long getFiredTransitions(List<String> transitions, List<Integer> side) {
        return transitions.stream()
                .map(t -> parseInt(t.substring(1)))
                .filter(side::contains)
                .count();
    }
}
