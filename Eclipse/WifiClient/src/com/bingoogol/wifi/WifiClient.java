/**
 * 客户端
 */
package com.bingoogol.wifi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.bingoogol.wifi.engine.Client;
import com.bingoogol.wifi.engine.Client.MessageListener;
import com.bingoogol.wifi.model.Message;
import com.bingoogol.wifi.util.DateUtil;

public class WifiClient extends JFrame implements ActionListener,KeyListener  {

	ServerSocket ss = null;
	Socket s = null;
	
	InputStreamReader isr = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	String infoFromServer = null;
	String response = null;
	
	JTextArea jta = null;
	JTextField jtf = null;
	JButton jb = null;
	JScrollPane jsp = null;
	JPanel jp = null;
	
	private Client mClient;
	
	public static void main(String[] args) {
		new WifiClient();
	}
	public WifiClient() {
		jta = new JTextArea();
		jta.setEditable(false);
		jsp = new JScrollPane(jta);
		
		jtf = new JTextField(15);
		jtf.addKeyListener(this);
		jb = new JButton("发送");
		jb.addActionListener(this);
		jb.addKeyListener(this);
		jb.setActionCommand("发送");
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jb);
		
		this.add(jsp,"Center");
		this.add(jp,"South");
		this.setTitle("客户端");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300,300);
		this.setVisible(true);
		
		mClient = new Client();
		mClient.setMessageListener(new MessageListener() {
			
			public void onMessage(int msg) {
				jta.append(" 服务器端  "+ DateUtil.date2String(new Date()) + "\r\n " + msg + "\r\n");
			}
			
			public void onMessage(String msg) {
				jta.append(" 服务器端  "+ DateUtil.date2String(new Date()) + "\r\n " + msg + "\r\n");
			}
		});
		mClient.start();

	}
	
	public void sendMessage() {
		response = jtf.getText();
		jta.append(" 客户端端  "+ DateUtil.date2String(new Date()) + "\r\n " + response + "\r\n");
		jtf.setText("");
		Message message = new Message();
		message.setMessageType(Message.STRINGVALUE);
		message.setStringValue(response);
		mClient.setMessage(message);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("发送")) {
			sendMessage();
		}
	}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			sendMessage();
		}
	}
	public void keyReleased(KeyEvent e) {}

}
