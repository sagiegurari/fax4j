package org.fax4j.bridge;

import java.io.File;
import org.fax4j.FaxException;

/**
 * This class defines the file info object.<br>
 * Since bridges may get fax requests for file data which is part of the payload and not
 * for a local file, the bridge will have to have enough information of the file data and type to
 * pass to the fax client.<br>
 * The file info object serves that purpose of holding all of the file information needed to send the fax.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.40.6
 */
public class FileInfo
{
	/**The file (in case the file is found in the local file system)*/
	private final File FILE;
	/**The file name (no path)*/
	private final String NAME;
	/**The file content (in case the file is part of the request payload and not found in the local file system)*/
	private final byte[] CONTENT;

	/**
	 * This is the class constructor.
	 * 
	 * @param	file
	 * 			The file
	 * @param	name
	 * 			The file name (no path)
	 * @param	content
	 * 			The file content
	 */
	private FileInfo(File file,String name,byte[] content)
	{
		super();
		
		//validate input
		if(file==null)
		{
			if((name==null)||(name.length()==0))
			{
				throw new FaxException("File name not provided.");
			}
			if((content==null)||(content.length==0))
			{
				throw new FaxException("File content not provided.");
			}
			
			//get values
			this.FILE=null;
			this.NAME=name.trim();
			this.CONTENT=content.clone();
		}
		else
		{
			//get values
			this.FILE=file;
			this.NAME=this.FILE.getName();
			this.CONTENT=null;
		}

		//validate input
		if(this.NAME.length()==0)
		{
			throw new FaxException("File name not provided.");
		}
	}

	/**
	 * This is the class constructor.
	 * 
	 * @param	file
	 * 			The file
	 */
	public FileInfo(File file)
	{
		this(file,null,null);
	}

	/**
	 * This is the class constructor.
	 * 
	 * @param	name
	 * 			The file name (no path)
	 * @param	content
	 * 			The file content
	 */
	public FileInfo(String name,byte[] content)
	{
		this(null,name,content);
	}

	/**
	 * This function returns the file.
	 * 
	 * @return	The file
	 */
	public final File getFile()
	{
		return this.FILE;
	}

	/**
	 * This function returns the file name (no path).
	 * 
	 * @return	The file name
	 */
	public final String getName()
	{
		return this.NAME;
	}

	/**
	 * This function returns the file content.
	 * 
	 * @return	The file content
	 */
	public final byte[] getContent()
	{
		byte[] content=this.CONTENT.clone();
		return content;
	}
}
