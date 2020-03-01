package com.example.myapplication;

public class GlobalVariables {

    private static GlobalVariables single_instance = null;

    private String whatsapp_prefix;
    private String whatsapp_phone;
    private String whatsapp_message;

    private String telegram_chatId;
    private String telegram_message;

    private boolean whatsapp_set;
    private boolean telegram_set;

    private String communicationMethod;

    public static GlobalVariables getInstance()
    {
        if (single_instance == null)
            single_instance = new GlobalVariables();

        return single_instance;
    }

    public void setTelegram_set() {
        telegram_set = true;
    }

    public void setWhatsapp_set() {
        whatsapp_set = true;
    }

    public boolean isConfigured() {
        return (telegram_set == true || whatsapp_set == true);
    }

    public void setCommunicationMethod(String communicationMethod) {
        this.communicationMethod = communicationMethod;
    }

    public String getCommunicationMethod() {
        return communicationMethod;
    }

    public String getWhatsapp_prefix() {
        return whatsapp_prefix;
    }

    public void setWhatsapp_prefix(String whatsapp_prefix) {
        this.whatsapp_prefix = whatsapp_prefix;
    }

    public String getWhatsapp_phone() {
        return whatsapp_phone;
    }

    public void setWhatsapp_phone(String whatsapp_phone) {
        this.whatsapp_phone = whatsapp_phone;
    }

    public String getWhatsapp_message() {
        return whatsapp_message;
    }

    public void setWhatsapp_message(String whatsapp_message) {
        this.whatsapp_message = whatsapp_message;
    }

    public String getTelegram_chatId() {
        return telegram_chatId;
    }

    public void setTelegram_chatId(String telegram_chatId) {
        this.telegram_chatId = telegram_chatId;
    }

    public String getTelegram_message() {
        return telegram_message;
    }

    public void setTelegram_message(String telegram_message) {
        this.telegram_message = telegram_message;
    }
}
