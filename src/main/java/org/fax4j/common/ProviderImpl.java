package org.fax4j.common;

import org.fax4j.FaxException;
import org.fax4j.Provider;

/**
 * This class implements the provider interface.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.03
 * @since	0.17
 */
public class ProviderImpl implements Provider
{
	/**The provider name*/
	private final String NAME;
	/**The provider description*/
	private final String DESCRIPTION;
	
	/**
	 * This is the class constructor.
	 * 
	 * @param 	name
	 * 			The provider name
	 * @param 	description
	 * 			The provider description
	 */
	public ProviderImpl(String name,String description)
	{
		super();
		
		//validate values
		if(name==null)
		{
			throw new FaxException("Provider name not provided.");
		}
		
		//update values
		String updatedDescription=description;
		if(updatedDescription==null)
		{
			updatedDescription="";
		}
		
		//get values
		this.NAME=name;
		this.DESCRIPTION=updatedDescription;
	}

	/**
	 * This function returns the provider name, 
	 * for example mycompany.
	 * 
	 * @return	The provider name
	 */
	public final String getName()
	{
		return this.NAME;
	}

	/**
	 * This function returns the provider description.
	 * 
	 * @return	The provider description
	 */
	public final String getDescription()
	{
		return this.DESCRIPTION;
	}
	
	/**
	 * This function string value of the provider used
	 * for printing purposes.
	 * 
	 * @return	The string value
	 */
	@Override
	public String toString()
	{
		//init buffer
		StringBuilder buffer=new StringBuilder(250);
		
		//add values
		buffer.append("Provider Name: ");
		buffer.append(this.NAME);
		buffer.append(Logger.SYSTEM_EOL);
		buffer.append("Provider Description: ");
		buffer.append(this.DESCRIPTION);
		
		//get text
		String text=buffer.toString();
		
		return text;
	}
	
	/**
	 * This function returns true if the provided object
	 * is equal to this object.
	 * 
	 * @param 	object
	 * 			The object to compare to
	 * @return	The comparison result
	 */
	@Override
	public boolean equals(Object object)
	{
		boolean result=super.equals(object);
		
		if(!result)
		{
			if(object instanceof Provider)
			{
				//get provider
				Provider provider=(Provider)object;

				//validate values
				if(this.NAME.equals(provider.getName()))
				{
					if(this.DESCRIPTION.equals(provider.getDescription()))
					{
						result=true;
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * This function returns the hash code value of the object.
	 * 
	 * @return	The hash code value of the object
	 */
	@Override
	public int hashCode()
	{
		//generate hashcode
		int hashCodeValue=this.NAME.hashCode()+this.DESCRIPTION.hashCode();
		
		return hashCodeValue;
	}
}