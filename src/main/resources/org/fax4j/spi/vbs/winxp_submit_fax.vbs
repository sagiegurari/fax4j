
''define variables
Dim faxServerInstance
Set faxServerInstance=faxCreateFaxServer()
Dim faxDocumentInstance
Set faxDocumentInstance=faxCreateFaxDocument()

''connect to the fax server
faxServerInstance.Connect "{0}"

''set the fax body
faxDocumentInstance.Body="{1}"

''name the document
faxDocumentInstance.DocumentName="{2}"

''set the fax priority
faxDocumentInstance.Priority={3}

''add the recipient (number, name)
faxDocumentInstance.Recipients.Add "{4}", "{5}"

''set to none receipt type
faxDocumentInstance.ReceiptType=frtNONE

''set the sender properties
faxDocumentInstance.Sender.Name="{6}"
faxDocumentInstance.Sender.FaxNumber="{7}"
faxDocumentInstance.Sender.Email="{8}"

''submit the document to the connected fax server
Dim faxJobIDs
ioPrintDebug "Sending fax"
faxJobIDs=faxDocumentInstance.ConnectedSubmit(faxServerInstance)
ioPrintDebug "Fax sent"

''disconnect from fax server
faxServerInstance.Disconnect

''print output start
ioPrintProcessOutputBeginText

''print fax job ID as empty since vbs does not support string array
Dim outputText
outputText=utilStringConcat("fax.job.id=","")
ioPrintText outputText
ioPrintText "fax.operation.done=true"
