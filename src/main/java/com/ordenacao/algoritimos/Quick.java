package com.ordenacao.algoritimos;

import com.ordenacao.entidadesAuxiliares.AlgoritmoDeOrdenacao;

public class Quick implements AlgoritmoDeOrdenacao{
  public void sort(int[] arr) {
    if (arr == null || arr.length <= 1) return;
    sort(arr, 0, arr.length - 1);
  }

  private static void sort(int[] arr, int inicio, int fim) {
    if (inicio < fim) {
        // Escolhe pivô aleatório
        int pivoIndex = inicio + (int)(Math.random() * (fim - inicio + 1));
        int temp = arr[pivoIndex];
        arr[pivoIndex] = arr[fim];
        arr[fim] = temp;

        int pivo = arr[fim];
        int i = inicio - 1;

        for (int j = inicio; j < fim; j++) {
            if (arr[j] <= pivo) {
                i++;
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        temp = arr[i + 1];
        arr[i + 1] = arr[fim];
        arr[fim] = temp;

        pivoIndex = i + 1;

        sort(arr, inicio, pivoIndex - 1);
        sort(arr, pivoIndex + 1, fim);
    }
  }
}