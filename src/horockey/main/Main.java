package horockey.main;

import horockey.captchaGenerator.CaptchaGenerator;
import horockey.layers.*;

public class Main {

	public static void main(String[] args) {
		CaptchaGenerator gen = new CaptchaGenerator(new CaptchaGenerator.Options());
		gen.setLayers(new IRenderable[]{
			new Base(new Base.Options()),
			new Waves(new Waves.Options())
		});
		gen.Generate(100, "D://Generated");
	}
}
