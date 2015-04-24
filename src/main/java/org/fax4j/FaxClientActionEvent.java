package org.fax4j;

import org.fax4j.common.Logger;

/**
 * This class holds the fax client action event data.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.09
 * @since	0.20b
 */
public final class FaxClientActionEvent extends AbstractFaxEvent
{
	/**The event ID*/
	private final FaxClientActionEventID ID;
	/**Default serialization UID*/
	private static final long serialVersionUID=1L;

	/**
	 * This is the fax client action event ID enum.<br>
	 * This enum holds all the possible event ID.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.20b
	 */
	public enum FaxClientActionEventID
	{
		/**The create fax job event ID*/
		CREATE_FAX_JOB,
		/**The submit fax job event ID*/
		SUBMIT_FAX_JOB,
		/**The suspend fax job event ID*/
		SUSPEND_FAX_JOB,
		/**The resume fax job event ID*/
		RESUME_FAX_JOB,
		/**The cancel fax job event ID*/
		CANCEL_FAX_JOB
	}

	/**
	 * This is the class constructor.
	 * 
	 * @param 	id
	 * 			The event ID
	 * @param 	faxJob
	 * 			The fax job
	 */
	public FaxClientActionEvent(FaxClientActionEventID id,FaxJob faxJob)
	{
		super(faxJob);
		
		//validate values
		if(id==null)
		{
			throw new FaxException("Fax client action event ID not provided.");
		}
		
		//get values
		this.ID=id;
	}
	
	/**
	 * This function returns the fax client action event ID.
	 * 
	 * @return	The fax client action event ID
	 */
	public final FaxClientActionEventID getID()
	{
		return this.ID;
	}

    /**
     * Returns a String representation of this fax client action event object.
     *
     * @return  A string representation of this fax client action event object.
     */
	@Override
    public final String toString()
	{
		//init buffer
		StringBuilder buffer=new StringBuilder(500);
		buffer.append("Fax Client Action Event:");
		
		//append values
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("ID: ");
		buffer.append(this.getID());
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append(this.getFaxJob());
		
		//get text
		String text=buffer.toString();
		
		return text;
    }
}