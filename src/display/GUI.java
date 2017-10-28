package display;

import javax.swing.JFrame;

import neural.NeuralNetwork;

public class GUI {
	
	private final JFrame frame;
	private final NeuralNetworkView view;
	private final NeuralNetwork net;
	
	public GUI(NeuralNetwork net) {
		this.net = net;
		this.view = new NeuralNetworkView(net);
		this.frame = new JFrame("Neural network visualizer");
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(view);
		frame.setVisible(true);
	}
}
