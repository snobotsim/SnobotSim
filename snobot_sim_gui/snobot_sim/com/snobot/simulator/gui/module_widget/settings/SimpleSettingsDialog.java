package com.snobot.simulator.gui.module_widget.settings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class SimpleSettingsDialog extends JDialog
{
    protected int mHandle;

    private JTextField mNameField;
    private JButton mSubmitButton;

    public SimpleSettingsDialog(String aTitle, int aHandle, String aName)
    {
        setTitle(aTitle);

        mHandle = aHandle;
        mNameField = new JTextField(aName, 20);
        mSubmitButton = new JButton("Submit");

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Display Name"));
        namePanel.add(mNameField);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(namePanel, BorderLayout.NORTH);
        contentPane.add(mSubmitButton, BorderLayout.SOUTH);

        setContentPane(contentPane);

        mSubmitButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent aEvent)
            {
                onSubmit();
                dispose();
            }
        });
    }

    public String getComponentName()
    {
        return mNameField.getText();
    }

    protected abstract void onSubmit();

}
