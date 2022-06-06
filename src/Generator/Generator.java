package Generator;

import Layers.ILayer;

import java.awt.image.BufferedImage;

public class Generator {
	public static class Options{
		public int width;
		public int height;
		public int letterCount;

		public static int defaultWidth = 180;
		public static int defaultHeight = 70;
		public static int defaultLetterCount = 5;

		public Options(int width, int height, int letterCount) {
			this.width = width;
			this.height = height;
			this.letterCount = letterCount;
		}

		public Options(){
			this.width = defaultWidth;
			this.height = defaultHeight;
			this.letterCount = defaultLetterCount;
		}

}
	private ILayer[] layers = null;
	private int width = 0;
	private int height = 0;
	private int letterCount = 0;

	public Generator(Generator.Options opts){
		if (opts == null){
			opts = new Options();
		}
		if(opts.width <= 0){
			opts.width = Options.defaultWidth;
		}
		if(opts.height <= 0){
			opts.height = Options.defaultHeight;
		}
		if(opts.letterCount <= 0){
			opts.letterCount = Options.defaultLetterCount;
		}

		this.width = opts.width;
		this.height = opts.height;
		this.letterCount = opts.letterCount;
	}

	public BufferedImage Generate(){
		var img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		for (var l : this.layers){
			img = l.render(img);
		}
		return img;
	}
}
