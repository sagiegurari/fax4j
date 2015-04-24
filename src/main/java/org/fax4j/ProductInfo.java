package org.fax4j;

import java.util.Properties;
import org.fax4j.common.Logger;
import org.fax4j.util.LibraryConfigurationLoader;

/**
 * This class is used to print the product info of the
 * fax4j library.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.05
 * @since	0.1
 */
public final class ProductInfo
{
	/**
	 * This is the class constructor.
	 */
	private ProductInfo()
	{
		super();
	}
	
	/**
	 * Prints the product info to the system out and
	 * exists the process.
	 * 
	 * @param	args
	 * 			The command line arguments (not used)
	 */
	public static void main(String[] args)
	{
		//print product info
		ProductInfo.printProductInfo();
		
		//exit
		System.exit(0);
	}
	
	/**
	 * Prints the product info to the system out.
	 */
	public static void printProductInfo()
	{
		//get product info
		String text=ProductInfo.getProductInfoPrintout();
		
		//print values
		System.out.println(text);
	}

	/**
	 * Returns the product info printout.
	 * 
	 * @return	The product info printout
	 */
	public static String getProductInfoPrintout()
	{
		//read properties
		Properties properties=LibraryConfigurationLoader.readInternalConfiguration();

		//get values
		String name=properties.getProperty("org.fax4j.product.name");
		String version=properties.getProperty("org.fax4j.product.version");

		//init buffer
		StringBuilder buffer=new StringBuilder();
		buffer.append(name);
		buffer.append(" library.");
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Version: ");
		buffer.append(version);
		
		//get text
		String text=buffer.toString();
		
		return text;
	}
}