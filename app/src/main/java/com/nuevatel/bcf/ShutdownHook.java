package com.nuevatel.bcf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asalazar on 6/4/15.
 */
public class ShutdownHook extends Thread {

    /**
     * Time to wait by the termination of the task.
     */
    private int terminationTime;

    private ExecutorService exSrv = null;

    List<Processor> processorList = new ArrayList<>();

    public ShutdownHook(int terminationTime, int size) {
        this.terminationTime = terminationTime;
        exSrv = Executors.newFixedThreadPool(size);
    }

    public void appendProcess(Processor p) {
        processorList.add(p);
    }

    public void clear() {
        processorList = new ArrayList<>();
    }

    @Override
    public void run() {
        for (Processor p : processorList) {
            exSrv.execute(()->p.shutdown(terminationTime));
        }
    }
}
