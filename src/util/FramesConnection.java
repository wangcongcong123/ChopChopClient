package util;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 
 * this class is used to stored the main connection parameters between
 * interfaces in the client including the uses' accounts, streams and language
 * version, like these sorts of information
 **/
public class FramesConnection {

	private boolean isChinese;
	private boolean isServerException = false;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private String account;

	public FramesConnection(boolean isChinese, boolean isServerException, Socket socket, BufferedReader br, PrintStream ps, String account) {
		this.isChinese = isChinese;
		this.isServerException = isServerException;
		this.socket = socket;
		this.br = br;
		this.ps = ps;
		this.account = account;
	}

	public boolean isChinese() {
		return isChinese;
	}

	public void setChinese(boolean isChinese) {
		this.isChinese = isChinese;
	}

	public boolean isServerException() {
		return isServerException;
	}

	public void setServerException(boolean isServerException) {
		this.isServerException = isServerException;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getBr() {
		return br;
	}

	public void setBr(BufferedReader br) {
		this.br = br;
	}

	public PrintStream getPs() {
		return ps;
	}

	public void setPs(PrintStream ps) {
		this.ps = ps;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
