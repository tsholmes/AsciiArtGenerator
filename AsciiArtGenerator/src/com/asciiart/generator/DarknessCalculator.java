package com.asciiart.generator;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;



class CharScore implements Comparable<CharScore>
{
	public char c;
	public double score;
	
	public int compareTo(CharScore o) {
		if ( score < o.score ) 
		{
			return -1;
		}
		else if ( score == o.score )
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	
}

public class DarknessCalculator {
	
	private Font mFont;
	private String mUsedCharacters;
	private PixelAnalyzer.PixelScore mPixelMethod;
	
	private ArrayList<CharScore> mScoreList;	
	
	public DarknessCalculator( String aCharacters, Font aFont, PixelAnalyzer.PixelScore aPixelMethod  )
	{
		mPixelMethod = aPixelMethod;
		mScoreList = new ArrayList<CharScore>();
		mFont = aFont;
		mUsedCharacters = aCharacters;
		
		double min = 1;
		double max = 0;
		
		// make image of all characters, and get score
		for ( int i = 0; i < mUsedCharacters.length(); i++ )
		{
			char c = mUsedCharacters.charAt(i);
				
			// make picture of c
			double score = getScoreOfChar(c);
			
			CharScore nextScore = new CharScore();
			nextScore.c = c;
			nextScore.score = score;

			min = Math.min(min, score);
			max = Math.max(score, max);
			
			mScoreList.add(nextScore);			
		}
		
		
		Collections.sort(mScoreList);
		for ( CharScore lCharScore : mScoreList )
		{
			lCharScore.score = ( lCharScore.score - min ) / ( max - min );
		}
		
	}

	private static int BrightnessAverage(int r, int g, int b)
	{
	   return  (r + g + b) / 3;
	}


	private static int BrightnessLuminosity(int r, int g, int b)
	{
	   return  (int)(0.21*r + 0.71*g + 0.07 *b);
	}
	
	private static int BrightnessSqrt(int r, int g, int b)
	{
	   return  (int) Math.sqrt(r * r * .241 + 
	      g * g * .691 + 
	      b * b * .068);
	}

	private static int BrightnessLightness(int r, int g, int b)
	{
	   return  ( Math.max(r, Math.max(b, g)) + Math.min(Math.min(r,g), b) ) / 2;
	}
	
	private double getScoreOfChar( char c )
	{
		// make font
		BufferedImage i = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		Graphics g = i.createGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 500, 500);
		
		g.setColor(Color.black);
		g.setFont( mFont );
		g.drawString( ""+c, 100, 400 ) ;
		g.dispose();

		
		return getScoreOfSection(i, 0, 0, i.getWidth(), i.getHeight());
		
		
	}
	
	public double getScoreOfSection( BufferedImage i, int x0, int y0, int dx, int dy )
	{
		double score = 0;
		
		for ( int x = x0; x < x0 + dx; x++ )
		{
			for ( int y = y0; y < y0 + dy; y++ )
			{
				int rgbval = i.getRGB(x, y);
				int r = (rgbval >> 16) & 0xff;
				int g = (rgbval >> 8) & 0xff;
				int b = rgbval & 0xff;
				
				double pixelScore = 0;
				switch ( mPixelMethod )
				{
				case AverageMethod : 
					pixelScore = BrightnessAverage(r, g, b);
					break;
				case  LuminosityMethod: 
					pixelScore = BrightnessLuminosity(r, g, b);
					break;
				case LightnessMethod : 
					pixelScore = BrightnessLightness(r, g, b);
				case SquareRootMethod:
					pixelScore = BrightnessSqrt(r, g, b);
					break;
				default:
					break;
				}
				if ( pixelScore < 0 )
				{
					System.exit(1);
				}
				pixelScore /= ( 255.0 );
				if ( pixelScore > 1 )
				{
					System.exit(-1);
				}
				score += pixelScore;
			}
		}
		
		score = score / ( ( dx ) * ( dy ) );
		
		return score;
	}

	public char getBestCharacterForScore(double score) {
		// binary search for best score
		int l = 0;
		int r = mScoreList.size() - 1;
		int m = ( l + r ) / 2;
		
		while ( l < r )
		{
			m = ( l + r ) / 2;
			
			if ( mScoreList.get(m).score < score )
			{
				l = m + 1;
			}
			else
			{
				r = m - 1 ;
			}
		}
		
		return mScoreList.get(m).c;
	}
}
