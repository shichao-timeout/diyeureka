package com.timeout.diy.server.node;

/**
 * master角色节点
 */
public class Master {
    private ControllerCandidate controllerCandidate;

    public Master(){
        this.controllerCandidate = new ControllerCandidate();
    }

    public void start(){}
}
