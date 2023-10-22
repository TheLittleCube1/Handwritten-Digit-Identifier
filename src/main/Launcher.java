package main;

import graphics.Backend;
import graphics.Looper;

public class Launcher {

	public static final int WIDTH = 920, HEIGHT = 720;
	public static Looper looper;
	public static Backend backend;

	public static void main(String[] args) {

		looper = new Looper("Handwritten Digits", WIDTH, HEIGHT);
		looper.start();
		backend = looper.backend;

	}

}
