package com.hanssecurity.uaa.service;

/**
 * There are 2 way for sending Email,
 * one is SMTP, another is http
 * @author hans
 */
public interface EmailService {

     void send(String email, String msg);
}
