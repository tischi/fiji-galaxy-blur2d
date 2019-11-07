package de.embl.cba.galaxy;

import de.embl.cba.blur2d.Blur2dCommand;

import ij.IJ;

import org.apache.commons.io.FilenameUtils;
import org.scijava.command.Command;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandModule;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;
import org.scijava.plugin.Attr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class YamlCreator extends OutputWriter
{
	
	public Command command;
	public String pluginId;
	
	public final CommandInfo info;
	
	
	
	GalaxyUtilities gu = GalaxyUtilities.getInstance();
	

	public YamlCreator(Command command,String help )
	{
		super(help);
		
		final CommandModule module = new CommandModule( new CommandInfo( Blur2dCommand.class ), command );
		
		this.command = command;
		this.info = module.getInfo();
		this.pluginId = command.getClass().getAnnotation(Plugin.class).menuPath();

	}
	
	
	@Override
	public void outputFile(CommandInfo info) throws IOException {
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("tool_id", this.pluginId);
		data.put("tool_name", this.pluginId);
		data.put("version", version );		
		data.put("description","Fiji" + this.pluginId);
		data.put("help",help);
		
		//loop input parameters
		
		List<Map> params = new ArrayList<>();
		
		for( final ModuleItem<?> input : info.inputs() )
		{
			if(GalaxyUtilities.hmap.containsKey(input.getType()))
			{
				Map<String,String> inParam = new HashMap<String,String>();
				inParam.put("name",input.getName());				
				inParam.put("data_type",gu.getGalaxyDataType(input.getType()));
				inParam.put("default_value",input.getDefaultValue().toString());
				params.add(inParam);

			}			
		}
		
		data.put("input_params",params);
		
		//loop output parameters
	
		List<Map> oparams = new ArrayList<>();
		
		
		Attr[] outAttr = this.command.getClass().getAnnotation(Plugin.class).attrs();
		
		String outputFormats[]=outAttr[0].value().split(",");
		for (String s: outputFormats)
		{
				Map<String,String> outParam = new HashMap<String,String>();
				outParam.put("name",s);				
				outParam.put("data_type",s);//not used in ansible
				outParam.put("format",s);
				
				for( final ModuleItem<?> output : info.outputs() )
				{
					
					if(output.getDefaultValue() instanceof File)
					{
						File tmpFile = (File) output.getDefaultValue();
						String ext="";
						int i = tmpFile.getName().lastIndexOf('.');
						int p = Math.max(tmpFile.getName().lastIndexOf('/'), tmpFile.getName().lastIndexOf('\\'));

						if (i > p) {
						    ext = tmpFile.getName().substring(i+1);
						}
						if(s.equals(ext)||(ext.equals("tif") && s.equals("tiff")))
						{
							outParam.put("default_value",tmpFile.getName());
						}
					}			
				}
				oparams.add(outParam);

		}
		

		
		data.put("output_params",oparams);

		Yaml yaml = new Yaml();
		
		FileWriter writer = new FileWriter("main.yaml");
		yaml.dump(data,writer);
		
	}
	
	
	public static void main( String[] args ) throws IOException
	{
		// Set parameters to be used for testing
		final Blur2dCommand command = new Blur2dCommand();
		//		command.run();

		YamlCreator yc = new YamlCreator( command, command.getHelp() );
		yc.outputFile( yc.info );

		
	}

}
