package org.fax4j.bridge;

import org.fax4j.FaxJob;

/**
 * This interface defines only the common capabilities of the context fax bridge.<br>
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
 * @param	<T>
 * 			The context type
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.41.4
 */
public interface ContextFaxBridge<T> extends FaxBridge
{
	/**
	 * This function will submit a new fax job.
	 * 
	 * @param	inputData
	 * 			The input data holding the fax job information
	 * @return	The submitted fax job
	 */
	public FaxJob submitFaxJob(T inputData);
}