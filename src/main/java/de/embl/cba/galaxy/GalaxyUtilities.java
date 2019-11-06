package de.embl.cba.galaxy;

import java.io.File;
import java.util.HashMap;

public class GalaxyUtilities {
	
	private static GalaxyUtilities galaxyUtilInstance = null;
	
	public static HashMap<Class, String> hmap; //this is used to filter non-galaxy data types
	
	private GalaxyUtilities()
	{
		hmap = new HashMap<Class,String>();
		
		hmap.put(File.class,"data");
		hmap.put(double.class,"text");
	}
	
	public static GalaxyUtilities getInstance()
	{
		 if (galaxyUtilInstance == null) 
			 galaxyUtilInstance = new GalaxyUtilities(); 
	  
	        return galaxyUtilInstance; 
	}
	
	public String getGalaxyDataType(Class<?> c)
	{
		return this.hmap.get(c);
	}
}
