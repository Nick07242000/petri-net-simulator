package org.nnf.pns.model.policy;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.function.Predicate;

public abstract class Policy {
    protected static final Logger log = Logger.getLogger(Policy.class);
    protected static Policy instance;
    protected int leftFiredTransitions = 0;
    protected int rightFiredTransitions = 0;

    public abstract int choose(List<Integer> transitions);

    protected int filterTransitions(List<Integer> transitions, Predicate<Integer> filter) {
        return transitions.stream()
                .filter(filter)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    protected void increaseCounter(int transition) {
        if (transition % 2 == 0) rightFiredTransitions++;
        else leftFiredTransitions++;

        log.debug("Branch executions: L[" + leftFiredTransitions + "] R[" + rightFiredTransitions + "]");
    }
}
