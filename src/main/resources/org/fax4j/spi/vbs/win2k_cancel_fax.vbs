
Dim faxJobIDStr
faxJobIDStr="{1}"
Dim faxJob
faxJob=cint(faxJobIDStr)
faxSetFaxJobStatusWin2k "{0}", faxJob, 1

''print output start
ioPrintProcessOutputBeginText

''print output
ioPrintText "fax.operation.done=true"
