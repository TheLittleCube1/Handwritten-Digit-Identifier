package input;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class MouseManager extends JFrame implements MouseListener {

	private static final long serialVersionUID = -8994576182964289862L;
	public static boolean mousePressed = false;
	public static int anchorX, anchorY, realX, realY;
	
	public static int mouseX() {
		return realX - anchorX + MouseInfo.getPointerInfo().getLocation().x;
	}
	
	public static int mouseY() {
		return realY - anchorY + MouseInfo.getPointerInfo().getLocation().y;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		anchorX = MouseInfo.getPointerInfo().getLocation().x;
		anchorY = MouseInfo.getPointerInfo().getLocation().y;
		realX = e.getPoint().x;
		realY = e.getPoint().y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
