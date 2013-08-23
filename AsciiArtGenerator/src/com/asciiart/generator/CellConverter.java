package com.asciiart.generator;

import java.awt.image.BufferedImage;

/**
 * Represents a converter that converts a cell of a picture to a character
 * 
 * @author tsholmes
 */
public abstract class CellConverter {

	protected ICharacterProvider charProvider;

	/**
	 * Constructor
	 * @param charProvider the character provider
	 */
	public CellConverter(ICharacterProvider charProvider) {
		this.charProvider = charProvider;
	}

	/**
	 * Converts an image to a character grid
	 * @param img the image to convert
	 * @param width the width of the cell
	 * @param height the height of the cell
	 * @return the character that most closely represents the given cell
	 */
	public abstract char[][] convertImage(BufferedImage img, int width, int height); // TODO: convert to raster for performance?
}
