package com.asciiart.generator.ryan;

/**
 * Represents a method for calculating a lightness value for a pixel
 */
public enum PixelScoreMethod {
	SquareRootMethod {
		@Override
		public int getScore(int r, int g, int b) {
			return (int) Math.sqrt(r * r * .241 + g * g * .691 + b * b * .068);
		}
	},
	LightnessMethod {
		@Override
		public int getScore(int r, int g, int b) {
			return (Math.max(r, Math.max(b, g)) + Math.min(Math.min(r, g), b)) / 2;
		}
	},
	AverageMethod {
		@Override
		public int getScore(int r, int g, int b) {
			return (r + g + b) / 3;
		}
	},
	LuminosityMethod {
		@Override
		public int getScore(int r, int g, int b) {
			return (int) (0.21 * r + 0.71 * g + 0.07 * b);
		}
	};

	/**
	 * Calculates the lightness value for a single pixel
	 * 
	 * @param r
	 *            the R value
	 * @param g
	 *            the G value
	 * @param b
	 *            the B value
	 * @return the score for the given pixel
	 */
	public abstract int getScore(int r, int g, int b); // TODO: switch to Color?
}
