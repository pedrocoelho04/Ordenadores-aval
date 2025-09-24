package com.ordenacao.algoritimos;

public class Bubble {
  public static void sort(int[] array) {
    int length = array.length;
    boolean trocou;

    for (int i = 0; i < length - 1; i++) {
      trocou = false;
      for (int j = 0; j < length - 1 - i; j++) {
        if (array[j] > array[j + 1]) {
          int temp = array[j];
          array[j] = array[j + 1];
          array[j + 1] = temp;
          trocou = true;
        }
      }
      if (!trocou) break;
    }
  }
}