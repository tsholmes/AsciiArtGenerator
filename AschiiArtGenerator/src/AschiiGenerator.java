import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


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


	public String generateTextFromPicture( String aPicturePath, int aCols, PictureDestination aPictureDestination )
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
		
		switch( aPictureDestination )
		{
		case Display:
			// display result
			BufferedImage lResultImage = new BufferedImage((int) (3000), (int)( 3000), BufferedImage.TYPE_INT_RGB);
			Graphics lResultGraphics = lResultImage.createGraphics();
			
			lResultGraphics.setColor(Color.white);
			lResultGraphics.fillRect(0, 0, lResultImage.getWidth(), lResultImage.getHeight());
			
			lResultGraphics.setFont( mFont );
			lResultGraphics.setColor(Color.black);
			drawString(lResultGraphics, ret.toString(), 0, 0);
			
			//lResultGraphics.drawString(ret.toString(), lResultImage.getWidth() / 2, lResultImage.getHeight() / 2);
	        JLabel label = new JLabel(new ImageIcon(lResultImage));  
	        JOptionPane.showMessageDialog(null, label);
	        
			break;
		case File:
			BufferedImage lResultImage1 = new BufferedImage((int) (3000), (int)( 3000), BufferedImage.TYPE_INT_RGB);
			Graphics lResultGraphics1 = lResultImage1.createGraphics();
			
	        // store that font width and height
	        FontMetrics bigFontMetrics = lResultGraphics1.getFontMetrics(mFont);

			double width = bigFontMetrics.stringWidth("oooo") / 4 * lCols;
			double height = bigFontMetrics.getHeight() * lRows;
	        
	        
	        
	        
		    // display result
			BufferedImage lSaveResultImage = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
			Graphics lSaveResultGraphics = lSaveResultImage.createGraphics();
		 	
			lSaveResultGraphics.setColor(Color.white);
			lSaveResultGraphics.fillRect(0, 0, lSaveResultImage.getWidth(), lSaveResultImage.getHeight());
			
			lSaveResultGraphics.setFont( mFont );
			lSaveResultGraphics.setColor(Color.black);
			drawString(lSaveResultGraphics, ret.toString(), 0, 0);
			
			try {
				ImageIO.write(lSaveResultImage, "PNG", new File(aPicturePath + "_toASCHII.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case None:
			break;
		default:
			break;
		
		}
		
        
        
        
		return ret.toString();
		
	}
	
	 private void drawString(Graphics g, String text, int x, int y) {
	        for (String line : text.split("\n"))
	            g.drawString(line, x, y += g.getFontMetrics().getHeight());
	    }
}
