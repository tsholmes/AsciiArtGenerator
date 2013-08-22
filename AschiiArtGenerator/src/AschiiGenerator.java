import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class AschiiGenerator {
	
	enum PictureDestination {Display, File, None};
	
	public static double sHeightToWidthRatio  ;
	
	PixelAnalyzer mPixelAnalyzer;
	Font mFont;
	
	public AschiiGenerator(PixelAnalyzer.PixelScore aPixelMethod, Font aFont)
	{
		mFont = aFont;
		mPixelAnalyzer = new PixelAnalyzer( "` 1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOPPPP{}ASDFGHJKL:\"ZXCVBNM<>?", aFont, 1000, aPixelMethod );
		
		// get font ratio
		BufferedImage im = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB );
		
		Graphics g = im.createGraphics();
		
		FontMetrics fm = g.getFontMetrics(aFont);
		
		double height = fm.getHeight();
		double width = fm.stringWidth("oooo") / 4;
		
		sHeightToWidthRatio = height / width;
		
	}


	public String[] generateTextFromPicture( BufferedImage im, int aCols )
	{
		Raster data = im.getData();
		
		StringBuffer ret = new StringBuffer("");
		
		double xresolution = im.getWidth() / (double)aCols;
		double yresolution = xresolution * sHeightToWidthRatio;
		
		int lRows = (int) (im.getHeight() / yresolution );
		int lCols = aCols;
		
		double [][] scores = new double[lRows][lCols];
		
		double min = 1;
		double max = 0;
		
		for ( int row = 0; row < lRows; row++ )
		{
			for ( int col = 0; col < lCols; col++ )
			{
				// determine character for this one
				int x0 = (int)(col * xresolution);
				int y0 = (int)(row * yresolution);
				int dx = (int) xresolution;
				int dy = (int) yresolution;
				
				
				double score = mPixelAnalyzer.getScoreOfSection(im, x0, y0, dx, dy);
				scores[row][col] = score;
				min = Math.min(score, min);
				max = Math.max(score,  max);
			}	
		}
		
		for ( int row = 0; row < lRows; row++ )
		{
			for ( int col = 0; col < lCols; col++ )
			{
				double score = scores[row][col];
				score -= min;
				score = score / ( max - min );

				mPixelAnalyzer.addScore(score);
				
				char c = mPixelAnalyzer.getBestCharacterForScore(score);
				ret.append(""+c);
				
			}
			ret.append( System.lineSeparator() );		
		}
		
		return ret.toString().split("\n");
		
	}

	public void textImageFromPicture( String aPicturePath, int aCols )
	{
		BufferedImage im = null;
		try 
		{
			im = ImageIO.read( new File( aPicturePath ) );
			
		      JLabel label = new JLabel(new ImageIcon(im));  
		      //JOptionPane.showMessageDialog(null, label);  
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		BufferedImage lResult = textImageFromPicture( im, aCols);
		
		try {
			ImageIO.write(lResult, "PNG", new File(aPicturePath + "_toASCHII.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public BufferedImage textImageFromPicture( BufferedImage im, int aCols )
	{
		String[] lText = generateTextFromPicture(im, aCols);		
		
		BufferedImage lResultImage1 = new BufferedImage((int) (3000), (int)( 3000), BufferedImage.TYPE_INT_RGB);
		Graphics lResultGraphics1 = lResultImage1.createGraphics();
		
        // store that font width and height
        FontMetrics bigFontMetrics = lResultGraphics1.getFontMetrics(mFont);

		double width = bigFontMetrics.stringWidth("oooo") / 4 * aCols;
		double height = bigFontMetrics.getHeight() * lText.length;
        
        
        
	    // display result
		BufferedImage lSaveResultImage = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
		Graphics lSaveResultGraphics = lSaveResultImage.createGraphics();
	 	
		lSaveResultGraphics.setColor(Color.white);
		lSaveResultGraphics.fillRect(0, 0, lSaveResultImage.getWidth(), lSaveResultImage.getHeight());
		
		lSaveResultGraphics.setFont( mFont );
		lSaveResultGraphics.setColor(Color.black);
		drawString(lSaveResultGraphics, lText, 0, 0);
		
		
		return lSaveResultImage;
	}
	private void drawString(Graphics g, String text, int x, int y) 
	{
		drawString(g, text.split("\n"), x, y);
	}
	private void drawString(Graphics g, String[] text, int x, int y) 
	{
		for (String line : text )
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}
	 
	 
	 
	 
	public void saveTextGifFromGifFile( String aGifPath, int aCols )
	{
		try 
		{
			ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();  
			ImageInputStream in = ImageIO.createImageInputStream( new File( aGifPath) );
			reader.setInput(in);  
			
			/*//determine framerate
			javax.imageio.IIOImage frame = reader.readAll(0, null);
		    
		    IIOMetadata meta = frame.getMetadata();
            IIOMetadataNode imgRootNode = null;
		    try
		    {
                imgRootNode = (IIOMetadataNode) meta.getAsTree("javax_imageio_gif_image_1.0");
            }
		    catch(IllegalArgumentException e) 
		    {
		    	e.printStackTrace();
            }
		    IIOMetadataNode gce = (IIOMetadataNode)
	                imgRootNode.getElementsByTagName("GraphicControlExtension").item(0);


            int delay = Integer.parseInt(gce.getAttribute("delayTime"));*/
            
            
			// create a new BufferedOutputStream with the last argument
			ImageOutputStream output = new FileImageOutputStream(new File(aGifPath + "_toAscii.gif"));
			
			// create a gif sequence with the type of the first image, 1 second
			// between frames, which loops continuously
			GifSequenceWriter gifwriter = new GifSequenceWriter(output, BufferedImage.TYPE_INT_RGB,70, true);
			
			
			System.out.println( reader.getNumImages(true) + "images in gif" );
			BufferedImage image = reader.read(0);
			Graphics g = image.getGraphics();
			
			g.setColor(Color.black);
			
			g.fillRect(0, 0, image.getWidth(), image.getHeight());
			BufferedImage lastImage = image;
		 
			for (int i = 0, count = reader.getNumImages(true); i < count; i++)  
			{  
				
			    BufferedImage nextimage = reader.read(i);
			    System.out.println(nextimage.getWidth() + " , " + nextimage.getHeight());
			    Graphics img = lastImage.getGraphics();
			    img.drawImage(nextimage, 0, 0, nextimage.getWidth(), nextimage.getHeight(), null);
			    
			    image = textImageFromPicture(lastImage, aCols);
			    gifwriter.writeToSequence(image);
			    //ImageIO.write(image, "PNG", new File(aGifPath + "output" + i + ".png"));  
			}   
			
			gifwriter.close();
			output.close();
 
		} 
		catch (IOException e) 
		{	 
			e.printStackTrace();	
		}
		
	} 
	 
	 
	 
}
