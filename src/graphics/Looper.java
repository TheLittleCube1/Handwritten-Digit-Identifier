package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import input.KeyManager;

public class Looper implements Runnable {

	private Display display;
	public int width, height;
	public String title;

	private boolean running = false;
	private Thread thread;

	public int frameCount = 0;

	private BufferStrategy bs;
	public Graphics2D g;
	
	private KeyManager keyManager;

	public Backend backend = new Backend(this);

	public Looper(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
		keyManager = new KeyManager();
	}

	private void init() {
		// Create Display
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		
		// Initialize
		backend.initialize();
	}

	public void tick() {
		keyManager.tick();

		backend.tick(g);

	}

	public void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}

		g = (Graphics2D) bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		backend.render(g);

		bs.show();
		g.dispose();
	}

	public void run() {

		init();

		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frameCount++;
				ticks++;
				delta--;
			}

			if (timer >= 1e9) {
				timer = 0;
//				System.out.println(ticks + " fps");
				ticks = 0;
			}
		}

		stop();

	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Display getDisplay() {
		return display;
	}

	public Graphics2D getGraphics2D() {
		return g;
	}

}