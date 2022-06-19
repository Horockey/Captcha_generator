package horockey.layers;

import horockey.helpers.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NoiseFill implements IRenderable {
	private final int brightnessThreshold;

	public static class Options{
		public int brightnessThreshold;

		private static final int defaultBrightnessThreshold = 1;

		public Options(){
			this.brightnessThreshold = defaultBrightnessThreshold;
		}
		public Options(int brigtnessTreshhold){
			this.brightnessThreshold = brigtnessTreshhold;
		}
	}

	public NoiseFill(NoiseFill.Options opts){
		if(opts.brightnessThreshold <= 0 || opts.brightnessThreshold > 255){
			opts.brightnessThreshold = Options.defaultBrightnessThreshold;
		}
		this.brightnessThreshold = opts.brightnessThreshold;
	}

	public BufferedImage render(BufferedImage src) {
		var dst = Helpers.copy(src);
		for(int y = 0; y < dst.getHeight(); y++){
			for(int x = 0; x < dst.getWidth(); x++){
				int pix = src.getRGB(x, y);
				int br = Helpers.bightness(pix);
				if (br <= this.brightnessThreshold){
					int r = (int)(Math.random() * 256);
					int g = (int)(Math.random() * 256);
					int b = (int)(Math.random() * 256);
					dst.setRGB(x, y, new Color(r, g, b, 255).getRGB());
				}
			}
		}
		return dst;
	}
}
