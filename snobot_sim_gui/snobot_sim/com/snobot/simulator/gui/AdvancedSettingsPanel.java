package com.snobot.simulator.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.snobot.simulator.gui.module_widget.settings.SpiAndI2CSettingsDialog;

public class AdvancedSettingsPanel extends JPanel
{
    private static final Logger sLOGGER = Logger.getLogger(AdvancedSettingsPanel.class);

    private JButton mSpiAndI2CSettingsButtion;

    public AdvancedSettingsPanel()
    {
        ImageIcon icon = null;
        try
        {
            Image img = ImageIO.read(getClass().getResource("/com/snobot/simulator/gui/module_widget/gear.png"));
            icon = new ImageIcon(img);
        }
        catch (IOException e)
        {
            sLOGGER.log(Level.ERROR, e);
        }

        mSpiAndI2CSettingsButtion = new JButton("SPI & I2C", icon);
        mSpiAndI2CSettingsButtion.setVisible(false);
        add(mSpiAndI2CSettingsButtion);

        mSpiAndI2CSettingsButtion.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {

                SpiAndI2CSettingsDialog dialog = new SpiAndI2CSettingsDialog("SPI and I2C");
                dialog.pack();
                dialog.setModal(true);
                dialog.setLocationRelativeTo(AdvancedSettingsPanel.this);
                dialog.setVisible(true);
            }
        });
    }

    public void showSettingsButtons(boolean aShow)
    {
        mSpiAndI2CSettingsButtion.setVisible(aShow);
    }

}
