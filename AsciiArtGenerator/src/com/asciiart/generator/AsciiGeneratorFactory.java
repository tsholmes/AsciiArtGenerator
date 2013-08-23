package com.asciiart.generator;

import java.awt.Font;

import com.asciiart.generator.ryan.PixelScoreMethod;
import com.asciiart.generator.ryan.RyanCellConverter;
import com.asciiart.generator.ryan.RyanCharacterProvider;
import com.asciiart.generator.ryan.RyanImageProducer;

public class AsciiGeneratorFactory {

	public static AsciiGenerator createRyanGenerator(Font f,
			PixelScoreMethod scoreMethod) {
		ICharacterProvider charProvider = RyanCharacterProvider.getInstance(f);
		CellConverter cellConverter = RyanCellConverter.getInstance(
				charProvider, scoreMethod);
		IImageProducer imageProducer = RyanImageProducer.getInstance(f);
		return new AsciiGenerator(charProvider, cellConverter, imageProducer);
	}

}
