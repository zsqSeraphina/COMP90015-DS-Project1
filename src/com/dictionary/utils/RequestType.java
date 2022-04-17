package com.dictionary.utils;

import java.io.Serializable;

/**
 * @author Siqi Zhou
 * student id 903274
 *
 * enum of all kinds of requests
 */
public enum RequestType implements Serializable {
    QUERY,
    ADD,
    REMOVE,
    UPDATE;
}
