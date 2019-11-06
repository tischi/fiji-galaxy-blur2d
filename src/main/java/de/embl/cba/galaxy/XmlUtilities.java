package de.embl.cba.galaxy;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlUtilities
{
	public static void print( Document document )
	{
		final XMLOutputter xmlOutputter = new XMLOutputter( Format.getPrettyFormat() );
		final String string = xmlOutputter.outputString( document );
		System.out.println( string );
	}
}
