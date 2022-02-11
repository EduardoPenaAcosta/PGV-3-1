package com.edusoft.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente extends Thread{

    private static Socket socket;
    private static PrintStream salidaCliente;
    private static BufferedReader entradaCliente;


    private static final String HOST = "localhost";
    private static final int PORT = 8800;

    public Cliente(Socket socket){
        Cliente.socket = socket;
    }

    public static void main(String[] args) {
        try{

            Socket socket = new Socket(HOST,PORT);
            salidaCliente = new PrintStream(socket.getOutputStream());
            entradaCliente = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Indique usuario: ");
            String usuario = entradaCliente.readLine();
            salidaCliente.println(usuario);

            Thread thread = new Cliente(socket);
            thread.start();

            String msg;
            while(true){
                msg = entradaCliente.readLine();

                if(msg.equals("bye")) {
                    System.out.println("Conexión cerrada!");
                    System.exit(0);
                }
                System.out.flush();
                System.out.println(msg);
                System.out.print("Mensaje --> ");
            }
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }

    }

    @Override
    public void run(){
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String msg;
            while (true) {
                msg = entrada.readLine();

                if(msg.equals("bye")) {
                    System.out.println("Conexión cerrada!");
                    System.exit(0);
                }
                System.out.flush();
                System.out.println(msg);
                System.out.print("Mensaje --> ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void cerrarConex(){

        if(socket != null){
            try {
                socket.close();
                salidaCliente.println("Goodbye");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
