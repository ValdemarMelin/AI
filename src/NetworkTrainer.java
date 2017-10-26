import java.io.File;


public class NetworkTrainer {
	
	private class Point {
		double[] input;
		double[] output;
		public Point(double[] in, double[] out) {
			this.input = in;
			this.output = out;
		}
	}

	private final NeuralNetwork net;
	
	public NetworkTrainer(NeuralNetwork net, File file) {
		this.net = net;
	}

	public void train() {
		for(int N = 0; N < 100; N++) {
			
		}
	}
	
	public double cost() {
		return 0;
	}
	
	public double dCostByBias(int l, int n) {
		return 0;
	}
	
	public double dCostByWeight(int l, int n, int w) {
		return 0;
	}
	
}
