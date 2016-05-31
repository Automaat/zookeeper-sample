package agh;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Objects;

public class ChildrenWatcher implements AsyncCallback.Children2Callback {

    private final String path;

    public ChildrenWatcher(String path) {
        this.path = path;
    }

    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {

        if (!this.path.equals(path) || Objects.isNull(children)) {
            return;
        }

        System.out.println("Znode: " + path + " has " + children.size() +  " children");
    }

}
