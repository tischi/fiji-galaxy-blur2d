package de.embl.cba.blur2d;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.io.File;

@Plugin(type = Command.class, headless = true, menuPath = "Blur2d")
public class Blur2dCommand implements Command {
	
//	@Parameter
//	public LogService logService;

	// TODO: can this also be an Img
	@Parameter(label = "Input image")
	public File inputImageFile;

	@Parameter(label = "Output image [.tif]", style = "save")
	public File outputImageFile;

	@Parameter(label = "Blur radius [pixels]")
	public double radius = 1.0;

	@Override
	public void run() {

		final ImagePlus imagePlus = IJ.openImage( inputImageFile.getAbsolutePath() );

		final GaussianBlur gaussianBlur = new GaussianBlur();
		final ImageProcessor processor = imagePlus.getProcessor();
		gaussianBlur.blurGaussian( processor, radius );
		final ImagePlus blurred = new ImagePlus( "blurred", processor );

		//final String inputImageDirectory = inputImageFile.getParent();
		IJ.save( blurred, outputImageFile.getAbsolutePath() );
	}
}