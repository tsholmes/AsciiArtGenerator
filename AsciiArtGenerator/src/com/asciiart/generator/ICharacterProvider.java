package com.asciiart.generator;

import java.awt.image.BufferedImage;

/**
 * Represents an ascii character provider
 * 
 * @author tsholmes
 */
public interface ICharacterProvider {

	/**
	 * @return the valid characters
	 */
	char[] getValidCharacters();
	
	/**
	 * Gets the image for a character, for matching
	 * @param c the character to get the image for
	 * @return the character image
	 */
	BufferedImage getCharacterImage(char c); //TODO: change to raster for performance?
	
	/**
	 * @return the height:width ratio of the font
	 */
	double getRatio();
}
