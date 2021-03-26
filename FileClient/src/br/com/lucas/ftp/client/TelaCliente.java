/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.lucas.ftp.client;

import br.com.lucas.ftp.Requisicao;
import br.com.lucas.ftp.Resposta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author lucas
 */
public class TelaCliente {
    public static int loop = 0;
    public static Long td;
    public static void main(String args[]){
        Socket socket;
        Scanner teclado = new Scanner(System.in);

        try{
            socket = new Socket("localhost", 40000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Resposta rep = (Resposta)in.readObject();//recebe ID da Thread criada
            td = rep.getThreadID();
            
            System.out.println("Bem vindo ao sistema de arquivos:");
            System.out.println("Sua Thread é a número " + td);
            do{

                System.out.println("Digite a opção desejada:");
                System.out.println("1 - Download;");
                System.out.println("2 - Upload;");
                System.out.println("3 - Sair;");
                int opcao = teclado.nextInt();
                teclado.nextLine();//limpa a memória de texto
                if (opcao == 1) {//download

                    TelaCliente.download(teclado, out, in, opcao);

                }else if(opcao == 2){//upload

                    TelaCliente.upload(teclado, out, in, opcao);

                }else if(opcao == 3){//sair

                    TelaCliente.sair(out, in, socket, td);

                }else{
                    System.out.println("Opção inválida, digite novamente");
                    loop = 0;
                }
            }while(loop != 1);
        }catch (Exception ex){
            System.err.println("Erro no cliente!");
            ex.printStackTrace();
        }
    }
    
    public static void download(Scanner teclado, ObjectOutputStream out, ObjectInputStream in, int opcao){
        try{
            System.out.print("Digite o nome do arquivo que deseja baixar:");
            String fileName = teclado.nextLine();
            //cria requisição
            Requisicao req = new Requisicao(); 
            req.setMenssageType(Requisicao.FILENAME_REQUEST);
            req.setMessageContent(fileName);

            //enviar a requisição para o servidor
            out.writeObject(req);

            //espera a resposta
            Resposta rep = (Resposta)in.readObject();
            if(rep.getResponseCode() == Resposta.FILE_EXITS){
                System.out.println("Arquivo existe. Deseja baixálo?");
                System.out.println("1 - Sim;");
                System.out.println("2 - Não;");
                opcao = teclado.nextInt();
                if(opcao == 1){
                    FileWriter fw = new FileWriter(new File(fileName));

                    do {
                        //Solicitando o download
                        req = new Requisicao();
                        req.setMenssageType(Requisicao.FILECONTENT_REQUEST);
                        req.setThreadID(td);
                        out.writeObject(req);

                        //Recebendo a resposta
                        rep = (Resposta)in.readObject();
                        if (rep.getResponseCode() == Resposta.CONTENT_AVAILABLE) {
                            fw.write(rep.getResponseContent() + "\n");
                        }else if(rep.getResponseCode()  == Resposta.END_OF_FILE){
                            System.out.println("Transferência concluída! Verifique o arquivo!");
                        }else{
                            System.out.println("Erro!");
                        }
                    } while (rep.getResponseCode() != Resposta.END_OF_FILE);
                    fw.close();
                }else{
                    System.out.println("Download não realizado!");
                }
            }else if(rep.getResponseCode() == Resposta.FILE_NOT_FOUND){
                System.out.println("Arquivo não encontrado. Código " + Resposta.FILE_NOT_FOUND);
            }else{
                System.out.println("Erro!");
            }
            loop = 0;
        }catch(Exception ex){
            System.err.println("Erro no cliente!");
            ex.printStackTrace();
        }
    }
    
    public static void upload(Scanner teclado, ObjectOutputStream out, ObjectInputStream in, int opcao){
        try{
            System.out.print("Digite o nome do arquivo que deseja fazer upload: ");
            String fileName = teclado.nextLine();
            File f = new File(fileName);
            if(f.exists()){
                //cria requisição
                Requisicao req = new Requisicao(); 
                req.setMenssageType(Requisicao.FILEUPLOAD_REQUEST);
                req.setMessageContent(fileName);

                //enviar a requisição para o servidor
                out.writeObject(req);

                //espera a resposta
                Resposta rep = (Resposta)in.readObject();

                if(rep.getResponseCode() == Resposta.YES_TO_UPLOAD){//arquivo não existe no servidor - inicia o upload
                    req = new Requisicao();
                    req.setMenssageType(Requisicao.FILEUPLOAD_START);
                    req.setMessageContent(fileName);

                    out.writeObject(req);

                    FileReader fr = new FileReader(f);//manipulo o arquivo em modo leitura
                    BufferedReader br = new BufferedReader(fr);//elemento para fazer as leituras
                    String linha = null;
                    do{
                        req = (Requisicao)in.readObject();

                        if (req.getMenssageType() == Requisicao.FILECONTENT_REQUEST) {
                            linha = br.readLine();
                            rep = new Resposta();
                            if(linha != null){
                                rep.setResponseCode(Resposta.CONTENT_AVAILABLE);
                                rep.setResponseContent(linha);
                            }else{
                                rep.setResponseCode(Resposta.END_OF_FILE);//Fim do upload
                                System.out.println("Fim do upload! Verifique o servidor!");
                            }
                            out.writeObject(rep);
                        }
                    }while(linha != null);
                }else if(rep.getResponseCode() == Resposta.SUBS_TO_UPLOAD){//arquivo já existe no servidor
                    System.out.println("Arquivo já existe, deseja substituir?");
                    System.out.println("1 - Sim;");
                    System.out.println("2 - Não");
                    opcao = teclado.nextInt();
                    if (opcao == 1) {
                        req = new Requisicao();
                        req.setMenssageType(Requisicao.FILEUPLOAD_START);
                        req.setMessageContent(fileName);

                        out.writeObject(req);

                        FileReader fr = new FileReader(f);//manipulo o arquivo em modo leitura
                        BufferedReader br = new BufferedReader(fr);//elemento para fazer as leituras
                        String linha = null;
                        do{
                            req = (Requisicao)in.readObject();

                            if (req.getMenssageType() == Requisicao.FILECONTENT_REQUEST) {
                                linha = br.readLine();
                                rep = new Resposta();
                                if(linha != null){
                                    rep.setResponseCode(Resposta.CONTENT_AVAILABLE);
                                    rep.setResponseContent(linha);
                                }else{
                                    rep.setResponseCode(Resposta.END_OF_FILE);//Fim do upload
                                    System.out.println("Fim do upload! Verifique o servidor!");
                                }
                                out.writeObject(rep);
                            }
                        }while(linha != null);
                    }else if(opcao == 2){
                        System.out.println("Arquivo não transferido!");
                    }
                }
            }else{
                System.out.println("Arquivo inexistente!");
            }
            loop = 0;
        }catch(Exception ex){
            System.err.println("Erro no cliente!");
            ex.printStackTrace();
        }
    }
    
    public static void sair(ObjectOutputStream out, ObjectInputStream in,Socket socket, Long td){
        try{
            Requisicao req = new Requisicao();
            System.out.println("Saindo...");
            req.setMenssageType(Requisicao.ENCERRA_CLIENTE);
            req.setThreadID(td);
            out.writeObject(req);
            in.close();
            out.close();
            socket.close();
            loop = 1;
        }catch(Exception ex){
            System.err.println("Erro no cliente!");
            ex.printStackTrace();
        }
    }
}
