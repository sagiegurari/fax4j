package org.fax4j.spi.java4less;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import org.fax4j.FaxException;
import org.fax4j.FaxJob;
import org.fax4j.spi.AbstractFax4JClientSpi;
import org.fax4j.util.IOHelper;
import org.fax4j.util.ReflectionHelper;

/**
 * This class implements the fax client service provider interface.<br>
 * This implementation is based on the java4less commercial product.<br>
 * This implementation is not meant to promote the java4less commercial product but to serve
 * as a service provider interface for those using this product.<br>
 * <br>
 * The configuration of the fax4j framework is made up of 3 layers.<br>
 * The configuration is based on simple properties.<br>
 * Each layer overrides the lower layers by adding/changing the property values.<br>
 * The first layer is the internal fax4j.properties file located in the fax4j jar.<br>
 * This layer contains the preconfigured values for the fax4j framework and can be changed
 * by updating these properties in the higher layers.<br>
 * The second layer is the external fax4j.properties file that is located on the classpath.<br>
 * This file is optional and provides the ability to override the internal configuration for the
 * entire fax4j framework.<br>
 * The top most layer is the optional java.util.Properties object provided by the external classes
 * when creating a new fax client.<br>
 * These properties enable to override the configuration of the lower 2 layers.<br>
 * <br>
 * <b>SPI Status (Draft, Beta, Stable): </b>Draft<br>
 * <br>
 * Below table describes the configuration values relevant for this class.<br>
 * <b>Configuration:</b>
 * <table summary="" border="1">
 * 	<tr>
 * 		<td>Name</td>
 * 		<td>Description</td>
 * 		<td>Preconfigured Value</td>
 * 		<td>Default Value</td>
 * 		<td>Mandatory</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.rfax.port.name</td>
 * 		<td>See java4less website for more info.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * 	<tr>
 * 		<td>org.fax4j.spi.rfax.fax.class</td>
 * 		<td>See java4less website for more info.</td>
 * 		<td>none</td>
 * 		<td>none</td>
 * 		<td>true</td>
 * </tr>
 * </table>
 * <br>
 * <b>Limitations:</b><br>
 * <ul>
 * 	<li>See java4less web site.
 * 	<li>Currently this SPI only supports submitting new fax jobs.
 * </ul>
 * <br>
 * <b>Dependencies:</b><br>
 * <ul>
 * 	<li>See java4less web site.
 * </ul>
 * <br>
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.10
 * @since	0.1
 */
public class RFaxFaxClientSpi extends AbstractFax4JClientSpi
{
	/**The COMM port name (COM1, ...)*/
	private String portName;
	/**The fax class*/
	private int faxClass;

	/**
	 * This class holds the SPI configuration constants.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.1
	 */
	public enum FaxClientSpiConfigurationConstants
	{
		/**The COMM port name (COM1, ...) property key*/
		PORT_NAME_PROPERTY_KEY("org.fax4j.spi.rfax.port.name"),
		/**The fax class property key*/
		FAX_CLASS_PROPERTY_KEY("org.fax4j.spi.rfax.fax.class");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxClientSpiConfigurationConstants(String value)
		{
			this.value=value;
		}
		
		/**
		 * This function returns the string value.
		 * 
		 * @return	The string value
		 */
		@Override
		public final String toString()
		{
			return this.value;
		}
	}

	/**
	 * This class holds the fax job extended properties.
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.02
	 * @since	0.1
	 */
	public enum FaxJobExtendedPropertyConstants
	{
		/**
		 * The document data type (TEXT, PDF)  fax job input. 
		 * If this property is not defined in the fax job, the default is text.
		 */
		DATA_TYPE_FAX_JOB_PROPERTY_KEY("data.type"),
		/**The image width*/
		WIDTH_FAX_JOB_PROPERTY_KEY("width"),
		/**The image height*/
		HEIGHT_FAX_JOB_PROPERTY_KEY("height"),
		/**
		 * The image type (constant values from BufferedImage).
		 * If this property is not defined in the fax job, the default is BufferedImage.TYPE_INT_RGB
		 */
		IMAGE_TYPE_FAX_JOB_PROPERTY_KEY("image.type");

		/**The string value*/
		private String value;

		/**
		 * This is the class constructor.
		 * 
		 * @param	value
		 * 			The string value
		 */
		private FaxJobExtendedPropertyConstants(String value)
		{
			this.value=value;
		}
		
		/**
		 * This function returns the string value.
		 * 
		 * @return	The string value
		 */
		@Override
		public final String toString()
		{
			return this.value;
		}
	}

	/**
	 * This is the default constructor.
	 */
	public RFaxFaxClientSpi()
	{
		super();
	}
	
	/**
	 * This function initializes the fax client SPI.
	 */
	@Override
	protected void initializeImpl()
	{
		//get port name
		this.portName=this.getConfigurationValue(FaxClientSpiConfigurationConstants.PORT_NAME_PROPERTY_KEY);
		if(this.portName==null)
		{
			throw new FaxException("Port name not defined in fax4j.properties. Property: "+FaxClientSpiConfigurationConstants.PORT_NAME_PROPERTY_KEY);
		}

		//get fax class
		this.faxClass=Integer.parseInt(this.getConfigurationValue(FaxClientSpiConfigurationConstants.FAX_CLASS_PROPERTY_KEY));
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 */
	@Override
	protected void submitFaxJobImpl(FaxJob faxJob)
	{
		try
		{
			this.submitFaxJobViaFaxModem(faxJob);
		}
		catch(RuntimeException exception)
		{
			throw exception;
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to send fax via fax modem.",exception);
		}
	}
	
	/**
	 * This function will submit a new fax job.<br>
	 * The fax job ID may be populated by this method in the provided
	 * fax job object.
	 * 
	 * @param 	faxJob
	 * 			The fax job object containing the needed information
	 * @throws	Exception
	 * 			Any exception
	 */
	protected void submitFaxJobViaFaxModem(FaxJob faxJob) throws Exception
	{
		//get type
		String type=faxJob.getProperty(FaxJobExtendedPropertyConstants.DATA_TYPE_FAX_JOB_PROPERTY_KEY.toString(),"TEXT");

		//get common values
		int width=Integer.parseInt(faxJob.getProperty(FaxJobExtendedPropertyConstants.WIDTH_FAX_JOB_PROPERTY_KEY.toString(),null));
		int height=Integer.parseInt(faxJob.getProperty(FaxJobExtendedPropertyConstants.HEIGHT_FAX_JOB_PROPERTY_KEY.toString(),null));
		int imageType=Integer.parseInt(faxJob.getProperty(FaxJobExtendedPropertyConstants.IMAGE_TYPE_FAX_JOB_PROPERTY_KEY.toString(),String.valueOf(BufferedImage.TYPE_INT_RGB)));

		//create image
		BufferedImage image=new BufferedImage(width,height,imageType);
		
		//create fax producer
		Object faxProducer=null;
		if(type.equalsIgnoreCase("PDF"))
		{
			//get type
			Class<?> classDefinition=ReflectionHelper.getType("com.java4less.rfax.PDFFaxProducer");

			//create fax producer
			faxProducer=ReflectionHelper.createInstance(classDefinition);
			
			ReflectionHelper.invokeMethod(classDefinition,faxProducer,"setPDFFile",new Class[]{String.class},new Object[]{faxJob.getFilePath()});
			Field field=ReflectionHelper.getField(classDefinition,"pageImage");
			field.set(faxProducer,image);
		}
		else if(type.equalsIgnoreCase("TEXT"))
		{
			//get type
			Class<?> classDefinition=ReflectionHelper.getType("com.java4less.rfax.TextFaxProducer");

			//create fax producer
			faxProducer=ReflectionHelper.createInstance(classDefinition);
			
			String text=IOHelper.readTextFile(faxJob.getFile());
			Field field=ReflectionHelper.getField(classDefinition,"text");
			field.set(faxProducer,text);
			field=ReflectionHelper.getField(classDefinition,"pageImage");
			field.set(faxProducer,image);
			ReflectionHelper.invokeMethod(classDefinition,faxProducer,"prepare",null,null);
		}
		else
		{
			throw new FaxException("Unsupported type provided: "+type);
		}

		//init fax modem
		Class<?> faxModemClassDefinition=ReflectionHelper.getType("com.java4less.rfax.FaxModem");
		Object faxModem=ReflectionHelper.createInstance(faxModemClassDefinition);
		ReflectionHelper.invokeMethod(faxModemClassDefinition,faxModem,"setPortName",new Class[]{String.class},new Object[]{this.portName});
		Field field=ReflectionHelper.getField(faxModemClassDefinition,"faxClass");
		field.set(faxModem,Integer.valueOf(this.faxClass));

		//send fax
		Class<?> faxProducerClassDefinition=ReflectionHelper.getType("com.java4less.rfax.FaxProducer");
		ReflectionHelper.invokeMethod(faxModemClassDefinition,faxModem,"open",new Class[]{faxProducerClassDefinition},new Object[]{faxProducer});
		boolean faxSent=((Boolean)ReflectionHelper.invokeMethod(faxModemClassDefinition,faxModem,"sendFax",new Class[]{String.class},new Object[]{faxJob.getTargetAddress()})).booleanValue();
		
		//release fax modem
		ReflectionHelper.invokeMethod(faxModemClassDefinition,faxModem,"close",null,null);
		
		if(!faxSent)
		{
			field=ReflectionHelper.getField(faxModemClassDefinition,"lastError");
			String lastError=(String)field.get(faxModem);
			throw new FaxException("Error while sending fax. Error: "+lastError);
		}
	}
}