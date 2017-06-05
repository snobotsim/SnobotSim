package com.snobot.simulator.gui.motor_display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MotorComparision extends JPanel
{

    public void plotSeries(XYSeriesCollection series)
    {

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Motion Profile", 
                "Time (sec)", 
                "Data", 
                series, 
                PlotOrientation.VERTICAL, 
                true, 
                true,
                false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        setLayout(new BorderLayout());
        add(chartPanel);
    }

    public static Map<String, XYSeries> getSeries(String prefix, String aFilename) throws IOException
    {
        Map<String, XYSeries> output = new HashMap<>();

        XYSeries positionSeries = new XYSeries(prefix + "Position");
        XYSeries velocitySeries = new XYSeries(prefix + "Velocity");
        XYSeries currentSeries = new XYSeries(prefix + "Current");

        output.put("Position", positionSeries);
        output.put("Velocity", velocitySeries);
        output.put("Current", currentSeries);

        BufferedReader br = new BufferedReader(new FileReader(aFilename));

        String line;

        while ((line = br.readLine()) != null)
        {
            String[] parts = line.split(",");

            double dt = Double.parseDouble(parts[0]);
            double pos = Double.parseDouble(parts[1]);
            double vel = Double.parseDouble(parts[2]);
            double cur = Double.parseDouble(parts[3]);

            positionSeries.add(dt, pos);
            velocitySeries.add(dt, vel);
            currentSeries.add(dt, cur);
        }

        br.close();

        return output;
    }
}
