package com.timeout.diy.server;


import com.timeout.diy.server.config.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.timeout.diy.server.config.Configuration;



/**
 * the main class for register center
 *
 */
public class RegitsterCenterServer {
    private static final Logger logger = LoggerFactory.getLogger(RegitsterCenterServer.class);
    public static void main(String[] args) {
        try {
            //1.read config from config file
            String configFilePath = args[0];
            //todo check args and parse args as the class properties
            if (configFilePath == null) {
                //todo throw self defined exception
            }
            //read the config file and convert it to a configuration object
            Configuration configuration = Configuration.getInstance();
            configuration.parsePropertiesConfig(configFilePath);

            if (logger.isDebugEnabled()) {
                logger.debug("properties file load finished ...");
            }
        }catch(ConfigurationException e) {
            logger.error("error when parsing configuration file", e);
            System.exit(2);
        }

    }

    private String getConfigFromArgs(String[] args) throws Exception{
        return null;
    }
}
