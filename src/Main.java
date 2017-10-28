import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;


public class Main {

	public static void main(String[] args) {
		NeuralNetwork net = new NeuralNetwork(1, 5, 10, 7, 3, 1);
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
			List<String> tokens = cmd.getTokens(s);
			File file = new File(tokens.get(1));
			new NetworkTrainer(net, file).train();
		}, "<train f> Trains the neural network with the learning set in the file specified.");
		
		cmd.setDefaultCommand(s -> {
			System.out.println("Unknown command. Try \"help\" for help, or \"exit\" for exit.");
		});
		cmd.run(scan);
		System.exit(0);
	}
}
