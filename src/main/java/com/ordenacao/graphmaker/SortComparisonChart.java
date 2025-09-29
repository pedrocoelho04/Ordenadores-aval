package com.ordenacao.graphmaker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.ordenacao.algoritimos.Bubble;
import com.ordenacao.algoritimos.Merge;

import javax.swing.*;
import java.util.Random;

public class SortComparisonChart extends JFrame {

    /**
     * Construtor que recebe um gráfico JFreeChart e o exibe em uma janela.
     * @param windowTitle O título da janela.
     * @param chart O gráfico a ser exibido.
     */
    public SortComparisonChart(String windowTitle, JFreeChart chart) {
        super(windowTitle);
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    /**
     * NOVA FUNÇÃO: Cria um gráfico de linha XY com base nos dados e títulos fornecidos.
     * @param dataset O conjunto de dados (séries) a ser plotado.
     * @param title O título do gráfico.
     * @param xAxisLabel O rótulo do eixo X.
     * @param yAxisLabel O rótulo do eixo Y.
     * @return um objeto JFreeChart configurado.
     */
    public static JFreeChart createChart(XYSeriesCollection dataset, String title, String xAxisLabel, String yAxisLabel) {
        return ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset, null, false, false, false
        );
    }


    // ---------- MAIN ----------
    public static void main(String[] args) {
        // --- 1. PREPARAÇÃO DOS DADOS ---
        XYSeries bubbleSeries = new XYSeries("BubbleSort");
        XYSeries mergeSeries = new XYSeries("MergeSort");

        int[] sizes = { 100000, 160000, 220000, 280000, 340000, 400000, 460000, 520000, 580000, 640000, 700000 };
        Random rand = new Random();

        for (int n : sizes) {
            int[] arr1 = rand.ints(n, 0, n).toArray();
            int[] arr2 = arr1.clone();

            // BubbleSort
            long start = System.nanoTime();
            Bubble.sort(arr1);
            long end = System.nanoTime();
            bubbleSeries.add(n, (end - start) / 1e6); // ms

            // MergeSort
            start = System.nanoTime();
            Merge.mergeSort(arr2);
            end = System.nanoTime();
            mergeSeries.add(n, (end - start) / 1e6); // ms
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(bubbleSeries);
        dataset.addSeries(mergeSeries);

        // --- 2. CRIAÇÃO DO GRÁFICO (usando a nova função) ---
        JFreeChart chart = createChart(
                dataset,
                "Comparação BubbleSort vs MergeSort",
                "Tamanho do vetor (n)",
                "Tempo (ms)"
        );

        // --- 3. EXIBIÇÃO DA JANELA ---
        SwingUtilities.invokeLater(() -> {
            SortComparisonChart example = new SortComparisonChart("Desempenho de Algoritmos", chart);
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}