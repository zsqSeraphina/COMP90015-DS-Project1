package com.dictionary.util;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Siqi Zhou
 * student id 903274
 *
 * class for storing all information of a request
 */
public class DictRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2439808276703516916L;

    private RequestType requestType;
    private String word;
    private String mean;

    public DictRequest(RequestType requestType, String word, String mean) {
        this.requestType = requestType;
        this.word = word;
        this.mean = mean;
    }
    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
