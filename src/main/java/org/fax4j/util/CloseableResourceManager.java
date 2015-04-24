package org.fax4j.util;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;
import org.fax4j.FaxException;

/**
 * This class is used to hold/release closeable resources.<br>
 * All registered closeable resources will be closed when the JVM exists.
 * 
 * @author	Sagie Gur-Ari
 * @version	1.03
 * @since	0.1
 */
public final class CloseableResourceManager
{
	/**Indicates if to enable/disable the register/unregister actions*/
	private static boolean active=true;
	/**The closeable resources*/
	protected static final Set<Closeable> RESOURCES=new HashSet<Closeable>();
	
	static
	{
		//add shutdown hook
		Runtime.getRuntime().addShutdownHook(new CloseableResourceShutdownHook());
	}
	
	/**
	 * This is the default constructor.
	 */
	private CloseableResourceManager()
	{
		super();
	}
	
	/**
	 * Registers the provided closeable.<br>
	 * Will throw an error in case the manager is no longer active (due
	 * to JVM shutdown).
	 *  
	 * @param 	closeable
	 * 			The closeable to register
	 */
	public static void registerCloseable(Closeable closeable)
	{
		CloseableResourceManager.updateCloseable(true,closeable);
	}
	
	/**
	 * Unregisters the provided closeable.<br>
	 * Will throw an error in case the manager is no longer active (due
	 * to JVM shutdown).
	 *  
	 * @param 	closeable
	 * 			The closeable to unregister
	 */
	public static void unregisterCloseable(Closeable closeable)
	{
		CloseableResourceManager.updateCloseable(false,closeable);
	}
	
	/**
	 * Registers/Unregisters the provided closeable.<br>
	 * Will throw an error in case the manager is no longer active (due
	 * to JVM shutdown).
	 *  
	 * @param	register
	 * 			True to register, false to unregister
	 * @param 	closeable
	 * 			The closeable to register/unregister
	 */
	private static void updateCloseable(boolean register,Closeable closeable)
	{
		boolean updated=false;
		synchronized(CloseableResourceManager.class)
		{
			if(register)
			{
				if(CloseableResourceManager.active)
				{
					//update resources data structure
					CloseableResourceManager.RESOURCES.add(closeable);
	
					//set flag
					updated=true;
				}
			}
			else
			{
				//update resources data structure
				CloseableResourceManager.RESOURCES.remove(closeable);

				//set flag
				updated=true;
			}
		}
		
		if(!updated)
		{
			throw new FaxException("Can not perform action, manager is being released.");
		}
	}
	
	/**
	 * Blocks any call to register/unregister new resources and releases all resources
	 * currently registered.
	 */
	protected static void blockAndReleaseAllCloseableResources()
	{
		synchronized(CloseableResourceManager.class)
		{
			//set flag
			CloseableResourceManager.active=false;
		}
		
		Closeable[] closeables=null;
		synchronized(CloseableResourceManager.RESOURCES)
		{
			//get resources
			closeables=CloseableResourceManager.RESOURCES.toArray(new Closeable[CloseableResourceManager.RESOURCES.size()]);
		}
		
		//get amount
		int amount=closeables.length;
		
		Closeable closeable=null;
		for(int index=0;index<amount;index++)
		{
			//get next element
			closeable=closeables[index];
			
			//close resource
			IOHelper.closeResource(closeable);
		}
	}
	
	/**
	 * This shutdown hook releases all the closeable resources when
	 * the JVM exists. 
	 * 
	 * @author 	Sagie Gur-Ari
	 * @version 1.01
	 * @since	0.1
	 */
	private static class CloseableResourceShutdownHook extends Thread
	{
		/**
		 * This is the default constructor.
		 */
		public CloseableResourceShutdownHook()
		{
			super();
		}
		
		/**
		 * Invoked by the JVM and will release all resources.
		 */
		@Override
		public void run()
		{
			CloseableResourceManager.blockAndReleaseAllCloseableResources();
		}
	}
}