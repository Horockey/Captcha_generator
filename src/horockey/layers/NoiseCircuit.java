package horockey.layers;

import horockey.helpers.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NoiseCircuit implements IRenderable {
	private final int brightnessThreshold;
	private final int borderEpsilon;

	public static class Options {
		public int brightnessThreshold;
		public int borderEpsilon;

		private static final int defaultBrightnessThreshold = 1;
		private static final int defaultBorderEpsilon = 1;

		public Options() {
			this.brightnessThreshold = defaultBrightnessThreshold;
			this.borderEpsilon = defaultBorderEpsilon;
		}

		public Options(int brightnessThreshold, int borderEpsilon) {
			this.brightnessThreshold = brightnessThreshold;
			this.borderEpsilon = borderEpsilon;
		}
	}

	public NoiseCircuit(NoiseCircuit.Options opts) {
		if (opts.brightnessThreshold <= 0 || opts.brightnessThreshold > 255) {
			opts.brightnessThreshold = Options.defaultBrightnessThreshold;
		}
		if (opts.borderEpsilon <= 0) {
			opts.borderEpsilon = Options.defaultBorderEpsilon;
		}
		this.brightnessThreshold = opts.brightnessThreshold;
		this.borderEpsilon = opts.borderEpsilon;
	}

	public BufferedImage render(BufferedImage src) {
		var dst = Helpers.copy(src);
		for (int y = 0; y < dst.getHeight(); y++) {
			for (int x = 0; x < dst.getWidth(); x++) {
				if (!Helpers.isBorderPixel(src, this.brightnessThreshold, this.borderEpsilon, x, y)) {
					int r = (int) (Math.random() * 256);
					int g = (int) (Math.random() * 256);
					int b = (int) (Math.random() * 256);
					dst.setRGB(x, y, new Color(r, g, b, 255).getRGB());
				} else {
					dst.setRGB(x, y, src.getRGB(x, y));
				}
			}
		}
		return dst;
	}
}


