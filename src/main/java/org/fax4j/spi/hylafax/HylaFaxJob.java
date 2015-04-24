package org.fax4j.spi.hylafax;

import gnu.hylafax.Job;
import java.io.File;
import org.fax4j.FaxException;
import org.fax4j.spi.AbstractFaxJob;

/**
 * This class provides the hylafax implementation of the fax job.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.07
 * @since	0.1
 */
public class HylaFaxJob extends AbstractFaxJob
{
	/**The internal hylafax job*/
	private final Job JOB;
	/**The file to fax*/
	private File file;
	
	/**
	 * This is the class constructor.
	 * 
	 * @param	job
	 * 			The internal hylafax job
	 */
	public HylaFaxJob(Job job)
	{
		super();
		
		//get job
		this.JOB=job;
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
		StringBuilder buffer=this.addToStringAttributes("Hylafax Fax Job");
		
		//get text
		String text=buffer.toString();
		
		return text;
	}
	
	/**
	 * Returns the internal hylafax job.
	 *  
	 * @return	The internal hylafax job
	 */
	protected Job getHylaFaxJob()
	{
		return this.JOB;
	}

	/**
	 * This function returns the fax job ID.
	 * 
	 * @return	The fax job ID
	 */
	public String getID()
	{
		return String.valueOf(this.JOB.getId());
	}

	/**
	 * This function sets the fax job ID.
	 * 
	 * @param	id
	 * 			The fax job ID
	 */
	public void setID(String id)
	{
		throw new FaxException("Fax job ID should not be set via this method.");
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
		int priority=Job.PRIORITY_NORMAL;
		try
		{
			priority=this.JOB.getPriority();
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while extracting job priority.",exception);
		}

		FaxJobPriority faxJobPriority=null;
		switch(priority)
		{
			case Job.PRIORITY_HIGH:
				faxJobPriority=FaxJobPriority.HIGH_PRIORITY;
				break;
			default:
				faxJobPriority=FaxJobPriority.MEDIUM_PRIORITY;
				break;
		}
		
		return faxJobPriority;
	}

	/**
	 * This function sets the priority.
	 * 
	 * @param	priority
	 * 			The priority
	 */
	public void setPriority(FaxJobPriority priority)
	{
		try
		{
			if(priority==FaxJobPriority.HIGH_PRIORITY)
			{
				this.JOB.setPriority(Job.PRIORITY_HIGH);
			}
			else
			{
				this.JOB.setPriority(Job.PRIORITY_NORMAL);
			}
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting job priority.",exception);
		}
	}

	/**
	 * This function returns the fax job target address.
	 * 
	 * @return	The fax job target address
	 */
	public String getTargetAddress()
	{
		String value=null;
		try
		{
			value=this.JOB.getDialstring();
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while extracting job target address.",exception);
		}

		return value;
	}

	/**
	 * This function sets the fax job target address.
	 * 
	 * @param	targetAddress
	 * 			The fax job target address
	 */
	public void setTargetAddress(String targetAddress)
	{
		try
		{
			this.JOB.setDialstring(targetAddress);
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting job target address.",exception);
		}
	}

	/**
	 * This function returns the fax job target name.
	 * 
	 * @return	The fax job target name
	 */
	public String getTargetName()
	{
		return null;
	}

	/**
	 * This function sets the fax job target name.
	 * 
	 * @param	targetName
	 * 			The fax job target name
	 */
	public void setTargetName(String targetName)
	{
		//empty implementation
	}

	/**
	 * This function returns the fax job sender name.
	 * 
	 * @return	The fax job sender name
	 */
	public String getSenderName()
	{
		String value=null;
		try
		{
			value=this.JOB.getFromUser();
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while extracting job sender name.",exception);
		}

		return value;
	}

	/**
	 * This function sets the fax job sender name.
	 * 
	 * @param	senderName
	 * 			The fax job sender name
	 */
	public void setSenderName(String senderName)
	{
		try
		{
			this.JOB.setFromUser(senderName);
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting job sender name.",exception);
		}
	}

	/**
	 * This function returns the fax job sender fax number.
	 * 
	 * @return	The fax job sender fax number
	 */
	public String getSenderFaxNumber()
	{
		return null;
	}

	/**
	 * This function sets the fax job sender fax number.
	 * 
	 * @param	senderFaxNumber
	 * 			The fax job sender fax number
	 */
	public void setSenderFaxNumber(String senderFaxNumber)
	{
		//empty implementation
	}

	/**
	 * This function returns the fax job sender email address.
	 * 
	 * @return	The fax job sender email address
	 */
	public String getSenderEmail()
	{
		return null;
	}

	/**
	 * This function sets the fax job sender email address.
	 * 
	 * @param	senderEmail
	 * 			The fax job sender email address
	 */
	public void setSenderEmail(String senderEmail)
	{
		//empty implementation
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
		try
		{
			this.JOB.setProperty(key,value);
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while setting job property.",exception);
		}
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
		String value=null;
		try
		{
			value=this.JOB.getProperty(key);
		}
		catch(Exception exception)
		{
			throw new FaxException("Error while extracting job property.",exception);
		}
		
		if(value==null)
		{
			value=defaultValue;
		}
		
		return value;
	}
}