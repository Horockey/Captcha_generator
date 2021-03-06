package horockey.captchaGenerator;

import horockey.layers.Base;
import horockey.layers.IRenderable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class CaptchaGenerator {
	public static class Options{
		public IRenderable[] layers;
		public int width;
		public int height;

		private static final IRenderable[] defaultLayers = new IRenderable[]{new Base(new Base.Options())};
		private static final int defaultWidth = 180;
		private static final int defaultHeight = 70;

		public Options(IRenderable[] layers, int width, int height, int letterCount) {
			this.layers = layers;
			this.width = width;
			this.height = height;
		}

		public Options(){
			this.layers = defaultLayers;
			this.width = defaultWidth;
			this.height = defaultHeight;
		}

	}
	private IRenderable[] layers = null;
	private int width = 0;
	private int height = 0;

	public CaptchaGenerator(CaptchaGenerator.Options opts){
		if (opts == null){
			opts = new Options();
		}
		if(opts.width <= 0){
			opts.width = Options.defaultWidth;
		}
		if(opts.height <= 0){
			opts.height = Options.defaultHeight;
		}
		if(opts.layers.length == 0){
			opts.layers = Options.defaultLayers;
		}

		this.width = opts.width;
		this.height = opts.height;
		this.layers = opts.layers;
	}

	public void Generate(int count, String dir){
		for(int i = 0; i < count; i++) {
			var img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
			for (var l : this.layers){
				img = l.render(img);
			}
			img.getGraphics().dispose();
			String fileName = ((Base)(this.layers[0])).generatedString;
			String fullFilename = Paths.get(dir, fileName+".png").toString();
			File fout = new File(fullFilename);
			try{
				ImageIO.write(img, "png", fout);
			}
			catch (IOException e){
				System.out.println(e.getMessage());
			}
		}
	}

	public void setLayers(IRenderable[] layers){
		if(layers != null && layers.length != 0) {
			this.layers = layers;
		}
	}
}
