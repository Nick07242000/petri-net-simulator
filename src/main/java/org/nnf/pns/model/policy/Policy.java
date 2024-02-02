package org.nnf.pns.model.policy;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

public abstract class Policy {
    protected static final Logger log = Logger.getLogger(Policy.class);
    protected static final Random random = new Random();
    protected static Policy instance;
    protected int leftFired = 0;
    protected int rightFired = 0;

    public abstract int choose(List<Integer> transitions);
}
