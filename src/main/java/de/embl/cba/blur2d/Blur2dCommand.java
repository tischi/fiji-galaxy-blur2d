package de.embl.cba.blur2d;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;
import net.imagej.ImageJ;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.widget.Button;

import java.io.File;

@Plugin(type = Command.class, headless = true, menuPath = "Blur2d")
public class Blur2dCommand implements Command {
	
//	@Parameter
//	public LogService logService;

	// TODO: can this also be an Img
	@Parameter( label = "Input image" )
	public File inputImageFile = new File( "/Users/tischer/Documents/fiji-galaxy-blur2d/src/test/resources/image2d.tif" );;

	@Parameter( label = "Blur radius [pixels]" )
	public double radius = 1.0;

	@Parameter( label = "Output image", type = ItemIO.OUTPUT )
	public File blurredOutputImageFile;

	@Parameter( label = "Help", callback = "showHelp" )
	public Button helpButton;

	public static void main( String[] args )
	{
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();

		// invoke the plugin
		ij.command().run( Blur2dCommand.class, true );
	}

	@Override
	public void run()
	{
		final ImagePlus imagePlus = IJ.openImage( inputImageFile.getAbsolutePath() );

		final GaussianBlur gaussianBlur = new GaussianBlur();
		final ImageProcessor processor = imagePlus.getProcessor();
		gaussianBlur.blurGaussian( processor, radius );
		final ImagePlus blurred = new ImagePlus( "blurred", processor );

		blurredOutputImageFile = new File( inputImageFile.getParent() + File.separator + "blur2d.tif" );
		IJ.save( blurred, blurredOutputImageFile.getAbsolutePath() );
	}

	private void showHelp()
	{
		IJ.showMessage( getHelp() );
	}

	public String getHelp()
	{
		return "aaaa";
	}
}