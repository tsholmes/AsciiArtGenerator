package com.asciiart.generator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

//TODO: Refactor AsciiGenerator class
public class AsciiGenerator {

	enum PictureDestination {
		Display, File, None
	};

	private ICharacterProvider charProvider;
	private CellConverter cellConverter;
	private IImageProducer imageProducer;

	public AsciiGenerator(ICharacterProvider charProvider,
			CellConverter cellConverter, IImageProducer imageProducer) {
		this.charProvider = charProvider;
		this.cellConverter = cellConverter;
		this.imageProducer = imageProducer;

	}

	public String[] generateTextFromPicture(BufferedImage im, int aCols) {
		double xresolution = im.getWidth() / (double) aCols;
		double yresolution = xresolution * charProvider.getRatio();

		int lRows = (int) (im.getHeight() / yresolution);
		int lCols = aCols;

		char[][] characters = cellConverter.convertImage(im, im.getWidth()
				/ lCols, im.getHeight() / lRows);

		String[] lines = new String[characters.length];

		for (int i = 0; i < lines.length; i++) {
			lines[i] = new String(characters[i]);
		}

		return lines;

	}

	public void textImageFromPicture(String aPicturePath, int aCols) {
		BufferedImage im = null;
		try {
			im = ImageIO.read(new File(aPicturePath));

			JLabel label = new JLabel(new ImageIcon(im));
			// JOptionPane.showMessageDialog(null, label);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage lResult = textImageFromPicture(im, aCols);

		try {
			ImageIO.write(lResult, "PNG", new File(aPicturePath
					+ "_toASCHII.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public BufferedImage textImageFromPicture(BufferedImage im, int aCols) {
		String[] lText = generateTextFromPicture(im, aCols);

		BufferedImage lSaveResultImage = imageProducer.renderASCII(lText);

		return lSaveResultImage;
	}

	public void saveTextGifFromGifFile(String aGifPath, int aCols) {
		try {
			ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
			ImageInputStream in = ImageIO.createImageInputStream(new File(
					aGifPath));
			reader.setInput(in);

			/*
			 * //determine framerate javax.imageio.IIOImage frame =
			 * reader.readAll(0, null);
			 * 
			 * IIOMetadata meta = frame.getMetadata(); IIOMetadataNode
			 * imgRootNode = null; try { imgRootNode = (IIOMetadataNode)
			 * meta.getAsTree("javax_imageio_gif_image_1.0"); }
			 * catch(IllegalArgumentException e) { e.printStackTrace(); }
			 * IIOMetadataNode gce = (IIOMetadataNode)
			 * imgRootNode.getElementsByTagName
			 * ("GraphicControlExtension").item(0);
			 * 
			 * 
			 * int delay = Integer.parseInt(gce.getAttribute("delayTime"));
			 */

			// create a new BufferedOutputStream with the last argument
			ImageOutputStream output = new FileImageOutputStream(new File(
					aGifPath + "_toAscii.gif"));

			// create a gif sequence with the type of the first image, 1 second
			// between frames, which loops continuously
			GifSequenceWriter gifwriter = new GifSequenceWriter(output,
					BufferedImage.TYPE_INT_RGB, 70, true);

			System.out.println(reader.getNumImages(true) + "images in gif");
			BufferedImage image = reader.read(0);
			Graphics g = image.getGraphics();

			g.setColor(Color.black);

			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			BufferedImage lastImage = image;

			for (int i = 0, count = reader.getNumImages(true); i < count; i++) {

				BufferedImage nextimage = reader.read(i);
				System.out.println(nextimage.getWidth() + " , "
						+ nextimage.getHeight());
				Graphics img = lastImage.getGraphics();
				img.drawImage(nextimage, 0, 0, nextimage.getWidth(),
						nextimage.getHeight(), null);

				image = textImageFromPicture(lastImage, aCols);
				gifwriter.writeToSequence(image);
				// ImageIO.write(image, "PNG", new File(aGifPath + "output" + i
				// + ".png"));
			}

			gifwriter.close();
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
