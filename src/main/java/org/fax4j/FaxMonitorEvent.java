package org.fax4j;

import org.fax4j.common.Logger;

/**
 * This class holds the fax monitor event data.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.40.2
 */
public class FaxMonitorEvent extends AbstractFaxEvent
{
	/**The event ID*/
	private final FaxMonitorEventID ID;
	/**The fax job status*/
	private final FaxJobStatus FAX_JOB_STATUS;
	/**Default serialization UID*/
	private static final long serialVersionUID=1L;

	/**
	 * This is the fax monitor event ID enum.<br>
	 * This enum holds all the possible event ID.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.0
	 * @since	0.40.2
	 */
	public enum FaxMonitorEventID
	{
		/**The fax job status change event ID*/
		FAX_JOB_STATUS_CHANGE
	}

	/**
	 * This is the class constructor.
	 * 
	 * @param 	id
	 * 			The event ID
	 * @param 	faxJob
	 * 			The fax job
	 * @param	faxJobStatus
	 * 			The fax job status
	 */
	public FaxMonitorEvent(FaxMonitorEventID id,FaxJob faxJob,FaxJobStatus faxJobStatus)
	{
		super(faxJob);
		
		//validate values
		if(id==null)
		{
			throw new FaxException("Fax monitor event ID not provided.");
		}
		
		//get values
		this.ID=id;
		this.FAX_JOB_STATUS=faxJobStatus;
	}
	
	/**
	 * This function returns the fax monitor event ID.
	 * 
	 * @return	The fax monitor event ID
	 */
	public final FaxMonitorEventID getID()
	{
		return this.ID;
	}

	/**
	 * This function returns the fax job status.
	 * 
	 * @return	The fax job status
	 */
	public final FaxJobStatus getFaxJobStatus()
	{
		return this.FAX_JOB_STATUS;
	}

    /**
     * Returns a String representation of this fax monitor event object.
     *
     * @return  A string representation of this fax monitor event object.
     */
	@Override
    public final String toString()
	{
		//init buffer
		StringBuilder buffer=new StringBuilder(500);
		buffer.append("Fax Monitor Event:");
		
		//append values
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("ID: ");
		buffer.append(this.getID());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append(this.getFaxJob());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append(this.getFaxJobStatus());
		
		//get text
		String text=buffer.toString();
		
		return text;
    }
}