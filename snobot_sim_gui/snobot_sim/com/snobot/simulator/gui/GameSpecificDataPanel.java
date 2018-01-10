package com.snobot.simulator.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.snobot.simulator.wrapper_accessors.DataAccessorFactory;
import com.snobot.simulator.wrapper_accessors.SimulatorDataAccessor.MatchType;

public class GameSpecificDataPanel extends JPanel
{
    private JComboBox<String> mGameDataCB;
    private JComboBox<MatchType> mMatchTypeCB;

    private ItemListener mItemListener = new ItemListener()
    {

        @Override
        public void itemStateChanged(ItemEvent e)
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                handleUpdate();
            }
        }
    };

    public GameSpecificDataPanel()
    {
        mGameDataCB = new JComboBox<>();
        mGameDataCB.addItem("LLL");
        mGameDataCB.addItem("LLR");
        mGameDataCB.addItem("LRL");
        mGameDataCB.addItem("RLL");
        mGameDataCB.addItem("RRL");
        mGameDataCB.addItem("LRR");
        mGameDataCB.addItem("RLR");
        mGameDataCB.addItem("RRR");
        mGameDataCB.addItemListener(mItemListener);

        mMatchTypeCB = new JComboBox<>();
        for (MatchType type : MatchType.values())
        {
            mMatchTypeCB.addItem(type);
        }
        mMatchTypeCB.addItemListener(mItemListener);

        add(mGameDataCB);
        add(mMatchTypeCB);

        handleUpdate();
    }

    private void handleUpdate()
    {
        DataAccessorFactory.getInstance().getSimulatorDataAccessor().setMatchInfo("EventName", (MatchType) mMatchTypeCB.getSelectedItem(), 0, 0,
                (String) mGameDataCB.getSelectedItem());
    }
}
