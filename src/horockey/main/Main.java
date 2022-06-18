package horockey.main;

import horockey.captchaGenerator.CaptchaGenerator;
import horockey.layers.*;

public class Main {

	public static void main(String[] args) {
		CaptchaGenerator gen = new CaptchaGenerator(new CaptchaGenerator.Options());
		gen.setLayers(new ILayer[]{
				new Base(new Base.Options()),
				new GradientCircuit(new GradientCircuit.Options())
		});
		gen.Generate(100, "D://Generated");
	}
}
