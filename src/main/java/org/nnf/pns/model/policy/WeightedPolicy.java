package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class WeightedPolicy extends Policy {
    public static Policy getInstance() {
        if (instance == null) instance = new WeightedPolicy();
        return instance;
    }

    @Override
    public synchronized int choose(List<Integer> transitions, List<String> firedTransitions) {
        int leftTransition = filterTransitions(transitions, t -> t % 2 != 0);
        int rightTransition = filterTransitions(transitions, t -> t % 2 == 0);

        long leftFiredTransitions = getFiredTransitions(firedTransitions, t -> t.equals("T" + leftTransition));
        long rightFiredTransitions = getFiredTransitions(firedTransitions, t -> t.equals("T" + rightTransition));

        boolean determinator = transitions.contains(9) ?
                leftFiredTransitions >= 4 * rightFiredTransitions :
                leftFiredTransitions >= rightFiredTransitions;

        return determinator ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);
    }
}
