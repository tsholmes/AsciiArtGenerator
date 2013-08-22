import java.awt.Font;
import java.io.File;
import java.io.PrintStream;


public class Main {
	public static void main(String[] args ) 
	{
		String lInputFile = "C:\\tmp\\asdf.png_toASCHII.png";
		int lColumns = 350;

		AschiiGenerator as = new AschiiGenerator( PixelAnalyzer.PixelScore.LightnessMethod, new Font("consolas", Font.PLAIN, 8));
		
		//as.saveTextGifFromGifFile(lInputFile, lColumns);
		as.textImageFromPicture(lInputFile, lColumns);
		
	}
	
}
