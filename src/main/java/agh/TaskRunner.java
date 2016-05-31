package agh;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;
import java.util.Optional;

public class TaskRunner implements Watcher {

    private final String pathToWatch;
    private final String[] commandToExecute;
    private final Runtime runtimeInstance;

    private Optional<Process> runningProcess = Optional.empty();

    public TaskRunner(String pathToWatch, String[] commandToExecute, Runtime runtimeInstance) {
        this.pathToWatch = pathToWatch;
        this.commandToExecute = commandToExecute;
        this.runtimeInstance = runtimeInstance;
    }

    public void process(WatchedEvent event) {

        if (!pathToWatch.equals(event.getPath())) {
            return;
        }

        final Event.EventType eventType = event.getType();

        switch (eventType) {
            case NodeCreated:
                start();
                break;
            case NodeDeleted:
                stop();
                break;
            default:
                break;
        }

    }

    private void start() {
        if (runningProcess.isPresent()) {
            return;
        }

        try {
            runningProcess = Optional.of(
                    runtimeInstance.exec(commandToExecute)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        if (!runningProcess.isPresent()) {
            return;
        }

        final Process process = runningProcess.get();
        process.destroy();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
