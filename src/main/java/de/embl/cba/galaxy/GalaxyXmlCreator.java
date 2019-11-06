package de.embl.cba.galaxy;

import de.embl.cba.blur2d.Blur2dCommand;
import org.scijava.command.Command;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandModule;
import org.scijava.module.ModuleItem;

import java.io.File;

public class GalaxyXmlCreator
{
	public GalaxyXmlCreator( Command command )
	{
		this( command, "" );
	}

	public GalaxyXmlCreator( Command command, String help )
	{
		final CommandModule module = new CommandModule( new CommandInfo( Blur2dCommand.class ), command );
		final CommandInfo info = module.getInfo();

		System.out.println( "INPUTS:");
		for( final ModuleItem<?> input : info.inputs() )
		{
			System.out.println( input.getName() );
			System.out.println( input.getType() );
			System.out.println( input.getDefaultValue() );
		}

		System.out.println( "OUTPUTS:");
		for( final ModuleItem<?> output : info.outputs() )
		{
			System.out.println( output.getName() );
			System.out.println( output.getType() );
		}
		// do s.th. with the help
	}

	public static void main( String[] args )
	{
		// Set parameters to be used for testing
		final Blur2dCommand command = new Blur2dCommand();
		//		command.run();

		new GalaxyXmlCreator( command, command.getHelp() );
	}
}
