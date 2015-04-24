package org.fax4j.bridge;

import java.io.File;
import java.io.IOException;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.common.Logger;
import org.fax4j.util.IOHelper;
import org.fax4j.util.ReflectionHelper;

/**
 * This class basic implementation of the fax bridge.<br>
 * Specific implementations may provide additional, more transport based capabilities.
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.bridge.vendor.policy.class.name</td>
 * 		<td>The vendor policy class name.</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>org.fax4j.bridge.EmptyVendorPolicy</td>
 * 		<td>false</td>
 * 	</tr>
 * </table>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.40.6
 */
public class FaxBridgeImpl extends AbstractFax4JBridge
{
	/**
	 * This is the class constructor.
	 */
	public FaxBridgeImpl()
	{
		super();
	}

	/**
	 * This function returns the vendor policy.<br>
	 * This function will not initialize the vendor policy since the flow owner object
	 * needs to be provided.
	 * 
	 * @return	The vendor policy
	 */
	@Override
	protected VendorPolicy createVendorPolicy()
	{
		//get class name
		String className=this.getConfigurationValue(FaxBridge.VENDOR_POLICY_CLASS_NAME);

		if(className==null)
		{
			className=EmptyVendorPolicy.class.getName();
		}

		//create new instance
		VendorPolicy vendorPolicy=(VendorPolicy)ReflectionHelper.createInstance(className);
		
		//log info
		Logger logger=this.getLogger();
		logger.logInfo(new Object[]{"Created new vendor policy of type: ",className},null);
		
		return vendorPolicy;
	}
	
	/**
	 * This function stores the file in the local machine and updates
	 * the fax job with the new file data.
	 * 
	 * @param 	faxJob
	 * 			The fax job object to be updated
	 * @param	fileInfo
	 * 			The file information of the requested fax
	 */
	@Override
	protected void updateFaxJobWithFileInfo(FaxJob faxJob,FileInfo fileInfo)
	{
		//get file
		File file=fileInfo.getFile();
		
		if(file==null)
		{
			//get file name
			String fileName=fileInfo.getName();
	
			//get file content
			byte[] content=fileInfo.getContent();

			try
			{
				//create temporary file
				file=File.createTempFile("fax_",fileName);
				
				//write content to file
				IOHelper.writeFile(content,file);
			}
			catch(IOException exception)
			{
				throw new FaxException("Unable to write file content to temporary file.",exception);
			}
		}
		
		//update fax job
		faxJob.setFile(file);
	}
}