package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;

import input.MouseManager;
import main.Launcher;
import networks.Data;
import networks.NeuralNetwork;
import tools.Toolbox;

public class Backend {

	public static int inputNeurons = 784, outputNeurons = 10;

	public static int[] layerSizes = new int[] { 784, 100, 10 };
	public Looper looper;
	public static NeuralNetwork network = new NeuralNetwork(layerSizes); // NEURAL NETWORK

	public static final int DRAWING_SIZE = 560;
	public static final int MARGIN = (Launcher.HEIGHT - DRAWING_SIZE) / 2;
	public static int[][] drawing = new int[DRAWING_SIZE][DRAWING_SIZE];
	public static double[] inputs = new double[784];

	public Backend(Looper looper) {
		this.looper = looper;
	}

	public void initialize() {
		Data.initializeData();
		Data.importNetwork();
	}

	public void tick(Graphics2D g) {
		if (MouseManager.mousePressed) {
			int x = MouseManager.mouseX() - MARGIN;
			int y = MouseManager.mouseY() - MARGIN;
			int range = 30;
			for (int i = -range; i <= range; i++) {
				for (int j = -range; j <= range; j++) {
					if (x + i < 0 || x + i >= DRAWING_SIZE || y + j < 0 || y + j >= DRAWING_SIZE) {
						continue;
					}
					if (i * i + j * j <= range * range) {
						drawing[y + j][x + i] = 255;
					}
				}
			}
		}

		if (Launcher.looper.frameCount % 20 == 0) {
			int s = DRAWING_SIZE / 28;
			for (int x = 0; x < 28; x++) {
				for (int y = 0; y < 28; y++) {
					double sum = 0;
					for (int i = 0; i < s; i++) {
						for (int j = 0; j < s; j++) {
							sum += drawing[y * s + j][x * s + i];
						}
					}
					inputs[y * 28 + x] = sum / (255.0 * s * s);
				}
			}
		}

	}

	public void render(Graphics2D g) {

		g.setColor(new Color(51, 51, 51));
		g.fillRect(0, 0, Launcher.WIDTH, Launcher.HEIGHT);

		drawBoard(g);
		renderResults(g);

	}

	public void drawBoard(Graphics2D g) {
		g.setColor(Color.WHITE);
		int MARGIN = (Launcher.HEIGHT - DRAWING_SIZE) / 2;
		g.drawRect(MARGIN - 1, MARGIN - 1, DRAWING_SIZE + 1, DRAWING_SIZE + 1);
		for (int x = 0; x < 28; x++) {
			for (int y = 0; y < 28; y++) {
				g.setColor(new Color((int) (255 * inputs[y * 28 + x]), (int) (255 * inputs[y * 28 + x]),
						(int) (255 * inputs[y * 28 + x])));
				g.fillRect(DRAWING_SIZE / 28 * x + MARGIN, DRAWING_SIZE / 28 * y + MARGIN, DRAWING_SIZE / 28,
						DRAWING_SIZE / 28);
			}
		}
	}

	public void renderResults(Graphics2D g) {
		double[] results = network.run(inputs);
		double sum = 0;
		int maxN = 0;
		double maxV = 0;
		for (double s : results)
			sum += s;
		for (int i = 0; i < 10; i++) {
			results[i] /= sum;
			if (results[i] > maxV) {
				maxV = results[i];
				maxN = i;
			}
		}
		Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_TOP);
		String[] names = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
		g.setFont(new Font("serif", Font.PLAIN, 30));
		for (int i = 0; i <= 9; i++) {
			if (maxN == i) g.setColor(Color.WHITE);
			else g.setColor(Color.GRAY);
			Toolbox.drawText(g, names[i], DRAWING_SIZE + MARGIN + 30, MARGIN + i * 40);
			Toolbox.drawText(g, Toolbox.stringRound(results[i] * 100, 2) + "%", DRAWING_SIZE + MARGIN + 120, MARGIN + i * 40);
		}
	}

	public String toString() {
		return "Game State";
	}

}