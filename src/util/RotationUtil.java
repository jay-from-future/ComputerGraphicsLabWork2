package util;

import Jama.Matrix;

public class RotationUtil {

    /**
     * Возвращает матрицу поворота для заданных углов.
     *
     * @param alpha Угол поворота вокруг горизонтальной оси (x)
     * @param beta  Угол поворота вокруг вертикальной оси (y)
     * @return Матрица поворота
     */
    public static Matrix getRotationMatrix(double alpha, double beta) {
        double[][] matrixAlpha = {
                {1, 0, 0},
                {0, Math.cos(alpha), Math.sin(alpha)},
                {0, -Math.sin(alpha), Math.cos(alpha)}
        };
        double[][] matrixBeta = {
                {Math.cos(beta), 0, -Math.sin(beta)},
                {0, 1, 0},
                {Math.sin(beta), 0, Math.cos(beta)}
        };
        return new Matrix(matrixAlpha).times(new Matrix(matrixBeta));
    }

    public static Point3D rotate(Point3D point3D, Matrix rotationMatrix) {
        double[] pointArray = point3D.getArray();
        double[][] pointMatrix = {
                {pointArray[0]},
                {pointArray[1]},
                {pointArray[2]}
        };
        double[][] rotationPoint = rotationMatrix.times(new Matrix(pointMatrix)).getArray();
        return new Point3D(rotationPoint[0][0], rotationPoint[1][0], rotationPoint[2][0]);
    }

    public static Point2D orthogonalProjection(Point3D point3D) {
        return new Point2D(point3D.getX(), point3D.getY());
    }

    public static String matrixToString(Matrix matrix) {
        StringBuilder result = new StringBuilder();
        double[][] m = matrix.getArray();
        for (int r = 0; r < matrix.getRowDimension(); r++) {
            for (int c = 0; c < matrix.getColumnDimension(); c++) {
                result.append(m[r][c]);
                result.append(" ");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
}
