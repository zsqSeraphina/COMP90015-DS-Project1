package com.dictionary.utils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

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
    private ArrayList<String> result;
    private String message;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
