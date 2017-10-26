
public class Matrix {
	
	public static void matrixProduct(double[][] A, double[][] B, double[][] output) {
		// row-major
		for(int i = 0; i < A.length; i++) {
			for(int j = 0; j < B[0].length; j++) {
				double sum = 0;
				for(int k = 0; k < B.length; k++) {
					sum += A[i][k]*B[k][j];
				}
				output[i][j] = sum;
			}
		}
	}
	
	public static double[][] matrixProduct(double[][] A, double[][] B) {
		double[][] C = new double[A.length][B[0].length];
		matrixProduct(A, B, C);
		return C;
	}
	
	public static String toString(double[][] matrix) {
		String s = "";
		for(int i = 0; i < matrix.length; i++) {
			if(i == 0) s += "/ ";
			else if(i == matrix.length-1) s += "\\ ";
			else s += "| ";
			for(int j = 0; j < matrix[i].length; j++) {
				s += matrix[i][j] + " ";
			}
			if(i == 0) s += "\\";
			else if(i == matrix.length-1) s += "/";
			else s += "|";
			s += System.lineSeparator();
		}
		return s;
	}
}
