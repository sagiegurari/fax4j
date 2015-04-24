package org.fax4j.bridge;

import java.util.Map;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;

/**
 * This class provides partial implementation of the fax bridge request parser used to extract the fax job
 * and file info from the input data.
 * 
 * @param	<T>
 * 			The request type
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.41.3
 */
public abstract class AbstractRequestParser<T> implements RequestParser<T>
{
	/**The initialized flag*/
	private boolean initialized;

	/**
	 * This is the class constructor.
	 */
	public AbstractRequestParser()
	{
		super();
		
		//set flag
		this.initialized=false;
	}

	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	public final synchronized void initialize(Map<String,String> configuration)
	{
		if(this.initialized)
		{
			throw new FaxException("Request parser already initialized.");
		}
		
		//set flag
		this.initialized=true;
		
		//initialize
		this.initializeImpl(configuration);
	}

	/**
	 * This function returns the file info from the input data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @return	The file info
	 */
	public FileInfo getFileInfoFromInputData(T inputData)
	{
		if(!this.initialized)
		{
			throw new FaxException("Fax bridge not initialized.");
		}

		//get file info
		FileInfo fileInfo=this.getFileInfoFromInputDataImpl(inputData);
		
		return fileInfo;
	}

	/**
	 * This function update the fax job from the input data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @param	faxJob
	 * 			The fax job to update
	 */
	public void updateFaxJobFromInputData(T inputData,FaxJob faxJob)
	{
		if(!this.initialized)
		{
			throw new FaxException("Fax bridge not initialized.");
		}
		
		//get fax job
		this.updateFaxJobFromInputDataImpl(inputData,faxJob);
	}

	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	protected abstract void initializeImpl(Map<String,String> configuration);

	/**
	 * This function returns the file info from the request data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @return	The file info
	 */
	protected abstract FileInfo getFileInfoFromInputDataImpl(T inputData);
	
	/**
	 * This function update the fax job from the request data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @param	faxJob
	 * 			The fax job to update
	 */
	protected abstract void updateFaxJobFromInputDataImpl(T inputData,FaxJob faxJob);
}