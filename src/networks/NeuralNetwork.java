package networks;

import java.util.Arrays;

public class NeuralNetwork {
	
	// Output is not a runnable layer. "layers" stores the 1st to 2nd last layer.
	
	public Layer[] layers;
	public int[] layerSizes;
	
	public static final int SIGMOID = 0, HYPERBOLIC_TANGENT = 1, RELU = 2, SILU = 3;
	public static int activation = RELU;
	
	public NeuralNetwork(int[] layerSizes) {
		layers = new Layer[layerSizes.length - 1]; // length - 1 because we don't store the output layer
		this.layerSizes = layerSizes;
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new Layer(layerSizes[i], layerSizes[i + 1], i);
		}
	}
	
	public void learn(double[][] trainingInputs, double[][] trainingOutputs, double learnRate) {
		for (int i = 0; i < trainingInputs.length; i++) {
			updateAllGradients(trainingInputs[i], trainingOutputs[i]);
		}
		applyAllGradients(learnRate / trainingInputs.length);
		clearAllGradients();
	}
	
	public void updateAllGradients(double[] input, double[] expectedOutput) {
		
		// Run through neural network to update weighted inputs and activations
		run(input);
		
		// Calculate output node values
		Layer outputLayer = layers[layers.length - 1];
		double[] nodeValues = outputLayer.outputNodeValues(expectedOutput);
		outputLayer.updateGradients(nodeValues);
		
		for (int layerIndex = layers.length - 2; layerIndex >= 0; layerIndex--) {
			Layer hiddenLayer = layers[layerIndex];
			nodeValues = hiddenLayer.hiddenNodeValues(layers[layerIndex + 1], nodeValues);
			hiddenLayer.updateGradients(nodeValues);
		}
		
	}
	
	public void clearAllGradients() {
		for (Layer layer : layers) {
			for (int out = 0; out < layer.nodesOut; out++) {
				for (int in = 0; in < layer.nodesIn; in++) {
					layer.weightGradients[in][out] = 0;
				}
				layer.biasGradients[out] = 0;
			}
		}
	}
	
	public void applyAllGradients(double learnRate) {
		for (Layer layer : layers) {
			layer.applyGradients(learnRate);
		}
	}
	
	public double[] run(double[] input) {
		for (Layer layer : layers) {
			input = layer.run(input);
		}
		return input;
	}
	
	public double cost(double[][] inputs, double[][] outputs) { // * DOES NOT DISTORT INPUTS AND OUTPUTS *
		double cost = 0;
		for (int i = 0; i < inputs.length; i++) {
			cost += cost(inputs[i], outputs[i]);
		}
		return cost / inputs.length;
	}
	
	public double cost(double[] in, double[] expectedOutput) {
		double[] input = new double[in.length];
		for (int i = 0; i < in.length; i++) input[i] = in[i];
		double[] output = run(input);
		double cost = 0;
		for (int i = 0; i < output.length; i++) {
			cost += nodeCost(output[i], expectedOutput[i]);
		}
		return cost;
	}
	
	public static double nodeCost(double output, double expectedOutput) {
		double error = expectedOutput - output;
		return error * error;
	}
	
	public static double nodeCostDerivative(double outputActivation, double expectedActivation) {
		return 2 * (outputActivation - expectedActivation);
	}
	
	// ACTIVATION FUNCTION (sigmoid)
	public static double activation(double x) {
		if (activation == SIGMOID) {
			return 1 / (1 + Math.exp(-x));
		} else if (activation == HYPERBOLIC_TANGENT) {
			double exp = Math.exp(x); double inv = 1 / exp;
			return (exp - inv) / (exp + inv);
		} else if (activation == RELU) {
			return Math.max(0, x);
		} else {
			return x / (1 + Math.exp(-x));
		}
	}
	public static double activationDerivative(double weightedInput) {
		if (activation == SIGMOID) {
			double activation = activation(weightedInput);
			return activation * (1 - activation);
		} else if (activation == HYPERBOLIC_TANGENT) {
			double activation = activation(weightedInput);
			return 1 - activation * activation;
		} else if (activation == RELU) {
			if (weightedInput >= 0) return 1;
			else return 0;
		} else {
			double inv = Math.exp(-weightedInput);
			double plus = inv + 1;
			return (plus + weightedInput * inv) / (plus * plus);
		}
	}
	
	public static double weightedInputDerivative(double activation) {
		return activation;
	}
	
	public int predict(double[] input) {
		double[] output = run(input);
		int index = 0; double maxConfidence = Long.MIN_VALUE;
		for (int i = 0; i < output.length; i++) {
			if (output[i] > maxConfidence) {
				index = i;
				maxConfidence = output[i];
			}
		}
		return index;
	}
	
	public void print() {
		for (int i = 1; i <= layers.length; i++) {
			System.out.println("(Layer " + i + ")");
			System.out.println("Weights:");
			Layer layer = layers[i - 1];
			for (int w = 0; w < layer.weights.length; w++) {
				System.out.println(Arrays.toString(layer.weights[w]));
			}
			System.out.println("Biases: " + Arrays.toString(layer.biases));
		}
	}
	
}
