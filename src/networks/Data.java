package networks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import graphics.Backend;
import main.Launcher;

public class Data {

	public static int trainingCases = 10000;
	public static int testCases = 100;
	
	public static void importNetwork() {
		File network = new File("Network.txt");
		try {
			
			Scanner s = new Scanner(network);
			int layers = s.nextInt();
			Backend.layerSizes = new int[layers];
			for (int l = 0; l < layers; l++) {
				Backend.layerSizes[l] = s.nextInt();
			}
			Backend.network = new NeuralNetwork(Backend.layerSizes);
			
			for (Layer layer : Backend.network.layers) {
				for (int out = 0; out < layer.nodesOut; out++) {
					for (int in = 0; in < layer.nodesIn; in++) {
						layer.weights[in][out] = s.nextDouble();
					}
				}
				for (int out = 0; out < layer.nodesOut; out++) {
					layer.biases[out] = s.nextDouble();
				}
			}
			
			s.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void export() {
		
		// Network size
		
		// for each layer:
		// 1. weights
		// (in0 out0), (in1, out0), (in2, out0), ...
		// (in0 out1), (in1, out1), (in2, out1), ...
		// ...
		
		// 2. biases
		// (out0), (out1), (out2), ...
		
		try {
			
			FileWriter writer = new FileWriter("Network.txt");			
			writer.write(Backend.layerSizes.length + "\n");
			for (int i = 0; i < Backend.layerSizes.length; i++) {
				writer.write(Backend.layerSizes[i] + " ");
			}
			writer.write("\n");
			
			for (int i = 0; i < Backend.network.layers.length; i++) {
				Layer layer = Backend.network.layers[i];
				for (int out = 0; out < layer.nodesOut; out++) {
					for (int in = 0; in < layer.nodesIn; in++) {
						writer.write(layer.weights[in][out] + " ");
					}
					writer.write("\n");
				}
				for (int out = 0; out < layer.nodesOut; out++) {
					writer.write(layer.biases[out] + " ");
				}
				writer.write("\n");
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int countCorrect(double[][] inputs, double[] predictions) {
		int count = 0;
		for (int i = 0; i < inputs.length; i++) {
			double[] input = new double[inputs[i].length];
			for (int j = 0; j < input.length; j++) {
				input[j] = inputs[i][j];
			}
			if (Launcher.backend.network.predict(input) == predictions[i]) {
				count++;
			}
		}
		return count;
	}
	
	public static void initializeData() {
		File data = new File("TrainingData.txt");
		try {
			Scanner in = new Scanner(data);
			
			for (int line = 0; line < trainingCases; line++) {
				int label = in.nextInt();
				double[] output = new double[Backend.outputNeurons]; output[label] = 1;
				trainingOutputs[line] = output;
				trainingPredictions[line] = label;
				double[] input = new double[Backend.inputNeurons];
				for (int y = 0; y < 28; y++) {
					for (int x = 0; x < 28; x++) {
						input[28 * y + x] = in.nextInt() / 255.0;
					}
				}
				trainingInputs[line] = input;
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		data = new File("TestData.txt");
		try {
			Scanner in = new Scanner(data);
			
			for (int line = 0; line < testCases; line++) {
				int label = in.nextInt();
				double[] output = new double[Backend.outputNeurons]; output[label] = 1;
				testOutputs[line] = output;
				testPredictions[line] = label;
				double[] input = new double[Backend.inputNeurons];
				for (int y = 0; y < 28; y++) {
					for (int x = 0; x < 28; x++) {
						input[28 * y + x] = in.nextInt() / 255.0;
					}
				}
				testInputs[line] = input;
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Data
	public static double[][] trainingInputs = new double[trainingCases][Backend.inputNeurons];
	public static double[][] trainingOutputs = new double[trainingCases][Backend.outputNeurons];
	public static int[] trainingPredictions = new int[trainingCases];
	public static double[][] testInputs = new double[testCases][Backend.inputNeurons];
	public static double[][] testOutputs = new double[testCases][Backend.outputNeurons];
	public static int[] testPredictions = new int[testCases];

}
