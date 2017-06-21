package com.snobot.simulator.gui.module_widget.settings;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SimpleSettingsDialog extends JDialog
{
    protected int mHandle;

    private JTextField mNameField;
    private JButton mSubmitButton;

    public SimpleSettingsDialog(String aTitle, int aHandle, String aName)
    {
        setTitle(aTitle);

        mHandle = aHandle;
        mNameField = new JTextField(aName);
        mSubmitButton = new JButton("Submit");
        

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(mNameField, BorderLayout.CENTER);
        contentPane.add(mSubmitButton, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    public void addSubmitListener(ActionListener aListener)
    {
        mSubmitButton.addActionListener(aListener);
    }

    public String getComponentName()
    {
        return mNameField.getText();
    }

}
