package com.ordenacao.pdfmaker;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPSXObject;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ordenacao.algoritimos.Bubble;
import com.ordenacao.algoritimos.Heap;
import com.ordenacao.algoritimos.Insertion;
import com.ordenacao.algoritimos.Merge;
import com.ordenacao.algoritimos.Quick;
import com.ordenacao.algoritimos.Selection;
import com.ordenacao.entidadesAuxiliares.AlgoritmoDeOrdenacao;
import com.ordenacao.entidadesAuxiliares.SortTime;
import com.ordenacao.graphmaker.SortComparisonChart;
import com.ordenacao.operations.CalculoTempo;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SortReportPDF {

    private static void configurarTabelaPadrao(PdfPTable table) {
        try {
            table.setWidthPercentage(100); // Tabela ocupa 100% da largura
            table.setSpacingBefore(10f);   // Espaço antes da tabela
            table.setSpacingAfter(10f);    // Espaço depois da tabela
            
            // Opcional: definir larguras das colunas
            float[] columnWidths = new float[]{10f, 15f, 15f, 15f, 15f, 15f, 15f};
            table.setWidths(columnWidths);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static Document documentoAnalise(SortTime[] sortTime, int lenght, Image chartImage) {

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Relatorio_Sorts.pdf"));
            document.open();

            // ---------- Cabeçalho ----------
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            document.add(new Paragraph("Relatório Comparativo de algoritmos", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "Este relatório apresenta uma análise teórica e empírica dos algoritmos de ordenação."));
            document.add(new Paragraph(" "));

            // ---------- Análise Teórica ----------
            document.add(new Paragraph("Análise Teórica:", titleFont));
            document.add(new Paragraph(" - BubbleSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" - HeapSort: O(n log n) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" - InsertionSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" - MergeSort: O(n log n) no pior caso, O(n) memória extra."));
            document.add(new Paragraph(" - QuickSort: O(n^2) no pior caso, O(n) memória extra (devido à recursão)."));
            document.add(new Paragraph(" - SelectionSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(7);

            configurarTabelaPadrao(table);

            // ---------- Tabela de Resultados ----------
            table.addCell("n");
            table.addCell("BubbleSort Tempo (ns)");
            table.addCell("MergeSort Tempo (ns)");
            table.addCell("HeapSort Tempo (ns)");
            table.addCell("InsertionSort Tempo (ns)");
            table.addCell("QuickSort Tempo (ns)");
            table.addCell("SelectionSort Tempo (ns)");

            table.addCell(String.valueOf(lenght));
            table.addCell(String.valueOf(sortTime[0]));
            table.addCell(String.valueOf(sortTime[1]));
            table.addCell(String.valueOf(sortTime[2]));
            table.addCell(String.valueOf(sortTime[3]));
            table.addCell(String.valueOf(sortTime[4]));
            table.addCell(String.valueOf(sortTime[5]));

            document.add(table);
            document.add(chartImage);

            document.close();
            System.out.println("Relatório PDF gerado com sucesso: Relatorio_Sorts.pdf");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException | IOException e) {
            System.err.println("Erro ao gerar relatório PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao gerar relatório", e);
        }

        return null;

    }

    public static void testePerformance() {
        XYSeries bubbleSeries;
        XYSeries heapSeries;
        XYSeries insertionSeries;
        XYSeries mergeSeries;
        XYSeries quickSeries;
        XYSeries selectionSeries;
        String[] groups = {
                "Crescente C/Repeticao",
                "Decrescente C/Repeticao",
                "Aleatorio C/Repeticao",
                "Crescente S/Repeticao",
                "Decrescente S/Repeticao",
                "Aleatorio S/Repeticao"
        };
        int[] sizes = { 100000, 160000, 220000, 280000, 340000, 400000, 460000, 520000, 580000, 640000, 700000 };
        //int[] sizes = { 10000, 16000, 22000, 28000, 34000, 40000, 46000, 52000, 58000, 64000, 70000 };
        Random rand = new Random();
        Runtime runtime = Runtime.getRuntime();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Relatorio_Sorts.pdf"));
            document.open();

            // ---------- Cabeçalho ----------
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            document.add(new Paragraph("Relatório Comparativo de algoritmos", titleFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "Este relatório apresenta uma análise teórica e empírica dos algoritmos de ordenação."));
            document.add(new Paragraph(" "));

            // ---------- Análise Teórica ----------
            document.add(new Paragraph("Análise Teórica:", titleFont));
            document.add(new Paragraph(" - BubbleSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" - HeapSort: O(n log n) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" - InsertionSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" - MergeSort: O(n log n) no pior caso, O(n) memória extra."));
            document.add(new Paragraph(" - QuickSort: O(n^2) no pior caso, O(n) memória extra (devido à recursão)."));
            document.add(new Paragraph(" - SelectionSort: O(n^2) no pior caso, O(1) memória extra (in-place)."));
            document.add(new Paragraph(" "));
            PdfPTable table;

            for (String j : groups) {
                bubbleSeries = new XYSeries("BubbleSort");
                heapSeries = new XYSeries("HeapSort");
                insertionSeries = new XYSeries("InsertionSort");
                mergeSeries = new XYSeries("MergeSort");
                quickSeries = new XYSeries("QuickSort");
                selectionSeries = new XYSeries("SelectionSort");
                table = new PdfPTable(7);
                configurarTabelaPadrao(table);
                document.add(new Paragraph(" "));

                document.add(new Paragraph(j));
                document.add(new Paragraph(" "));
                // ---------- Tabela de Resultados ----------
                table.addCell("n");
                table.addCell("BubbleSort Tempo (ns)");
                table.addCell("MergeSort Tempo (ns)");
                table.addCell("HeapSort Tempo (ns)");
                table.addCell("InsertionSort Tempo (ns)");
                table.addCell("QuickSort Tempo (ns)");
                table.addCell("SelectionSort Tempo (ns)");
                for (int n : sizes) {
                    // Gerar numeros
                    int[] arr1 = {};

                    switch (j) {
                        case "Crescente C/Repeticao":
                            arr1 = rand.ints(n, 0, n / 10).boxed().sorted().mapToInt(Integer::intValue).toArray();
                            break;
                        case "Descrescente C/Repeticao":
                            // Primeiro gera ordem crescente
                            arr1 = rand.ints(n, 0, n / 10).boxed().sorted().mapToInt(Integer::intValue).toArray();
                            // Depois inverte manualmente
                            for (int i = 0; i < arr1.length / 2; i++) {
                                int temp = arr1[i];
                                arr1[i] = arr1[arr1.length - 1 - i];
                                arr1[arr1.length - 1 - i] = temp;
                            }
                            break;
                        case "Aleatorio C/Repeticao":
                            arr1 = rand.ints(n, 0, n / 10).toArray();
                            break;
                        case "Crescente S/Repeticao":
                            List<Integer> lista = IntStream.range(0, n).boxed().collect(Collectors.toList());
                            arr1 = lista.stream().mapToInt(Integer::intValue).toArray();
                            break;
                        case "Descrescente S/Repeticao":
                            arr1 = IntStream.range(0, n)
                                    .map(i -> n - 1 - i) // Gera diretamente em ordem decrescente
                                    .toArray();
                            break;
                        case "Aleatorio S/Repeticao":
                            List<Integer> lista3 = IntStream.range(0, n).boxed().collect(Collectors.toList());
                            Collections.shuffle(lista3);
                            arr1 = lista3.stream().mapToInt(Integer::intValue).toArray();
                            break;
                        default:
                            break;
                    }

                    int[] arr2 = arr1.clone();

                    long bubbleTime = CalculoTempo.calcularTempo(runtime, arr2, bubbleSeries, n, new Bubble());

                    arr2 = arr1.clone();

                    long mergeTime = CalculoTempo.calcularTempo(runtime, arr2, mergeSeries, n, new Merge());

                    arr2 = arr1.clone();

                    long heapTime = CalculoTempo.calcularTempo(runtime, arr2, heapSeries, n, new Heap());

                    arr2 = arr1.clone();

                    long insertionTime = CalculoTempo.calcularTempo(runtime, arr2, insertionSeries, n, new Insertion());

                    arr2 = arr1.clone();

                    long quickTime = CalculoTempo.calcularTempo(runtime, arr2, quickSeries, n, new Quick());

                    arr2 = arr1.clone();

                    long selectionTime = CalculoTempo.calcularTempo(runtime, arr2, selectionSeries, n, new Selection());

                    // Adiciona na tabela
                    table.addCell(String.valueOf(n));
                    table.addCell(String.valueOf(bubbleTime));
                    table.addCell(String.valueOf(mergeTime));
                    table.addCell(String.valueOf(heapTime));
                    table.addCell(String.valueOf(insertionTime));
                    table.addCell(String.valueOf(quickTime));
                    table.addCell(String.valueOf(selectionTime));
                }

                document.add(table);
                document.newPage();
                XYSeriesCollection dataset = new XYSeriesCollection();
                dataset.addSeries(bubbleSeries);
                dataset.addSeries(mergeSeries);
                dataset.addSeries(heapSeries);
                dataset.addSeries(insertionSeries);
                dataset.addSeries(quickSeries);
                dataset.addSeries(selectionSeries);

                JFreeChart chart = SortComparisonChart.createChart(dataset, j, "Tamanho do vetor (n)", "Tempo (ns)");

                // Render chart to image and add to PDF
                BufferedImage bufferedImage = chart.createBufferedImage(500, 400);
                Image chartImage = Image.getInstance(bufferedImage, null);
                document.add(chartImage);
                document.add(new Paragraph(" "));

                dataset.removeAllSeries();
            }

            // ---------- Conclusão ----------
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Conclusão:", titleFont));
            document.add(new Paragraph("Qual algoritmo apresenta melhor desempenho com dados ordenados de forma crescente?"));
            document.add(new Paragraph("Em uma serie de dados crescente com valores repetidos, de inicio ao Fim, o InsertionSort teve mais sucesso que os outros algoritmos em menor quantidade de tempo. Porem sem repetição desses dados, o BubbleSort teve mais sucesso em menor tempo."));
            document.add(new Paragraph("Qual é o mais eficiente com dados ordenados de forma decrescente?"));
            document.add(new Paragraph("Em uma serie de dados decrescente, os dois algoritmos que mais destacaram foi o MergeSort e Quicksort. Sendo o MergeSort o mais estável nos dados com repetição e sem repetição."));
            document.add(new Paragraph("Qual algoritmo é mais estável em relação ao tempo de execução, independentemente da organização dos dados?"));
            document.add(new Paragraph("O tempo de execução do HeapSort e do MergeSort permanece consistentemente baixo e com um crescimento suave, sem os picos extremos vistos em outros algoritmos."));

            document.close();
            
            System.out.println("Relatório PDF gerado com sucesso: Relatorio_Sorts.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}