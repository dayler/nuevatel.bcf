package com.nuevatel.bcf;

/**
 * Responsible to execute background process.
 *
 * Created by asalazar on 6/4/15.
 */
public interface Processor {

    /**
     * Execute the task of the process.
     */
    void execute();

    /**
     * End to execute the process.
     *
     * @param ts Termination time in seconds. The time to wait by the termination of the process, before to kill the
     *           associated tasks.
     */
    void shutdown(int ts);
}
