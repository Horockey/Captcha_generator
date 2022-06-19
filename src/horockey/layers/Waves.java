package horockey.layers;

import horockey.helpers.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Waves implements IRenderable {
	private final double amplitudeMin;
	private final double amplitudeMax;
	private final double phaseMin;
	private final double phaseMax;
	private final double frequencyMin;
	private final double frequencyMax;

	public static class Options{
		public double amplitudeMin;
		public double amplitudeMax;
		public double phaseMin;
		public double phaseMax;
		public double frequencyMin;
		public double frequencyMax;

		public static final double defaultAmplitudeMin = 0.9;
		public static final double defaultAmplitudeMax = 1.0;
		public static final double defaultPhaseMin = 0.0;
		public static final double defaultPhaseMax = Math.PI;
		public static final double defaultFrequencyMin = 0.1;
		public static final double defaultFrequencyMax = 0.2;

		public Options(){
			this.amplitudeMin = defaultAmplitudeMin;
			this.amplitudeMax = defaultAmplitudeMax;
			this.phaseMin = defaultPhaseMin;
			this.phaseMax = defaultPhaseMax;
			this.frequencyMin = defaultFrequencyMin;
			this.frequencyMax = defaultFrequencyMax;
		}

		public Options(
				double amplitudeMin,
				double amplitudeMax,
				double phaseMin,
				double phaseMax,
				double frequencyMin,
				double frequencyMax){
			this.amplitudeMin = amplitudeMin;
			this.amplitudeMax = amplitudeMax;
			this.phaseMin = phaseMin;
			this.phaseMax = phaseMax;
			this.frequencyMin = frequencyMin;
			this.frequencyMax = frequencyMax;
		}
	}

	public Waves(Waves.Options opts){
		if(opts.amplitudeMin <= 0.0 || opts.amplitudeMax >= 1.0){
			opts.amplitudeMin = Options.defaultAmplitudeMin;
		}
		if(opts.amplitudeMax >= 1.0 || opts.amplitudeMax <= opts.amplitudeMin){
			opts.amplitudeMax = Options.defaultAmplitudeMax;
			opts.amplitudeMin = Options.defaultAmplitudeMin;
		}
		if(opts.phaseMin < 0.0 || opts.phaseMin >= 2*Math.PI){
			opts.phaseMin = Options.defaultPhaseMin;
		}
		if(opts.phaseMax >= 2*Math.PI || opts.phaseMax <= opts.phaseMin){
			opts.phaseMax = Options.defaultPhaseMax;
			opts.phaseMin = Options.defaultPhaseMin;
		}
		if(opts.frequencyMin <= 0.0 || opts.frequencyMin >= 0.5){
			opts.frequencyMin = Options.defaultFrequencyMin;
		}
		if(opts.frequencyMax >= 0.5 || opts.frequencyMax <= opts.frequencyMin){
			opts.frequencyMax = Options.defaultFrequencyMax;
			opts.frequencyMin = Options.defaultFrequencyMin;
		}

		this.amplitudeMin = opts.amplitudeMin;
		this.amplitudeMax = opts.amplitudeMax;
		this.phaseMin = opts.phaseMin;
		this.phaseMax = opts.phaseMax;
		this.frequencyMin = opts.frequencyMin;
		this.frequencyMax = opts.frequencyMax;
	}

	public BufferedImage render(BufferedImage src){
		double[][] frequencies = new double[2][2];
		for(int i = 0; i < frequencies.length; i++){
			for(int j = 0; j < frequencies[0].length; j++){
				frequencies[i][j] = this.frequencyMin + Math.random()*(this.frequencyMax-this.frequencyMin);
			}
		}
		double[][] phases = new double[2][2];
		for(int i = 0; i < phases.length; i++) {
			for (int j = 0; j < phases[0].length; j++) {
				phases[i][j] = this.phaseMin + Math.random() * (this.phaseMax - this.phaseMin);
			}
		}
		double[] amplitudes = new double[2];
		amplitudes[0] = this.amplitudeMin + Math.random()*(this.amplitudeMax-this.amplitudeMin);
		amplitudes[1] = this.amplitudeMin + Math.random()*(this.amplitudeMax-this.amplitudeMin);

		var dst = Helpers.copy(src);
		int cnt = 0;
		for(int dstY = 0; dstY < dst.getHeight(); dstY++){
			for(int dstX = 0; dstX < dst.getWidth(); dstX++) {
				int srcX = (int)(
						(dstX +
						Math.sin(dstX*frequencies[0][0] + phases[0][0]) +
						Math.sin(dstY*frequencies[0][1] + phases[0][1]))
						* amplitudes[0]
				);
				int srcY = (int)(
						(dstY +
						Math.sin(dstX*frequencies[1][0] + phases[1][0]) +
						Math.sin(dstY*frequencies[1][1] + phases[1][1]))
						* amplitudes[1]
				);
				if(srcX < 0 || srcX >= src.getWidth() || srcY < 0 || srcY >= src.getHeight()){
					dst.setRGB(dstX, dstY, Color.WHITE.getRGB());
					cnt++;
					continue;
				}
				dst.setRGB(dstX, dstY, src.getRGB(srcX, srcY));
			}
		}
		return dst;
	}
}
