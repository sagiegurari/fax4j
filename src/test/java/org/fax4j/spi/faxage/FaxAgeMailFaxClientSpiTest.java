package org.fax4j.spi.faxage;

import org.fax4j.spi.faxage.FaxAgeMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxAgeMailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public FaxAgeMailFaxClientSpiTest()
	{
		super(FaxAgeMailFaxClientSpi.class.getName(),FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString(),FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString());
	}
}