package org.fax4j.spi.windows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.FaxJobStatus;
import org.fax4j.common.Logger;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.windows.WindowsFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.util.IOHelper;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This is a helper class for the windows based SPIs.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.41.5
 */
public final class WindowsFaxClientSpiHelper
{
	/**The loaded native library flag*/
	private static boolean nativeLibraryLoaded=false;
	/**The lock used to synchronize native calls*/
	public static final Object NATIVE_LOCK=new Object();

	/**
	 * This is the class constructor.
	 */
	private WindowsFaxClientSpiHelper()
	{
		super();
	}
	
	/**
	 * This function extracts the native resources (the fax4j.exe and fax4j.dll) 
	 * and pushes them to the fax4j temporary directory.
	 */
	public static void extractNativeResources()
	{
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			//get target directory
			File directory=IOHelper.getFax4jInternalTemporaryDirectory();
			
			//extract resources
			String[] names=new String[]{"fax4j.dll","fax4j.exe"};
			int amount=names.length;
			String name=null;
			File file=null;
			InputStream inputStream=null;
			OutputStream outputStream=null;
			for(int index=0;index<amount;index++)
			{
				//get next resource
				name=names[index];
				
				//get file
				file=new File(directory,name);
				
				if(!file.exists())
				{
					//get input stream
					inputStream=WindowsFaxClientSpiHelper.class.getResourceAsStream(name);
					if(inputStream!=null)
					{
						try
						{
							//create output stream
							outputStream=new FileOutputStream(file);
							
							//write data to file
							IOHelper.readAndWriteStreams(inputStream,outputStream);
						}
						catch(IOException exception)
						{
							throw new FaxException("Unable to extract resource: "+name,exception);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Loads the native library if not loaded before.
	 * 
	 * @param	logger
	 * 			The logger
	 */
	public static void loadNativeLibrary(Logger logger)
	{
		synchronized(WindowsFaxClientSpiHelper.NATIVE_LOCK)
		{
			if(!WindowsFaxClientSpiHelper.nativeLibraryLoaded)
			{
				try
				{
					//get temporary directory
					File directory=IOHelper.getFax4jInternalTemporaryDirectory();

					//get dll path
					File dllFile=new File(directory,"fax4j.dll");

					//get path
					String path=dllFile.getPath();

					//load native library
					System.load(path);
					logger.logDebug(new Object[]{"Loaded native library runtime path."},null);
					
					//set flag
					WindowsFaxClientSpiHelper.nativeLibraryLoaded=true;
				}
				catch(Throwable throwable)
				{
					logger.logError(new Object[]{"Error while loading native library from runtime path."},throwable);
				}

				if(!WindowsFaxClientSpiHelper.nativeLibraryLoaded)
				{
					try
					{
						//load native library
						System.loadLibrary("fax4j");
						logger.logDebug(new Object[]{"Loaded native library from native path."},null);
						
						//set flag
						WindowsFaxClientSpiHelper.nativeLibraryLoaded=true;
					}
					catch(Throwable throwable)
					{
						logger.logError(new Object[]{"Error while loading native library from native path."},throwable);
					}
				}
			}
		}
	}
	
	/**
	 * This function returns true if the native library was loaded.
	 * 
	 * @return	True if the native library was loaded
	 */
	public static boolean isNativeLibraryLoaded()
	{
		return WindowsFaxClientSpiHelper.nativeLibraryLoaded;
	}
	
	/**
	 * This function returns the server name from the SPI configuration.
	 * 
	 * @param 	faxClientSpi
	 * 			The fax client SPI
	 * @return	The server name
	 */
	public static String getServerNameFromConfiguration(FaxClientSpi faxClientSpi)
	{
		//get logger
		Logger logger=faxClientSpi.getLogger();
		
		//get fax server name
		String faxServerName=faxClientSpi.getConfigurationValue(FaxClientSpiConfigurationConstants.FAX_SERVER_NAME_PROPERTY_KEY);
		logger.logDebug(new Object[]{"Fax server name: ",faxServerName},null);

		return faxServerName;
	}

	/**
	 * This function returns the fax job ID (if valid).
	 * If fax job ID is not valid, an error will be thrown.
	 * 
	 * @param 	faxJob
	 * 			The fax job object
	 * @return	The fax job ID
	 */
	public static int getFaxJobID(FaxJob faxJob)
	{
		if(faxJob==null)
		{
			throw new FaxException("Fax job not provided.");
		}

		//get fax job ID
		String faxJobID=faxJob.getID();
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJobID);
		int faxJobIDInt=Integer.parseInt(faxJobID);

		return faxJobIDInt;
	}

	/**
	 * This function validates the provided fax job ID (not checking
	 * fax server, only format).
	 * In case of an invalid ID, an error will be thrown.
	 * 
	 * @param 	faxJob
	 * 			The fax job holding the ID to validate
	 */
	public static void validateFaxJobID(FaxJob faxJob)
	{
		WindowsFaxClientSpiHelper.getFaxJobID(faxJob);
	}

	/**
	 * This function validates the provided fax job ID (not checking
	 * fax server, only format).
	 * In case of an invalid ID, an error will be thrown.
	 * 
	 * @param 	faxJobID
	 * 			The fax job ID to validate
	 */
	public static void validateFaxJobID(String faxJobID)
	{
		if((faxJobID==null)||(faxJobID.length()==0))
		{
			throw new FaxException("Fax job ID not provided.");
		}
		int faxJobIDInt=Integer.parseInt(faxJobID);
		WindowsFaxClientSpiHelper.validateFaxJobID(faxJobIDInt);
	}

	/**
	 * This function validates the provided fax job ID (not checking
	 * fax server, only format).
	 * In case of an invalid ID, an error will be thrown.
	 * 
	 * @param 	faxJobID
	 * 			The fax job ID to validate
	 */
	public static void validateFaxJobID(int faxJobID)
	{
		if(faxJobID==0)
		{
			throw new FaxException("Invalid fax job ID provided: "+faxJobID);
		}
	}

	/**
	 * This function returns the fax job status based on the windows
	 * fax job status string value.
	 * 
	 * @param 	faxJobStatusStr
	 * 			The fax job status string value
	 * @return	The fax job status
	 */
	public static FaxJobStatus getFaxJobStatusFromWindowsFaxJobStatusString(String faxJobStatusStr)
	{
		FaxJobStatus faxJobStatus=FaxJobStatus.UNKNOWN;
		if((faxJobStatusStr!=null)&&(faxJobStatusStr.length()>0))
		{
			if((faxJobStatusStr.equalsIgnoreCase("JS_PENDING"))||(faxJobStatusStr.equalsIgnoreCase("JS_PAUSED"))||(faxJobStatusStr.equalsIgnoreCase("JS_RETRYING")))
			{
				faxJobStatus=FaxJobStatus.PENDING;
			}
			else if(faxJobStatusStr.equalsIgnoreCase("JS_INPROGRESS"))
			{
				faxJobStatus=FaxJobStatus.IN_PROGRESS;
			}
			else if((faxJobStatusStr.equalsIgnoreCase("JS_FAILED"))||(faxJobStatusStr.equalsIgnoreCase("JS_NOLINE"))||(faxJobStatusStr.equalsIgnoreCase("JS_RETRIES_EXCEEDED")))
			{
				faxJobStatus=FaxJobStatus.ERROR;
			}
		}
		
		return faxJobStatus;
	}

	/**
	 * This function returns the relevant part from the process output.
	 * 
	 * @param 	processOutput
	 * 			The process output
	 * @param 	prefix
	 * 			The prefix to look for in the output
	 * @return	The relevant output part
	 */
	public static String getOutputPart(ProcessOutput processOutput,String prefix)
	{
		//get output
		String output=processOutput.getOutputText();

		if(output!=null)
		{
			//set flag
			boolean validOutput=false;

			int index=output.indexOf(prefix);
			if(index!=-1)
			{
				//get index
				index=index+prefix.length();
	
				if(output.length()>index)
				{
					//get output
					output=output.substring(index);
					
					output=output.trim();
					index=output.indexOf("\n");
					if(index!=-1)
					{
						output=output.substring(0,index);
						output=output.trim();
					}

					if(output.length()>0)
					{
						//set flag
						validOutput=true;
					}
				}
			}
			
			if(!validOutput)
			{
				output=null;
			}
		}
		
		return output;
	}
}