package com.ordenacao.algoritimos;

public class Merge {
  public static void mergeSort(int[] arr){
    int lenght = arr.length;    
    if (lenght <= 1) return;

    int mei = lenght / 2;
    int[] esqArr = new int[mei];
    int[] dirArr = new int[lenght - mei];

    int i = 0;
    int j = 0;

    for(; i < lenght; i++){
      if (i < mei) {
        esqArr[i] = arr[i];
      } else {
        dirArr[j] = arr[i];
      }
    }

    mergeSort(esqArr);
    mergeSort(dirArr);

    merge(esqArr, dirArr, arr);
  }

  private static void merge(int[] esqArr, int[] dirArr, int[] arr) {
    int esqTam = arr.length/2;
    int dirTam = arr.length - esqTam;
    int i = 0, l = 0, r = 0;

    while (l < esqTam && r < dirTam) {
      if (esqArr[l] < dirArr[r]) {
        arr[i] = esqArr[l];
        i++;
        l++;
      } else {
        arr[i] = dirArr[r];
        i++;
        r++;
      }
    }
    
    while (l < esqTam){
      arr[i] = esqArr[l];
      i++;
      l++;
    }
    while (r < esqTam) {
      arr[i] = dirArr[r];
      i++;
      r++;
    }
  }
}
