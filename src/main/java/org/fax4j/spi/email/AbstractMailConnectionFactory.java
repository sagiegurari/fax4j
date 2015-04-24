package org.fax4j.spi.email;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.fax4j.spi.email.AbstractMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.util.AbstractFax4JConnectionFactory;

/**
 * Provides partial implementation of the mail connection factory.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.39a
 */
public abstract class AbstractMailConnectionFactory extends AbstractFax4JConnectionFactory<MailResourcesHolder> implements MailConnectionFactory
{
	/**The mail connection properties*/
	protected Properties mailConnectionProperties;
	/**The mail connection user name*/
	protected String userName;
	/**The mail connection password*/
	protected String password;
	/**The transport protocol*/
	protected String transportProtocol;
	/**The transport host*/
	protected String transportHost;
	/**The transport port*/
	protected int transportPort;

	/**
	 * This is the class constructor.
	 */
	public AbstractMailConnectionFactory()
	{
		super();
	}

	/**
	 * This function initializes the connection factory.
	 */
	@Override
	protected void initializeImpl()
	{
		//create properties from configuration (all javax mail properties will be defined in the fax4j properties)
		this.mailConnectionProperties=new Properties();
		Map<String,String> configuration=this.factoryConfigurationHolder.getConfiguration();
		Iterator<Entry<String,String>> iterator=configuration.entrySet().iterator();
		Entry<String,String> entry=null;
		String key=null;
		String value=null;
		while(iterator.hasNext())
		{
			//get next entry
			entry=iterator.next();

			//get next key
			key=entry.getKey();
			if((key!=null)&&(key.indexOf("org.fax4j.")==-1))	//filter out fax4j properties
			{
				//get next value
				value=entry.getValue();
				
				//put in properties
				this.mailConnectionProperties.setProperty(key,value);
			}
		}
		
		//get user/password values
		this.userName=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.USER_NAME_PROPERTY_KEY);
		this.password=this.factoryConfigurationHolder.getConfigurationValue(FaxClientSpiConfigurationConstants.PASSWORD_PROPERTY_KEY);

		//get transport protocol
		this.transportProtocol=this.factoryConfigurationHolder.getConfigurationValue("mail.transport.protocol");
		if(this.transportProtocol==null)
		{
			this.transportProtocol="smtp";
		}

		//get transport host
		this.transportHost=this.factoryConfigurationHolder.getConfigurationValue("mail.smtp.host");
		
		//get transport port
		String transportPortStr=this.factoryConfigurationHolder.getConfigurationValue("mail.smtp.port");
		this.transportPort=-1;
		if(transportPortStr!=null)
		{
			this.transportPort=Integer.parseInt(transportPortStr);
		}
	}
}