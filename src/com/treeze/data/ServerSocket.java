package com.treeze.data;import java.net.Socket;public class ServerSocket {	static private ServerSocket serverSocket;	Socket socket;	public Socket getSocket() {		return socket;	}	public void setSocket(Socket socket) {		this.socket = socket;	}	private ServerSocket(){			}	static	public ServerSocket getInstance() {		if(serverSocket==null){			serverSocket = new ServerSocket();		}		return serverSocket;	}}