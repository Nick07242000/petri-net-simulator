package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy extends Policy {
    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public int choose(List<Integer> transitions) {
        if (transitions.size() == 1)
            return transitions.get(0);

        if (transitions.contains(1) && transitions.contains(2))
            return which(1,2);

        if (transitions.contains(5) && transitions.contains(6))
            return which(5,6);

        if (transitions.contains(9) && transitions.contains(10))
            return which(9,10);

        return transitions.get(transitions.size() - 1);
    }

    @Override
    public boolean canExecute(int transition) {
        switch(transition) {
            case 1:
            case 2:
                return which(1,2) == transition;
            case 5:
            case 6:
                return which(5,6) == transition;
            case 9:
            case 10:
                return which(9,10) == transition;
            default:
                return true;
        }
    }
}
