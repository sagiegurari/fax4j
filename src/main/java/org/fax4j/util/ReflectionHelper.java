package org.fax4j.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.fax4j.FaxException;

/**
 * Holds general functions for working with reflection and dynamic invocation
 * of code/classes. 
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.40.6
 */
public final class ReflectionHelper
{
	/**
	 * This is the default constructor.
	 */
	private ReflectionHelper()
	{
		super();
	}

	/**
	 * This function returns the thread context class loader.
	 * 
	 * @return	The thread context class loader
	 */
	public static ClassLoader getThreadContextClassLoader()
	{
		Thread thread=Thread.currentThread();
		ClassLoader classLoader=thread.getContextClassLoader();
		
		return classLoader;
	}
	
	/**
	 * This function returns the class based on the class name.
	 * 
	 * @param 	className
	 * 			The class name of the requested type
	 * @return	The type
	 */
	public static Class<?> getType(String className)
	{
		ClassLoader classLoader=ReflectionHelper.getThreadContextClassLoader();
		Class<?> type=null;
		try
		{
			//load class definition
			type=classLoader.loadClass(className);
		}
		catch(ClassNotFoundException exception)
		{
			throw new FaxException("Unable to load class: "+className,exception);
		}
		
		return type;
	}
	
	/**
	 * This function creates a new instance of the requested type.
	 * 
	 * @param 	type
	 * 			The class type
	 * @return	The instance
	 */
	public static Object createInstance(Class<?> type)
	{
		Object instance=null;
		try
		{
			//create instance
			instance=type.newInstance();
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to create new instance of type: "+type,exception);
		}
		
		return instance;
	}

	/**
	 * This function creates a new instance of the requested type.
	 * 
	 * @param 	className
	 * 			The class name
	 * @return	The instance
	 */
	public static Object createInstance(String className)
	{
		//get type
		Class<?> type=ReflectionHelper.getType(className);

		//create instance
		Object instance=ReflectionHelper.createInstance(type);
		
		return instance;
	}
	
	/**
	 * This function invokes the requested method.
	 * 
	 * @param 	type
	 * 			The class type
	 * @param 	instance
	 * 			The instance
	 * @param 	methodName
	 * 			The method name to invoke
	 * @param 	inputTypes
	 * 			An array of input types
	 * @param 	input
	 * 			The method input
	 * @return	The method output
	 */
	public static Object invokeMethod(Class<?> type,Object instance,String methodName,Class<?>[] inputTypes,Object[] input)
	{
		Method method=null;
		try
		{
			//get method
			method=type.getDeclaredMethod(methodName,inputTypes);
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to extract method: "+methodName+" from type: "+type,exception);
		}

		//set accessible
		method.setAccessible(true);

		Object output=null;
		try
		{
			//invoke method
			output=method.invoke(instance,input);
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to invoke method: "+methodName+" of type: "+type,exception);
		}
		
		return output;
	}
	
	/**
	 * This function returns the field wrapper for the requested field
	 * 
	 * @param 	type
	 * 			The class type
	 * @param	fieldName
	 * 			The field name
	 * @return	The field
	 */
	public static Field getField(Class<?> type,String fieldName)
	{
		Field field=null;
		try
		{
			//get field
			field=type.getDeclaredField(fieldName);
		}
		catch(Exception exception)
		{
			throw new FaxException("Unable to extract field: "+fieldName+" from type: "+type,exception);
		}

		//set accessible
		field.setAccessible(true);

		return field;
	}
}