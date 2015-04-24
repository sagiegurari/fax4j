/**
 * fax4j native layer.
 *
 * @author 	Sagie Gur-Ari
 */

// The following ifdef block is the standard way of creating macros which make exporting 
// from a DLL simpler. All files within this DLL are compiled with the FAX4J_EXPORTS
// symbol defined on the command line. this symbol should not be defined on any project
// that uses this DLL. This way any other project whose source files include this file see 
// FAX4J_API functions as being imported from a DLL, whereas this DLL sees symbols
// defined with this macro as being exported.
#ifdef FAX4J_EXPORTS
#define FAX4J_API __declspec(dllexport)
#else
#define FAX4J_API __declspec(dllimport)
#endif

//exclude rarely-used stuff from Windows headers
#define WIN32_LEAN_AND_MEAN

//windows API
#include <windows.h>

//add support for fax
#include <winfax.h>

//add support for java
#include <jni.h>

/**
 * Holds the response data.
 */
struct Response
{
	/**The method result. True for successful, false for failed.*/
	bool result;
	/**The fax job ID*/
	DWORD faxJobID;
	/**General value*/
	char* value;
	/**The error code*/
	int errorCode;
	/**The error message*/
	char* errorMessage;
};

/**
 * This function sets the debug mode of the native layer.
 * 
 * @param 	debug
 * 			True to print debug messages, otherwise false
 */
FAX4J_API void setDebugMode(bool debugMode);

/**
 * Logs the debug message.
 *
 * @param 	message
 * 			The debug message
 */
FAX4J_API void logDebug(char* message);

/**
 * Logs the debug message.
 *
 * @param 	errorCode
 * 			The error code
 * @param 	errorMessage
 * 			The error message
 */
FAX4J_API void logDebug(int errorCode,char* errorMessage);

/**
 * Logs the debug message.
 *
 * @param 	message
 * 			The debug message
 */
FAX4J_API void logDebug(LPCTSTR message);

/**
 * Converts char array to LPCTSTR.
 * Since this library is compiled as non unicode this is possible.
 *
 * @param 	text
 * 			The text to convert
 * @return	The converted text
 */
FAX4J_API LPCTSTR convertCharArrayToLPCTSTR(char* text);

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
FAX4J_API Response submitFaxJobNative(LPCTSTR serverName,LPCTSTR targetAddress,LPCTSTR targetName,LPCTSTR senderName,LPCTSTR fileName);

/**
 * This function will suspend an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API Response suspendFaxJobNative(LPCTSTR serverName,DWORD faxJobID);

/**
 * This function will resume an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API Response resumeFaxJobNative(LPCTSTR serverName,DWORD faxJobID);

/**
 * This function will cancel an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API Response cancelFaxJobNative(LPCTSTR serverName,DWORD faxJobID);

/**
 * This function returns the fax job status.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API Response getFaxJobStatusNative(LPCTSTR serverName,DWORD faxJobID);

/**
 * Returns the last error message.
 *
 * @return	The last error message
 */
FAX4J_API char* getLastErrorMessageDLL();

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
FAX4J_API DWORD submitFaxJobDLL(char* serverName,char* targetAddress,char* targetName,char* senderName,char* fileName);

/**
 * This function will suspend an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API void suspendFaxJobDLL(char* serverName,DWORD faxJobID);

/**
 * This function will resume an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API void resumeFaxJobDLL(char* serverName,DWORD faxJobID);

/**
 * This function will cancel an existing fax job.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 */
FAX4J_API void cancelFaxJobDLL(char* serverName,DWORD faxJobID);

/**
 * This function returns the fax job status.
 * 
 * @param 	serverName
 * 			The fax server name
 * @param 	faxJobID
 * 			The fax job ID
 * @return	The response object
 */
FAX4J_API char* getFaxJobStatusDLL(char* serverName,DWORD faxJobID);

/**
 * This is the command line interface to the fax4j.dll fax capabilities.
 * 
 * @param 	argc
 * 			Amount of arguments
 * @param 	argv
 * 			The CLI arguments
 */
FAX4J_API int runCLI(int argc,const char* argv[]);
