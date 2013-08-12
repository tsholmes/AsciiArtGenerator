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


	public String generateTextFromPicture( String aPicturePath, int aCols )
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
		
		System.out.println(mPixelAnalyzer.getBuckets());
		
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
        
        
        
		return ret.toString();
		
	}
	
	 private void drawString(Graphics g, String text, int x, int y) {
	        for (String line : text.split("\n"))
	            g.drawString(line, x, y += g.getFontMetrics().getHeight());
	    }
}
