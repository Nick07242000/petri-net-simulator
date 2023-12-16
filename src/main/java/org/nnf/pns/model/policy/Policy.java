package org.nnf.pns.model.policy;

import java.util.List;

public interface Policy {
    int choose(List<Integer> transitions);
}
