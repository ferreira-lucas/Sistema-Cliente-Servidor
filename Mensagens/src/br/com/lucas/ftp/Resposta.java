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
public class Resposta implements java.io.Serializable{

    //Respostas para localização e download de arquivos por parte do cliente
    public static int FILE_EXITS = 1;
    public static int FILE_NOT_FOUND = 2;
    public static int CONTENT_AVAILABLE = 3;
    public static int END_OF_FILE = 4;
    
    //Respostaas para upload de arquivos do cliente para o servidor
    public static int YES_TO_UPLOAD = 5;
    public static int SUBS_TO_UPLOAD = 6;
    
    private int responseCode;
    private String responseContent;
    private Long threadID;

    /**
     * @return the FILE_EXITS
     */
    public static int getFILE_EXITS() {
        return FILE_EXITS;
    }

    /**
     * @param aFILE_EXITS the FILE_EXITS to set
     */
    public static void setFILE_EXITS(int aFILE_EXITS) {
        FILE_EXITS = aFILE_EXITS;
    }

    /**
     * @return the FILE_NOT_FOUND
     */
    public static int getFILE_NOT_FOUND() {
        return FILE_NOT_FOUND;
    }

    /**
     * @param aFILE_NOT_FOUND the FILE_NOT_FOUND to set
     */
    public static void setFILE_NOT_FOUND(int aFILE_NOT_FOUND) {
        FILE_NOT_FOUND = aFILE_NOT_FOUND;
    }

    /**
     * @return the CONTENT_AVAILABLE
     */
    public static int getCONTENT_AVAILABLE() {
        return CONTENT_AVAILABLE;
    }

    /**
     * @param aCONTENT_AVAILABLE the CONTENT_AVAILABLE to set
     */
    public static void setCONTENT_AVAILABLE(int aCONTENT_AVAILABLE) {
        CONTENT_AVAILABLE = aCONTENT_AVAILABLE;
    }

    /**
     * @return the END_OF_FILE
     */
    public static int getEND_OF_FILE() {
        return END_OF_FILE;
    }

    /**
     * @param aEND_OF_FILE the END_OF_FILE to set
     */
    public static void setEND_OF_FILE(int aEND_OF_FILE) {
        END_OF_FILE = aEND_OF_FILE;
    }

    /**
     * @return the YES_TO_UPLOAD
     */
    public static int getYES_TO_UPLOAD() {
        return YES_TO_UPLOAD;
    }

    /**
     * @param aYES_TO_UPLOAD the YES_TO_UPLOAD to set
     */
    public static void setYES_TO_UPLOAD(int aYES_TO_UPLOAD) {
        YES_TO_UPLOAD = aYES_TO_UPLOAD;
    }

    /**
     * @return the SUBS_TO_UPLOAD
     */
    public static int getNO_TO_UPLOAD() {
        return SUBS_TO_UPLOAD;
    }

    /**
     * @param aNO_TO_UPLOAD the SUBS_TO_UPLOAD to set
     */
    public static void setNO_TO_UPLOAD(int aNO_TO_UPLOAD) {
        SUBS_TO_UPLOAD = aNO_TO_UPLOAD;
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the responseContent
     */
    public String getResponseContent() {
        return responseContent;
    }

    /**
     * @param responseContent the responseContent to set
     */
    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
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
