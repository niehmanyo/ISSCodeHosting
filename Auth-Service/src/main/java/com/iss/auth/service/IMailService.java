package com.iss.auth.service;

import java.util.List;

public interface IMailService {

    void sendSimpleMail(String mailRecipient,String subject ,String messageText);

    void sendHtmlMail(String mailRecipient,String subject , String  htmlText);

    void sendAppendixMail(String mailRecipient , String subject , String messageText , List<String> filePathList);
}
