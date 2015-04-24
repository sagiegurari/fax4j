package org.fax4j.spi.nextivafax;

import org.fax4j.spi.nextivafax.NextivafaxMailFaxClientSpi.FaxClientSpiConfigurationConstants;
import org.fax4j.test.TestUtil.PredefinedMailFaxClientSpiTest;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class NextivafaxMailFaxClientSpiTest extends PredefinedMailFaxClientSpiTest
{
	/**
	 * This is the class constructor.
	 */
	public NextivafaxMailFaxClientSpiTest()
	{
		super(NextivafaxMailFaxClientSpi.class.getName(),FaxClientSpiConfigurationConstants.MAIL_ADDRESS_TEMPLATE_VALUE.toString(),FaxClientSpiConfigurationConstants.MAIL_SUBJECT_TEMPLATE_VALUE.toString());
	}
}