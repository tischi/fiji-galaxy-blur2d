package de.embl.cba.galaxy;

import de.embl.cba.blur2d.Blur2dCommand;

import ij.IJ;

import org.scijava.command.Command;
import org.scijava.command.CommandInfo;
import org.scijava.command.CommandModule;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.Attr;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.CDATA;


public class GalaxyXmlCreator
{
	
	public final CommandInfo info;
	public final Command command;
	public final String help;
	
	public String pluginId;
	
	GalaxyUtilities gu = GalaxyUtilities.getInstance();
	
	public GalaxyXmlCreator( Command command )
	{
		this( command, "" );
	}

	public GalaxyXmlCreator( Command command, String help )
	{
		final CommandModule module = new CommandModule( new CommandInfo( Blur2dCommand.class ), command );
		
		this.command = command;
		this.info = module.getInfo();
		this.help = help;
		this.pluginId = command.getClass().getAnnotation(Plugin.class).menuPath();

	}
	
	public Document buildXML(CommandInfo info) throws IOException
	{		
		
		String version = IJ.getVersion();

		Document doc = new Document();
		
		Element tool = new Element("tool");

		tool.setAttribute(new Attribute("id","fiji"+pluginId));
		tool.setAttribute(new Attribute("name",pluginId));
		tool.setAttribute(new Attribute("version",version));
		
		//description
		Element description = new Element("description");
		description.setText("Fiji"+pluginId);
		tool.addContent(description);
		
	
		//inputs
		Element inputs = new Element("inputs");
		
		
		for( final ModuleItem<?> input : info.inputs() )
		{
			Element param = new Element("param");
			if(GalaxyUtilities.hmap.containsKey(input.getType()))
			{
				param.setAttribute("name",input.getName());
				param.setAttribute("type",gu.getGalaxyDataType(input.getType()));
				inputs.addContent(param);
			}
			
		}
		
		tool.addContent(inputs);	
		
		
		//outputs
		Element outputs = new Element("outputs");
		
		Attr[] outAttr = this.command.getClass().getAnnotation(Plugin.class).attrs();
		
		String outputFormats[]=outAttr[0].value().split(",");
		
		for (String s: outputFormats)
		{
			Element data = new Element("data");
			data.setAttribute("name",s);
			data.setAttribute("format",s);
			
			Element discoverDataset = new Element("discover_datasets");
			discoverDataset.setAttribute("pattern","__designation_and_ext__");
			discoverDataset.setAttribute("directory","output");
			discoverDataset.setAttribute("visible","true");
			
			data.addContent(discoverDataset);
			outputs.addContent(data);
		}
		
				
		tool.addContent(outputs);
		
		
		//stdio
		Element stdio = new Element("stdio");
		Element exitCode=new Element("exit_code");
		exitCode.setAttribute("range","1:");
		exitCode.setAttribute("level","fatal");
		exitCode.setAttribute("description","Error");
		stdio.addContent(exitCode);
		
		tool.addContent(stdio);
		
		//tests
		Element tests = new Element("tests");
		
		tool.addContent(tests);
		
		//help
		Element help = new Element("help");
		help.setText(this.help);
		
		tool.addContent(help);
		
		
	
		//command
		Element command = new Element("command");
		
		String cmdStr = buildCmd(tool,info);
		command.addContent(new CDATA(cmdStr));
		tool.addContent(command);

		doc.setRootElement(tool);
		
		
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());		
		//System.out.println(xmlOutputter.outputString(doc));
		xmlOutputter.output(doc, new FileWriter(this.pluginId+".xml"));
		return doc;
		
	}
	
	private String buildCmd(Element tool,CommandInfo info)
	{
		String cmd[] = {"mkdir working_dir &&" 
						+ "mkdir output &&"};
		
		String ijArgs[]= {""};
		
		List<Element> params =tool.getChild("inputs").getChildren("param"); 
		
		params.forEach(p->{			
			if("data".equals(p.getAttributeValue("type")))
			{
				String inputName = p.getAttributeValue("name");
				cmd[0]=cmd[0] + "ln -s $"+inputName + " working_dir/$"+inputName+".element_identifier && ";
				ijArgs[0]=ijArgs[0]+p.getAttributeValue("name")+"='working_dir/$"+p.getAttributeValue("name")+".element_identifier',";
			}
			else
			{
				ijArgs[0]=ijArgs[0]+p.getAttributeValue("name")+"='$"+p.getAttributeValue("name")+"'";
			}
		});
		
		

		cmd[0]= cmd[0]+ "ImageJ --ij2 --headless --run " +"\""+this.pluginId+"\" "+"\""+ijArgs[0]+"\""+"&& ";
		
		for( final ModuleItem<?> output : info.outputs() )
		{
			String outputVal = output.getDefaultValue().toString();
			String mv = "mv working_dir/"+outputVal + " output";
			cmd[0]=cmd[0]+mv;
		}
		

		return cmd[0];
	}

	public static void main( String[] args ) throws IOException
	{
		// Set parameters to be used for testing
		final Blur2dCommand command = new Blur2dCommand();
		//		command.run();

		GalaxyXmlCreator gxc = new GalaxyXmlCreator( command, command.getHelp() );
		gxc.buildXML(gxc.info);
		
	}
}
