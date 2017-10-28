package display;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import neural.NeuralNetwork;

public class NeuralNetworkView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private NeuralNetwork net;
	
	public NeuralNetworkView(NeuralNetwork net) {
		this.net = net;
		Timer timer = new Timer(300, (e) -> {
			this.repaint();
		});
		timer.setRepeats(true);
		timer.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point[][] neuronLocation = getNeuronLocations();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		int size = 30;
		for(int i = 0; i < neuronLocation.length; i++) {
			for(int j = 0; j < neuronLocation[i].length; j++) {
				int x = neuronLocation[i][j].x;
				int y = neuronLocation[i][j].y;
				Color c = neuronLocation[i][j].getColor();
				g.setColor(c);
				g.fillOval(x-size/2, y-size/2, size, size);
				g.setColor(Color.white);
				g.drawOval(x-size/2, y-size/2, size, size);
				if(i > 0)
					for(int l = 0; l < neuronLocation[i-1].length; l++) {
						int x2 = neuronLocation[i-1][l].x;
						int y2 = neuronLocation[i-1][l].y;
						Color c2 = neuronLocation[i-1][l].getColor();
						g.setColor(c2);
						g.drawLine(x, y, x2, y2);
					}
			}
		}
		
	}

	private Point[][] getNeuronLocations() {
		Point[][] points = new Point[net.getLayerCount()+1][];
		int w = getWidth();
		int h = getHeight();
		double marginX = w / (net.getLayerCount() + 1);
		double marginY = h / net.getInputLayerSize();
		points[0] = new Point[net.getInputLayerSize()];
		for(int j = 0; j < net.getInputLayerSize(); j++) {
			int x = (int) (marginX/2);
			int y = (int) (marginY/2 + j * marginY);
			points[0][j] = new Point(x, y, net.getInputArray()[j]);
		}
		for(int i = 0; i < net.getLayerCount(); i++) {
			points[i+1] = new Point[net.getLayerSize(i)];
			int x = (int) (3*marginX/2 + marginX * i);
			marginY = h / (net.getLayerSize(i));
			for(int j = 0; j < net.getLayerSize(i); j++) {
				int y = (int) (marginY/2 + marginY * j);
				points[i+1][j] = this.new Point(x, y, net.getOutputFromNeuron(i, j));
			}
		}
		return points;
	}
	
	private class Point {
		public int x;
		public int y;
		double value;
		public Point(int x, int y, double value) {
			this.x = x;
			this.y = y;
			this.value = value;
		}
		
		public Color getColor() {
			int b = (int) Math.max(Math.min(value*255, 255), 30);
			Color c = new Color(b,b,b);
			return c;
		}
	}
}
