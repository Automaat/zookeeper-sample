package agh;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        if (args.length < 4) {
            System.err.println("USAGE: Main connectionString znode program [args ...]");
            System.exit(2);
        }
        String connectionString = args[0];
        String znode = args[1];
        String exec[] = new String[args.length - 2];
        System.arraycopy(args, 2, exec, 0, exec.length);
        zooKeeper = new ZooKeeper(connectionString, 3000, null);

        Thread watcher = new Thread(new MainWatcher(znode, zooKeeper, exec, Runtime.getRuntime()));
        watcher.start();

        final Scanner scanner = new Scanner(System.in);

        while (true) {
            final String input = scanner.next();

            if ("show".equalsIgnoreCase(input)){
                printTree(znode, 0);

            } else if ("exit".equalsIgnoreCase(input)){
                watcher.interrupt();
                break;
            }
        }

    }

    private static void printTree(String znode, int indent) throws KeeperException, InterruptedException {
        for (int i =0; i<indent; i++){
            System.out.println("\t");
        }
        System.out.println(znode);

        for (String child : zooKeeper.getChildren(znode, false)) {
            printTree(znode + "/" + child, indent + 1);
        }
    }
}
