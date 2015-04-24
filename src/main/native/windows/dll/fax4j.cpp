/**
 * fax4j native layer.
 *
 * @author 	Sagie Gur-Ari
 */

#include "fax4j.h"

static bool globalDebugMode=false;
static char* lastErrorMessage=NULL;

/**
 * This function sets the debug mode of the native layer.
 * 
 * @param 	debug
 * 			True to print debug messages, otherwise false
 */
FAX4J_API void setDebugMode(bool debugMode)
{
	globalDebugMode=debugMode;
	if(globalDebugMode)
	{
		logDebug("Debug mode enabled.");
	}
}

/**
 * Logs the debug message.
 *
 * @param 	message
 * 			The debug message
 */
FAX4J_API void logDebug(char* message)
{
	LPCTSTR messageStr=convertCharArrayToLPCTSTR(message);
	logDebug(messageStr);
}

/**
 * Logs the debug message.
 *
 * @param 	message
 * 			The debug message
 */
FAX4J_API void logDebug(LPCTSTR message)
{
	if(globalDebugMode)
	{
		printf("[fax4j-native][DEBUG] %s\n",message);
		fflush(stdout);
	}
}

/**
 * Logs the debug message.
 *
 * @param 	errorCode
 * 			The error code
 * @param 	errorMessage
 * 			The error message
 */
FAX4J_API void logDebug(int errorCode,char* errorMessage)
{
	if(globalDebugMode)
	{
		printf("[fax4j-native][DEBUG] Error Code: %d Message: %s \n",errorCode,errorMessage);
		fflush(stdout);
	}
}

/**
 * Converts char array to LPCTSTR.
 * Since this library is compiled as non unicode this is possible.
 *
 * @param 	text
 * 			The text to convert
 * @return	The converted text
 */
FAX4J_API LPCTSTR convertCharArrayToLPCTSTR(char* text)
{
	LPCTSTR stringValue=NULL;
	if(text!=NULL)
	{
		stringValue=LPCTSTR(text);
	}

	return stringValue;
}

/**
 * This function will create and return a new 
 * initial response object.
 *
 * @return	The response object
 */
Response createResponse()
{
	Response response;
	response.errorMessage="General Error.";
	response.result=false;

	return response;
}

/**
 * Updates the fax job status
 *
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @param 	command
 * 			The new status - JC_DELETE, JC_PAUSE, JC_RESUME, JC_RESTART
 * @return	The response object
 */
Response setFaxJobStatus(LPCTSTR serverName,DWORD faxJobID,DWORD command)
{
	logDebug("Invoking setFaxJobStatus");

	//create initial response
	Response response=createResponse();

	//connect to fax server
	HANDLE faxServerHandle;
	logDebug("Connecting to fax server.");
	int result=FaxConnectFaxServer(serverName,&faxServerHandle);
	if(result==0)
	{
		response.errorCode=GetLastError();
		switch(response.errorCode)
		{
			case ERROR_INVALID_PARAMETER:
				response.errorMessage="Unable to connect to fax server - ERROR_INVALID_PARAMETER";
				break;
			case ERROR_NOT_ENOUGH_MEMORY:
				response.errorMessage="Unable to connect to fax server - ERROR_NOT_ENOUGH_MEMORY";
				break;
			case ERROR_ACCESS_DENIED:
				response.errorMessage="Unable to connect to fax server - ERROR_ACCESS_DENIED";
				break;
			default:
				response.errorMessage="Unable to connect to fax server.";
				break;
		}

		logDebug(response.errorCode,response.errorMessage);
		
		lastErrorMessage=response.errorMessage;

		return response;
	}

	//update fax job
	result=FaxSetJob(faxServerHandle,faxJobID,command,NULL);
	if(result==0)
	{
		response.errorCode=GetLastError();
		switch(response.errorCode)
		{
			case ERROR_ACCESS_DENIED:
				response.errorMessage="Unable to update fax job - ERROR_ACCESS_DENIED";
				break;
			case ERROR_INVALID_PARAMETER:
				response.errorMessage="Unable to update fax job - ERROR_INVALID_PARAMETER";
				break;
			default:
				response.errorMessage="Unable to update fax job.";
				break;
		}

		logDebug(response.errorCode,response.errorMessage);
		
		lastErrorMessage=response.errorMessage;

		return response;
	}
	else
	{
		logDebug("Fax job updated.");
		response.result=true;
	}

	//close connection to fax server
	logDebug("Closing connection to fax server.");
	FaxClose(faxServerHandle);
	
	lastErrorMessage=NULL;

	return response;
}

/**
 * Returns the fax job status
 *
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
Response getFaxJobStatus(LPCTSTR serverName,DWORD faxJobID)
{
	logDebug("Invoking getFaxJobStatus");

	//create initial response
	Response response=createResponse();

	//connect to fax server
	HANDLE faxServerHandle;
	logDebug("Connecting to fax server.");
	int result=FaxConnectFaxServer(serverName,&faxServerHandle);
	if(result==0)
	{
		response.errorCode=GetLastError();
		switch(response.errorCode)
		{
			case ERROR_INVALID_PARAMETER:
				response.errorMessage="Unable to connect to fax server - ERROR_INVALID_PARAMETER";
				break;
			case ERROR_NOT_ENOUGH_MEMORY:
				response.errorMessage="Unable to connect to fax server - ERROR_NOT_ENOUGH_MEMORY";
				break;
			case ERROR_ACCESS_DENIED:
				response.errorMessage="Unable to connect to fax server - ERROR_ACCESS_DENIED";
				break;
			default:
				response.errorMessage="Unable to connect to fax server.";
				break;
		}

		logDebug(response.errorCode,response.errorMessage);
		
		lastErrorMessage=response.errorMessage;

		return response;
	}

	//get fax job info
	PFAX_JOB_ENTRY faxJobInfo;
	result=FaxGetJob(faxServerHandle,faxJobID,&faxJobInfo);
	if(result==0)
	{
		response.errorCode=GetLastError();
		switch(response.errorCode)
		{
			case ERROR_ACCESS_DENIED:
				response.errorMessage="Unable to extract fax job info - ERROR_ACCESS_DENIED";
				break;
			case ERROR_INVALID_PARAMETER:
				response.errorMessage="Unable to extract fax job info - ERROR_INVALID_PARAMETER";
				break;
			case ERROR_NOT_ENOUGH_MEMORY:
				response.errorMessage="Unable to extract fax job info - ERROR_NOT_ENOUGH_MEMORY";
				break;
			default:
				response.errorMessage="Unable to extract fax job info.";
				break;
		}

		logDebug(response.errorCode,response.errorMessage);
		
		lastErrorMessage=response.errorMessage;

		return response;
	}
	else
	{
		logDebug("Fax job info extracted.");

		//get fax job status
		response.value="";
		DWORD status=faxJobInfo->QueueStatus;
		switch(status)
		{
			case JS_PENDING:
				response.value="JS_PENDING";
				break;
			case JS_INPROGRESS:
				response.value="JS_INPROGRESS";
				break;
			case JS_DELETING:
				response.value="JS_DELETING";
				break;
			case JS_FAILED:
				response.value="JS_FAILED";
				break;
			case JS_PAUSED:
				response.value="JS_PAUSED";
				break;
			case JS_NOLINE:
				response.value="JS_NOLINE";
				break;
			case JS_RETRYING:
				response.value="JS_RETRYING";
				break;
			case JS_RETRIES_EXCEEDED:
				response.value="JS_RETRIES_EXCEEDED";
				break;
		}

		logDebug("Fax job status extracted:");
		logDebug(response.value);

		response.result=true;
	}

	//close connection to fax server
	logDebug("Closing connection to fax server.");
	FaxClose(faxServerHandle);
	
	lastErrorMessage=NULL;

	return response;
}

/**
 * This function will submit a new fax job.
 * The fax job ID will be returned by this method.
 *
 * @param 	serverName
 * 			The fax server name
 * @param	targetAddress
 * 			The fax job target address
 * @param	targetName
 * 			The fax job target name
 * @param	senderName
 * 			The fax job sender name
 * @param	fileName
 * 			The file to fax
 * @return	The response object
 */
FAX4J_API Response submitFaxJobNative(LPCTSTR serverName,LPCTSTR targetAddress,LPCTSTR targetName,LPCTSTR senderName,LPCTSTR fileName)
{
	logDebug("Invoking submitFaxJobNative");

	//create initial response
	Response response=createResponse();

	//connect to fax server
	HANDLE faxServerHandle;
	logDebug("Connecting to fax server.");
	int result=FaxConnectFaxServer(serverName,&faxServerHandle);
	if(result==0)
	{
		response.errorCode=GetLastError();
		switch(response.errorCode)
		{
			case ERROR_INVALID_PARAMETER:
				response.errorMessage="Unable to connect to fax server - ERROR_INVALID_PARAMETER";
				break;
			case ERROR_NOT_ENOUGH_MEMORY:
				response.errorMessage="Unable to connect to fax server - ERROR_NOT_ENOUGH_MEMORY";
				break;
			case ERROR_ACCESS_DENIED:
				response.errorMessage="Unable to connect to fax server - ERROR_ACCESS_DENIED";
				break;
			default:
				response.errorMessage="Unable to connect to fax server.";
				break;
		}

		logDebug(response.errorCode,response.errorMessage);
		
		lastErrorMessage=response.errorMessage;

		return response;
	}

	PFAX_JOB_PARAM faxJobParameters;
	PFAX_COVERPAGE_INFO faxCoverPageInfo;
	logDebug("Initializing fax job parameters.");
	result=FaxCompleteJobParams(&faxJobParameters,&faxCoverPageInfo);
	if(result==0)
	{
		response.errorCode=GetLastError();
		response.errorMessage="Unable to initialize fax job parameters, closing fax server connection.";

		logDebug(response.errorMessage);
		
		//close connection to fax server
		FaxClose(faxServerHandle);
		
		lastErrorMessage=response.errorMessage;

		return response;
	}

	//release cover page info
	logDebug("Releasing cover page info.");
	faxCoverPageInfo->SizeOfStruct=sizeof(FAX_COVERPAGE_INFO);
	FaxFreeBuffer(faxCoverPageInfo);

	//set fax job parameters
	logDebug("Setting up fax job parameters.");
	faxJobParameters->SizeOfStruct=sizeof(FAX_JOB_PARAM);
	logDebug("Target Address:");
	logDebug(targetAddress);
	faxJobParameters->RecipientNumber=targetAddress;
	faxJobParameters->RecipientName=targetName;
	faxJobParameters->SenderName=senderName;
	faxJobParameters->ScheduleAction=JSA_NOW;
	faxJobParameters->DeliveryReportType=DRT_NONE;

	//send fax
	DWORD faxJobID;
	logDebug("Sending fax with file:");
	logDebug(fileName);
	result=FaxSendDocument(faxServerHandle,fileName,faxJobParameters,NULL,&faxJobID);
	if(result==0)
	{
		response.errorCode=GetLastError();
		switch(response.errorCode)
		{
			case ERROR_INVALID_PARAMETER:
				response.errorMessage="Unable to send fax - ERROR_INVALID_PARAMETER";
				break;
			case ERROR_INVALID_FUNCTION:
				response.errorMessage="Unable to send fax - ERROR_INVALID_FUNCTION";
				break;
			case ERROR_ACCESS_DENIED:
				response.errorMessage="Unable to send fax - ERROR_ACCESS_DENIED";
				break;
			case ERROR_FILE_NOT_FOUND:
				response.errorMessage="Unable to send fax - ERROR_FILE_NOT_FOUND";
				break;
			case ERROR_INVALID_DATA:
				response.errorMessage="Unable to send fax - ERROR_INVALID_DATA";
				break;
			case ERROR_NOT_SUPPORTED:
				response.errorMessage="Unable to send fax - ERROR_NOT_SUPPORTED";
				break;
			case ERROR_INVALID_HANDLE:
				response.errorMessage="Unable to send fax - ERROR_INVALID_HANDLE";
				break;
			default:
				response.errorMessage="Unable to send fax.";
				break;
		}
		
		logDebug(response.errorCode,response.errorMessage);
		
		lastErrorMessage=response.errorMessage;
	}
	else
	{
		logDebug("Fax sent.");
		response.faxJobID=faxJobID;
		response.result=true;
		
		lastErrorMessage=NULL;
	}

	//clear buffer
	logDebug("Releasing fax job parameters.");
	FaxFreeBuffer(faxJobParameters);

	//close connection to fax server
	logDebug("Closing connection to fax server.");
	FaxClose(faxServerHandle);

	return response;
}

/**
 * This function will suspend an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API Response suspendFaxJobNative(LPCTSTR serverName,DWORD faxJobID)
{
	logDebug("Invoking suspendFaxJobNative");

	//update fax job
	Response response=setFaxJobStatus(serverName,faxJobID,JC_PAUSE);

	return response;
}

/**
 * This function will resume an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API Response resumeFaxJobNative(LPCTSTR serverName,DWORD faxJobID)
{
	logDebug("Invoking resumeFaxJobNative");

	//update fax job
	Response response=setFaxJobStatus(serverName,faxJobID,JC_RESUME);

	return response;
}

/**
 * This function will cancel an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API Response cancelFaxJobNative(LPCTSTR serverName,DWORD faxJobID)
{
	logDebug("Invoking cancelFaxJobNative");

	//update fax job
	Response response=setFaxJobStatus(serverName,faxJobID,JC_DELETE);

	return response;
}

/**
 * This function returns the fax job status.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API Response getFaxJobStatusNative(LPCTSTR serverName,DWORD faxJobID)
{
	logDebug("Invoking cancelFaxJobNative");

	//update fax job
	Response response=getFaxJobStatus(serverName,faxJobID);

	return response;
}

/**
 * Returns the last error message.
 *
 * @return	The last error message
 */
FAX4J_API char* getLastErrorMessageDLL()
{
	return lastErrorMessage;
}

/**
 * This function will submit a new fax job.
 * The fax job ID will be returned by this method.
 *
 * @param 	serverName
 * 			The fax server name
 * @param	targetAddress
 * 			The fax job target address
 * @param	targetName
 * 			The fax job target name
 * @param	senderName
 * 			The fax job sender name
 * @param	fileName
 * 			The file to fax
 * @return	The fax job ID (null in case of an error)
 */
FAX4J_API DWORD submitFaxJobDLL(char* serverName,char* targetAddress,char* targetName,char* senderName,char* fileName)
{
	//get values
	LPCTSTR serverNameStr=NULL;
	LPCTSTR targetAddressStr=NULL;
	LPCTSTR targetNameStr=NULL;
	LPCTSTR senderNameStr=NULL;
	LPCTSTR fileNameStr=NULL;
	if(serverName!=NULL)
	{
		serverNameStr=convertCharArrayToLPCTSTR(serverName);
	}
	if(targetAddress!=NULL)
	{
		targetAddressStr=convertCharArrayToLPCTSTR(targetAddress);
	}
	if(targetName!=NULL)
	{
		targetNameStr=convertCharArrayToLPCTSTR(targetName);
	}
	if(senderName!=NULL)
	{
		senderNameStr=convertCharArrayToLPCTSTR(senderName);
	}
	if(fileName!=NULL)
	{
		fileNameStr=convertCharArrayToLPCTSTR(fileName);
	}

	//invoke fax action
	Response response=submitFaxJobNative(serverNameStr,targetAddressStr,targetNameStr,senderNameStr,fileNameStr);
	
	//get output
	DWORD faxJobID=0;
	if(response.result)
	{
		faxJobID=response.faxJobID;
	}

	return faxJobID;
}

/**
 * This function will suspend an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API void suspendFaxJobDLL(char* serverName,DWORD faxJobID)
{
	//get values
	LPCTSTR serverNameStr=NULL;
	if(serverName!=NULL)
	{
		serverNameStr=convertCharArrayToLPCTSTR(serverName);
	}
	
	//invoke fax action
	suspendFaxJobNative(serverNameStr,faxJobID);
}

/**
 * This function will resume an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API void resumeFaxJobDLL(char* serverName,DWORD faxJobID)
{
	//get values
	LPCTSTR serverNameStr=NULL;
	if(serverName!=NULL)
	{
		serverNameStr=convertCharArrayToLPCTSTR(serverName);
	}
	
	//invoke fax action
	resumeFaxJobNative(serverNameStr,faxJobID);
}

/**
 * This function will cancel an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API void cancelFaxJobDLL(char* serverName,DWORD faxJobID)
{
	//get values
	LPCTSTR serverNameStr=NULL;
	if(serverName!=NULL)
	{
		serverNameStr=convertCharArrayToLPCTSTR(serverName);
	}
	
	//invoke fax action
	cancelFaxJobNative(serverNameStr,faxJobID);
}

/**
 * This function returns the fax job status.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API char* getFaxJobStatusDLL(char* serverName,DWORD faxJobID)
{
	//get values
	LPCTSTR serverNameStr=NULL;
	if(serverName!=NULL)
	{
		serverNameStr=convertCharArrayToLPCTSTR(serverName);
	}
	
	//invoke fax action
	Response response=getFaxJobStatusNative(serverNameStr,faxJobID);

	//get output
	char* status=NULL;
	if(response.result)
	{
		status=response.value;
	}

	return status;
}

/**
 * This is the command line interface to the fax4j.dll fax capabilities.
 * 
 * @param 	argc
 * 			Amount of arguments
 * @param 	argv
 * 			The CLI arguments
 */
FAX4J_API int runCLI(int argc,const char* argv[])
{
	//enable debug
	setDebugMode(true);
	
	//get arguments
	char* actionType=NULL;
	char* server=NULL;
	char* faxJobIDStr=NULL;
	DWORD faxJobID=0;
	char* targetAddress=NULL;
	char* targetName=NULL;
	char* senderName=NULL;
	char* fileName=NULL;

	bool donePrinted=false;
	if(argc>1)
	{
		char* argument=NULL;
		char* value=NULL;
		for(register int index=1;index<argc;index=index+2)
		{
			//get next argument
			argument=(char*)argv[index];
			logDebug("Checking argument:");
			logDebug(argument);
			value=(char*)argv[index+1];
			logDebug("Argument value:");
			logDebug(value);

			if(strcmp(argument,"-action")==0)
			{
				actionType=value;
			}
			else if(strcmp(argument,"-server")==0)
			{
				server=value;
			}
			else if(strcmp(argument,"-job")==0)
			{
				faxJobIDStr=value;
				faxJobID=atoi(faxJobIDStr);
			}
			else if(strcmp(argument,"-target_address")==0)
			{
				targetAddress=value;
			}
			else if(strcmp(argument,"-target_name")==0)
			{
				targetName=value;
			}
			else if(strcmp(argument,"-sender_name")==0)
			{
				senderName=value;
			}
			else if(strcmp(argument,"-file")==0)
			{
				fileName=value;
			}
		}
		
		logDebug("Read input:");
		logDebug("Action:");
		logDebug(actionType);
		logDebug("Server:");
		logDebug(server);
		logDebug("Fax Job ID:");
		logDebug(faxJobIDStr);
		logDebug("Target Address:");
		logDebug(targetAddress);
		logDebug("Target Name:");
		logDebug(targetName);
		logDebug("Sender Name:");
		logDebug(senderName);
		logDebug("File:");
		logDebug(fileName);

		if(actionType!=NULL)
		{
			bool validAction=true;
			char* status=NULL;
			logDebug("Handling specific fax action.");
			if(strcmp(actionType,"submit")==0)
			{
				//invoke fax action
				faxJobID=submitFaxJobDLL(server,targetAddress,targetName,senderName,fileName);
			}
			else if(strcmp(actionType,"suspend")==0)
			{
				//invoke fax action
				suspendFaxJobDLL(server,faxJobID);
			}
			else if(strcmp(actionType,"resume")==0)
			{
				//invoke fax action
				resumeFaxJobDLL(server,faxJobID);
			}
			else if(strcmp(actionType,"cancel")==0)
			{
				//invoke fax action
				cancelFaxJobDLL(server,faxJobID);
			}
			else if(strcmp(actionType,"getstatus")==0)
			{
				//invoke fax action
				status=getFaxJobStatusDLL(server,faxJobID);
			}
			else
			{
				validAction=false;
			}

			if(validAction)
			{
				char* errorMessage=getLastErrorMessageDLL();
				donePrinted=true;
				if(errorMessage==NULL)
				{
					//print done
					printf("fax.operation.done=true\n");

					//print output
					if(strcmp(actionType,"submit")==0)
					{
						printf("fax.job.id=%d\n",faxJobID);
					}
					else if(strcmp(actionType,"getstatus")==0)
					{
						printf("fax.job.status=%s\n",status);
					}
				}
				else
				{
					printf("fax.operation.done=false\n");
					printf("fax.error=%s\n",errorMessage);
				}
			}
		}
	}

	if(!donePrinted)
	{
		printf("fax.operation.done=false\n");
	}

    return 0;
}
