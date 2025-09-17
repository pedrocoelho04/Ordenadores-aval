// package com.example;

// import com.itextpdf.text.*;
// import com.itextpdf.text.pdf.PdfPSXObject;
// import com.itextpdf.text.pdf.PdfPTable;
// import com.itextpdf.text.pdf.PdfWriter;

// import java.awt.image.BufferedImage;
// import java.io.FileOutputStream;
// import java.util.Random;

// import org.jfree.chart.ChartFactory;
// import org.jfree.chart.JFreeChart;
// import org.jfree.data.xy.XYSeries;
// import org.jfree.data.xy.XYSeriesCollection;


// public class SortReportPDF {

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

//     public static void main(String[] args) {
//         XYSeries bubbleSeries = new XYSeries("BubbleSort");
//         XYSeries mergeSeries = new XYSeries("MergeSort");
//         int[] sizes = { 500, 1000, 2000, 4000, 8000 };
//         Random rand = new Random();
//         Runtime runtime = Runtime.getRuntime();

//         try {
//             Document document = new Document();
//             PdfWriter.getInstance(document, new FileOutputStream("Relatorio_Sorts.pdf"));
//             document.open();

//             // ---------- Cabeçalho ----------
//             Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
//             document.add(new Paragraph("Relatório Comparativo: BubbleSort vs MergeSort", titleFont));
//             document.add(new Paragraph(" "));
//             document.add(new Paragraph(
//                     "Este relatório apresenta uma análise teórica e empírica dos algoritmos BubbleSort e MergeSort."));
//             document.add(new Paragraph(" "));

//             // ---------- Análise Teórica ----------
//             document.add(new Paragraph("Análise Teórica:", titleFont));
//             document.add(new Paragraph(" - BubbleSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
//             document.add(new Paragraph(" - MergeSort: O(n log n) no pior caso, O(n) memória extra."));
//             document.add(new Paragraph(" "));

//             // ---------- Tabela de Resultados ----------
//             PdfPTable table = new PdfPTable(5);
//             table.addCell("n");
//             table.addCell("BubbleSort Tempo (ms)");
//             table.addCell("BubbleSort Memória (bytes)");
//             table.addCell("MergeSort Tempo (ms)");
//             table.addCell("MergeSort Memória (bytes)");

//             for (int n : sizes) {
//                 int[] arr1 = rand.ints(n, 0, n).toArray();
//                 int[] arr2 = arr1.clone();

//                 // BubbleSort
//                 runtime.gc();
//                 long memBefore = runtime.totalMemory() - runtime.freeMemory();
//                 long start = System.nanoTime();
//                 bubbleSort(arr1);
//                 long end = System.nanoTime();
//                 bubbleSeries.add(n, (end - start) / 1e6); // ms
//                 long memAfter = runtime.totalMemory() - runtime.freeMemory();
//                 long bubbleTime = (end - start) / 1_000_000;
//                 long bubbleMem = Math.max(memAfter - memBefore, 0);

//                 // MergeSort
//                 runtime.gc();
//                 memBefore = runtime.totalMemory() - runtime.freeMemory();
//                 start = System.nanoTime();
//                 mergeSort(arr2, 0, arr2.length - 1);
//                 end = System.nanoTime();
//                 mergeSeries.add(n, (end - start) / 1e6); // ms
//                 memAfter = runtime.totalMemory() - runtime.freeMemory();
//                 long mergeTime = (end - start) / 1_000_000;
//                 long mergeMem = Math.max(memAfter - memBefore, 0);

//                 // Adiciona na tabela
//                 table.addCell(String.valueOf(n));
//                 table.addCell(String.valueOf(bubbleTime));
//                 table.addCell(String.valueOf(bubbleMem));
//                 table.addCell(String.valueOf(mergeTime));
//                 table.addCell(String.valueOf(mergeMem));
//             }

//             document.add(table);

//             XYSeriesCollection dataset = new XYSeriesCollection();
//             dataset.addSeries(bubbleSeries);
//             dataset.addSeries(mergeSeries);

//             JFreeChart chart = ChartFactory.createXYLineChart(
//                 "Comparação BubbleSort vs MergeSort",
//                 "Tamanho do vetor (n)",
//                 "Tempo (ms)",
//                 dataset
//             );

//             // Render chart to image and add to PDF
//             BufferedImage bufferedImage = chart.createBufferedImage(500, 400);
//             Image chartImage = Image.getInstance(bufferedImage, null);
//             document.add(chartImage);

//             // ---------- Conclusão ----------
//             document.add(new Paragraph(" "));
//             document.add(new Paragraph("Conclusão:", titleFont));
//             document.add(new Paragraph(
//                     "O BubbleSort cresce quadraticamente em tempo, tornando-se impraticável para grandes valores de n, "
//                             +
//                             "mas consome pouca memória. Já o MergeSort é significativamente mais rápido em grandes entradas, "
//                             +
//                             "embora utilize memória auxiliar proporcional ao tamanho do vetor."));

//             document.close();
//             System.out.println("Relatório PDF gerado com sucesso: Relatorio_Sorts.pdf");

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }
