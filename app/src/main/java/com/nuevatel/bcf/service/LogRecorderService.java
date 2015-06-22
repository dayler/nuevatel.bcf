package com.nuevatel.bcf.service;

import com.nuevatel.bcf.core.domain.SessionRecord;

/**
 * Schedule to store in the database all transactions. Use schema <b>bcf_record</b>.
 *
 * @author Ariel Salazar
 */
public interface LogRecorderService {

    /**
     * Schedule SessionRecord to insert in the database.
     *
     * @param sr ecord to schedule.
     */
    void appendSessionRecord(SessionRecord sr);

    /**
     * Initialize the job.
     */
    void start();

    /**
     * Shutdown the service. Usually await 60 to finalize any pending task.
     */
    void shutdown();

    /**
     * Pause the service. It is used to avoid inconsistencies when the table is rotating.
     */
    void sealed();

    /**
     *
     * @return <b>true</b> is the service is running. <b>false</b> in other case.
     */
    boolean isRunning();

    boolean isSealed();

    /**
     * Release service, only works if the service was sealed.
     */
    void release();
}
