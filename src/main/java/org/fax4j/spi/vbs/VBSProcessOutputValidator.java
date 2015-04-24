package org.fax4j.spi.vbs;

import org.fax4j.FaxException;
import org.fax4j.common.FaxActionType;
import org.fax4j.spi.FaxClientSpi;
import org.fax4j.spi.process.ExitCodeProcessOutputValidator;
import org.fax4j.util.ProcessExecutorHelper.ProcessOutput;

/**
 * This process output validator checks the fax4j vbscript output data for errors.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.0
 * @since	0.42.3
 */
public class VBSProcessOutputValidator extends ExitCodeProcessOutputValidator
{
	/**The fax operation done text*/
	private static final String OPERATION_OUTPUT_DONE="fax.operation.done=true";
	/**The active x not installed error message*/
	private static final String ACTIVE_X_NOT_INSTALLED="ActiveX component can't create object";

	/**
	 * This is the class constructor.
	 */
	public VBSProcessOutputValidator()
	{
		super();
	}
	
	/**
	 * This function returns the VBS error line for the exception message.
	 * 
	 * @param 	errorPut
	 * 			The error put
	 * @return	The message
	 */
	protected String getVBSFailedLineErrorMessage(String errorPut)
	{
		String message="";
		if(errorPut!=null)
		{
			String prefix=".vbs(";
			int start=errorPut.indexOf(prefix);
			if(start!=-1)
			{
				start=start+prefix.length();
				int end=errorPut.indexOf(", ",start-1);
				if(end!=-1)
				{
					String lineNumberStr=errorPut.substring(start,end);
					if(lineNumberStr.length()>0)
					{
						int lineNumber=-1;
						try
						{
							lineNumber=Integer.parseInt(lineNumberStr);
						}
						catch(NumberFormatException exception)
						{
							//ignore
						}
						
						if(lineNumber>=1)
						{
							message=" error found at line "+lineNumber+", ";
						}
					}
				}
			}
		}
		
		return message;
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
		String errorPut=processOutput.getErrorText();

		boolean throwError=false;
		if((output!=null)&&(output.length()>0))
		{
			if(output.indexOf(VBSProcessOutputValidator.OPERATION_OUTPUT_DONE)==-1)
			{
				throwError=true;
			}
		}
		else
		{
			throwError=true;
		}
		
		if(throwError)
		{
			String message=this.getVBSFailedLineErrorMessage(errorPut);
			if((errorPut!=null)&&(errorPut.indexOf(VBSProcessOutputValidator.ACTIVE_X_NOT_INSTALLED)!=-1))
			{
				throw new FaxException("Error while invoking VBS script (fax server ActiveX not installed on system),"+message+" script output:\n"+output+"\nScript error:\n"+errorPut);
			}
			
			throw new FaxException("Error while invoking VBS script,"+message+" script output:\n"+output+"\nScript error:\n"+errorPut);
		}
	}
}