package horockey.main;

import horockey.captchaGenerator.CaptchaGenerator;
import horockey.layers.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Main {
	static final TreeMap<String, IRenderable> layerNamings = new TreeMap<>();
	static final Logger logger = Logger.getLogger("captcha_generator");

	static class GenerationParameters{
		public String directory = "";
		public int count = 0;
		public ArrayList<IRenderable> layers = new ArrayList<>();

		public String toString(){
			String layerNames = "";
			for (var t : layers){
				layerNames += t.getClass().getName() + " ";
			}
			return String.format("Directory: %s\nCount: %d\nLayers: %s",
					directory,
					count,
					layerNames
					);
		}
	}

	public static GenerationParameters init(String[] args) throws IllegalArgumentException{
		var argList = Arrays.stream(args).toList();
		if(argList.isEmpty()){
			logger.severe("Not enough arguments");
			throw new IllegalArgumentException();
		}
		var expectedFlags = new ArrayList<String>(Arrays.stream(new String[] {"-directory", "-count", "-layers"}).toList());

		ArrayList<String> givenFlags = new ArrayList<>();
		ArrayList<Integer> flagsIndexes = new ArrayList<>();
		for(int idx = 0; idx < argList.size(); idx++){
			String arg = argList.get(idx);
			if(arg.startsWith("-")){
				if (!expectedFlags.contains(arg)){
					logger.severe("Unexpected flag");
					throw new IllegalArgumentException();
				}
				givenFlags.add(arg);
				flagsIndexes.add(idx);
			}
		}
		if(flagsIndexes.get(0) > 0){
			logger.severe("Unflagged arguments detected");
			throw new IllegalArgumentException();
		}
		if(!givenFlags.contains("-layers")){
			logger.severe("No -layers flag detected");
			throw new IllegalArgumentException();
		}
		ArrayList< ArrayList <String> > argSlices = new ArrayList<>();
		for(var i = 0; i < flagsIndexes.size(); i++) {
			if (flagsIndexes.get(i) >= argList.size()) {
				logger.severe("Empty last flag");
				throw new IllegalArgumentException();
			}
			argSlices.add(new ArrayList<String> (argList.subList(
					flagsIndexes.get(i) + 1,
					((i < flagsIndexes.size() - 1) ? flagsIndexes.get(i + 1) : argList.size())
			)));
		}
		layerNamings.put("base", new Base(new Base.Options()));
		layerNamings.put("noise_fill", new NoiseFill(new NoiseFill.Options()));
		layerNamings.put("noise_circuit", new NoiseCircuit(new NoiseCircuit.Options()));
		layerNamings.put("gradient_fill", new GradientFill(new GradientFill.Options()));
		layerNamings.put("gradient_circuit", new GradientCircuit(new GradientCircuit.Options()));
		layerNamings.put("twirl", new Twirl(new Twirl.Options()));
		layerNamings.put("waves", new Waves(new Waves.Options()));

		GenerationParameters params = new GenerationParameters();
		for (int i = 0; i < givenFlags.size(); i++){
			var flag = givenFlags.get(i);
			if(flag.equals("-layers")){
				for(var layerName : argSlices.get(i)){
					if(!layerNamings.containsKey(layerName)){
						logger.severe("Unknown layer name");
						throw new IllegalArgumentException();
					}
					params.layers.add(layerNamings.get(layerName));
				}
			}
			if(flag.equals("-count")){
				if(argSlices.get(i).size() != 1){
					logger.severe("Wrong parameters for -count flag");
					throw new IllegalArgumentException();
				}
				params.count = Integer.parseInt(argSlices.get(i).get(0));
			}
			if(flag.equals("-directory")){
				if(argSlices.get(i).size() != 1){
					logger.severe("Wrong parameters for -directory flag");
					throw new IllegalArgumentException();
				}
				params.directory = argSlices.get(i).get(0);
			}
		}
		if(params.count == 0){
			params.count = 100;
		}
		if(params.directory.equals("")){
			params.directory = Paths.get(System.getProperty("user.dir"), "generated").toString();
			if(!Files.exists(Paths.get(params.directory))){
				new File(params.directory).mkdirs();
			}
		}
		logger.info("Captcha generator CLI initialized");
		return params;
	}

	public static void main(String[] args) {
		GenerationParameters params = new GenerationParameters();
		try{
			params = init(args);
		}
		catch (IllegalArgumentException e){
			return;
		}
		CaptchaGenerator gen = new CaptchaGenerator(new CaptchaGenerator.Options());
		var lrs = new IRenderable[params.layers.size()];
		lrs = params.layers.toArray(lrs);
		gen.setLayers(lrs);

		logger.info(String.format("Start generation. Parameters:\n%s", params));
		gen.Generate(params.count, params.directory);
	}
}
