package horockey.layers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Base implements ILayer {
	public String generatedString;

	private final ArrayList<String> fonts = new ArrayList<String>(Arrays.asList(
			"Calibri",
			"Cascadia mono",
			"Impact",
			"Ink Journal",
			"Rockwell Nova"
	));
	private final int count;
	private final double minDegree;
	private final double maxDegree;
	private final int minShiftX;
	private final int maxShiftX;
	private final int minShiftY;
	private final int maxShiftY;

	public static class Options{
		ArrayList<String> abandonedFonts;
		int count;
		double minDegree;
		double maxDegree;
		int minShiftX;
		int maxShiftX;
		int minShiftY;
		int maxShiftY;

		private static final int defaultCount = 5;
		private static final double defaultMinDegree = -Math.PI/6.0;
		private static final double defaultMaxDegree = Math.PI/6.0;
		private static final int defaultMinShiftX = 0;
		private static final int defaultMaxShiftX = 5;
		private static final int defaultMinShiftY = 0;
		private static final int defaultMaxShiftY = 10;

		public Options(
				int count,
				double minDegree,
				double maxDegree,
				int minShiftX,
				int maxShiftX,
				int minShiftY,
				int maxShiftY){
			this.count = count;
			this.minDegree = minDegree;
			this.maxDegree = maxDegree;
			this.minShiftX = minShiftX;
			this.maxShiftX = maxShiftX;
			this.minShiftY = minShiftY;
			this.maxShiftY = maxShiftY;
		}

		public Options(){
			this.count = defaultCount;
			this.minDegree = defaultMinDegree;
			this.maxDegree = defaultMaxDegree;
			this.minShiftX = defaultMinShiftX;
			this.maxShiftX = defaultMaxShiftX;
			this.minShiftY = defaultMinShiftY;
			this.maxShiftY = defaultMaxShiftY;
		}
	}

	public Base(Base.Options opts){
		if (opts.count <= 0 || opts.count >= 8){
			opts.count = Options.defaultCount;
		}
		if (opts.minShiftX <= Options.defaultMinShiftX || opts.minShiftX >= Options.defaultMaxShiftX){
			opts.minShiftX = Options.defaultMinShiftX;
		}
		if (opts.maxShiftX <= opts.minShiftX || opts.maxShiftX >=  Options.defaultMaxShiftX){
			opts.maxShiftX = Options.defaultMaxShiftX;
		}
		if (opts.minShiftY <= Options.defaultMinShiftY || opts.minShiftY >= Options.defaultMaxShiftY){
			opts.minShiftY = Options.defaultMinShiftY;
		}
		if (opts.maxShiftY <= opts.minShiftY || opts.maxShiftY >=  Options.defaultMaxShiftY){
			opts.maxShiftY = Options.defaultMaxShiftY;
		}
		opts.minDegree %= 2*Math.PI;
		if (opts.minDegree < Options.defaultMinDegree || opts.minDegree > Options.defaultMaxDegree) {
			opts.minDegree = Options.defaultMinDegree;
		}
		opts.maxDegree %= 2*Math.PI;
		if(opts.maxDegree < Options.defaultMinDegree || opts.maxDegree > Options.defaultMaxDegree){
			opts.maxDegree = Options.defaultMaxDegree;
		}
		if(opts.abandonedFonts != null) {
			for (var abFont : opts.abandonedFonts){
				this.fonts.remove(abFont);
			}
		}

		this.generatedString = "";
		this.count = opts.count;
		this.minDegree = opts.minDegree;
		this.maxDegree = opts.maxDegree;
		this.minShiftX = opts.minShiftX;
		this.maxShiftX = opts.maxShiftX;
		this.minShiftY = opts.minShiftY;
		this.maxShiftY = opts.maxShiftY;
	}

	private String getRandomSymbol(){
		int idx = (int)(Math.random() * (62));
		char c;
		if(idx >= 0 && idx <= 25){
			c = (char)('a' + idx);
		}
		else if(idx >= 26 && idx <= 51){
			c = (char)('A' + idx - 26);
		}
		else if (idx >= 52 && idx <= 61) {
			c = (char)('0' + idx - 52);
		}
		else c = '?';
		return Character.toString(c);
	}


	public BufferedImage render(BufferedImage src){
		this.generatedString = "";
		var g2 = (Graphics2D)src.getGraphics();
		g2.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		g2.fillRect(0, 0, src.getWidth(), src.getHeight());
		g2.setColor(Color.BLACK);
		for(int i = 0; i < this.count; i++){
			var font = new Font(this.fonts.get((int)(Math.random()*this.fonts.size())), Font.PLAIN, 26);
			int baseX = (int)(src.getWidth()*0.15) + i*((int)(src.getWidth()*0.7) / (this.count));
			int baseY = src.getHeight() / 2;
			int shiftX = (minShiftX + (int)(Math.random()*(maxShiftX - minShiftX))) * (int)Math.signum(Math.random()-0.5);
			if(baseX + shiftX < 0){
				shiftX = -baseX;
			}
			if(shiftX+baseX > src.getWidth() - 10){
				shiftX = src.getWidth() - baseX;
			}
			int shiftY = minShiftX + (int)(Math.random()*(maxShiftY - minShiftY)) * (int)Math.signum(Math.random()-0.5);
			if(baseY + shiftY < 0){
				shiftY = -baseY;
			}
			if(baseY + shiftY > src.getHeight() - 10){
				shiftY = src.getHeight() - baseY;
			}
			double phi = minDegree + Math.random()*(maxDegree-minDegree);
			g2.setFont(font);
			var afTrans = new AffineTransform();
			afTrans.rotate(phi, 0, 0);
			var rotatedFont = font.deriveFont(afTrans);
			g2.setFont(rotatedFont);
			String symb = getRandomSymbol();
			this.generatedString += symb;
			g2.drawString(symb, baseX+shiftX, baseY+shiftY);
		}
		return src;
	}
}
