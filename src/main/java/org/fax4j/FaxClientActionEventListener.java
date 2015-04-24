package org.fax4j;

import java.util.EventListener;

/**
 * This interface defines the fax client action event listener.<br>
 * The listener picks up events based on actions invoked on the fax client and not
 * based on the status changes of the actual fax job.<br>
 * For example is a fax job is submitted via fax client, than the faxJobSubmitted event
 * method will be invoked.<br>
 * However the fax job itself might still be pending transmission by the actual
 * fax modem/server.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.20b
 */
public interface FaxClientActionEventListener extends EventListener
{
	/**
	 * This function is invoked after a new fax job is created.
	 * 
	 * @param 	event
	 * 			The fax event
	 */
	public void faxJobCreated(FaxClientActionEvent event);

	/**
	 * This function is invoked after a fax job is submitted.
	 * 
	 * @param 	event
	 * 			The fax event
	 */
	public void faxJobSubmitted(FaxClientActionEvent event);

	/**
	 * This function is invoked after a fax job is suspended.
	 * 
	 * @param 	event
	 * 			The fax event
	 */
	public void faxJobSuspended(FaxClientActionEvent event);

	/**
	 * This function is invoked after a fax job is resumed.
	 * 
	 * @param 	event
	 * 			The fax event
	 */
	public void faxJobResumed(FaxClientActionEvent event);

	/**
	 * This function is invoked after a fax job is cancelled.
	 * 
	 * @param 	event
	 * 			The fax event
	 */
	public void faxJobCancelled(FaxClientActionEvent event);
}