package org.fax4j.bridge;

import java.util.Properties;
import org.fax4j.FaxJob;

/**
 * This is an optional helper class to run the fax bridge flow from
 * external classes.
 * 
 * @param	<T>
 * 			The context type
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.41.4
 */
public class FaxBridgeFlowHelper<T>
{
	/**The fax bridge*/
	private final ContextFaxBridge<T> FAX_BRIDGE;
	
	/**
	 * This is the class constructor.
	 * 
	 * @param 	faxBridge
	 * 			The fax bridge
	 * @param	type
	 * 			The fax client type (may be null for default type)
	 * @param	configuration
	 * 			The fax client configuration (may be null)
	 * @param 	flowOwner
	 * 			The flow owner (servlet, CLI main, ....) to be passed to the vendor policy
	 */
	public FaxBridgeFlowHelper(ContextFaxBridge<T> faxBridge,String type,Properties configuration,Object flowOwner)
	{
		super();
		
		//get fax bridge
		this.FAX_BRIDGE=faxBridge;
		
		//initialize
		this.FAX_BRIDGE.initialize(type,configuration,flowOwner);
	}
	
	/**
	 * This function returns the fax bridge.
	 * 
	 * @return	The fax bridge
	 */
	public ContextFaxBridge<T> getFaxBridge()
	{
		return this.FAX_BRIDGE;
	}
	
	/**
	 * This function invokes the submit fax job flow.<br>
	 * As part of the flow, if requested, the vendor policy would be invoked as well.
	 * 
	 * @param 	inputData
	 * 			The input data for the fax bridge
	 * @param 	contextData
	 * 			The conext data used by the vendor policy (may be same as the input data)
	 * @param	invokeVendorPolicy
	 * 			True to invoke the vendor policy
	 * @return	The flow response
	 */
	public FlowResponse submitFaxJob(T inputData,Object contextData,boolean invokeVendorPolicy)
	{
    	boolean continueFlow=true;
    	VendorPolicy vendorPolicy=null;
		if(invokeVendorPolicy)
		{
	    	//get vendor policy
	    	vendorPolicy=this.FAX_BRIDGE.getVendorPolicy();
	
	    	//invoke policy
	    	continueFlow=vendorPolicy.invokePolicyForRequest(contextData);
		}
    	
		FaxJob faxJob=null;
    	if(continueFlow)
    	{
    		//submit fax
    		faxJob=this.FAX_BRIDGE.submitFaxJob(inputData);

    		if(invokeVendorPolicy)
    		{
	        	//invoke policy
	        	continueFlow=vendorPolicy.invokePolicyForResponse(contextData,faxJob);
    		}
    	}

    	//create response
    	FlowResponse flowResponse=new FlowResponse(faxJob,continueFlow);
    	
    	return flowResponse;
	}

	/**
	 * This class holds the response of the fax bridge flow invocation.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.41.4
	 */
	public static class FlowResponse
	{
		/**The fax job*/
		private final FaxJob FAX_JOB;
		/**The continue flow flag*/
		private final boolean CONTINUE_FLOW;
		
		/**
		 * This is the class constructor.
		 * 
		 * @param	faxJob
		 * 			The fax job
		 * @param	continueFlow
		 * 			The continue flow flag
		 */
		public FlowResponse(FaxJob faxJob,boolean continueFlow)
		{
			super();
			
			//get info
			this.FAX_JOB=faxJob;
			this.CONTINUE_FLOW=continueFlow;
		}
		
		/**
		 * This function returns the fax job.
		 * 
		 * @return	The fax job
		 */
		public FaxJob getFaxJob()
		{
			return this.FAX_JOB;
		}
		
		/**
		 * This function returns the continue flow flag.
		 * 
		 * @return	The continue flow flag
		 */
		public boolean isContinueFlow()
		{
			return this.CONTINUE_FLOW;
		}
	}
}