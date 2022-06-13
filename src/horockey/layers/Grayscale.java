package horockey.layers;


import horockey.helpers.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Grayscale implements ILayer {
	public BufferedImage render(BufferedImage src) {
		var dst = Helper.copy(src);
		for (int y = 0; y < dst.getHeight(); y++) {
			for (int x = 0; x < dst.getWidth(); x++) {
				int pix = src.getRGB(x, y);
				int br = Helper.bightness(pix);
				dst.setRGB(x, y, new Color(br, br, br).getRGB());
			}
		}
		return dst;
	}
}
