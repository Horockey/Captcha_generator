package horockey.main;

import horockey.generator.Generator;
import horockey.layers.Base;
import horockey.layers.Grayscale;
import horockey.layers.ILayer;

public class Main {

	public static void main(String[] args) {
		Generator gen = new Generator(new Generator.Options());
		gen.setLayers(new ILayer[]{new Base(new Base.Options()), new Grayscale()});
		gen.Generate(100, "D://Generated");
	}
}
