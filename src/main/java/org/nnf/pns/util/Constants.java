package org.nnf.pns.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constants {
    public static final int PLACES_COUNT = 19;
    public static final int TRANSITIONS_COUNT = 15;
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
}
