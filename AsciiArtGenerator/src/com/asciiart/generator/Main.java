package com.asciiart.generator;
import java.awt.Font;


public class Main {
	public static void main(String[] args ) 
	{
		String lInputFile = "me.jpg";
		int lColumns = 350;

		AsciiGenerator as = new AsciiGenerator( PixelAnalyzer.PixelScore.SquareRootMethod, new Font("consolas", Font.PLAIN, 8));
		
		//as.saveTextGifFromGifFile(lInputFile, lColumns);
		as.textImageFromPicture(lInputFile, lColumns);
		
	}
	
}
