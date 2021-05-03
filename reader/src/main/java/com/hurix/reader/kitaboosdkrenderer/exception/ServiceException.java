package com.hurix.reader.kitaboosdkrenderer.exception;



import com.hurix.reader.kitaboosdkrenderer.constants.Constants;

import java.util.HashMap;

public class ServiceException extends Exception
{
	private int _code;
	private String _message = null;
	private Constants.SERVICETYPES _type;
	private HashMap<String, Integer> _invalidFields;
	private String _callbackException;
    public ServiceException(int code, String message, Constants.SERVICETYPES type, HashMap<String, Integer> listoffield)
    {
        super(message);
        _code = code;
        _message = message;
        _invalidFields = listoffield;
        setType(type);
    }
    public void setCallBackException(String callbackException)
    {
        _callbackException = callbackException;
    }
    public String getCallBackException()
    {
        return _type.toString();
    }
    public int getCode()
    {
    	return _code;
    }
    	    
    @Override
    public String toString() 
    {
        return _message;
    }
 
    @Override
    public String getMessage() 
    {
        return _message;
    }
    
	public Constants.SERVICETYPES getResponseRequestType()
	{
		return _type;
	}

	public void setType(Constants.SERVICETYPES _type)
	{
		this._type = _type;
	}

	public HashMap<String, Integer> getInvalidFields()
	{
		return _invalidFields;
	}

}
