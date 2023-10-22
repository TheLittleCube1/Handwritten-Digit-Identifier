package networks;

import java.util.Random;

public class Layer {

	public int nodesIn, nodesOut;
	
	public double[][] weights; // weights[in][out]
	public double[] biases;
	public double[][] weightGradients;
	public double[] biasGradients;
	
	public double[] weightedInputs;
	public double[] activations;
	public double[] plainInputs;
	
	public int index;

	// Constructor
	public Layer(int nodesIn, int nodesOut, int index) {
		this.nodesIn = nodesIn;
		this.nodesOut = nodesOut;
		this.index = index;

		weights = new double[nodesIn][nodesOut];
		biases = new double[nodesOut];
		weightGradients = new double[nodesIn][nodesOut];
		biasGradients = new double[nodesOut];
		
		weightedInputs = new double[nodesOut];
		activations = new double[nodesOut];
		plainInputs = new double[nodesIn];
		
		initialize();
	}
	
	public void updateGradients(double[] nodeValues) {
		for (int out = 0; out < nodesOut; out++) {
			for (int in = 0; in < nodesIn; in++) {
				double finalWeightDerivative = plainInputs[in] * nodeValues[out];
				weightGradients[in][out] += finalWeightDerivative;
			}
			double finalBiasDerivative = nodeValues[out];
			biasGradients[out] += finalBiasDerivative;
		}
	}
	
	public double[] outputNodeValues(double[] expectedOutput) {
		double[] nodeValues = new double[expectedOutput.length];
		for (int i = 0; i < expectedOutput.length; i++) {
			double costDerivative = NeuralNetwork.nodeCostDerivative(activations[i], expectedOutput[i]);
			double activationDerivative = NeuralNetwork.activationDerivative(weightedInputs[i]);
			nodeValues[i] = costDerivative * activationDerivative;
		}
		return nodeValues;
	}
	
	public double[] hiddenNodeValues(Layer oldLayer, double[] oldNodeValues) {
		double[] newNodeValues = new double[nodesOut];
		for (int node = 0; node < newNodeValues.length; node++) {
			double newNodeValue = 0;
			for (int oldNode = 0; oldNode < oldNodeValues.length; oldNode++) {
				double weightedInputDerivative = oldLayer.weights[node][oldNode];
				newNodeValue += weightedInputDerivative * oldNodeValues[oldNode];
			}
			newNodeValue *= NeuralNetwork.activationDerivative(weightedInputs[node]);
			newNodeValues[node] = newNodeValue;
		}
		return newNodeValues;
	}
	
	public void applyGradients(double learnRate) {
		for (int out = 0; out < nodesOut; out++) {
			biases[out] -= biasGradients[out] * learnRate;
			for (int in = 0; in < nodesIn; in++) {
				weights[in][out] -= weightGradients[in][out] * learnRate;
			}
		}
	}
	
	public void initialize() {
		Random r = new Random();
		for (int in = 0; in < nodesIn; in++) {
			for (int out = 0; out < nodesOut; out++) {
				weights[in][out] = (2 * r.nextDouble() - 1) / Math.sqrt(nodesIn);
			}
		}
	}

	public double[] run(double[] inputLayer) {
		double[] outputLayer = new double[nodesOut];
		for (int out = 0; out < nodesOut; out++) {
			double weightedSum = 0;
			for (int in = 0; in < nodesIn; in++) {
				weightedSum += weights[in][out] * inputLayer[in];
				plainInputs[in] = inputLayer[in];
			}
			weightedInputs[out] = weightedSum + biases[out];
			activations[out] = NeuralNetwork.activation(weightedInputs[out]);
			outputLayer[out] = activations[out];
		}
		return outputLayer;
	}

}
