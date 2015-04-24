package org.fax4j.common;

/**
 * This is the fax action type enum.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.26
 */
public enum FaxActionType
{
	/**The submit fax job action type*/
	SUBMIT_FAX_JOB,
	/**The suspend fax job action type*/
	SUSPEND_FAX_JOB,
	/**The resume fax job action type*/
	RESUME_FAX_JOB,
	/**The cancel fax job action type*/
	CANCEL_FAX_JOB,
	/**The get fax job status action type*/
	GET_FAX_JOB_STATUS
}