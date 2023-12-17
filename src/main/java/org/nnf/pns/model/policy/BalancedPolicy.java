package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy extends Policy {
    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public synchronized int choose(List<Integer> transitions) {
        //Balance branches
        int chosen = leftBranchCount >= rightBranchCount ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);

        if (chosen == transitions.get(0))
            increaseCounter(transitions.get(0));

        return chosen;
    }
}
