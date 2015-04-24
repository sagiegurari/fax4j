package org.fax4j.spi;

import java.io.File;
import java.util.Properties;
import org.fax4j.common.Logger;

/**
 * This class provides basic implementation of the fax job.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.05
 * @since	0.11
 */
public class FaxJobImpl extends AbstractFaxJob
{
	/**The job ID*/
	private String id;
	/**The file to fax*/
	private File file;
	/**The fax priority*/
	private FaxJobPriority priority;
	/**The fax job target address*/
	private String targetAddress;
	/**The fax job target name*/
	private String targetName;
	/**The fax job sender name*/
	private String senderName;
	/**The fax job sender fax number*/
	private String senderFaxNumber;
	/**The fax job sender email address*/
	private String senderEmail;
	/**The fax job properties*/
	private final Properties PROPERTIES;

	/**
	 * This is the class constructor.
	 */
	public FaxJobImpl()
	{
		super();
		
		//init properties
		this.PROPERTIES=new Properties();
	}

	/**
	 * This function returns the string value of the fax job.
	 * 
	 * @return	The string value
	 */
	@Override
	public String toString()
	{
		//setup toString
		StringBuilder buffer=this.addToStringAttributes("Fax Job");
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Properties:");
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append(this.PROPERTIES);
		
		//get text
		String text=buffer.toString();
		
		return text;
	}

	/**
	 * This function returns the fax job ID.
	 * 
	 * @return	The fax job ID
	 */
	public String getID()
	{
		return this.id;
	}

	/**
	 * This function sets the fax job ID.
	 * 
	 * @param	id
	 * 			The fax job ID
	 */
	public void setID(String id)
	{
		this.id=id;
	}

	/**
	 * This function returns the file to fax.
	 * 
	 * @return	The file to fax
	 */
	public File getFile()
	{
		return this.file;
	}

	/**
	 * This function sets the file to fax.
	 * 
	 * @param	file
	 * 			The file to fax
	 */
	public void setFile(File file)
	{
		this.file=file;
	}

	/**
	 * This function returns the priority.
	 * 
	 * @return	The priority
	 */
	public FaxJobPriority getPriority()
	{
		return this.priority;
	}

	/**
	 * This function sets the priority.
	 * 
	 * @param	priority
	 * 			The priority
	 */
	public void setPriority(FaxJobPriority priority)
	{
		this.priority=priority;
	}

	/**
	 * This function returns the fax job target address.
	 * 
	 * @return	The fax job target address
	 */
	public String getTargetAddress()
	{
		return this.targetAddress;
	}

	/**
	 * This function sets the fax job target address.
	 * 
	 * @param	targetAddress
	 * 			The fax job target address
	 */
	public void setTargetAddress(String targetAddress)
	{
		this.targetAddress=targetAddress;
	}

	/**
	 * This function returns the fax job target name.
	 * 
	 * @return	The fax job target name
	 */
	public String getTargetName()
	{
		return this.targetName;
	}

	/**
	 * This function sets the fax job target name.
	 * 
	 * @param	targetName
	 * 			The fax job target name
	 */
	public void setTargetName(String targetName)
	{
		this.targetName=targetName;
	}

	/**
	 * This function returns the fax job sender name.
	 * 
	 * @return	The fax job sender name
	 */
	public String getSenderName()
	{
		return this.senderName;
	}

	/**
	 * This function sets the fax job sender name.
	 * 
	 * @param	senderName
	 * 			The fax job sender name
	 */
	public void setSenderName(String senderName)
	{
		this.senderName=senderName;
	}

	/**
	 * This function returns the fax job sender fax number.
	 * 
	 * @return	The fax job sender fax number
	 */
	public String getSenderFaxNumber()
	{
		return this.senderFaxNumber;
	}

	/**
	 * This function sets the fax job sender fax number.
	 * 
	 * @param	senderFaxNumber
	 * 			The fax job sender fax number
	 */
	public void setSenderFaxNumber(String senderFaxNumber)
	{
		this.senderFaxNumber=senderFaxNumber;
	}

	/**
	 * This function returns the fax job sender email address.
	 * 
	 * @return	The fax job sender email address
	 */
	public String getSenderEmail()
	{
		return this.senderEmail;
	}

	/**
	 * This function sets the fax job sender email address.
	 * 
	 * @param	senderEmail
	 * 			The fax job sender email address
	 */
	public void setSenderEmail(String senderEmail)
	{
		this.senderEmail=senderEmail;
	}

	/**
	 * This function sets the fax job property.
	 * 
	 * @param 	key
	 * 			The property key
	 * @param 	value
	 * 			The property value
	 */
	public void setProperty(String key,String value)
	{
		//set property value
		this.PROPERTIES.setProperty(key,value);
	}

	/**
	 * This function returns the fax job property for the
	 * given key.
	 * 
	 * @param 	key
	 * 			The property key
	 * @param 	defaultValue
	 * 			The default value
	 * @return	The property value
	 */
	public String getProperty(String key,String defaultValue)
	{
		//get property value
		return this.PROPERTIES.getProperty(key,defaultValue);
	}
}