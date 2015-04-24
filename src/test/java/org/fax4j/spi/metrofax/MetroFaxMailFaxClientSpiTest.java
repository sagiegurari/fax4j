package org.fax4j.spi.metrofax;

import org.fax4j.spi.metrofax.MetroFaxMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class MetroFaxMailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public MetroFaxMailFaxClientSpiTest()
	{
		super(MetroFaxMailFaxClientSpi.class.getName(),FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString(),FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString());
	}
}