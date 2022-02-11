package com.edusoft.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class Servidor extends Thread{

    private static Map<String,PrintStream> mensajes_clientes = new HashMap<String,PrintStream>();

    ArrayList<String> usuarios = new ArrayList<>();
    private static final int PORT = 8800;
    Socket socket;
    private String usuario;


    public Servidor(Socket socket){
        this.socket = socket;
    }

    public static void main(String[] args) {
        try{
            ServerSocket svsk = new ServerSocket(PORT);
            System.out.println("Servidor funcionando el puerto " + PORT);

            while(true){
                Socket conexion = svsk.accept();
                Thread thread = new Servidor(conexion);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintStream salidaCliente = new PrintStream(this.socket.getOutputStream());

            salidaCliente.println("Introduce el nombre de usuario ->");
            this.usuario = entradaCliente.readLine();

            mensajes_clientes.put(this.usuario, salidaCliente);
            String[] msg = entradaCliente.readLine().split(":");
            while(!msg[0].trim().equals("")){
                Date dt = new Date();
                int hour = dt.getHours();
                int min = dt.getMinutes();
                int scnds = dt.getSeconds();
                enviarMensaje(salidaCliente, "[" + hour + " :" + min + ": " + scnds + "escribió: ");
            }

            System.out.println(this.usuario + " ha salido del chat!");
            String[] out = {" del chat!!"};
            enviarMensaje(salidaCliente, "salió del chat!!");
            //delUsuarios(this.usuario);
            mensajes_clientes.remove(this.usuario);
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(PrintStream salidaCliente, String msg){
        for (Map.Entry<String, PrintStream> cliente : mensajes_clientes.entrySet()) {
            PrintStream chat = cliente.getValue();
            if (chat != salidaCliente) {
                chat.println(this.usuario + " " + msg);
            }
        }
    }

}
