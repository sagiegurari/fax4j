
Dim faxJobIDStr
faxJobIDStr="{1}"
Dim faxJob
faxJob=cint(faxJobIDStr)
Dim status
status=faxGetFaxJobStatusWin2k("{0}",faxJob)

''print output start
ioPrintProcessOutputBeginText

''print output
ioPrintText "fax.operation.done=true"
Dim output
output=utilStringConcat("fax.job.status=",status)
ioPrintText output
