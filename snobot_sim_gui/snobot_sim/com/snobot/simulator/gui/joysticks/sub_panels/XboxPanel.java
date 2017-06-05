package com.snobot.simulator.gui.joysticks.sub_panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.snobot.simulator.gui.Util;
import com.snobot.simulator.joysticks.IMockJoystick;

public class XboxPanel extends JPanel
{
    private static final Color sPRESSED_BTN_COLOR = new Color(0, 255, 0, 180);
    private static final Color sJOYSTICK_BACKGROUND = new Color(255, 255, 255, 180);

    private Image mControllerImage;
    private IMockJoystick mJoystick;

    public XboxPanel() throws IOException
    {
        URL url = getClass().getResource("/com/snobot/simulator/gui/joysticks/sub_panels/xbox_controller.png");
        mControllerImage = ImageIO.read(url);

        setPreferredSize(new Dimension(mControllerImage.getWidth(null), mControllerImage.getHeight(null)));
    }

    public void setJoystick(IMockJoystick aJoystick)
    {
        mJoystick = aJoystick;
    }

    public void updateDisplay()
    {
        repaint();
    }

    public void paint(Graphics g)
    {
        if (mControllerImage == null)
        {
            return;
        }

        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawImage(mControllerImage, 0, 0, null);

        colorButton(g, mJoystick.getRawButton(XboxButtonMap.X_BUTTON - 1), 490, 218);
        colorButton(g, mJoystick.getRawButton(XboxButtonMap.Y_BUTTON - 1), 550, 170);
        colorButton(g, mJoystick.getRawButton(XboxButtonMap.B_BUTTON - 1), 602, 215);
        colorButton(g, mJoystick.getRawButton(XboxButtonMap.A_BUTTON - 1), 540, 260);
        colorBumper(g, mJoystick.getRawButton(XboxButtonMap.LB_BUTTON - 1), 95, 95);
        colorBumper(g, mJoystick.getRawButton(XboxButtonMap.RB_BUTTON - 1), 510, 95);

        colorButton(g, mJoystick.getRawButton(XboxButtonMap.BACK_BUTTON - 1), 265, 220);
        colorButton(g, mJoystick.getRawButton(XboxButtonMap.START_BUTTON - 1), 415, 220);
        colorButton(g, mJoystick.getRawButton(XboxButtonMap.XBOX_BUTTON), 340, 217);

        drawJoystick(g, mJoystick.getRawButton(XboxButtonMap.L3_BUTTON - 1), mJoystick.getRawAxis(XboxButtonMap.LEFT_X_AXIS),
                mJoystick.getRawAxis(XboxButtonMap.LEFT_Y_AXIS), 115, 228);
        drawJoystick(g, mJoystick.getRawButton(XboxButtonMap.R3_BUTTON - 1), mJoystick.getRawAxis(XboxButtonMap.RIGHT_X_AXIS),
                mJoystick.getRawAxis(XboxButtonMap.RIGHT_Y_AXIS), 420, 330);

        drawTrigger(g, mJoystick.getRawAxis(XboxButtonMap.LEFT_TRIGGER), 155, 40);
        drawTrigger(g, mJoystick.getRawAxis(XboxButtonMap.RIGHT_TRIGGER), 530, 40);

        drawPOV(g, mJoystick.getPovValues());
    }

    private void drawPOV(Graphics g, short[] aPov)
    {
        if (aPov.length != 0)
        {
            short pov = aPov[0];

            switch (pov)
            {
            case 0:
                drawPOV(g, 250, 300);
                break;
            case 45:
                drawPOV(g, 250, 300);
                drawPOV(g, 288, 340);
                break;
            case 90:
                drawPOV(g, 288, 340);
                break;
            case 135:
                drawPOV(g, 288, 340);
                drawPOV(g, 250, 365);
                break;
            case 180:
                drawPOV(g, 250, 365);
                break;
            case 225:
                drawPOV(g, 250, 365);
                drawPOV(g, 210, 340);
                break;
            case 270:
                drawPOV(g, 210, 340);
                break;
            case -45:
                drawPOV(g, 210, 340);
                drawPOV(g, 250, 300);
                break;
            case -1:
                break;
            default:
                System.err.println("Unexpected POV value: " + pov);
            }
        }
    }

    private void drawPOV(Graphics g, int x, int y)
    {
        g.setColor(Color.red);
        g.fillRect(x, y, 40, 40);
    }

    private void drawTrigger(Graphics g, double value, int x, int y)
    {
        Color color = Util.alphaColor(Util.colorGetShadedColor(value, 1, -1), 40);

        g.setColor(color);
        g.fillRect(x, y, 60, 60);
    }

    private void drawJoystick(Graphics g, boolean pressed, double xAxis, double yAxis, int x, int y)
    {
        int WIDTH = 98;
        int HEIGHT = 80;

        if (pressed)
        {
            g.setColor(sPRESSED_BTN_COLOR);
        }
        else
        {
            g.setColor(sJOYSTICK_BACKGROUND);
        }
        g.fillRect(x, y, WIDTH, HEIGHT);

        int xAxisSpot = (int) (xAxis * WIDTH * .5 + WIDTH * .5 + x);
        int yAxisSpot = (int) (yAxis * HEIGHT * .5 + HEIGHT * .5 + y);

        g.setColor(Color.black);
        g.fillOval(xAxisSpot, yAxisSpot, 5, 5);

    }

    private void colorBumper(Graphics g, boolean pressed, int x, int y)
    {
        if (pressed)
        {
            g.setColor(sPRESSED_BTN_COLOR);
            g.fillRect(x, y, 140, 55);
        }
    }

    private void colorButton(Graphics g, boolean pressed, int x, int y)
    {
        if (pressed)
        {
            g.setColor(sPRESSED_BTN_COLOR);
            g.fillOval(x, y, 60, 60);
        }
    }
}
