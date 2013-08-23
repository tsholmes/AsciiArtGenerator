package com.asciiart.generator.ryan;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.asciiart.generator.CellConverter;
import com.asciiart.generator.ICharacterProvider;
import com.asciiart.generator.Pair;

public class RyanCellConverter extends CellConverter {

	private static Map<Pair<ICharacterProvider, PixelScoreMethod>, CellConverter> instances = new HashMap<Pair<ICharacterProvider, PixelScoreMethod>, CellConverter>();

	public static CellConverter getInstance(ICharacterProvider charProvider,
			PixelScoreMethod scoreMethod) {
		Pair<ICharacterProvider, PixelScoreMethod> id = new Pair<ICharacterProvider, PixelScoreMethod>(
				charProvider, scoreMethod);
		if (!instances.containsKey(id)) {
			instances.put(id, new RyanCellConverter(charProvider, scoreMethod));
		}
		return instances.get(id);
	}

	private PixelScoreMethod scoreMethod;
	private TreeMap<Double, Character> scoreToCharacter;

	private RyanCellConverter(ICharacterProvider charProvider,
			PixelScoreMethod scoreMethod) {
		super(charProvider);
		this.scoreMethod = scoreMethod;

		scoreToCharacter = new TreeMap<Double, Character>();
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		Map<Character, Double> initialScores = new HashMap<Character, Double>();
		for (char c : charProvider.getValidCharacters()) {
			BufferedImage img = charProvider.getCharacterImage(c);
			double score = scoreCell(img, 0, 0, img.getWidth(), img.getHeight());
			initialScores.put(c, score);
			min = Math.min(min, score);
			max = Math.max(max, score);
		}
		for (char c : charProvider.getValidCharacters()) {
			double score = initialScores.get(c);
			scoreToCharacter.put((score - min) / (max - min), c);
		}
	}

	private double scoreCell(BufferedImage img, int x0, int y0, int width,
			int height) {
		double score = 0;

		for (int x = x0; x < x0 + width; x++) {
			for (int y = y0; y < y0 + height; y++) {
				int rgbVal = img.getRGB(x, y);
				int r = (rgbVal >> 16) & 0xff;
				int g = (rgbVal >> 8) & 0xff;
				int b = rgbVal & 0xff;

				double pixelScore = 0;

				pixelScore = scoreMethod.getScore(r, g, b);

				if (pixelScore < 0) {
					System.exit(1);
				}
				pixelScore /= (255.0);
				if (pixelScore > 1) {
					System.exit(-1);
				}
				score += pixelScore;
			}
		}

		score = score / ((width) * (height));
		return score;
	}

	@Override
	public char[][] convertImage(BufferedImage img, int width, int height) {

		int cols = img.getWidth() / width;
		int rows = img.getHeight() / height;

		double[][] scores = new double[cols][rows];
		double minScore = Double.MAX_VALUE;
		double maxScore = -Double.MAX_VALUE;

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				double score = scoreCell(img, x * width, y * height, width,
						height);
				scores[x][y] = score;

				minScore = Math.min(minScore, score);
				maxScore = Math.max(maxScore, score);
			}
		}

		char[][] ret = new char[rows][cols];

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				double score = scores[x][y];
				char c = ' ';

				if (scoreToCharacter.containsKey(score)) {
					c = scoreToCharacter.get(score);
				} else {
					Double lowScore = scoreToCharacter.lowerKey(score);
					if (lowScore == null) {
						lowScore = scoreToCharacter.firstKey();
					}
					Double highScore = scoreToCharacter.higherKey(score);
					if (highScore == null) {
						highScore = scoreToCharacter.lastKey();
					}
					if (Math.abs(score - lowScore) < Math
							.abs(highScore - score)) {
						c = scoreToCharacter.get(lowScore);
					} else {
						c = scoreToCharacter.get(highScore);
					}
				}
				ret[y][x] = c;
			}
		}
		return ret;
	}

}
