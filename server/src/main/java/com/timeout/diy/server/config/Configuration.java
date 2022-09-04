package com.timeout.diy.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * configuration is represent a config object that contain many k-v pair config;
 */
public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static final String NODE_TYPE = "node.type";
    public static final String NODE_TYPE_MASTER = "master";
    public static final String NODE_TYPE_SLAVE = "slave";
    /**
     * node type:master or slave
     */
    private String nodeType;

    private Configuration() {
    }

    public static class SingleTon {
        public static Configuration instance = new Configuration();
    }

    public static Configuration getInstance() {
        return SingleTon.instance;
    }

    /**
     * parse the filepath as properties file,and parse it to properties object,and init this node type.
     * @param filePath the properties file path
     */
    public void parsePropertiesConfig(String filePath) throws ConfigurationException{
        //todo check filepath
        File configFile = new File(filePath);
        Properties configProperties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(configFile);
            configProperties.load(fis);
            String nodeType = configProperties.getProperty(NODE_TYPE);
            if(nodeType == null) {
                throw new IllegalArgumentException("node.type cannot be empty......");
            }else if (nodeType != NODE_TYPE_MASTER && nodeType != NODE_TYPE_SLAVE){
                throw new IllegalArgumentException("node.type must be master or slave......");
            }else{
                this.nodeType = nodeType;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
