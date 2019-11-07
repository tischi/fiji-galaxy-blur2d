package de.embl.cba.galaxy;

import java.io.File;
import java.io.IOException;

import org.scijava.command.Command;
import org.scijava.command.CommandInfo;

import ij.IJ;

public abstract class OutputWriter {
	

	public final String help;
    public final String version;	
	
	OutputWriter(String help)
	{
		this.help = help;
		this.version= IJ.getVersion();
	}
	public abstract void outputFile(CommandInfo info) throws IOException;

}
