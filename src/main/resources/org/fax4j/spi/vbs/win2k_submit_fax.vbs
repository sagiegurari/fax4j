
''define variables
Dim faxServerInstance
Set faxServerInstance=faxCreateFaxServerWin2k()

''connect to the fax server
faxServerInstance.Connect "{0}"

''create document
Dim faxDocumentInstance
Set faxDocumentInstance=faxServerInstance.CreateDocument("{1}")

''name the document
If "" <> "{2}" Then
	faxDocumentInstance.DisplayName="{2}"
End If

''add the recipient number
faxDocumentInstance.FaxNumber="{3}"

''add the recipient name
If "" <> "{4}" Then
	faxDocumentInstance.RecipientName="{4}"
End If

''set the sender properties
If "" <> "{5}" Then
	faxDocumentInstance.SenderName="{5}"
End If
If "" <> "{6}" Then
	faxDocumentInstance.SenderFax="{6}"
End If

''submit the document to the connected fax server
Dim faxJobID
ioPrintDebug "Sending fax"
faxJobID=faxDocumentInstance.Send
ioPrintDebug "Fax sent"

''print output start
ioPrintProcessOutputBeginText

''print fax job ID
Dim outputText
outputText=utilStringConcat("fax.job.id=",faxJobID)
ioPrintText outputText
ioPrintText "fax.operation.done=true"
