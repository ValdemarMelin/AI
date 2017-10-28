

public class NeuralNetwork {
	
	double[/*layer*/][/*neuron*/][/*parameter*/] weights;
	double[/*layer*/][/*neuron*/] bias;
	double[/*layer*/][/*neuron*/] output;
	double[/*layer*/][/*neuron*/][/*parameter*/] jacobian;
	
	private double[/*layer*/][/*neuron*/] activation;
	
	public final double[] input;
	
	public NeuralNetwork(int... layers) {
		jacobian = new double[layers.length-1][][];
		weights = new double[layers.length-1][][];
		bias = new double[layers.length-1][];
		for(int i = 1; i < layers.length; i++) {
			int layer = i-1;
			jacobian[layer] = new double[layers[i]][];
			weights[layer] = new double[layers[i]][]; // number of neurons
			bias[layer] = new double[layers[i]];
			for(int j = 0; j < layers[i]; j++) {
				weights[layer][j] = new double[layers[i-1]]; // number of coefficients
				jacobian[layer][j] = new double[layers[i-1]];
				bias[layer][j] = randomizeBias();
				for(int l = 0; l < layers[i-1]; l++) {
					weights[layer][j][l] = randomizeWeight();
				}
			}
		}
		
		output = new double[layers.length-1][];
		activation = new double[layers.length-1][];
		for(int i = 0; i < output.length; i++) {
			output[i] = new double[layers[i+1]];
			activation[i] = new double[layers[i+1]];
		}
		
		input = new double[layers[0]];
	}
	
	private final double randomizeBias() {
		return 1;
		//return Math.random() - 0.5;
	}
	
	private final double randomizeWeight() {
		return 1; 
		//return Math.random() - 0.5;
	}
	
	void computeJacobiansFromLayer(int l) {
		for(int L = getLayerCount() - 1; L >= l; L--) {
			for(int i = 0; i < getLayerSize(L); i++) {
				for(int j = 0; j < weights[L][i].length; j++) {
					jacobian[L][i][j] = weights[L][i][j] * df_dx(activation[L][i]);
				}
			}
		}
	}
	
	public double[] derivativeWeight(int l, int n, int w) {
		double dNdw = output[l-1][w]*df_dx(activation[l][n]);
		double[] result = new double[getLayerSize(getLayerCount()-1)];
		if(l == getLayerCount() - 1) {
			for(int i = 0; i < result.length; i++) {
				if(i == n) result[i] = dNdw;
				else result[i] = 0;
			}
		}
		else {
			computeJacobiansFromLayer(l+1);
			double[][] dOdL = jacobian[getLayerCount()-1];
			for(int L = getLayerCount() - 2; L > l; L--) {
				dOdL = Matrix.matrixProduct(dOdL, jacobian[L]);
			}
			for(int i = 0; i < result.length; i++) {
				result[i] = dOdL[i][n] * dNdw;
			}
		}
		return result;
	}
	
	public double[] derivativeBias(int l, int n) {
		double dNdb = df_dx(activation[l][n]);
		double[] result = new double[getLayerSize(getLayerCount()-1)];
		if(l == getLayerCount() - 1) {
			for(int i = 0; i < result.length; i++) {
				if(i == n) result[i] = dNdb;
				else result[i] = 0;
			}
		}
		else {
			computeJacobiansFromLayer(l+1);
			double[][] dOdL = jacobian[getLayerCount()-1];
			for(int L = getLayerCount() - 2; L > l; L--) {
				dOdL = Matrix.matrixProduct(dOdL, jacobian[L]);
			}
			for(int i = 0; i < result.length; i++) {
				result[i] = dOdL[i][n] * dNdb;
			}
		}
		return result;
	}
	
	// PUBLIC METHODS
	
	public void propagate() {
		for(int j = 0; j < weights[0].length; j++) {
			double sum = bias[0][j];
			for(int l = 0; l < weights[0][j].length; l++) {
				sum += weights[0][j][l]*input[l];
			}
			this.activation[0][j] = sum;
			this.output[0][j] = f(sum);
		}
		for(int i = 1; i < getLayerCount(); i++) {
			for(int j = 0; j < weights[i].length; j++) {
				double sum = bias[i][j];
				for(int l = 0; l < weights[i][j].length; l++) {
					sum += weights[i][j][l]*this.output[i-1][l];
				}
				this.activation[i][j] = sum;
				this.output[i][j] = f(sum);
			}
		}
	}
	
	public int getLayerCount() {
		return weights.length;
	}
	
	public int getLayerSize(int layer) {
		return weights[layer].length;
	}

	public int getInputLayerSize() {
		return weights[0][0].length; // Number of weights minus bias
	}
	
	public int getOutputLayerSize() {
		return getLayerSize(getLayerCount()-1);
	}
	
	public double getOutputFromNeuron(int i, int j) {
		return output[i][j];
	}
	
	public double getOutput(int i) {
		return getOutputFromNeuron(getLayerCount()-1, i);
	}
	
	public double[] getInputArray() {
		return input;
	}
	
	public String getStructure() {
		String s = "Neural network, " + getLayerCount() + " layers." + System.lineSeparator();
		s += "(" + getInputLayerSize() + " inputs, " + getLayerSize(getLayerCount()-1) + " outputs)" + System.lineSeparator();
		for(int i = 0; i < getLayerCount(); i++) {
			s += "    Layer " + (i+1) + ": " + getLayerSize(i) + " neurons." + System.lineSeparator();
			for(int j = 0; j < getLayerSize(i); j++) {
				s += "        Neuron " + j + ": [bias: " + bias[i][j] + ",";
				for(int l = 0; l < this.weights[i][j].length; l++) {
					s += " a" + l + ": " + weights[i][j][l];
				}
				s += "]" + System.lineSeparator();
			}
		}
		return s;
	}
	
	// STATIC METHODS
	
	private static final double f(double x) {
		//return 1 / (1 + Math.exp(-x));
		return x*x;
	}
	
	private static final double df_dx(double x) {
		//double fx = f(x);
		//return fx*(1-fx);
		return 2*x;
	}
	
}
