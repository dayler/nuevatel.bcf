package com.nuevatel.bcf.core.domain;

import java.util.Collections;
import java.util.List;

/**
 * Created by asalazar on 6/25/15.
 */
public class Config {

    private List<String> toSms = Collections.emptyList();

    private List<String> toEmail = Collections.emptyList();

    private String fromSms;

    private String fromEmail;

    private String endpointDispatcher;

    private String endpointEmailMiddleware;

    private String alertHeader;

    private String emailSubject;

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getFromSms() {
        return fromSms;
    }

    public void setFromSms(String fromSms) {
        this.fromSms = fromSms;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getEndpointDispatcher() {
        return endpointDispatcher;
    }

    public void setEndpointDispatcher(String endpointDispatcher) {
        this.endpointDispatcher = endpointDispatcher;
    }

    public String getEndpointEmailMiddleware() {
        return endpointEmailMiddleware;
    }

    public void setEndpointEmailMiddleware(String endpointEmailMiddleware) {
        this.endpointEmailMiddleware = endpointEmailMiddleware;
    }

    public String getAlertHeader() {
        return alertHeader;
    }

    public void setAlertHeader(String alertHeader) {
        this.alertHeader = alertHeader;
    }

    public List<String> getToSmsList() {
        return toSms;
    }

    public void setToSms(List<String> toSms) {
        this.toSms = toSms;
    }

    public List<String> getToEmailList() {
        return toEmail;
    }

    public void setToEmail(List<String> toEmail) {
        this.toEmail = toEmail;
    }
}
