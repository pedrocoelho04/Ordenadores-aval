package com.ordenacao.algoritimos;

public class Quick {
  public static void sort(int[] arr) {
    if (arr == null || arr.length <= 1) return;
    sort(arr, 0, arr.length - 1);
  }


  private static void sort(int[] arr, int inicio, int fim) {
    if (inicio < fim) {
      // Escolhe pivô aleatório e troca com o fim
      int pivoRandom = inicio + (int)(Math.random() * (fim - inicio + 1));
      int temp = arr[fim];
      arr[fim] = arr[pivoRandom];
      arr[pivoRandom] = temp;
      int pivoIndex = partition(arr, inicio, fim);
      sort(arr, inicio, pivoIndex - 1);
      sort(arr, pivoIndex + 1, fim);
    }
  }

  private static int partition(int[] arr, int inicio, int fim) {
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
    return i + 1;
  }
}