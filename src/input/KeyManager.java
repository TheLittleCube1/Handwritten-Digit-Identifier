package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import graphics.Backend;

public class KeyManager implements KeyListener {
	
	public KeyManager() {
		
	}
	
	public void tick() {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'c') {
			Backend.drawing = new int[Backend.DRAWING_SIZE][Backend.DRAWING_SIZE];
			Backend.inputs = new double[784];
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
