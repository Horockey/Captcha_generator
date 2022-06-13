package horockey.main;

import horockey.generator.Generator;

public class Main {

	public static void main(String[] args) {
		Generator gen = new Generator(new Generator.Options());
		gen.Generate(100, "D://Generated");
	}
}
