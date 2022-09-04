package com.timeout.diy.server;


import com.timeout.diy.server.config.ConfigurationException;
import com.timeout.diy.server.constant.NodeType;
import com.timeout.diy.server.node.Master;
import com.timeout.diy.server.node.NodeStatus;
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
        NodeStatus nodeStatus = NodeStatus.getInstance();
        nodeStatus.setStatus(NodeStatus.INITIALIZING);
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

            //2.start master node
            String nodeType = configuration.getNodeType();
            startNode(nodeType);


            //3.wait until the world end...
            waitForShutdown();
        }catch(ConfigurationException e) {
            logger.error("error when parsing configuration file", e);
            System.exit(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void waitForShutdown() throws InterruptedException {
        while(NodeStatus.getInstance().getStatus() != NodeStatus.SHUTDOWN){
            Thread.sleep(300);
        }
    }

    private String getConfigFromArgs(String[] args) throws Exception{
        return null;
    }

    private static void startNode(String nodeType){
        //when the node type is master
        if(NodeType.MASTER.equals(nodeType)){
            Master master = new Master();
            master.start();
        }
    }




}
