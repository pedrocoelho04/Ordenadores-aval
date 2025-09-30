package com.ordenacao.entidadesAuxiliares;

public class SortTime {
    final String name;
    final long time;

    public SortTime(String name, long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return time + " ns";
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }
}
