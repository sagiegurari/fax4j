package org.fax4j.bridge;

import org.fax4j.FaxJob;
import org.fax4j.common.Fax4JProvider;

/**
 * The vendor policy enables vendors to hook into the fax submitting flow, 
 * enabling to restrict access, bill customers and so on, before/after the fax is submitted.<br>
 * This implementation simply lets the fax flow continue without doing any policy handling.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.01
 */
public class EmptyVendorPolicy extends AbstractVendorPolicy
{
	/**
	 * This is the class constructor.
	 */
	public EmptyVendorPolicy()
	{
		super(Fax4JProvider.FAX4J_PROVIDER);
	}

	/**
	 * This function initializes the vendor policy.
	 */
	@Override
	protected void initializeImpl()
	{
		//empty
	}

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
	@Override
	protected boolean invokePolicyForRequestImpl(Object requestDataHolder)
	{
		return true;
	}

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
	@Override
	protected boolean invokePolicyForResponseImpl(Object requestDataHolder,FaxJob faxJob)
	{
		return true;
	}
}