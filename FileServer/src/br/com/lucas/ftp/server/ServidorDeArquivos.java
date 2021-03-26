/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lucas.ftp.server;

import br.com.lucas.ftp.Requisicao;
import br.com.lucas.ftp.Resposta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lucas
 */
public class ServidorDeArquivos extends Thread {

    public static void main(String args[]){
        ServerSocket serverSocket;
        //Socket cliente;
        Resposta rep = new Resposta();

        try{
            serverSocket = new ServerSocket(40000);
            System.out.println("DEBUG - Servidor iniciou na porta 40000");

            while (true){//aplicação que vai rodar de maneira dedicada - loop
                Socket cliente = serverSocket.accept();//conectou
                
                ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());                
                
                Thread td = new Thread(new Runnable() {//cria a thread
                    @Override
                    public void run() {
                        try{
                            int loop = 1;
                            do{
                                Requisicao req = (Requisicao)in.readObject();//aguarda a primeira requisição

                                System.out.println("DEBUG - REQ: " + req.getMenssageType() + "/" + req.getMessageContent());//Histórico de requisições

                                if(req.getMenssageType() == Requisicao.FILENAME_REQUEST){//Requisição de download - verificação da existência do arquivo

                                    ServidorDeArquivos.downloadCliente(req, rep, in, out);

                                }else if(req.getMenssageType() == Requisicao.FILEUPLOAD_REQUEST){//Requisição de upload - solicitando o upload

                                    ServidorDeArquivos.uploadCliente(req, rep, in, out);

                                }else if(req.getMenssageType() == Requisicao.ENCERRA_CLIENTE){
                                    System.out.println("DEBUG - REQ: " + req.getMenssageType() + "/" + req.getMessageContent());//Histórico de requisições - encerra cliente
                                    System.out.println("Thread " + req.getThreadID() + " encerrada!");
                                    in.close();
                                    out.close();
                                    cliente.close();
                                    loop = 0;
                                }
                            }while(loop != 0);
                        }catch(Exception ex){
                            System.err.println("Erro na thread!");
                            ex.printStackTrace();
                        }
                    }
                });
                td.start();
                System.out.println("Thread ID: " + td.getId() + " criada!");
                rep.setThreadID(td.getId());
                out.writeObject(rep);//envia para o cliente o ID da sua Thread
            }
        }catch (Exception ex){
            System.err.println("Erro no servidor!");
            ex.printStackTrace();
        }
    }
    
    public static void downloadCliente(Requisicao req, Resposta rep, ObjectInputStream in, ObjectOutputStream out){
        try{
            //abri o arquivo que veio como conteúdo da Requisição
            File f = new File(req.getMessageContent());
            rep = new Resposta();
            if(f.exists()){
                rep.setResponseCode(Resposta.FILE_EXITS);
                out.writeObject(rep);

                FileReader fr = new FileReader(f);//manipulo o arquivo em modo leitura
                BufferedReader br = new BufferedReader(fr);//elemento para fazer as leituras
                String linha = null;
                do{
                    req = (Requisicao)in.readObject();//aguardo novo pedido do cliente

                    if(req.getMenssageType() == Requisicao.FILECONTENT_REQUEST){//pedido de download
                        linha = br.readLine();
                        rep = new Resposta();
                        if(linha != null){
                            System.out.println("DEBUG - Client ID: " + req.getThreadID() + "REQ: " + req.getMenssageType() + "/" + req.getMessageContent());//Histórico de requisições de conteúdo
                            rep.setResponseCode(Resposta.CONTENT_AVAILABLE);
                            rep.setResponseContent(linha);
                        }else{
                            rep.setResponseCode(Resposta.END_OF_FILE);//Fim do download
                            System.out.println("DEBUG - RESPOSTA: " + Resposta.END_OF_FILE);//Histórico de resposta (END_OF_FILE)
                        }
                        out.writeObject(rep);
                    }
                }while(linha != null);
                fr.close();
            }else{
                rep.setResponseCode(Resposta.FILE_NOT_FOUND);
                out.writeObject(rep);
            }
        }catch(Exception ex){
            System.err.println("Erro no servidor!");
            ex.printStackTrace();
        }
    }
    
    public static void uploadCliente(Requisicao req, Resposta rep, ObjectInputStream in, ObjectOutputStream out){
        try{
            File f = new File(req.getMessageContent());
            if(!f.exists()){//arquivo não existe ainda
                rep = new Resposta();
                rep.setResponseCode(Resposta.YES_TO_UPLOAD);
                out.writeObject(rep);

                req = (Requisicao)in.readObject();//aguardando novo pedido do cliente

                if(req.getMenssageType() == Requisicao.FILEUPLOAD_START){//inicia o upload - servidor solicita para cliente o arquivo
                    String fileName = req.getMessageContent();
                    FileWriter fw = new FileWriter(new File(fileName));

                    do {
                        //Solicitando o upload
                        req = new Requisicao();
                        req.setMenssageType(Requisicao.FILECONTENT_REQUEST);
                        out.writeObject(req);

                        //Recebendo a resposta
                        rep = (Resposta)in.readObject();
                        if (rep.getResponseCode() == Resposta.CONTENT_AVAILABLE) {
                            fw.write(rep.getResponseContent() + "\n");
                        }else if(rep.getResponseCode()  == Resposta.END_OF_FILE){
                            System.out.println("DEBUG - Arquivo " + fileName + " recebido!");
                        }else{
                            System.out.println("Erro!");
                        }
                    } while (rep.getResponseCode() != Resposta.END_OF_FILE);
                    fw.close();
                }
            }else if(f.exists()){//arquivo já existe
                rep = new Resposta();
                rep.setResponseCode(Resposta.SUBS_TO_UPLOAD);
                out.writeObject(rep);
                
                req = (Requisicao)in.readObject();//aguardando novo pedido do cliente

                if(req.getMenssageType() == Requisicao.FILEUPLOAD_START){//exclui o arquivo existente e inicia o upload - servidor solicita para cliente o arquivo
                    f.delete();//exclui o arquivo
                    //f = new File(req.getMessageContent());
                    String fileName = req.getMessageContent();
                    FileWriter fw = new FileWriter(new File(fileName));

                    do {
                        //Solicitando o upload
                        req = new Requisicao();
                        req.setMenssageType(Requisicao.FILECONTENT_REQUEST);
                        out.writeObject(req);

                        //Recebendo a resposta
                        rep = (Resposta)in.readObject();
                        if (rep.getResponseCode() == Resposta.CONTENT_AVAILABLE) {
                            fw.write(rep.getResponseContent() + "\n");
                        }else if(rep.getResponseCode()  == Resposta.END_OF_FILE){
                            System.out.println("DEBUG - Arquivo " + fileName + " recebido!");
                        }else{
                            System.out.println("Erro!");
                        }
                    } while (rep.getResponseCode() != Resposta.END_OF_FILE);
                    fw.close();
                }
            }
        }catch(Exception ex){
            System.err.println("Erro no servidor!");
            ex.printStackTrace();
        }
    }
    
}
