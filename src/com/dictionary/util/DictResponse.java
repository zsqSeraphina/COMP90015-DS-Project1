package com.dictionary.util;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Siqi Zhou
 * student id 903274
 *
 * class for storing all information of a response
 */
public class DictResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -5776950265314210036L;

    private Boolean isSuccess = false;
    private String result;
    private String message;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
