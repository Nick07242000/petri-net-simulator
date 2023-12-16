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
    public synchronized int choose(List<Integer> transitions) {
        //If no transitions are sensitized choose T0
        if (transitions.size() == 1)
            return transitions.get(0);

        //Balance branches
        int chosenTransition = leftBranchCount >= 4 * rightBranchCount ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);

        increaseCounter(chosenTransition);

        return chosenTransition;
    }
}
