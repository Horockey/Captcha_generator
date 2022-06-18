package horockey.helpers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Helpers {
	public static BufferedImage copy(BufferedImage bufferedImage)
	{
		ColorModel colorModel = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = bufferedImage.isAlphaPremultiplied();
		WritableRaster writableRaster = bufferedImage.copyData(null);
		return new BufferedImage(colorModel, writableRaster, isAlphaPremultiplied, null );
	}
	public static int bightness(int pix){
		int r = (pix & 0x00_FF_00_00) / 0x00_00_FF_FF;
		int g = (pix & 0x00_00_FF_00) / 0x00_00_00_FF;
		int b = pix & 0x00_00_00_FF;
		int br = (r+g+b) / 3;
		return br;
	}
	public static BufferedImage generateGradientPattern(int width, int height, Color c1, Color c2, Point p1, Point p2){
		for(Point p : new Point[]{p1, p2}){
			if(p.x < 0) p.x = 0;
			if(p.x >= width) p.x = width-1;
			if(p.y < 0) p.y = 0;
			if(p.y >= height) p.y = height-1;
		}
		var img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int maxDist = dist(new Point(0, 0), new Point(width-1, height-1));
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				Point p = new Point(x, y);
				double ratioColor1 = (double)dist(p, p1)/maxDist;
				double ratioColor2 = (double)dist(p, p2)/maxDist;
				int r = ((int)(c1.getRed()*ratioColor1) + (int)(c2.getRed()*ratioColor2)) % 256;
				int g = ((int)(c1.getGreen()*ratioColor1) + (int)(c2.getGreen()*ratioColor2)) % 256;
				int b = ((int)(c1.getBlue()*ratioColor1) + (int)(c2.getBlue()*ratioColor2)) % 256;
				img.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}
		return img;
	}
	private static int dist(Point p0, Point p){
		return (int)(Math.sqrt((p.x-p0.x)*(p.x-p0.x)+(p.y-p0.y)*(p.y-p0.y)));
	}

	public static boolean isBorderPixel(BufferedImage img, int brightnessThreshold, int borderEpsilon, int x0, int y0){
		//Is it target pixel or background one
		if(Helpers.bightness(img.getRGB(x0, y0)) < brightnessThreshold){
			return false;
		}
		for(int dist = 1; dist <= borderEpsilon; dist++){
			//Can we place upper left corner in bounds?
			if(x0-dist >= 0 && y0-dist >= 0) {
				//Scan upper horizontal border
				for (int x = x0 - dist; x <= x0 + dist && x0 + dist < img.getWidth(); x++) {
					if (Helpers.bightness(img.getRGB(x, y0 - dist)) < brightnessThreshold) {
						return true;
					}
				}
				//Scan left vertical border
				for (int y = y0 - dist; y < y0+dist && y0+dist < img.getHeight(); y++){
					if(Helpers.bightness(img.getRGB(x0-dist, y)) < brightnessThreshold){
						return true;
					}
				}
			}
			//Can we place lower right corner in bounds?
			if(x0+dist < img.getWidth() && y0+dist < img.getHeight()){
				//Scan lower horizontal border
				for (int x = x0 + dist; x >= x0-dist && x >= 0; x--) {
					if (Helpers.bightness(img.getRGB(x, y0+dist)) < brightnessThreshold) {
						return true;
					}
				}
				//Scan right vertical border
				for (int y = y0 + dist; y >= y0-dist && y >= 0; y--){
					if(Helpers.bightness(img.getRGB(x0+dist, y)) < brightnessThreshold){
						return true;
					}
				}
			}
		}
		return false;
	}
}
