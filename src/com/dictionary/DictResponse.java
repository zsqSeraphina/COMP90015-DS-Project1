package com.dictionary;

import java.io.Serializable;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class DictResponse implements Serializable {
    private Boolean isSuccess = false;
    private String result;
    private String errorMessage;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
