package tools;

import java.awt.Graphics2D;
import java.text.NumberFormat;

public class Toolbox {
	
	public static final int ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_RIGHT = 2, ALIGN_TOP = 3, ALIGN_BOTTOM = 4;
	public static int alignX = ALIGN_CENTER, alignY = ALIGN_CENTER;
	
	public static double map(double startLow, double startHigh, double mappedLow, double mappedHigh, double value) {
		double fraction = (value - startLow) / (startHigh - startLow);
		return mappedLow + (mappedHigh - mappedLow) * fraction;
	}
	
	public static double constrain(double low, double high, double value) {
		if (value < low) return low;
		else if (value > high) return high;
		else return value;
	}
	
	public static void drawText(Graphics2D g, String text, int x, int y) {
		
		int height = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
		int width = g.getFontMetrics().stringWidth(text);
		int textX, textY;
		
		if (alignX == ALIGN_LEFT) {
			textX = x;
		} else if (alignX == ALIGN_CENTER) {
			textX = x - width / 2;
		} else {
			textX = x - width;
		}
		
		if (alignY == ALIGN_TOP) {
			textY = y + height;
		} else if (alignY == ALIGN_CENTER) {
			textY = y + height / 2;
		} else {
			textY = y;
		}
		
		g.drawString(text, textX, textY);
		
	}
	
	public static void setAlign(int x, int y) {
		alignX = x;
		alignY = y;
	}
	
	public static String stringRound(double x, int dec) {
		String r = "" + round(x, dec);
		int i = r.indexOf(".");
		if (i == -1) {
			r += ".";
			for (int j = 0; j < dec; j++) r += "0";
			return r;
		} else {
			int d = dec - (r.length() - i - 1);
			for (int j = 0; j < d; j++) {
				r += "0";
			}
			return r;
		}
	}
	
	public static double round(double x, int dec) {
		double m = 1;
		for (int i = 0; i < dec; i++) {
			m *= 10;
		}
		return ((int) (x * m)) / m;
	}
	
	public static String formatNanoseconds(long time) {
		time /= 1e9;
		long minutes = time / 60;
		long seconds = time - 60 * minutes;
		String m = "", s = "";
		if (minutes < 10) {
			m = "0" + minutes;
		} else {
			m = "" + minutes;
		}
		if (seconds < 10) {
			s = "0" + seconds;
		} else {
			s = "" + seconds;
		}
		return m + ":" + s;
	}
	
	public static String formatInt(int n) {
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(true);
		return format.format(n);
	}
	
}
