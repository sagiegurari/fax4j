/**
 * fax4j native layer.
 *
 * @author 	Sagie Gur-Ari
 */

#include "org_fax4j_spi_windows_WindowsJNIFaxClientSpi.h"
#include "fax4j.h"

/**
 * This function converts and returns the LPCTSTR.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param	javaString
 * 			The java string
 * @return	The string (LPCTSTR) value
 */
LPCTSTR convertToStr(const char* cConstCharArray)
{
	if(cConstCharArray==NULL)
	{
		return NULL;
	}

	//convert to char array
	char* cCharArray=(char*)cConstCharArray;

	//convert to LPCTSTR
	logDebug("Converting java char array to string.");
	logDebug(cCharArray);
	LPCTSTR stringType=convertCharArrayToLPCTSTR(cCharArray);

	return stringType;
}

/**
 * This function extracts the string value.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param	javaString
 * 			The java string
 * @return	The string value
 */
const char* getString(JNIEnv* jniEnv,jstring javaString)
{
	if(javaString==NULL)
	{
		return NULL;
	}

	//convert to char array
	const char* cConstCharArray=(*jniEnv).GetStringUTFChars(javaString,0);

	return cConstCharArray;
}

/**
 * This function creates a new java string.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param	charArray
 * 			The C char array
 * @return	The java string
 */
jstring createString(JNIEnv* jniEnv,char* charArray)
{
	if(charArray==NULL)
	{
		return NULL;
	}

	//convert to const char array
	const char* cConstCharArray=charArray;

	//convert to java string
	jstring string=(*jniEnv).NewStringUTF(cConstCharArray);

	return string;
}

/**
 * This function releases the string memory.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param	javaString
 * 			The java string
 * @param	cConstCharArray
 * 			The C char string
 */
void releaseString(JNIEnv* jniEnv,jstring javaString,const char* cConstCharArray)
{
	if(javaString!=NULL)
	{
		(*jniEnv).ReleaseStringUTFChars(javaString,cConstCharArray);
	}
}

/**
 * This function throws an exception based on the provided
 * response.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param	response
 *			The response
 */
void throwException(JNIEnv* jniEnv,Response response)
{
	//get exception class
	jclass classDefinition=jniEnv->FindClass("org/fax4j/FaxException");

	//get error message
	char* errorMessage=response.errorMessage;

	//throw exception
	jniEnv->ThrowNew(classDefinition,errorMessage);
}

/**
 * This function sets the debug mode of the native layer.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param 	classDefinition
 * 			The java class definition
 * @param 	debugMode
 * 			True to print debug messages, otherwise false
 */
JNIEXPORT void JNICALL Java_org_fax4j_spi_windows_WindowsJNIFaxClientSpi_setDebugModeNative(JNIEnv* jniEnv,jclass classDefinition,jboolean debugMode)
{
	bool debug=false;
	if(debugMode)
	{
		debug=true;
	}
	setDebugMode(debug);
}

/**
 * This function will submit a new fax job.
 * The fax job ID will be returned by this method.
 *
 * @param 	jniEnv
 * 			The JNI env
 * @param 	classDefinition
 * 			The java class definition
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
JNIEXPORT jint JNICALL Java_org_fax4j_spi_windows_WindowsJNIFaxClientSpi_submitFaxJobNative(JNIEnv* jniEnv,jclass classDefinition,jstring serverName,jstring targetAddress,jstring targetName,jstring senderName,jstring fileName)
{
	logDebug("Invoking JNI submitFaxJobNative");

	//get values
	logDebug("Converting java data types to C data types.");
	const char* cConstServerName=getString(jniEnv,serverName);
	LPCTSTR cServerName=convertToStr(cConstServerName);
	const char* cConstTargetAddress=getString(jniEnv,targetAddress);
	LPCTSTR cTargetAddress=convertToStr(cConstTargetAddress);
	const char* cConstTargetName=getString(jniEnv,targetName);
	LPCTSTR cTargetName=convertToStr(cConstTargetName);
	const char* cConstSenderName=getString(jniEnv,senderName);
	LPCTSTR cSenderName=convertToStr(cConstSenderName);
	const char* cConstFileName=getString(jniEnv,fileName);
	LPCTSTR cFileName=convertToStr(cConstFileName);

	//submit fax job
	Response response=submitFaxJobNative(cServerName,cTargetAddress,cTargetName,cSenderName,cFileName);

	logDebug("Releasing jni data types.");
	releaseString(jniEnv,serverName,cConstServerName);
	releaseString(jniEnv,targetAddress,cConstTargetAddress);
	releaseString(jniEnv,targetName,cConstTargetName);
	releaseString(jniEnv,senderName,cConstSenderName);
	releaseString(jniEnv,fileName,cConstFileName);

	jint faxJobID=0;
	if(response.result)
	{
		faxJobID=response.faxJobID;
	}
	else
	{
		//throw exception
		throwException(jniEnv,response);
	}

    return faxJobID;
}

/**
 * This function will suspend an existing fax job.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param 	classDefinition
 * 			The java class definition
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
JNIEXPORT void JNICALL Java_org_fax4j_spi_windows_WindowsJNIFaxClientSpi_suspendFaxJobNative(JNIEnv* jniEnv,jclass classDefinition,jstring serverName,jint faxJobID)
{
	logDebug("Invoking JNI suspendFaxJobNative");

	//get values
	logDebug("Converting java data types to C data types.");
	const char* cConstServerName=getString(jniEnv,serverName);
	LPCTSTR cServerName=convertToStr(cConstServerName);
	DWORD cFaxJobID=faxJobID;

	//suspend fax job
	Response response=suspendFaxJobNative(cServerName,cFaxJobID);

	logDebug("Releasing jni data types.");
	releaseString(jniEnv,serverName,cConstServerName);

	if(!response.result)
	{
		//throw exception
		throwException(jniEnv,response);
	}
}

/**
 * This function will resume an existing fax job.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param 	classDefinition
 * 			The java class definition
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
JNIEXPORT void JNICALL Java_org_fax4j_spi_windows_WindowsJNIFaxClientSpi_resumeFaxJobNative(JNIEnv* jniEnv,jclass classDefinition,jstring serverName,jint faxJobID)
{
	logDebug("Invoking JNI resumeFaxJobNative");

	//get values
	logDebug("Converting java data types to C data types.");
	const char* cConstServerName=getString(jniEnv,serverName);
	LPCTSTR cServerName=convertToStr(cConstServerName);
	DWORD cFaxJobID=faxJobID;

	//resume fax job
	Response response=resumeFaxJobNative(cServerName,cFaxJobID);

	logDebug("Releasing jni data types.");
	releaseString(jniEnv,serverName,cConstServerName);

	if(!response.result)
	{
		//throw exception
		throwException(jniEnv,response);
	}
}

/**
 * This function will cancel an existing fax job.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param 	classDefinition
 * 			The java class definition
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
JNIEXPORT void JNICALL Java_org_fax4j_spi_windows_WindowsJNIFaxClientSpi_cancelFaxJobNative(JNIEnv* jniEnv,jclass classDefinition,jstring serverName,jint faxJobID)
{
	logDebug("Invoking JNI cancelFaxJobNative");

	//get values
	logDebug("Converting java data types to C data types.");
	const char* cConstServerName=getString(jniEnv,serverName);
	LPCTSTR cServerName=convertToStr(cConstServerName);
	DWORD cFaxJobID=faxJobID;

	//cancel fax job
	Response response=cancelFaxJobNative(cServerName,cFaxJobID);

	logDebug("Releasing jni data types.");
	releaseString(jniEnv,serverName,cConstServerName);

	if(!response.result)
	{
		//throw exception
		throwException(jniEnv,response);
	}
}

/**
 * This function returns the fax job status.
 * 
 * @param 	jniEnv
 * 			The JNI env
 * @param 	classDefinition
 * 			The java class definition
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The fax job status
 */
JNIEXPORT jstring JNICALL Java_org_fax4j_spi_windows_WindowsJNIFaxClientSpi_getFaxJobStatusNative(JNIEnv* jniEnv,jclass classDefinition,jstring serverName,jint faxJobID)
{
	logDebug("Invoking JNI getFaxJobStatusNative");

	//get values
	logDebug("Converting java data types to C data types.");
	const char* cConstServerName=getString(jniEnv,serverName);
	LPCTSTR cServerName=convertToStr(cConstServerName);
	DWORD cFaxJobID=faxJobID;

	//cancel fax job
	Response response=getFaxJobStatusNative(cServerName,cFaxJobID);

	logDebug("Releasing jni data types.");
	releaseString(jniEnv,serverName,cConstServerName);

	if(!response.result)
	{
		//throw exception
		throwException(jniEnv,response);
	}

	//get value
	char* statusCharArray=response.value;

	//create string
	jstring status=createString(jniEnv,statusCharArray);

	return status;
}
