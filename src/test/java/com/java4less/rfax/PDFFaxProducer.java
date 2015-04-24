package com.java4less.rfax;

import java.awt.Image;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class PDFFaxProducer implements FaxProducer
{
	/**Class member*/
	public Image pageImage;
	
	/**
	 * Test method.
	 * 
	 * @param	value
	 * 			Any value
	 */
	public void setPDFFile(String value)
	{
		if(value==null)
		{
			throw new IllegalArgumentException("PDF file is null.");
		}
	}
}