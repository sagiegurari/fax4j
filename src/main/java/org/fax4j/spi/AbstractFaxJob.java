package org.fax4j.spi;

import java.io.File;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;

/**
 * This class provides common partial implementation of the fax job.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.34
 */
public abstract class AbstractFaxJob implements FaxJob
{
	/**
	 * This is the class constructor.
	 */
	public AbstractFaxJob()
	{
		super();
	}

	/**
	 * This function returns the path to the file to fax.
	 * 
	 * @return	The path to the file to fax
	 */
	public String getFilePath()
	{
		//get file
		File fileInstance=this.getFile();
		
		String filePath=null;
		if(fileInstance!=null)
		{
			filePath=fileInstance.getPath();
		}
		
		return filePath;
	}

	/**
	 * This function sets the path to the file to fax.
	 * 
	 * @param	filePath
	 * 			The path to the file to fax
	 */
	public void setFilePath(String filePath)
	{
		//create a new file
		File fileInstance=null;
		if(filePath!=null)
		{
			fileInstance=new File(filePath);
		}
		
		//set file
		this.setFile(fileInstance);
	}
	
	/**
	 * This function adds the common attributes for the toString printing.
	 * 
	 * @param 	faxJobType
	 * 			The fax job type
	 * @return	The buffer used
	 */
	protected StringBuilder addToStringAttributes(String faxJobType)
	{
		//init buffer
		StringBuilder buffer=new StringBuilder(500);
		buffer.append(faxJobType);
		buffer.append(":");

		//append values
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("ID: ");
		buffer.append(this.getID());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("File: ");
		buffer.append(this.getFilePath());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Priority: ");
		buffer.append(this.getPriority());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Target Address: ");
		buffer.append(this.getTargetAddress());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Target Name: ");
		buffer.append(this.getTargetName());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Sender Name: ");
		buffer.append(this.getSenderName());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Sender Fax Number: ");
		buffer.append(this.getSenderFaxNumber());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Sender Email: ");
		buffer.append(this.getSenderEmail());
		
		return buffer;
	}
}