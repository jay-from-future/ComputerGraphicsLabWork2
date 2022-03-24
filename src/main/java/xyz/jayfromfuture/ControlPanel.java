package xyz.jayfromfuture;

import xyz.jayfromfuture.interfaces.ControlPanelListener;
import xyz.jayfromfuture.interfaces.RotateListener;
import xyz.jayfromfuture.util.Point3D;

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
    private final double[] xValues = {-100, 0, 100, 100, 100, 0, 0, -50, -50, -100};
    private final double[] yValues = {0, 100, 100, 0, 100, 100, 0, -100, -100, 0};
    private final double[] zValues = {-100, 0, 0, 0, 100, 100, 100, 100, 0, -100};

    private final ControlPanelListener controlPanelListener;
    private final int pointCount;

    private final List<JTextField> pointXFields;
    private final List<JTextField> pointYFields;
    private final List<JTextField> pointZFields;

    private final JCheckBox isBaseLineVisible;
    private final JCheckBox isCurvePointMarked;

    public ControlPanel(ControlPanelListener controlPanelListener, int pointCount) {

        this.controlPanelListener = controlPanelListener;
        this.pointCount = pointCount;

        JPanel pointPanel = new JPanel(new GridLayout(4, 10));
        List<JLabel> pointNumber = new ArrayList<>(pointCount);
        pointXFields = new ArrayList<>(pointCount);
        pointYFields = new ArrayList<>(pointCount);
        pointZFields = new ArrayList<>(pointCount);

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

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));

        ButtonListener buttonListener = new ButtonListener();
        CheckBoxListener checkBoxListener = new CheckBoxListener();

        JButton repaintButton = new JButton(REPAINT_BUTTON_STR);
        repaintButton.addActionListener(buttonListener);
        JButton setRandomButton = new JButton(SET_RANDOM_BUTTON);
        setRandomButton.addActionListener(buttonListener);
        JButton setDefaultButton = new JButton(DEFAULT_POINT_STR);
        setDefaultButton.addActionListener(buttonListener);
        JButton setDefaultRotationButton = new JButton(DEFAULT_ROTATION_STR);
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

        JPanel controlPanel = new JPanel(new GridLayout(2, 2));

        controlPanel.add(pointPanel);
        controlPanel.add(buttonPanel);

        add(controlPanel);
    }

    private void sendBasePoints() {
        List<Point3D> basePoints = new ArrayList<>();
        double x;
        double y;
        double z;
        for (int i = 0; i < pointCount; i++) {
            x = Double.parseDouble(pointXFields.get(i).getText());
            y = Double.parseDouble(pointYFields.get(i).getText());
            z = Double.parseDouble(pointZFields.get(i).getText());
            basePoints.add(new Point3D(x, y, z));
        }
        controlPanelListener.setBasePoints(basePoints);
    }

    class ButtonListener implements ActionListener {

        public static final double MAX_VALUE = 300;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(ControlPanel.REPAINT_BUTTON_STR)) {
                try {
                    sendBasePoints();
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Заданы некорректные значения координат точек.");
                }
            } else if (e.getActionCommand().equals(ControlPanel.SET_RANDOM_BUTTON)) {
                for (int i = 0; i < pointCount; i++) {
                    pointXFields.get(i).setText(String.valueOf(getRandomNumber()).
                            substring(0, ControlPanel.COLUMNS));
                    pointYFields.get(i).setText(String.valueOf(getRandomNumber()).
                            substring(0, ControlPanel.COLUMNS));
                    pointZFields.get(i).setText(String.valueOf(getRandomNumber()).
                            substring(0, ControlPanel.COLUMNS));
                }
            } else {
                if (e.getActionCommand().equals(ControlPanel.DEFAULT_POINT_STR)) {
                    for (int i = 0; i < pointCount; i++) {
                        pointXFields.get(i).setText(String.valueOf(xValues[i]));
                        pointYFields.get(i).setText(String.valueOf(yValues[i]));
                        pointZFields.get(i).setText(String.valueOf(zValues[i]));
                    }
                } else if (e.getActionCommand().equals(ControlPanel.DEFAULT_ROTATION_STR)) {
                    ((RotateListener) controlPanelListener).setDefaultRotation();
                }
            }
        }

        private double getRandomNumber() {
            int sign = (Math.random() > 0.5) ? -1 : 1;
            return sign * (Math.random() * ButtonListener.MAX_VALUE);
        }
    }

    class CheckBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(ControlPanel.BASE_POINT_STR)) {
                controlPanelListener.setBaseLineVisible(isBaseLineVisible.isSelected());
            } else if (e.getActionCommand().equals(ControlPanel.CURVE_POINT_STR)) {
                controlPanelListener.setCurvePointMarked(isCurvePointMarked.isSelected());
            }
        }
    }
}

