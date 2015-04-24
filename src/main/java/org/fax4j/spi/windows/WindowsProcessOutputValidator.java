package org.fax4j.spi.windows;

import org.fax4j.FaxException;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.process.ExitCodeProcessOutputValidator;
import org.fax4j.spi.windows.WindowsProcessFaxClientSpi.Fax4jExeConstants;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This process output validator checks the fax4j.exe output data for errors.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.02
 * @since	0.26
 */
public class WindowsProcessOutputValidator extends ExitCodeProcessOutputValidator
{
	/**
	 * This is the class constructor.
	 */
	public WindowsProcessOutputValidator()
	{
		super();
	}

	/**
	 * This function validates the process output for errors.<br>
	 * If not valid, an exception should be thrown.
	 * 
	 * @param	faxClientSpi
	 * 			The fax client SPI
	 * @param	processOutput
	 * 			The process output to validate
	 * @param	faxActionType
	 * 			The fax action type
	 */
	@Override
	public void validateProcessOutput(FaxClientSpi faxClientSpi,ProcessOutput processOutput,FaxActionType faxActionType)
	{
		//call exit code validation
		super.validateProcessOutput(faxClientSpi,processOutput,faxActionType);

		//get output
		String output=processOutput.getOutputText();

		if((output!=null)&&(output.length()>0))
		{
			//if no valid done output was found
			if(output.indexOf(Fax4jExeConstants.OPERATION_OUTPUT_DONE.toString())==-1)
			{
				//get error message
				String prefix=Fax4jExeConstants.FAX_ERROR_OUTPUT_PREFIX.toString();
				int index=output.indexOf(prefix);
				int prefixLenght=prefix.length();
				String errorMessage=null;
				if(index!=-1)
				{
					//get index
					index=index+prefixLenght;

					if(output.length()>index)
					{
						//get output
						errorMessage=output.substring(index);
						
						errorMessage=errorMessage.trim();
						index=errorMessage.indexOf("\n");
						if(index!=-1)
						{
							errorMessage=errorMessage.substring(0,index);
							errorMessage=errorMessage.trim();
						}
						
						//ignore empty message
						if(errorMessage.length()==0)
						{
							errorMessage=null;
						}
					}
				}
				
				if(errorMessage==null)
				{
					throw new FaxException("Error while invoking fax4j.exe, no error message detected.\nNative Output:\n"+output);
				}
				throw new FaxException("Error while invoking fax4j.exe, error: "+errorMessage+"\nNative Output:\n"+output);
			}
		}
		else
		{
			throw new FaxException("Error while invoking fax4j.exe, no output detected.");
		}
	}
}