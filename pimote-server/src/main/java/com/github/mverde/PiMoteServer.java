package com.github.mverde;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Thread;

public class PiMoteServer {
    private class ClientThread extends Thread {
    	private Socket socket;
    
    	ClientThread(Socket socket) {
    		this.socket = socket;
    	}

    	public void run() {
        	try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
        		String inLine;

        		while (!(inLine = in.readLine()).equals("")) {
        			out.println(inLine);
        		}

        		socket.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	}
    }
    
    public static void main(String[] args) {
    	if (args.length != 1) {
    		System.out.println("Usage: java PiMoteServer [port]");
    		return;
    	}
    	
    	PiMoteServer server = new PiMoteServer();
    	server.run(Integer.parseInt(args[0]));
    }

    public void run(int port) {
    	try (ServerSocket serverSocket = new ServerSocket(port);) {
    		while (true) {
    			ClientThread thread = new ClientThread(serverSocket.accept());
    			thread.start();
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}
