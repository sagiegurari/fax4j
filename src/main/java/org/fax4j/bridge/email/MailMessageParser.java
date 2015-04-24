package org.fax4j.bridge.email;

import javax.mail.Message;
import org.fax4j.bridge.RequestParser;

/**
 * This interface defines the mail message parser used to extract the fax job
 * and file info from the email data.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.41.3
 */
public interface MailMessageParser extends RequestParser<Message>
{
	//all capabilities defined in super interface
}