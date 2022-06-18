package horockey.main;

import horockey.captchaGenerator.CaptchaGenerator;
import horockey.layers.Base;
import horockey.layers.Grayscale;
import horockey.layers.ILayer;
import horockey.layers.NoiseFill;

public class Main {

	public static void main(String[] args) {
		CaptchaGenerator gen = new CaptchaGenerator(new CaptchaGenerator.Options());
		gen.setLayers(new ILayer[]{
				new Base(new Base.Options()),
				new NoiseFill(new NoiseFill.Options())
		});
		gen.Generate(100, "D://Generated");
	}
}
