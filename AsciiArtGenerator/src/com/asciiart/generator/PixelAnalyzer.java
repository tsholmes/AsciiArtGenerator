package com.asciiart.generator;
import java.awt.Font;
import java.awt.image.BufferedImage;


public class PixelAnalyzer {
	
	enum PixelScore
	{
		SquareRootMethod, 
		LightnessMethod, 
		AverageMethod, 
		LuminosityMethod
		
	}

	

	private DarknessCalculator mDarknessCalculator;
	
	int [] mBuckets;
	int mNumBuckets;

	private PixelScore mPixelMethod;
	
	public PixelAnalyzer(String string, Font string2, int aBuckets, PixelScore aPixelMethod ) {
		
		mPixelMethod = aPixelMethod;
		mDarknessCalculator = new DarknessCalculator(string, string2, aPixelMethod);
		
		mNumBuckets = aBuckets;
		mBuckets = new int[mNumBuckets];
		
		for ( int i = 0; i < mBuckets.length; i++ )
		{
			mBuckets[i] = 0;
		}
		
		
	}
	
	public void addScore( double score ) 
	{
		// determine bucket position
		mBuckets[(int) (score * (mNumBuckets-1))]++;
	}

	public double getScoreOfSection(BufferedImage im, int x0, int y0, int dx, int dy) {
		
		return mDarknessCalculator.getScoreOfSection(im, x0, y0, dx, dy);
	}

	public char getBestCharacterForScore(double score) {
		return mDarknessCalculator.getBestCharacterForScore(score);
	}
	
	public String getBuckets() 
	{
		String ret = "{";
		
		if ( mBuckets.length > 1 )
		{
			ret += mBuckets[0];
		}
		for ( int i = 1; i < mBuckets.length; i++ )
		{
			ret += mBuckets[i] + ",";
		}
		
		ret += "}";
		
		return ret;
		
	}

	

}
