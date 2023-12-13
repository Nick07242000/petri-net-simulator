package org.nnf.pns.model.policy;

public interface Policy {
    int choose(int[] transitions);
}
