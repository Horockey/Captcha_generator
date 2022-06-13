package horockey.layers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Base implements ILayer {
	private final ArrayList<String> fonts = new ArrayList<String>(Arrays.asList(
			"Calibri",
			"Cascadia mono",
			"Impact",
			"Ink Journal",
			"Rockwell Nova"
	));
	private int count;
	private double minDegree;
	private double maxDegree;
	private int minShift;
	private int maxShift;


	public static class Options{
		ArrayList<String> abandonedFonts;
		int count;
		double minDegree;
		double maxDegree;
		int minShift;
		int maxShift;

		private static final int defaultCount = 5;
		private static final double defaultMinDegree = -Math.PI/6.0;
		private static final double defaultMaxDegree = Math.PI/6.0;
		private static final int defaultMinShift = 0;
		private static final int defaultMaxShift = 20;

		public Options(int count, double minDegree, double maxDegree, int minShift, int maxShift){
			this.count = count;
			this.minDegree = minDegree;
			this.maxDegree = maxDegree;
			this.minShift = minShift;
			this.maxShift = maxShift;
		}

		public Options(){
			this.count = defaultCount;
			this.minDegree = defaultMinDegree;
			this.maxDegree = defaultMaxDegree;
			this.minShift = defaultMinShift;
			this.maxShift = defaultMaxShift;
		}
	}

	public Base(Base.Options opts){
		if (opts.count <= 0 || opts.count >= 8){
			opts.count = Options.defaultCount;
		}
		if (opts.minShift <= Options.defaultMinShift || opts.minShift >= Options.defaultMaxShift){
			opts.minShift = Options.defaultMinShift;
		}
		if (opts.maxShift <= opts.minShift || opts.maxShift >=  Options.defaultMaxShift){
			opts.maxShift = Options.defaultMaxShift;
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

		this.count = opts.count;
		this.minDegree = opts.minDegree;
		this.maxDegree = opts.maxDegree;
		this.minShift = opts.minShift;
		this.maxShift = opts.maxShift;
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
		var g2 = (Graphics2D)src.getGraphics();
		g2.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		g2.fillRect(0, 0, src.getWidth(), src.getHeight());
		g2.setColor(Color.BLACK);
		for(int i = 0; i < this.count; i++){
			var font = new Font(this.fonts.get((int)(Math.random()*this.fonts.size())), Font.PLAIN, 30);
			int baseX = (src.getWidth() / (2*this.count)) + i*(src.getWidth() / (this.count));
			int baseY = src.getHeight() / 2;
			int shiftX = (minShift + (int)(Math.random()*(maxShift-minShift))) * (int)Math.signum(Math.random()-0.5);
			if(baseX + shiftX < 0){
				shiftX = -baseX;
			}
			if(shiftX+baseX > src.getWidth() - 10){
				shiftX = src.getWidth() - baseX;
			}
			int shiftY = minShift + (int)(Math.random()*(maxShift-minShift)) * (int)Math.signum(Math.random()-0.5);
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
			g2.drawString(getRandomSymbol(), baseX+shiftX, baseY+shiftY);
		}
		return src;
	}
}
