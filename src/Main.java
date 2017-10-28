import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;

import display.NeuralNetworkView;
import neural.Matrix;
import neural.NetworkTrainer;
import neural.NeuralNetwork;


public class Main {

	public static void main(String[] args) throws IOException {
		NeuralNetwork net = new NeuralNetwork(1, 5, 1);
		NetworkTrainer t = NetworkTrainer.sinNetworkTrainer(-6, 6, 1000, net);
		t.speed = 0.002;
		File f = new File("C:/Users/Valdemar/Desktop/output.txt");
		f.createNewFile();
		while(true) {
			t.trainForMS(1000);
			PrintStream ps = new PrintStream(new FileOutputStream(f));
			for(int i = 0; i < 1000; i++) {
				double x = i*(6 - -6)/1000.0 -6;
				net.input[0] = x;
				net.propagate();
				ps.print(net.input[0] + ", " + net.getOutput(0) + System.lineSeparator());
			}
			ps.close();
		}
		//startCommandPrompt(net);
	}

	/*
	private static void startCommandPrompt(NeuralNetwork net) {
		Scanner scan = new Scanner(System.in);
		CommandPrompt cmd = new CommandPrompt();
		
		cmd.addCommand("help", (s) -> {
			for(CommandPrompt.Command c: cmd.getCommands().values()) {
				System.out.println(c.name + ": " + c.description);
			}
		}, "prints help info");
		
		cmd.addCommand("exit", (s) -> {
			cmd.stop();
		}, "exits the program");
		
		cmd.addCommand("structure", s -> {
			System.out.println(net.getStructure());
		}, "prints the network structure");
		
		cmd.addCommand("input", s -> {
			String[] words = s.split(" ");
			for(int i = 0; i < net.input.length; i++) {
				double m = Double.parseDouble(words[i+1].trim());
				net.input[i] = m;
			}
		});
		
		cmd.addCommand("propagate", s -> {
			net.propagate();
		}, "propagates the current input through the network");
		
		cmd.addCommand("output", s -> {
			System.out.println(Arrays.toString(net.output[net.output.length-1]));
		}, "Prints the output");
		
		cmd.addCommand("weight", s -> {
			String[] words = s.split(" ");
			int l = Integer.parseInt(words[1]);
			int n = Integer.parseInt(words[2]);
			int i = Integer.parseInt(words[3]);
			System.out.println(net.weights[l][n][i]);
		});
		
		cmd.addCommand("bias", s -> {
			String[] words = s.split(" ");
			int l = Integer.parseInt(words[1]);
			int n = Integer.parseInt(words[2]);
			System.out.println(net.bias[l][n]);
		});
		
		cmd.addCommand("jacobian", s-> {
			int l = Integer.parseInt(s.split(" ")[1]);
			net.computeJacobiansFromLayer(l);
			System.out.println(Matrix.toString(net.jacobian[l]));
		}, "prints the jacobian");
		
		cmd.addCommand("display", s -> {
			JFrame frame = new JFrame("Test");
			frame.setSize(800, 600);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.add(new NeuralNetworkView(net));
			frame.setVisible(true);
		}, "opens a display visualizing the network");
		
		cmd.addCommand("dWeight", s -> {
			String[] words = s.split(" ");
			int l = Integer.parseInt(words[1]);
			int n = Integer.parseInt(words[2]);
			int w = Integer.parseInt(words[3]);
			double[] out = net.derivativeWeight(l, n, w);
			System.out.println(Arrays.toString(out));
		}, "<dWeight l n w> computes the derivative of the network output w.r.t the specified weight");
		
		cmd.addCommand("dBias", s -> {
			String[] words = s.split(" ");
			int l = Integer.parseInt(words[1]);
			int n = Integer.parseInt(words[2]);
			double[] out = net.derivativeBias(l, n);
			System.out.println(Arrays.toString(out));
		}, "<dBias l n> computes the derivative of the network output w.r.t the specified bias");
		
		cmd.addCommand("train", s -> {
			NetworkTrainer t;
			t = NetworkTrainer.sinNetworkTrainer(-6, 6, 100, net);
			System.out.println("Current cost: " + t.cost());
			t.trainForMS(600000);
			System.out.println("Cost after training: " + t.cost());
		}, "<train f> Trains the neural network with the learning set in the file specified.");
		
		cmd.setDefaultCommand(s -> {
			System.out.println("Unknown command. Try \"help\" for help, or \"exit\" for exit.");
		});
		cmd.run(scan);
		System.exit(0);
	}
	*/
}
