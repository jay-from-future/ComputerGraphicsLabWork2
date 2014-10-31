package main;

import Jama.Matrix;
import util.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final static double T_STEP = 0.1; // шаг переменной t (t принадлежит [0, 1])
    private final static double[][] basisMatrix = {
            {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 3, 0, 0},
            {1, 0, 0, 0}
    };

    public static List<Point3D> getCurvePoints(Point3D p0, Point3D p1, Point3D p2, Point3D p3) {

        List<Point3D> curvePoints = new ArrayList<Point3D>();

        double[][] t_matrix = new double[1][4];
        double[][] p_matrix = new double[4][3];

        // заполням матрицу точек p (столбец)
        // TODO Преобразовать в цикл
        p_matrix[0][0] = p0.getX();
        p_matrix[0][1] = p0.getY();
        p_matrix[0][2] = p0.getZ();

        p_matrix[1][0] = p1.getX();
        p_matrix[1][1] = p1.getY();
        p_matrix[1][2] = p1.getZ();

        p_matrix[2][0] = p2.getX();
        p_matrix[2][1] = p2.getY();
        p_matrix[2][2] = p2.getZ();

        p_matrix[3][0] = p3.getX();
        p_matrix[3][1] = p3.getY();
        p_matrix[3][2] = p3.getZ();

        for (double t = 0; t <= 1.0; t += T_STEP) {

            // заполняем матрицу t (строка)
            t_matrix[0][0] = Math.pow(t, 3);
            t_matrix[0][1] = Math.pow(t, 2);
            t_matrix[0][2] = t;
            t_matrix[0][3] = 1;

            double[][] b_point_matrix = (new Matrix(t_matrix).times(new Matrix(basisMatrix))).
                    times(new Matrix(p_matrix)).getArray();

            curvePoints.add(new Point3D(b_point_matrix[0][0], b_point_matrix[0][1], b_point_matrix[0][2]));
        }
        return curvePoints;
    }
}
