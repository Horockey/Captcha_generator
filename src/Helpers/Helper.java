package Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Helper {
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
}
