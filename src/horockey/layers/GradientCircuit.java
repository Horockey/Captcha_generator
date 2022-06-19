package horockey.layers;

import horockey.helpers.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GradientCircuit implements IRenderable {
	private final int brightnessThreshold;
	private final int borderEpsilon;
	private final Color color1;
	private final Color color2;

	public static class Options{
		public int brightnessThreshold;
		public int borderEpsilon;
		public Color color1;
		public Color color2;

		public static final int defaultBrightnessThreshold = 1;
		public static final int defaultBorderEpsilon = 1;
		public static final Color defaultColor1 = new Color(255, 0, 0, 255);
		public static final Color defaultColor2 = new Color(0, 0, 255, 255);

		public Options(){
			this.brightnessThreshold = defaultBrightnessThreshold;
			this.borderEpsilon = defaultBorderEpsilon;
			this.color1 = defaultColor1;
			this.color2 = defaultColor2;
		}

		public Options(int brightnessThreshold, int borderEpsilon, Color color1, Color color2){
			this.brightnessThreshold = brightnessThreshold;
			this.borderEpsilon = borderEpsilon;
			this.color1 = color1;
			this.color2 = color2;
		}
	}

	public GradientCircuit(GradientCircuit.Options opts){
		if(opts. brightnessThreshold <= 0 || opts.brightnessThreshold > 255){
			opts.brightnessThreshold = Options.defaultBrightnessThreshold;
		}
		if(opts.borderEpsilon <= 0){
			opts.borderEpsilon = Options.defaultBorderEpsilon;
		}
		if(opts.color1 == null){
			opts.color1 = Options.defaultColor1;
		}
		if(opts.color2 == null){
			opts.color2 = Options.defaultColor2;
		}

		this.brightnessThreshold = opts.brightnessThreshold;
		this.borderEpsilon = opts.borderEpsilon;
		this.color1 = opts.color1;
		this.color2 = opts.color2;
	}

	public BufferedImage render(BufferedImage src){
		// Lefter quarter for width, any height
		Point p1 = new Point(
				(int)(Math.random()*src.getWidth()*0.25 + (int)(src.getWidth()*0.25)),
				(int)(Math.random()*src.getHeight()));
		// Righter quarter for width, any height
		Point p2 = new Point(
				(int)(Math.random()*src.getWidth()*0.25) + (int)(src.getWidth()*0.5),
				(int)(Math.random()*src.getHeight()));

		var pattern = Helpers.generateGradientPattern(
				src.getWidth(),
				src.getHeight()*2,
				this.color1,
				this.color2,
				p1,
				p2);

		var dst = Helpers.copy(src);
		for(int y = 0; y < dst.getHeight(); y++){
			for(int x = 0; x < dst.getWidth(); x++){
				if(!Helpers.isBorderPixel(src, this.brightnessThreshold, this.borderEpsilon, x, y)){
					dst.setRGB(x, y, pattern.getRGB(x, y));
				} else {
					dst.setRGB(x, y, pattern.getRGB(x, y+src.getHeight()));
				}
			}
		}
		return dst;
	}
}
