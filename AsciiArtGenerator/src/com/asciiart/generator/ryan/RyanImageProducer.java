package com.asciiart.generator.ryan;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.asciiart.generator.IImageProducer;

public class RyanImageProducer implements IImageProducer {

	private static Map<Font, IImageProducer> instances = new HashMap<Font, IImageProducer>();

	public static IImageProducer getInstance(Font font) {
		if (!instances.containsKey(font)) {
			instances.put(font, new RyanImageProducer(font));
		}
		return instances.get(font);
	}

	private Font font;

	private RyanImageProducer(Font font) {
		this.font = font;
	}

	@Override
	public BufferedImage renderASCII(String[] lines) {
		FontMetrics fm = new Canvas().getFontMetrics(font);

		int width = (int) (fm.stringWidth("oooo") / 4.0 * lines[0].length());
		int height = fm.getHeight() * lines.length;

		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		
		Graphics g = img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setFont(font);
		g.setColor(Color.black);
		
		int y = 0;
		for (String line : lines) {
			g.drawString(line, 0, y += fm.getHeight());
		}
		
		g.dispose();

		return img;
	}

}
