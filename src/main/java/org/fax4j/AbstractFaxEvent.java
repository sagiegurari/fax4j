package org.fax4j;

import java.util.EventObject;

/**
 * Base class for all fax4j events.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.40.5
 */
public abstract class AbstractFaxEvent extends EventObject
{
	/**The fax job*/
	private final transient FaxJob FAX_JOB;
	/**Default serialization UID*/
	private static final long serialVersionUID=1L;

	/**
	 * This is the class constructor.
	 * 
	 * @param 	faxJob
	 * 			The fax job
	 */
	public AbstractFaxEvent(FaxJob faxJob)
	{
		super(faxJob);
		
		//get fax job
		this.FAX_JOB=faxJob;
	}

	/**
	 * This function returns the fax job.
	 * 
	 * @return	The fax job
	 */
	public final FaxJob getFaxJob()
	{
		return this.FAX_JOB;
	}
}