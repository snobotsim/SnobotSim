package com.snobot.simulator.gui.motor_display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.snobot.simulator.DcMotorModelConfig;


public class MotorCurveDisplay extends JPanel
{
    private ValueMarker mRpmMarker;

    public MotorCurveDisplay(DcMotorModelConfig aModel)
    {
        this(aModel.NOMINAL_VOLTAGE, aModel.FREE_SPEED_RPM, aModel.STALL_CURRENT, aModel.FREE_CURRENT, aModel.STALL_TORQUE);
    }

    public MotorCurveDisplay(double aNominalVoltage, double aFreeSpeedRpm, double aStallCurrent, double aFreeCurrent, double aStallTorque)
    {
        XYSeriesCollection series = new XYSeriesCollection();

        double currentSlope = (aFreeCurrent - aStallCurrent) / aFreeSpeedRpm;
        double torqueSlope = (0 - aStallTorque) / aFreeSpeedRpm;

        XYSeries currentPoints = new XYSeries("Current");
        XYSeries torquePoints = new XYSeries("Torque");
        XYSeries powerPoints = new XYSeries("Power");
        XYSeries efficiencyPoints = new XYSeries("Efficiency");

        series.addSeries(currentPoints);
        series.addSeries(torquePoints);
        series.addSeries(powerPoints);
        series.addSeries(efficiencyPoints);

        for (int rpm = 0; rpm < aFreeSpeedRpm; rpm += 10)
        {
            double omega = 2 * rpm * Math.PI / 60;
            double current = aStallCurrent + rpm * currentSlope;
            double torque = aStallTorque + rpm * torqueSlope;
            double input_power = aNominalVoltage * current;
            double output_power = torque * omega;
            double efficiency = output_power / input_power * 100;

            currentPoints.add(rpm, current);
            torquePoints.add(rpm, torque);
            powerPoints.add(rpm, output_power);
            efficiencyPoints.add(rpm, efficiency);
        }

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Motion Profile", 
                "Time (sec)", 
                "Data", 
                series, 
                PlotOrientation.VERTICAL, true, 
                true,
                false);


        mRpmMarker = new ValueMarker(1300);
        mRpmMarker.setPaint(Color.black);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.addDomainMarker(mRpmMarker);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        setLayout(new BorderLayout());
        add(chartPanel);
    }

    public void setCurrentRpm(double aRpm)
    {
        mRpmMarker.setPaint(Color.black);
        mRpmMarker.setValue(aRpm);
    }

}
