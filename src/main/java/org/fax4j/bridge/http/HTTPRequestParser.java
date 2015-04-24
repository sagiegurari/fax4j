package org.fax4j.bridge.http;

import org.fax4j.bridge.RequestParser;
import org.fax4j.spi.http.HTTPRequest;

/**
 * This interface defines the HTTP parser used to extract the fax job
 * and file info from the HTTP request.
 * 
 * @author 	Sagie Gur-Ari
 * @version 1.01
 * @since	0.40.6
 */
public interface HTTPRequestParser extends RequestParser<HTTPRequest>
{
	//all capabilities defined in super interface
}