package org.fax4j.bridge;

import java.util.Properties;
import org.fax4j.FaxClient;
import org.fax4j.FaxJob;
import org.fax4j.common.ConfigurationHolder;
import org.fax4j.common.Logger;
import org.fax4j.common.ProviderImplementation;

/**
 * This interface defines only the common capabilities of the fax bridge.<br>
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
 * @version 1.03
 * @since	0.40.6
 */
public interface FaxBridge extends ProviderImplementation,ConfigurationHolder
{
	/**The vendor policy class name property key*/
	public static final String VENDOR_POLICY_CLASS_NAME="org.fax4j.bridge.vendor.policy.class.name";
	
	/**
	 * This function initializes the fax bridge.
	 * 
	 * @param	type
	 * 			The fax client type (may be null for default type)
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @param 	flowOwner
	 * 			The flow owner (servlet, CLI main, ....) to be passed to the vendor policy
	 */
	public void initialize(String type,Properties configuration,Object flowOwner);

	/**
	 * Returns the internal logger.
	 *  
	 * @return	The internal logger
	 */
	public Logger getLogger();

	/**
	 * Returns the internal fax client. 
	 * 
	 * @return	The fax client
	 */
	public FaxClient getFaxClient();

	/**
	 * This function returns the vendor policy.
	 * 
	 * @return	The vendor policy
	 */
	public VendorPolicy getVendorPolicy();

	/**
	 * This function creates a new fax job instance to be used
	 * by the caller to submit a new fax job and so on.
	 *  
	 * @return	The fax job instance
	 */
	public FaxJob createFaxJob();
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.<br>
	 * The file information in the provided fax job will be ignored by this method, 
	 * instead the file data will be taken from the FileInfo.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information (not including the file information)
	 * @param	fileInfo
	 * 			The file information of the requested fax
	 */
	public void submitFaxJob(FaxJob faxJob,FileInfo fileInfo);
}