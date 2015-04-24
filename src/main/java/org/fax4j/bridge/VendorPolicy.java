package org.fax4j.bridge;

import org.fax4j.FaxJob;
import org.fax4j.Provider;

/**
 * The vendor policy enables vendors to hook into the fax submitting flow, 
 * enabling to restrict access, bill customers and so on, before/after the fax is submitted.<br>
 * <b>Implementing classes must have a default empty constructor and the implementation must be thread safe.</b>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.01
 */
public interface VendorPolicy
{
	/**
	 * This function returns the vendor policy provider.
	 * 
	 * @return	The vendor policy provider
	 */
	public Provider getProvider();

	/**
	 * This function initializes the vendor policy.
	 * 
	 * @param 	flowOwner
	 * 			The flow owner (the servlet, CLI main, ....)
	 */
	public void initialize(Object flowOwner);

	/**
	 * This function invokes the vendor policy.<br>
	 * The policy may charge a customer for the service, or validate the user
	 * has permissions to invoke the action and so on.<br>
	 * In case the policy takes over the flow and the fax bridge should not
	 * be invoked, this method should return false.
	 * 
	 * @param 	requestDataHolder
	 * 			The request data holder
	 * @return	True if to continue the flow, else false
	 */
	public boolean invokePolicyForRequest(Object requestDataHolder);

	/**
	 * This function invokes the vendor policy.<br>
	 * The policy may charge a customer for the service, or validate the user
	 * has permissions to invoke the action and so on.<br>
	 * In case the policy takes over the flow and the fax bridge should not
	 * be invoked, this method should return false.
	 * 
	 * @param 	requestDataHolder
	 * 			The request data holder
	 * @param	faxJob
	 * 			The submitted fax job
	 * @return	True if to continue the flow, else false (in case the policy sends the response)
	 */
	public boolean invokePolicyForResponse(Object requestDataHolder,FaxJob faxJob);
}