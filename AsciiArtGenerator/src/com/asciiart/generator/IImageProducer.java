package com.asciiart.generator;

import java.awt.image.BufferedImage;

/**
 * Represents an ascii renderer
 * 
 * @author tsholmes
 */
public interface IImageProducer {
	
	/**
	 * Renders the lines of text as an image
	 * @param lines the lines of text to render
	 * @return the 
	 */
	BufferedImage renderASCII(String[] lines);
}
