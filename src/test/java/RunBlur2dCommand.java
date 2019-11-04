import de.embl.cba.blur2d.Blur2dCommand;
import net.imagej.ImageJ;

public class RunBlur2dCommand
{
	public static void main( String[] args )
	{
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();

		// invoke the plugin
		ij.command().run( Blur2dCommand.class, true );

		// /Applications/Fiji.app/Contents/MacOS/ImageJ-macosx --headless --run "Blur2d" "inputImageFile='/Users/tischer/Documents/fiji-galaxy-blur2d/src/test/resources/image2d.tif', outputImageFile='/Users/tischer/Documents/fiji-galaxy-blur2d/src/test/resources/image2d_blurred.tif',radius='4.0'"
	}
}
