package com.java4less.rfax;

/**
 * Test Class 
 * 
 * @author 	Sagie Gur-Ari
 */
public class FaxModem
{
	/**Class member*/
	public int faxClass;
	
	/**
	 * Test method 
	 * 
	 * @param 	value
	 * 			Any value
	 */
	public void setPortName(String value)
	{
		if(value==null)
		{
			throw new IllegalArgumentException("port name is null.");
		}
	}

	/**
	 * Test method 
	 * 
	 * @param 	producer
	 * 			Any producer
	 */
	public void open(FaxProducer producer)
	{
		if(producer==null)
		{
			throw new IllegalArgumentException("fax producer is null.");
		}
	}

	/**
	 * Test method 
	 * 
	 * @param 	number
	 * 			Any number
	 * @return	true
	 */
	public boolean sendFax(String number)
	{
		if(number==null)
		{
			throw new IllegalArgumentException("fax number is null.");
		}
		
		return true;
	}

	/**
	 * Test method 
	 */
	public void close()
	{
		//empty
	}
}