package com.ordenacao.graphmaker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortComparisonChart extends JFrame {

    /**
     * Construtor que recebe um gráfico JFreeChart e o exibe em uma janela.
     * 
     * @param windowTitle O título da janela.
     * @param chart       O gráfico a ser exibido.
     */
    public SortComparisonChart(String windowTitle, JFreeChart chart) {
        super(windowTitle);
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    /**
     * NOVA FUNÇÃO: Cria um gráfico de linha XY com base nos dados e títulos
     * fornecidos.
     * 
     * @param dataset    O conjunto de dados (séries) a ser plotado.
     * @param title      O título do gráfico.
     * @param xAxisLabel O rótulo do eixo X.
     * @param yAxisLabel O rótulo do eixo Y.
     * @return um objeto JFreeChart configurado.
     */
    public static JFreeChart createChart(XYSeriesCollection dataset, String title, String xAxisLabel,
            String yAxisLabel) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, true, true, false);

        // Personaliza a legenda
        LegendTitle legend = chart.getLegend();
        if (legend != null) {
            legend.setPosition(RectangleEdge.BOTTOM); // Posição da legenda
            legend.setItemFont(new Font("Arial", Font.BOLD, 14)); // Fonte
            legend.setBackgroundPaint(Color.WHITE); // Fundo branco
            legend.setFrame(new BlockBorder(Color.GRAY)); // Borda cinza
        }

        return chart;
    }

    public static JFreeChart createChart(DefaultCategoryDataset dataset, String title, String xAxisLabel,
            String yAxisLabel) {
        JFreeChart chart = ChartFactory.createBarChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, true, true, false);
        return chart;
    }

}