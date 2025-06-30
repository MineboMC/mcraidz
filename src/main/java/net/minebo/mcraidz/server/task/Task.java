package net.minebo.mcraidz.server.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {

    private final int taskId;
    private final long time;
}