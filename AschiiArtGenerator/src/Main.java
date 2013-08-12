import java.awt.Font;
import java.io.File;
import java.io.PrintStream;


public class Main {
	public static void main(String[] args ) 
	{
		try {
		    System.setOut(new PrintStream(new File("c:\\tmp\\output-file.txt")));
		} catch (Exception e) {
		     e.printStackTrace();
		}
		String lInputFile = "C:\\tmp\\Wonder-Woman-AP (1).jpg";
		int lColumns = 700;
//		
//		System.out.println("sqrt");
//		AschiiGenerator as = new AschiiGenerator( PixelAnalyzer.PixelScore.SquareRootMethod );
//		System.out.println( as.generateTextFromPicture(lInputFile, lColumns ) );
//
//		System.out.println("average");
//		as = new AschiiGenerator( PixelAnalyzer.PixelScore.AverageMethod );
//		System.out.println( as.generateTextFromPicture( lInputFile, lColumns ) );

		System.out.println("lightness");
		AschiiGenerator as = new AschiiGenerator( PixelAnalyzer.PixelScore.LightnessMethod, new Font("consolas", Font.BOLD, 3));
		System.out.println( as.generateTextFromPicture(lInputFile, lColumns ) );
		
//
//		System.out.println("luminosity");
//		as = new AschiiGenerator( PixelAnalyzer.PixelScore.LuminosityMethod );
//		System.out.println( as.generateTextFromPicture( lInputFile, lColumns ) );
//		
//		
	}
	
}
