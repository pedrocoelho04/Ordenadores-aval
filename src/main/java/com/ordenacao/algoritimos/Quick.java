package com.ordenacao.algoritimos;

public class Quick {
  public static void sort(int[] arr) {
    if (arr == null || arr.length <= 1) return;
    sort(arr, 0, arr.length - 1);
  }

  private static void sort(int[] arr, int inicio, int fim) {
    if (inicio < fim) {
      int pivo = arr[fim];
      int i = inicio - 1;

      for (int j = inicio; j < fim; j++) {
        if (arr[j] <= pivo) {
          i++;
          int temp = arr[i];
          arr[i] = arr[j];
          arr[j] = temp;
        }
      }

      int temp = arr[i + 1];
      arr[i + 1] = arr[fim];
      arr[fim] = temp;

      int pivoIndex = i + 1;

      sort(arr, inicio, pivoIndex - 1);
      sort(arr, pivoIndex + 1, fim);
    }
  }
}