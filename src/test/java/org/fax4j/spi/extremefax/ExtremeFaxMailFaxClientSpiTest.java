package org.fax4j.spi.extremefax;

import org.fax4j.spi.extremefax.ExtremeFaxMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class ExtremeFaxMailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public ExtremeFaxMailFaxClientSpiTest()
	{
		super(ExtremeFaxMailFaxClientSpi.class.getName(),FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString(),FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString());
	}
}