package com.asciiart.generator;

import java.awt.Font;

import com.asciiart.generator.ryan.PixelScoreMethod;

public class Main {
	public static void main(String[] args) {
		String lInputFile = "me.jpg";
		int lColumns = 350;

		Font f = new Font("consolas", Font.PLAIN, 8);
		AsciiGenerator as = AsciiGeneratorFactory.createRyanGenerator(f,
				PixelScoreMethod.LuminosityMethod);

		// as.saveTextGifFromGifFile(lInputFile, lColumns);
		as.textImageFromPicture(lInputFile, lColumns);

	}

}
