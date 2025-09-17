// package com.example;

// import org.jfree.chart.ChartFactory;
// import org.jfree.chart.ChartPanel;
// import org.jfree.chart.JFreeChart;
// import org.jfree.data.xy.XYSeries;
// import org.jfree.data.xy.XYSeriesCollection;

// import javax.swing.*;
// import java.util.Random;

// public class SortComparisonChart extends JFrame {

//     // ---------- BUBBLESORT ----------
//     public static void bubbleSort(int[] arr) {
//         int n = arr.length;
//         boolean swapped;
//         for (int i = 0; i < n - 1; i++) {
//             swapped = false;
//             for (int j = 0; j < n - i - 1; j++) {
//                 if (arr[j] > arr[j + 1]) {
//                     int temp = arr[j];
//                     arr[j] = arr[j + 1];
//                     arr[j + 1] = temp;
//                     swapped = true;
//                 }
//             }
//             if (!swapped)
//                 break;
//         }
//     }

//     // ---------- MERGESORT ----------
//     public static void mergeSort(int[] arr, int left, int right) {
//         if (left < right) {
//             int mid = (left + right) / 2;
//             mergeSort(arr, left, mid);
//             mergeSort(arr, mid + 1, right);
//             merge(arr, left, mid, right);
//         }
//     }

//     private static void merge(int[] arr, int left, int mid, int right) {
//         int n1 = mid - left + 1;
//         int n2 = right - mid;

//         int[] L = new int[n1];
//         int[] R = new int[n2];

//         for (int i = 0; i < n1; ++i)
//             L[i] = arr[left + i];
//         for (int j = 0; j < n2; ++j)
//             R[j] = arr[mid + 1 + j];

//         int i = 0, j = 0, k = left;
//         while (i < n1 && j < n2) {
//             if (L[i] <= R[j])
//                 arr[k++] = L[i++];
//             else
//                 arr[k++] = R[j++];
//         }
//         while (i < n1)
//             arr[k++] = L[i++];
//         while (j < n2)
//             arr[k++] = R[j++];
//     }

//     // ---------- MAIN ----------
//     public SortComparisonChart() {
//         XYSeries bubbleSeries = new XYSeries("BubbleSort");
//         XYSeries mergeSeries = new XYSeries("MergeSort");

//         int[] sizes = { 500, 1000, 2000, 4000, 8000, 16000 };

//         Random rand = new Random();

//         for (int n : sizes) {
//             int[] arr1 = rand.ints(n, 0, n).toArray();
//             int[] arr2 = arr1.clone();

//             // BubbleSort
//             long start = System.nanoTime();
//             bubbleSort(arr1);
//             long end = System.nanoTime();
//             bubbleSeries.add(n, (end - start) / 1e6); // ms

//             // MergeSort
//             start = System.nanoTime();
//             mergeSort(arr2, 0, arr2.length - 1);
//             end = System.nanoTime();
//             mergeSeries.add(n, (end - start) / 1e6); // ms
//         }

//         XYSeriesCollection dataset = new XYSeriesCollection();
//         dataset.addSeries(bubbleSeries);
//         dataset.addSeries(mergeSeries);

//         JFreeChart chart = ChartFactory.createXYLineChart(
//                 "Comparação BubbleSort vs MergeSort",
//                 "Tamanho do vetor (n)",
//                 "Tempo (ms)",
//                 dataset);

//         ChartPanel panel = new ChartPanel(chart);
//         setContentPane(panel);
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             SortComparisonChart example = new SortComparisonChart();
//             example.setSize(800, 600);
//             example.setLocationRelativeTo(null);
//             example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//             example.setVisible(true);
//         });
//     }
// }
