package org.fax4j.spi.send2fax;

import org.fax4j.spi.send2fax.Send2FaxMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class Send2FaxMailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public Send2FaxMailFaxClientSpiTest()
	{
		super(Send2FaxMailFaxClientSpi.class.getName(),FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString(),FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString());
	}
}