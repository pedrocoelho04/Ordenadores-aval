package com.ordenacao.operations;

import org.jfree.data.xy.XYSeries;

import com.ordenacao.entidadesAuxiliares.AlgoritmoDeOrdenacao;

public class CalculoTempo {
    public static long calcularTempo(Runtime runtime, int[] arr2, XYSeries series, int size, AlgoritmoDeOrdenacao algoritmo){
        runtime.gc();
        long start = System.nanoTime();
        algoritmo.sort(arr2);
        long end = System.nanoTime();
        series.add(size, (end - start) / 1e6); // ms
        long time = (end - start);

        return time;
    }
}
