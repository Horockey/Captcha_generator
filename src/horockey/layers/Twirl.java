package horockey.layers;

import horockey.helpers.Helpers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Twirl implements IRenderable {
	private final double phiMin;
	private final double phiMax;
	private final int rMin;
	private final int rMax;
	private final int pointsCountMin;
	private final int pointsCountMax;

	private static class TwirlPoint{
		public Point coords;
		public double phi;
		public int r;

		public TwirlPoint(Point coords, double phi, int r){
			this.coords = coords;
			this.phi = phi;
			this.r = r;
		}
	}
	public static class Options{
		public double phiMin;
		public double phiMax;
		public int rMin;
		public int rMax;
		public int pointsCountMin;
		public int pointsCountMax;

		public static final double defaultPhiMin = 0.0;
		public static final double defaultPhiMax = Math.PI / 4.0;
		public static final int defaultPointsCountMin = 1;
		public static final int defaultPointsCountMax = 3;
		public static final int defaultRMin = 40;
		public static final int defaultRMax = 60;

		public Options(){
			this.phiMin = defaultPhiMin;
			this.phiMax = defaultPhiMax;
			this.pointsCountMin = defaultPointsCountMin;
			this.pointsCountMax = defaultPointsCountMax;
			this.rMin = defaultRMin;
			this.rMax = defaultRMax;
		}
		public Options(
				double phiMin,
				double phiMax,
				int rMin,
				int rMax,
				int pointsCountMin,
				int pointsCountMax
		){
			this.phiMin = phiMin;
			this.phiMax = phiMax;
			this.rMin = rMin;
			this.rMax = rMax;
			this.pointsCountMin = pointsCountMin;
			this.pointsCountMax = pointsCountMax;
		}
	}

	public Twirl(Twirl.Options opts){
		if(opts.phiMin < 0.0 || opts.phiMin > 2*Math.PI){
			opts.phiMin = Options.defaultPhiMin;
		}
		if(opts.phiMax < opts.phiMin || opts.phiMax > 2*Math.PI){
			opts.phiMax = Options.defaultPhiMax;
			opts.phiMin = Options.defaultPhiMin;
		}
		if(opts.rMin <= 0 || opts.rMin >= 80){
			opts.rMin = Options.defaultRMin;
		}
		if(opts.rMax >= 80 || opts.rMax < opts.rMin){
			opts.rMax = Options.defaultRMax;
			opts.rMin = Options.defaultRMin;
		}
		if(opts.pointsCountMin <= 0 || opts.pointsCountMin >= 10){
			opts.pointsCountMin = Options.defaultPointsCountMin;
		}
		if(opts.pointsCountMax >= 10 || opts.pointsCountMax < opts.pointsCountMin){
			opts.pointsCountMax = Options.defaultPointsCountMax;
			opts.pointsCountMin = Options.defaultPointsCountMin;
		}

		this.phiMin = opts.phiMin;
		this.phiMax = opts.phiMax;
		this.rMin = opts.rMin;
		this.rMax = opts.rMax;
		this.pointsCountMin = opts.pointsCountMin;
		this.pointsCountMax = opts.pointsCountMax;
	}

	public BufferedImage render(BufferedImage src){
		int twirlPointsCount = this.pointsCountMin + (int)(Math.random()*(this.pointsCountMax-this.pointsCountMin));
		TwirlPoint[] twirlPoints = new TwirlPoint[twirlPointsCount];
		for(int i = 0; i < twirlPoints.length; i++){
			int x = (int)(Math.random()*src.getWidth());
			int y = (int)(Math.random()*src.getHeight());
			double phi = this.phiMin + Math.random()*(this.phiMax-this.phiMin);
			int r = this.rMin + (int)(Math.random()*(this.rMax-this.rMin));
			twirlPoints[i] = new TwirlPoint(new Point(x, y), phi, r);
		}

		var dst = Helpers.copy(src);
		var g2 = (Graphics2D)(dst.getGraphics());

		for(int y = 0; y < dst.getHeight(); y++){
			for(int x = 0; x < dst.getWidth(); x++){
				dst.setRGB(x, y, new Color(0, 255, 0, 255).getRGB());
			}
		}

		for(var twirlPoint : twirlPoints){
			for(double phi = 0.0; phi <= 2*Math.PI; phi += Math.PI / 3600.0){
				for(int r = 1; r < twirlPoint.r; r++){
					var dstPoint = Helpers.polarToEuclidian(twirlPoint.coords, phi, r);
					var srcPoint = Helpers.polarToEuclidian(
							twirlPoint.coords,
							phi + (twirlPoint.phi * twirlForce(twirlPoint.r, r)),
							r);

					if(srcPoint.x < 0 || srcPoint.x >= src.getWidth()
						|| srcPoint.y < 0 || srcPoint.y >= src.getHeight()
						|| dstPoint.x < 0 || dstPoint.x >= dst.getWidth()
						|| dstPoint.y < 0 || dstPoint.y >= dst.getHeight()){
						break;
					}
					dst.setRGB(dstPoint.x, dstPoint.y, src.getRGB(srcPoint.x, srcPoint.y));
				}
			}
		}
		for(int y = 0; y < dst.getHeight(); y++){
			for(int x = 0; x < dst.getWidth(); x++){
				if(dst.getRGB(x, y) == new Color(0, 255, 0, 255).getRGB()){
					dst.setRGB(x, y, src.getRGB(x, y));
				}
			}
		}
		return dst;
	}
	private double twirlForce(int R, int r){
		double ratio = (double)(r)/R;
		double arg = 0.5*Math.PI*ratio;
		return Math.cos(arg);
	}
}
