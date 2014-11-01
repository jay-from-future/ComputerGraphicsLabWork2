package main;

import interfaces.ControlPanelListener;
import interfaces.RotateListener;
import util.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends JPanel {

    private static final String REPAINT_BUTTON_STR = "Перерисовать кривую";
    private static final String SET_RANDOM_BUTTON = "Задать кооррдинаты случайными значениями";
    private static final String BASE_POINT_STR = "Отображать пространственные точки";
    private static final String CURVE_POINT_STR = "Отображать точки, по которым строится кривая Безье";
    private static final String DEFAULT_POINT_STR = "Устанавить координаты точек значениями по умолчанию";
    private static final String DEFAULT_ROTATION_STR = "Установить углы вращения по умолчанию";

    private static final int COLUMNS = 6;

    // начальные значения координат точек
    // TODO Можно изобразить более "красивую" кривую
    private double[] xValues = {10, 10, 10, 10, 100, 100, 100, -100, -100, -100};
    private double[] yValues = {10, 50, 50, 10, 10, 50, 50, 200, 200, 200};
    private double[] zValues = {10, 10, 80, 80, 10, 10, 80, 80, 180, 280};

    private ControlPanelListener controlPanelListener;
    private int pointCount;

    private JPanel controlPanel;
    private JPanel pointPanel;
    private JPanel buttonPanel;

    private List<JLabel> pointNumber;
    private List<JTextField> pointXFields;
    private List<JTextField> pointYFields;
    private List<JTextField> pointZFields;

    private JCheckBox isBaseLineVisible;
    private JCheckBox isCurvePointMarked;

    private JButton repaintButton;
    private JButton setRandomButton;
    private JButton setDefaultButton;
    private JButton setDefaultRotationButton;

    public ControlPanel(ControlPanelListener controlPanelListener, int pointCount) {

        this.controlPanelListener = controlPanelListener;
        this.pointCount = pointCount;

        pointPanel = new JPanel(new GridLayout(4, 10));
        pointNumber = new ArrayList<JLabel>(pointCount);
        pointXFields = new ArrayList<JTextField>(pointCount);
        pointYFields = new ArrayList<JTextField>(pointCount);
        pointZFields = new ArrayList<JTextField>(pointCount);

        for (int i = 0; i < pointCount; i++) {
            pointNumber.add(new JLabel(String.valueOf(i + 1)));
            pointXFields.add(new JTextField(String.valueOf(xValues[i]), COLUMNS));
            pointYFields.add(new JTextField(String.valueOf(yValues[i]), COLUMNS));
            pointZFields.add(new JTextField(String.valueOf(zValues[i]), COLUMNS));
        }

        pointPanel.add(new JLabel("Номер точки"));
        for (JLabel l : pointNumber) {
            l.setHorizontalAlignment(JLabel.CENTER);
            pointPanel.add(l);
        }

        JLabel xLabel = new JLabel("X");
        xLabel.setHorizontalAlignment(JLabel.CENTER);
        pointPanel.add(xLabel);
        for (JTextField field : pointXFields) {
            pointPanel.add(field);
        }

        JLabel yLabel = new JLabel("Y");
        yLabel.setHorizontalAlignment(JLabel.CENTER);
        pointPanel.add(yLabel);
        for (JTextField field : pointYFields) {
            pointPanel.add(field);
        }

        JLabel zLabel = new JLabel("Z");
        zLabel.setHorizontalAlignment(JLabel.CENTER);
        pointPanel.add(zLabel);
        for (JTextField field : pointZFields) {
            pointPanel.add(field);
        }

        buttonPanel = new JPanel(new GridLayout(3, 2));

        ButtonListener buttonListener = new ButtonListener();
        CheckBoxListener checkBoxListener = new CheckBoxListener();

        repaintButton = new JButton(REPAINT_BUTTON_STR);
        repaintButton.addActionListener(buttonListener);
        setRandomButton = new JButton(SET_RANDOM_BUTTON);
        setRandomButton.addActionListener(buttonListener);
        setDefaultButton = new JButton(DEFAULT_POINT_STR);
        setDefaultButton.addActionListener(buttonListener);
        setDefaultRotationButton = new JButton(DEFAULT_ROTATION_STR);
        setDefaultRotationButton.addActionListener(buttonListener);

        isBaseLineVisible = new JCheckBox(BASE_POINT_STR, true);
        isBaseLineVisible.addActionListener(checkBoxListener);
        isCurvePointMarked = new JCheckBox(CURVE_POINT_STR, true);
        isCurvePointMarked.addActionListener(checkBoxListener);

        buttonPanel.add(repaintButton);
        buttonPanel.add(setRandomButton);
        buttonPanel.add(setDefaultButton);
        buttonPanel.add(setDefaultRotationButton);
        buttonPanel.add(isBaseLineVisible);
        buttonPanel.add(isCurvePointMarked);

        controlPanel = new JPanel(new GridLayout(2, 2));

        controlPanel.add(pointPanel);
        controlPanel.add(buttonPanel);

        add(controlPanel);
    }

    class ButtonListener implements ActionListener {

        public static final double MAX_VALUE = 300;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == ControlPanel.REPAINT_BUTTON_STR) {
                try {
                    List<Point3D> basePoints = new ArrayList<Point3D>();
                    double x;
                    double y;
                    double z;
                    for (int i = 0; i < pointCount; i++) {
                        x = Double.valueOf(pointXFields.get(i).getText());
                        y = Double.valueOf(pointYFields.get(i).getText());
                        z = Double.valueOf(pointZFields.get(i).getText());
                        basePoints.add(new Point3D(x, y, z));
                    }
                    controlPanelListener.setBasePoints(basePoints);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Заданы некорректные значения координат точек.");
                }
            } else if (e.getActionCommand() == ControlPanel.SET_RANDOM_BUTTON) {
                for (int i = 0; i < pointCount; i++) {
                    pointXFields.get(i).setText(String.valueOf(getRandomNumber(MAX_VALUE)).
                            substring(0, ControlPanel.COLUMNS));
                    pointYFields.get(i).setText(String.valueOf(getRandomNumber(MAX_VALUE)).
                            substring(0, ControlPanel.COLUMNS));
                    pointZFields.get(i).setText(String.valueOf(getRandomNumber(MAX_VALUE)).
                            substring(0, ControlPanel.COLUMNS));
                }
            } else if (e.getActionCommand() == ControlPanel.DEFAULT_POINT_STR) {
                for (int i = 0; i < pointCount; i++) {
                    pointXFields.get(i).setText(String.valueOf(xValues[i]));
                    pointYFields.get(i).setText(String.valueOf(yValues[i]));
                    pointZFields.get(i).setText(String.valueOf(zValues[i]));
                }
            } else if (e.getActionCommand() == ControlPanel.DEFAULT_ROTATION_STR) {
                ((RotateListener) controlPanelListener).setDefaultRotation();
            }
        }

        private double getRandomNumber(double max) {
            int sign = (((int) Math.random()) == 1) ? -1 : 1;
            return sign * (Math.random() * max);
        }
    }

    class CheckBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == ControlPanel.BASE_POINT_STR) {
                controlPanelListener.setBaseLineVisible(isBaseLineVisible.isSelected());
            } else if (e.getActionCommand() == ControlPanel.CURVE_POINT_STR) {
                controlPanelListener.setCurvePointMarked(isCurvePointMarked.isSelected());
            }
        }
    }
}

