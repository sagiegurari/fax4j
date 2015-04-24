package org.fax4j.bridge;

import org.fax4j.FaxJob;
import org.fax4j.common.Initializable;

/**
 * This interface defines the fax bridge request parser used to extract the fax job
 * and file info from the input data.
 * 
 * @param	<T>
 * 			The request type
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.41.3
 */
public interface RequestParser<T> extends Initializable
{
	/**
	 * This function returns the file info from the input data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @return	The file info
	 */
	public FileInfo getFileInfoFromInputData(T inputData);

	/**
	 * This function update the fax job from the input data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @param	faxJob
	 * 			The fax job to update
	 */
	public void updateFaxJobFromInputData(T inputData,FaxJob faxJob);
}