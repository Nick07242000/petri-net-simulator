package org.nnf.pns;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.model.policy.BalancedPolicy;
import org.nnf.pns.model.policy.Policy;
import org.nnf.pns.service.Generator;
import org.nnf.pns.service.Monitor;
import org.nnf.pns.service.Worker;

import static org.nnf.pns.util.Constants.INCIDENCE_MATRIX;
import static org.nnf.pns.util.Constants.INITIAL_MARKING;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {


        Policy policy = BalancedPolicy.getInstance();
        Monitor monitor = Monitor.getInstance(policy);

        Thread Tazul = new Thread(new Generator(monitor));
        Thread Trosa = new Thread(new Worker(monitor, new int[] {0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        Thread Tvioleta = new Thread(new Worker(monitor, new int[] {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        Thread Tnaranja = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        Thread Tamarillo = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0}));
        Thread Tverde = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0}));
        Thread Trojo = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        Thread Tmarron = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0}));
        Thread Tceleste = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0}));
        Thread Tmagenta = new Thread(new Worker(monitor, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}));

        Tazul.start();
        System.out.println("Hilo generador iniciado");
        Trosa.start();
        Tvioleta.start();
        Tamarillo.start();
        Tverde.start();
        Tmarron.start();
        Tceleste.start();
        Tmagenta.start();
        System.out.println("Hilos iniciados: TODOS");

    }
}
