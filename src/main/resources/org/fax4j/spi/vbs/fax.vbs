
Sub ioPrintText(text)
	WScript.Echo text
End Sub

Sub ioPrintDebug(text)
	WScript.Echo "[fax4j-vbs][DEBUG] "&text
End Sub

Sub ioPrintProcessOutputBeginText
	ioPrintText "-----------OUTPUT-----------"
End Sub

Function utilStringConcat(prefixString,suffixString)
	Dim stringValue
	stringValue=prefixString&suffixString
	utilStringConcat=stringValue
End Function

Function utilCreateObject(objectName)
	Dim instance
	Set instance=CreateObject(objectName)
	Set utilCreateObject=instance
End Function

Function faxCreateFaxServer()
	Dim instance
	Set instance=utilCreateObject("FaxComEx.FaxServer")
	Set faxCreateFaxServer=instance
End Function

Function faxCreateFaxDocument()
	Dim instance
	Set instance=utilCreateObject("FaxComEx.FaxDocument")
	Set faxCreateFaxDocument=instance
End Function

Function faxCreateFaxServerWin2k()
	Dim instance
	Set instance=utilCreateObject("FaxServer.FaxServer")
	Set faxCreateFaxServerWin2k=instance
End Function

Sub faxSetFaxJobStatusWin2k(faxServerName,faxJobID,status)
	Dim faxServerInstance
	Set faxServerInstance=faxCreateFaxServerWin2k()
	
	faxServerInstance.Connect faxServerName
	
	Dim faxJobs
	Set faxJobs=faxServerInstance.GetJobs()
	
	Dim faxJobsAmount
	faxJobsAmount=faxJobs.Count
	Dim outputText
	outputText=utilStringConcat("Found fax jobs amount: ",faxJobsAmount)
	ioPrintDebug outputText
	
	Dim faxJob
	Dim currentFaxJobID
	outputText=utilStringConcat("Looking for fax job: ",faxJobID)
	ioPrintDebug outputText
	For count=1 to faxJobsAmount
	    Set faxJob=faxJobs.Item(count)
	    currentFaxJobID=faxJob.JobId
		outputText=utilStringConcat("Extracted for fax job: ",currentFaxJobID)
		ioPrintDebug outputText
	    If currentFaxJobID=faxJobID Then
	    	ioPrintDebug "found fax job, setting new status"
	    	faxJob.SetStatus status
		End If
	Next
End Sub

Function faxGetFaxJobStatusWin2k(faxServerName,faxJobID)
	Dim faxServerInstance
	Set faxServerInstance=faxCreateFaxServerWin2k()
	
	faxServerInstance.Connect faxServerName
	
	Dim faxJobs
	Set faxJobs=faxServerInstance.GetJobs()
	
	Dim faxJobsAmount
	faxJobsAmount=faxJobs.Count
	Dim outputText
	outputText=utilStringConcat("Found fax jobs amount: ",faxJobsAmount)
	ioPrintDebug outputText
	
	Dim faxJob
	Dim currentFaxJobID
	outputText=utilStringConcat("Looking for fax job: ",faxJobID)
	ioPrintDebug outputText
	Dim status
	status="UNKNOWN"
	For count=1 to faxJobsAmount
	    Set faxJob=faxJobs.Item(count)
	    currentFaxJobID=faxJob.JobId
		outputText=utilStringConcat("Extracted for fax job: ",currentFaxJobID)
		ioPrintDebug outputText
	    If currentFaxJobID=faxJobID Then
	    	ioPrintDebug "found fax job, extracting status"
	    	status=faxJob.QueueStatus
		End If
	Next
	
	faxGetFaxJobStatusWin2k=status
End Function
