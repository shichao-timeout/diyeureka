package com.timeout.diy.server.config;

public class ConfigurationException extends Exception{
    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Exception e) {
        super(msg, e);
    }

}
