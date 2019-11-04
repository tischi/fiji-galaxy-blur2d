package de.embl.cba.galaxy;

import de.embl.cba.blur2d.Blur2dCommand;
import org.scijava.command.Command;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandModule;
import org.scijava.module.ModuleItem;

public class GalaxyXmlCreator
{
	public GalaxyXmlCreator( Command command )
	{
		final CommandModule module = new CommandModule( new CommandInfo( Blur2dCommand.class ), command );
		final CommandInfo info = module.getInfo();

		System.out.println( "INPUTS:");
		for( final ModuleItem<?> input : info.inputs() )
		{
			System.out.println( input.getName() );
			System.out.println( input.getType() );
		}

		System.out.println( "OUTPUTS:");
		for( final ModuleItem<?> output : info.outputs() )
		{
			System.out.println( output.getName() );
			System.out.println( output.getType() );
		}
	}

	public static void main( String[] args )
	{
		new GalaxyXmlCreator( new Blur2dCommand() );
	}
}
