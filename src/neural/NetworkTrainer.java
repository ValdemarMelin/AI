package neural;
import java.util.ArrayList;
import java.util.List;


public class NetworkTrainer {
	
	public static NetworkTrainer sinNetworkTrainer(double min, double max, int N, NeuralNetwork net) {
		ArrayList<Point> ps = new ArrayList<Point>();
		for(int i = 0; i < N; i++) {
			final double x = (i+0.5)*(max - min)/N + min;
			ps.add(new Point(new double[] {x}, new double[] {Math.sin(x)*0.5+0.5}));
		}
		return new NetworkTrainer(net, ps);
	}
	
	public static class Point {
		private double[] input;
		private double[] output;
		public Point(double[] in, double[] out) {
			this.input = in;
			this.output = out;
		}
	}

	private final NeuralNetwork net;
	private final List<Point> trainingData;
	public double speed;
	
	public NetworkTrainer(NeuralNetwork net, List<Point> points) {
		this.net = net;
		this.trainingData = new ArrayList<Point>();
		this.speed = 0.01;
		for(Point p : points) {
			this.trainingData.add(p);
		}
	}
	
	public void trainForMS(long ms) {
		long t0 = System.currentTimeMillis();
		long t1 = 0;
		double d_bias_cost;
		double d_weight_cost;
		double k;
		do {
			for(int l = 0; l < net.getLayerCount(); l++) {
				for(int n = 0; n < net.getLayerSize(l); n++) {
					d_bias_cost = dCostByBias(l, n);
					k = (d_bias_cost < 0 ? 1 : -1);
					net.bias[l][n] += (d_bias_cost < 0 ? 1 : -1)*speed;
					
					for(int w = 0; w < net.weights[l][n].length; w++) {
						d_weight_cost = dCostByWeight(l, n, w);
						k = (d_weight_cost < 0 ? 1 : -1);
						net.weights[l][n][w] += k*speed;
					}
				}
			}
			
			t1 = System.currentTimeMillis();
		} while(t1 - t0 < ms);
	}

	public void train(int iter) {
		double speed = 0.0001;
		long t0 = System.currentTimeMillis(), t1 = 0;
		long time = 1000*5;
		double d_bias_cost;
		double d_weight_cost;
		double k;
		for(int N = 0; N < iter; N++) {
			for(int l = 0; l < net.getLayerCount(); l++) {
				for(int n = 0; n < net.getLayerSize(l); n++) {
					d_bias_cost = dCostByBias(l, n);
					k = (d_bias_cost < 0 ? 1 : -1);
					net.bias[l][n] += (d_bias_cost < 0 ? 1 : -1)*speed;
					
					for(int w = 0; w < net.weights[l][n].length; w++) {
						d_weight_cost = dCostByWeight(l, n, w);
						k = (d_weight_cost < 0 ? 1 : -1);
						net.weights[l][n][w] += k*speed;
					}
				}
			}
			
			t1 = System.currentTimeMillis();
			
			if(t1 - t0 > time) {
				t0 = t1;
				System.out.println("Current cost(after " + N + " iterations): " + cost());
			}
		}
	}
	
	public double cost() {
		double cost = 0;
		for(Point p: trainingData) {
			for(int i = 0; i < net.getInputLayerSize(); i++) {
				net.input[i] = p.input[i];
			}
			net.propagate();
			double pointCost = 0;
			for(int i = 0; i < net.getOutputLayerSize(); i++) {
				final double diff = net.getOutput(i) - p.output[i];
				pointCost += diff*diff;
			}
			cost += pointCost;
		}
		return cost;
	}
	
	public double dCostByBias(int l, int n) {
		double d_cost = 0;
		for(Point p: trainingData) {
			for(int i = 0; i < net.getInputLayerSize(); i++) {
				net.input[i] = p.input[i];
			}
			net.propagate();
			double[] d_out = net.derivativeBias(l, n);
			
			double d_pointCost = 0;
			for(int i = 0; i < net.getOutputLayerSize(); i++) {
				final double diff = net.getOutput(i) - p.output[i];
				d_pointCost += d_out[i]*2*diff; // df/dx * d/dx[(f-k)^2] = df/dx * 2(f-k)
			}
			d_cost += d_pointCost;
		}
		return d_cost;
	}
	
	public double dCostByWeight(int l, int n, int w) {
		double d_cost = 0;
		for(Point p: trainingData) {
			for(int i = 0; i < net.getInputLayerSize(); i++) {
				net.input[i] = p.input[i];
			}
			net.propagate();
			double[] d_out = net.derivativeWeight(l, n, w);
			
			double d_pointCost = 0;
			for(int i = 0; i < net.getOutputLayerSize(); i++) {
				final double diff = net.getOutput(i) - p.output[i];
				d_pointCost += d_out[i]*2*diff; // df/dx * d/dx[(f-k)^2] = df/dx * 2(f-k)
			}
			d_cost += d_pointCost;
		}
		return d_cost;
	}
	
}
