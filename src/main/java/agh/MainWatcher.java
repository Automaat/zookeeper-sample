package agh;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class MainWatcher implements Runnable, Watcher {

    private final ZooKeeper zooKeeper;
    private final String znode;
    private TaskRunner taskRunner;
    private ChildrenWatcher childrenWatcher;

    public MainWatcher(String znode,
                       ZooKeeper zooKeeper,
                       String commandToExecute[],
                       Runtime executionContext) throws KeeperException, IOException {

        this.znode = znode;
        this.zooKeeper = zooKeeper;
        zooKeeper.register(this);
        taskRunner = new TaskRunner(znode, commandToExecute, executionContext);
        childrenWatcher = new ChildrenWatcher(znode);
        zooKeeper.exists(znode, true, null, this);
    }

    public void process(WatchedEvent event) {
        taskRunner.process(event);
        zooKeeper.exists(znode, true, null, this);
        zooKeeper.getChildren(znode, true, childrenWatcher, this);
    }

    public void run() {
        while (true) {
        }
    }


}
