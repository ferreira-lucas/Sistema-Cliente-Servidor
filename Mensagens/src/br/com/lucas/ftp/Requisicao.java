/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lucas.ftp;

/**
 *
 * @author lucas
 */
public class Requisicao implements java.io.Serializable {
    
    //Requisições para localização do arquivo e download   
    public static int FILENAME_REQUEST = 0;
    public static int FILECONTENT_REQUEST = 1;
    
    //Requisições para upload do arquivo
    public static int FILEUPLOAD_REQUEST = 2;
    public static int FILEUPLOAD_START = 3;
    
    //Encerramento do cliente
    public static int ENCERRA_CLIENTE = 4;
    
    private int menssageType;
    private String messageContent;
    private Long threadID;

    /**
     * @return the FILENAME_REQUEST
     */
    public static int getFILENAME_REQUEST() {
        return FILENAME_REQUEST;
    }

    /**
     * @param aFILELAME_REQUEST the FILENAME_REQUEST to set
     */
    public static void setFILENAME_REQUEST(int aFILENAME_REQUEST) {
        FILENAME_REQUEST = aFILENAME_REQUEST;
    }

    /**
     * @return the FILECONTENT_REQUEST
     */
    public static int getFILECONTENT_REQUEST() {
        return FILECONTENT_REQUEST;
    }

    /**
     * @param aFILECONTENT_REQUEST the FILECONTENT_REQUEST to set
     */
    public static void setFILECONTENT_REQUEST(int aFILECONTENT_REQUEST) {
        FILECONTENT_REQUEST = aFILECONTENT_REQUEST;
    }

    /**
     * @return the FILEUPLOAD_REQUEST
     */
    public static int getFILEUPLOAD_REQUEST() {
        return FILEUPLOAD_REQUEST;
    }

    /**
     * @param aFILEUPLOAD_REQUEST the FILEUPLOAD_REQUEST to set
     */
    public static void setFILEUPLOAD_REQUEST(int aFILEUPLOAD_REQUEST) {
        FILEUPLOAD_REQUEST = aFILEUPLOAD_REQUEST;
    }

    /**
     * @return the FILEUPLOAD_START
     */
    public static int getFILEUPLOAD_START() {
        return FILEUPLOAD_START;
    }

    /**
     * @param aFILEUPLOAD_START the FILEUPLOAD_START to set
     */
    public static void setFILEUPLOAD_START(int aFILEUPLOAD_START) {
        FILEUPLOAD_START = aFILEUPLOAD_START;
    }

    /**
     * @return the menssageType
     */
    public int getMenssageType() {
        return menssageType;
    }

    /**
     * @param menssageType the menssageType to set
     */
    public void setMenssageType(int menssageType) {
        this.menssageType = menssageType;
    }

    /**
     * @return the messageContent
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @param messageContent the messageContent to set
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * @return the threadID
     */
    public Long getThreadID() {
        return threadID;
    }

    /**
     * @param threadID the threadID to set
     */
    public void setThreadID(Long threadID) {
        this.threadID = threadID;
    }
 
}
