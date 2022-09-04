package com.timeout.diy.server.node;

/**
 * node status:maybe one of the following value:
 * INITIALIZING
 * RUNNING
 * SHUTDOWN
 */
public class NodeStatus {
    public static final int INITIALIZING = 0;
    public static final int RUNNING = 1;
    public static final int SHUTDOWN = 2;

    private volatile int status;

    private NodeStatus() {
    }

    private static class SingleTon {
        static NodeStatus instance = new NodeStatus();
    }



    public static NodeStatus getInstance() {
        return SingleTon.instance;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static int get() {
        return getInstance().getStatus();
    }

}
