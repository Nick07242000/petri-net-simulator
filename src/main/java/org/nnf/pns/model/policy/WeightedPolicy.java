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
    public int choose(List<Integer> transitions) {
        if (transitions.size() == 1)
            return transitions.get(0);

        boolean determinant = transitions.contains(9) ?
                leftFiredTransitions >= 4 * rightFiredTransitions :
                leftFiredTransitions >= rightFiredTransitions;

        return determinant ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);
    }
}
