package org.nnf.pns.model.policy;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.function.Predicate;

public abstract class Policy {
    protected static final Logger log = Logger.getLogger(Policy.class);
    protected static Policy instance;

    public abstract int choose(List<Integer> transitions, List<String> firedTransitions);

    protected int filterTransitions(List<Integer> transitions, Predicate<Integer> filter) {
        return transitions.stream()
                .filter(filter)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    protected long getFiredTransitions(List<String> transitions, Predicate<String> filter) {
        return transitions.stream()
                .filter(filter)
                .count();
    }
}
