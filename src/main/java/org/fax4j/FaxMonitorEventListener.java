package org.fax4j;

import java.util.EventListener;

/**
 * This interface defines the fax monitor event listener.<br>
 * The listener picks up remote events based on changes done on the remote
 * fax modem.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.40.2
 */
public interface FaxMonitorEventListener extends EventListener
{
	/**
	 * This function is invoked when the fax job status is changed.
	 * 
	 * @param 	event
	 * 			The fax event
	 */
	public void faxJobStatusChanged(FaxMonitorEvent event);
}