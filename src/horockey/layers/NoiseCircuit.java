package horockey.layers;

import horockey.helpers.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NoiseCircuit implements ILayer{
	private final int brightnessThreshold;
	private final int borderEpsilon;

	public static class Options{
		public int brightnessThreshold;
		public int borderEpsilon;

		private static final int defaultBrightnessThreshold = 1;
		private static final int defaultBorderEpsilon = 1;

		public Options(){
			this.brightnessThreshold = defaultBrightnessThreshold;
			this.borderEpsilon = defaultBorderEpsilon;
		}
		public Options(int brightnessThreshold, int borderEpsilon){
			this.brightnessThreshold = brightnessThreshold;
			this.borderEpsilon = borderEpsilon;
		}
	}

	public NoiseCircuit(NoiseCircuit.Options opts){
		if(opts.brightnessThreshold <= 0 || opts.brightnessThreshold > 255){
			opts.brightnessThreshold = Options.defaultBrightnessThreshold;
		}
		if(opts.borderEpsilon <= 0){
			opts.borderEpsilon = Options.defaultBorderEpsilon;
		}
		this.brightnessThreshold = opts.brightnessThreshold;
		this.borderEpsilon = opts.borderEpsilon;
	}

	public BufferedImage render(BufferedImage src) {
		var dst = Helpers.copy(src);
		for(int y = 0; y < dst.getHeight(); y++){
			for(int x = 0; x < dst.getWidth(); x++){
				if(!isBorderPixel(src, x, y)){
					int r = (int)(Math.random() * 256);
					int g = (int)(Math.random() * 256);
					int b = (int)(Math.random() * 256);
					dst.setRGB(x, y, new Color(r, g, b, 255).getRGB());
				} else {
					dst.setRGB(x, y, src.getRGB(x, y));
				}
			}
		}
		return dst;
	}

	public boolean isBorderPixel(BufferedImage img, int x0, int y0){
		//Is it target pixel or background one
		if(Helpers.bightness(img.getRGB(x0, y0)) < this.brightnessThreshold){
			return false;
		}
		for(int dist = 1; dist <= this.borderEpsilon; dist++){
			//Can we place upper left corner in bounds?
			if(x0-dist >= 0 && y0-dist >= 0) {
				//Scan upper horizontal border
				for (int x = x0 - dist; x <= x0 + dist && x0 + dist < img.getWidth(); x++) {
					if (Helpers.bightness(img.getRGB(x, y0 - dist)) < this.brightnessThreshold) {
						return true;
					}
				}
				//Scan left vertical border
				for (int y = y0 - dist; y < y0+dist && y0+dist < img.getHeight(); y++){
					if(Helpers.bightness(img.getRGB(x0-dist, y)) < this.brightnessThreshold){
						return true;
					}
				}
			}
			//Can we place lower right corner in bounds?
			if(x0+dist < img.getWidth() && y0+dist < img.getHeight()){
				//Scan lower horizontal border
				for (int x = x0 + dist; x >= x0-dist && x >= 0; x--) {
					if (Helpers.bightness(img.getRGB(x, y0+dist)) < this.brightnessThreshold) {
						return true;
					}
				}
				//Scan right vertical border
				for (int y = y0 + dist; y >= y0-dist && y >= 0; y--){
					if(Helpers.bightness(img.getRGB(x0+dist, y)) < this.brightnessThreshold){
						return true;
					}
				}
			}
		}
		return false;
	}
}
