package org.fax4j.bridge;

import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.Provider;

/**
 * The vendor policy enables vendors to hook into the fax submitting flow, 
 * enabling to restrict access, bill customers and so on, before/after the fax is submitted.<br>
 * <b>This class implementation must be thread safe.</b>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.01
 */
public abstract class AbstractVendorPolicy implements VendorPolicy
{
	/**The initialized flag*/
	private boolean initialized;
	/**The flow owner*/
	private Object vendorPolicyFlowOwner;
	/**The provider*/
	private final Provider PROVIDER;

	/**
	 * This is the class constructor.
	 * 
	 * @param	provider
	 * 			The provider
	 */
	protected AbstractVendorPolicy(Provider provider)
	{
		super();
		
		if(provider==null)
		{
			throw new FaxException("Provider object is null.");
		}
		
		//get provider
		this.PROVIDER=provider;
		
		//set flag
		this.initialized=false;
	}

	/**
	 * This function returns the vendor policy provider.
	 * 
	 * @return	The vendor policy provider
	 */
	public final Provider getProvider()
	{
		return this.PROVIDER;
	}
	
	/**
	 * This function returns the flow owner of this vendor policy.
	 * 
	 * @return	The flow owner of this vendor policy
	 */
	protected Object getFlowOwner()
	{
		return this.vendorPolicyFlowOwner;
	}

	/**
	 * This function initializes the vendor policy.
	 * 
	 * @param 	flowOwner
	 * 			The flow owner (the servlet, CLI main, ....)
	 */
	public final synchronized void initialize(Object flowOwner)
	{
		if(this.initialized)
		{
			throw new FaxException("Vendor policy already initialized.");
		}
		
		if(flowOwner==null)
		{
			throw new FaxException("Flow owner not provided.");
		}

		//set flag
		this.initialized=true;

		//get flow owner
		this.vendorPolicyFlowOwner=flowOwner;
		
		//initialize
		this.initializeImpl();
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
	public boolean invokePolicyForRequest(Object requestDataHolder)
	{
		if(requestDataHolder==null)
		{
			throw new FaxException("Request data holder not provided.");
		}
		
		//invoke policy
		boolean continueFlow=this.invokePolicyForRequestImpl(requestDataHolder);
		
		return continueFlow;
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
	public boolean invokePolicyForResponse(Object requestDataHolder,FaxJob faxJob)
	{
		if(requestDataHolder==null)
		{
			throw new FaxException("Request data holder not provided.");
		}
		if(faxJob==null)
		{
			throw new FaxException("Fax job not provided.");
		}
		
		//invoke policy
		boolean continueFlow=this.invokePolicyForResponseImpl(requestDataHolder,faxJob);
		
		return continueFlow;
	}

	/**
	 * This function initializes the vendor policy.
	 */
	protected abstract void initializeImpl();

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
	protected abstract boolean invokePolicyForRequestImpl(Object requestDataHolder);

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
	protected abstract boolean invokePolicyForResponseImpl(Object requestDataHolder,FaxJob faxJob);
}