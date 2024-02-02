package org.nnf.pns.util;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constants {
    public static final int PLACES_COUNT = 19;
    public static final int TRANSITIONS_COUNT = 15;
    public static final int MAX_GENERATED = 200;
    public static final double[][] INITIAL_MARKING = {{0,1,1,0,3,0,0,1,1,0,2,0,0,0,1,0,0,0,1}};
    public static final double[][] INCIDENCE_MATRIX = {
            {1,-1,-1,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,-1,0,1,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,-1,0,1,0,0,0,0,0,0,0,0,0,0},
            {0,1,0,-1,0,0,0,0,0,0,0,0,0,0,0},
            {0,-1,-1,1,1,0,0,0,0,0,0,0,0,-1,1},
            {0,0,1,0,-1,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,-1,-1,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,-1,0,1,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,-1,0,1,0,0,0,0,0,0},
            {0,0,0,0,0,1,0,-1,0,0,0,0,0,0,0},
            {0,0,0,0,0,-1,-1,1,1,0,0,0,0,0,0},
            {0,0,0,0,0,0,1,0,-1,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,1,1,-1,-1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,1,0,-1,0,0,0},
            {0,0,0,0,0,0,0,0,0,-1,-1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,1,0,-1,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,1,1,-1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,1,-1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,-1,1}
    };
    public static final Map<Integer,Integer> TIMED_TRANSITIONS;
    static {
        TIMED_TRANSITIONS = new HashMap<>();
        TIMED_TRANSITIONS.put(0,56);
        TIMED_TRANSITIONS.put(3,50);
        TIMED_TRANSITIONS.put(4,50);
        TIMED_TRANSITIONS.put(7,40);
        TIMED_TRANSITIONS.put(8,40);
        TIMED_TRANSITIONS.put(11,10);
        TIMED_TRANSITIONS.put(12,10);
        TIMED_TRANSITIONS.put(14,59);
    }
}
