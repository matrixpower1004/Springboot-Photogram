package com.matrix.photogram.handler.ex;

import java.util.Map;

public class CustomException extends RuntimeException{
	
	// 객체를 구분할 때!! 중요한 게 아님
    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
    	super(message);
    }
    
}
