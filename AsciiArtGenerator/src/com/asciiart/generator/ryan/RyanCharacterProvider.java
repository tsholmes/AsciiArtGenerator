package com.asciiart.generator.ryan;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.asciiart.generator.ICharacterProvider;

public class RyanCharacterProvider implements ICharacterProvider {

	private static final String characters = "` 1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOPPPP{}ASDFGHJKL:\"ZXCVBNM<>?";
	private static Map<Font, ICharacterProvider> instances = new HashMap<Font, ICharacterProvider>();

	public static ICharacterProvider getInstance(Font font) {
		if (!instances.containsKey(font)) {
			instances.put(font, new RyanCharacterProvider(font));
		}
		return instances.get(font);
	}

	private Font font;
	private Map<Character, BufferedImage> characterImages;
	private double ratio;

	private RyanCharacterProvider(Font font) {
		characterImages = new HashMap<Character, BufferedImage>();
		this.font = font;
		
		FontMetrics fm = new Canvas().getFontMetrics(font);
		ratio = fm.getHeight() / (fm.stringWidth("oooo") / 4.0);
	}

	@Override
	public char[] getValidCharacters() {
		return characters.toCharArray();
	}

	@Override
	public BufferedImage getCharacterImage(char c) {
		if (!characterImages.containsKey(c)) {
			generateCharacter(c);
		}

		return characterImages.get(c);
	}

	private void generateCharacter(char c) {
		BufferedImage i = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
		Graphics g = i.createGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, 50, 50);

		g.setColor(Color.black);
		g.setFont(font);
		g.drawString(String.valueOf(c), 10, 40);
		g.dispose();
		
		characterImages.put(c, i);
	}

	@Override
	public double getRatio() {
		return ratio;
	}

}
