package com.hanssecurity.uaa.service;

/**
 * There are 2 services
 * @author hans
 */
public interface SmsService {
    public void send(String mobile, String msg);
}
