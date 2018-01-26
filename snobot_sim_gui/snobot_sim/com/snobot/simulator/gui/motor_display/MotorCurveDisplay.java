package com.snobot.simulator.gui.motor_display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.snobot.simulator.motor_sim.DcMotorModelConfig;


public class MotorCurveDisplay extends JPanel
{
    private ValueMarker mRpmMarker;

    protected final XYSeries mCurrentPoints;
    protected final XYSeries mTorquePoints;
    protected final XYSeries mPowerPoints;
    protected final XYSeries mEfficiencyPoints;
    protected final JFreeChart mChart;
    
    protected final int mNumPoints;

    public MotorCurveDisplay()
    {
        mNumPoints = 600;

        mCurrentPoints = new XYSeries("Current");
        mTorquePoints = new XYSeries("Torque");
        mPowerPoints = new XYSeries("Power");
        mEfficiencyPoints = new XYSeries("Efficiency");

        XYSeriesCollection series = new XYSeriesCollection();
        series.addSeries(mCurrentPoints);
        series.addSeries(mTorquePoints);
        series.addSeries(mPowerPoints);
        series.addSeries(mEfficiencyPoints);

        mChart = ChartFactory.createXYLineChart(
                "Unknown Motor", 
                "RPM", 
                "Data", 
                series, 
                PlotOrientation.VERTICAL, true, 
                true,
                false);


        mRpmMarker = new ValueMarker(0);
        mRpmMarker.setPaint(Color.black);

        XYPlot plot = (XYPlot) mChart.getPlot();
        plot.addDomainMarker(mRpmMarker);

        ChartPanel chartPanel = new ChartPanel(mChart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        setLayout(new BorderLayout());
        add(chartPanel);
    }

    public void setCurveParams(DcMotorModelConfig aModel)
    {
        setCurveParams(aModel.mFactoryParams.mMotorType, aModel.mMotorParams.NOMINAL_VOLTAGE, aModel.mMotorParams.FREE_SPEED_RPM,
                aModel.mMotorParams.STALL_CURRENT, aModel.mMotorParams.FREE_CURRENT, aModel.mMotorParams.STALL_TORQUE);
    }

    public void setCurveParams(String aMotorName, double aNominalVoltage, double aFreeSpeedRpm, double aStallCurrent, double aFreeCurrent, double aStallTorque)
    {
        SwingUtilities.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                mChart.setTitle(aMotorName);

                mCurrentPoints.clear();
                mTorquePoints.clear();
                mPowerPoints.clear();
                mEfficiencyPoints.clear();


                double currentSlope = (aFreeCurrent - aStallCurrent) / aFreeSpeedRpm;
                double torqueSlope = (0 - aStallTorque) / aFreeSpeedRpm;

                int dRpm = (int) Math.ceil(aFreeSpeedRpm / mNumPoints);

                for (int rpm = 0; rpm < aFreeSpeedRpm; rpm += dRpm)
                {
                    addPoint(aNominalVoltage, aStallCurrent, aStallTorque, rpm, currentSlope, torqueSlope);
                }

                // Add the last point always
                addPoint(aNominalVoltage, aStallCurrent, aStallTorque, (int) aFreeSpeedRpm, currentSlope, torqueSlope);
            }
        });
    }
    
    private void addPoint(
            double aNominalVoltage, double aStallCurrent, double aStallTorque, 
            int rpm, double currentSlope, double torqueSlope)
    {
        double omega = 2 * rpm * Math.PI / 60;
        double current = aStallCurrent + rpm * currentSlope;
        double torque = aStallTorque + rpm * torqueSlope;
        double input_power = aNominalVoltage * current;
        double output_power = torque * omega;
        double efficiency = output_power / input_power * 100;

        mCurrentPoints.add(rpm, current);
        mTorquePoints.add(rpm, torque);
        mPowerPoints.add(rpm, output_power);
        mEfficiencyPoints.add(rpm, efficiency);
    }

    public void setCurrentRpm(double aRpm)
    {
        mRpmMarker.setPaint(Color.black);
        mRpmMarker.setValue(aRpm);
    }

}
