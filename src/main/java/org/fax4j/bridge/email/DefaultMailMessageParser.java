package org.fax4j.bridge.email;

import java.io.IOException;
import java.util.Map;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.bridge.AbstractRequestParser;
import org.fax4j.bridge.FileInfo;
import org.fax4j.util.IOHelper;

/**
 * This class provides default predefined implementation of the HTTP request parser
 * used to extract the fax job and file info from the HTTP request.<br>
 * This default parser expects the mail message as follows:<br>
 * <ul>
 * 	<li>Subject line: fax:[number] where number is the target address
 * 	<li>From email address would be used as sender email
 * 	<li>First attachment (or in case of none, the body text) would be used as the fax data
 * </ul>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.41.3
 */
public class DefaultMailMessageParser extends AbstractRequestParser<Message> implements MailMessageParser
{
	/**
	 * This is the class constructor.
	 */
	public DefaultMailMessageParser()
	{
		super();
	}

	/**
	 * This function initializes the component.
	 * 
	 * @param	configuration
	 * 			The component configuration
	 */
	@Override
	protected void initializeImpl(Map<String,String> configuration)
	{
		//empty
	}

	/**
	 * This function returns the file info from the request data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @return	The file info
	 */
	@Override
	protected FileInfo getFileInfoFromInputDataImpl(Message inputData)
	{
		FileInfo fileInfo=null;
		try
		{
			//get file info
			fileInfo=this.getFileInfo(inputData);
		}
		catch(MessagingException exception)
		{
			throw new FaxException("Unable to extract fax job file data from mail message.",exception);
		}
		catch(IOException exception)
		{
			throw new FaxException("Unable to extract fax job file data from mail message.",exception);
		}
		
		return fileInfo;
	}

	/**
	 * This function update the fax job from the request data.<br>
	 * This fax job will not have any file data.
	 * 
	 * @param	inputData
	 * 			The input data
	 * @param	faxJob
	 * 			The fax job to update
	 */
	@Override
	protected void updateFaxJobFromInputDataImpl(Message inputData,FaxJob faxJob)
	{
		String targetAddress=null;
		String senderEmail=null;
		try
		{
			//get target address
			targetAddress=this.getTargetAddress(inputData);
			
			//get sender email
			senderEmail=this.getSenderEmail(inputData);
		}
		catch(MessagingException exception)
		{
			throw new FaxException("Unable to extract fax job data from mail message.",exception);
		}
		
		//update fax job
		faxJob.setTargetAddress(targetAddress);
		faxJob.setSenderEmail(senderEmail);
	}
	
	/**
	 * Returns the target address from the provided mail object. 
	 * 
	 * @param 	mailMessage
	 * 			The mail message with the fax data
	 * @return	The target address
	 * @throws 	MessagingException
	 * 			Any exception while handling the mail message
	 */
	protected String getTargetAddress(Message mailMessage) throws MessagingException
	{
		//by default the target address is taken from the mail subject which
		//is expected to be in the format of: fax:<number>
		String subject=mailMessage.getSubject();
		String targetAddress=null;
		if((subject!=null)&&(subject.startsWith("fax:"))&&(subject.length()>4))
		{
			targetAddress=subject.substring(4);
		}

		return targetAddress;
	}
	
	/**
	 * Returns the sender email from the provided mail object. 
	 * 
	 * @param 	mailMessage
	 * 			The mail message with the fax data
	 * @return	The sender email
	 * @throws 	MessagingException
	 * 			Any exception while handling the mail message
	 */
	protected String getSenderEmail(Message mailMessage) throws MessagingException
	{
		Address[] addresses=mailMessage.getFrom();
		String senderEmail=null;
		if((addresses!=null)&&(addresses.length>0))
		{
			//get sender mail address (only first from is used)
			Address address=addresses[0];
			
			//get as string
			senderEmail=address.toString();
		}

		return senderEmail;
	}
	
	/**
	 * Returns the file info from the provided mail object.<br>
	 * This function does not handle multiple file attachments.
	 * 
	 * @param 	mailMessage
	 * 			The mail message with the fax data
	 * @return	The file info
	 * @throws 	MessagingException
	 * 			Any exception while handling the mail message
	 * @throws	IOException
	 * 			Any IO exception
	 */
	protected FileInfo getFileInfo(Message mailMessage) throws MessagingException,IOException
	{
		//get content
		Object messageContent=mailMessage.getContent();
		
		String charset=null;
		if(mailMessage instanceof MimeMessage)
		{
			//get mime message
			MimeMessage mimeMessage=(MimeMessage)mailMessage;
			
			//get charset 
			charset=mimeMessage.getEncoding();
		}
		
		String name="fax.txt";
		byte[] content=null;
		if(messageContent instanceof String)
		{
			//get content
			content=IOHelper.convertStringToBinary((String)messageContent,charset);
		}
		else if(messageContent instanceof Multipart)
		{
			//get multi part
			Multipart multiPart=(Multipart)messageContent;
			
			//get parts amount
			int amount=multiPart.getCount();
			
			Part part=null;
			DataHandler dataHandler=null;
			if(amount>0)
			{
				//handle only the first body part
				part=multiPart.getBodyPart(0);
				
				//get data handler
				dataHandler=part.getDataHandler();
				
				//get values
				name=dataHandler.getName();
				if(name==null)
				{
					name="fax.txt";
				}

				//get data
				content=IOHelper.readStream(part.getInputStream());
			}
		}
		
		//create file info
		FileInfo fileInfo=new FileInfo(name,content);

		return fileInfo;
	}
}