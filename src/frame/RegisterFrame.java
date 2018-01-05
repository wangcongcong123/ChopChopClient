package frame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.GUIUtil;

public class RegisterFrame extends JFrame implements ActionListener, KeyListener, WindowListener, Runnable {

	private JMenuBar jmb = new JMenuBar();
	private JMenu jm;
	private JMenuItem jmi1;
	private JMenuItem jmi2;
	private JMenuItem jmi3;
	private JLabel lbAccount;
	private JTextField tfAccount;
	private JLabel lbPassword1;
	private JPasswordField pfPassword1;
	private JLabel lbPassword2;
	private JPasswordField pfPassword2;
	private JLabel lbName;
	private JTextField tfName;
	private JButton btLogin;
	private JButton btRegister;
	private JButton btExit;
	private JLabel nopd;
	private boolean isChinese; // true for Chinese and false for English
	private boolean isServerException = false;// check if server is exceptional
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;

	// construct the class
	public RegisterFrame(boolean version, Socket socket, BufferedReader br, PrintStream ps, boolean isServerException) {
		this.setLayout(new FlowLayout());
		if (version) {
			isChinese = true;
			nopd = new JLabel("******ª∂”≠Chop_Chop******");
			nopd.setForeground(Color.BLUE);
			nopd.setFont(new Font("TimeRoman", Font.BOLD, 14));
			this.setTitle("◊¢≤·");
			jm = new JMenu("…Ë÷√");
			jmi1 = new JMenuItem("”Ô—‘");
			jmi1.setToolTipText("English");
			jmi2 = new JMenuItem("◊÷ÃÂ");
			jmi3 = new JMenuItem("πÿ”⁄");
			jm.add(jmi1);
			jm.add(jmi2);
			jm.add(jmi3);
			lbAccount = new JLabel(" ‰»Î’À∫≈£∫");
			lbAccount.setForeground(Color.black);
			tfAccount = new JTextField(10);
			lbPassword1 = new JLabel(" ‰»Î√‹¬Î£∫");
			lbPassword1.setForeground(Color.black);
			pfPassword1 = new JPasswordField(10);
			lbPassword2 = new JLabel("»∑∂®√‹¬Î£∫");
			lbPassword2.setForeground(Color.black);
			pfPassword2 = new JPasswordField(10);
			lbName = new JLabel(" ‰»Î–’√˚£∫");
			lbName.setForeground(Color.black);
			tfName = new JTextField(10);
			btLogin = new JButton("µ«¬º");
			btRegister = new JButton("◊¢≤·");
			btExit = new JButton("ÕÀ≥ˆ");
		} else {
			isChinese = false;
			nopd = new JLabel("******Welcome to Chop_Chop******");
			nopd.setForeground(Color.BLUE);
			nopd.setFont(new Font("TimeRoman", Font.BOLD, 14));
			this.setTitle("Register");
			jm = new JMenu("Setup");
			jmi1 = new JMenuItem("Language");
			jmi1.setToolTipText("÷–Œƒ");
			jmi2 = new JMenuItem("Font");
			jmi3 = new JMenuItem("About");
			jm.add(jmi1);
			jm.add(jmi2);
			jm.add(jmi3);
			lbAccount = new JLabel(" Account:");
			lbAccount.setForeground(Color.black);
			tfAccount = new JTextField(10);
			lbPassword1 = new JLabel("PassOne:");
			lbPassword1.setForeground(Color.black);
			pfPassword1 = new JPasswordField(10);
			lbPassword2 = new JLabel("PassTwo:");
			lbPassword2.setForeground(Color.black);
			pfPassword2 = new JPasswordField(10);
			lbName = new JLabel("    Name:");
			lbName.setForeground(Color.black);
			tfName = new JTextField(10);
			btLogin = new JButton("Login");
			btRegister = new JButton("Register");
			btExit = new JButton("Exit");
		}

		this.setJMenuBar(jmb);
		jmb.add(jm);
		this.add(nopd);
		this.add(lbAccount);
		this.add(tfAccount);
		this.add(lbPassword1);
		this.add(pfPassword1);
		this.add(lbPassword2);
		this.add(pfPassword2);
		this.add(lbName);
		this.add(tfName);
		this.add(btLogin);
		this.add(btRegister);
		this.add(btExit);
		this.setSize(220, 300);
		GUIUtil.toCenter(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.getContentPane().setBackground(new Color(167, 184, 203));
		// add monitor to the object
		jmi1.addActionListener(this);
		btLogin.addActionListener(this);
		btRegister.addActionListener(this);
		btExit.addActionListener(this);
		tfName.addKeyListener(this);
		this.addWindowListener(this);
		this.socket = socket;
		this.br = br;
		this.ps = ps;
		this.isServerException = isServerException;
		if (isServerException) {
			JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
		}
		// start a thread to listen the information form the server in this
		// class
		new Thread(this).start();

	}

	// implement the Event Listener
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btRegister) {
			if (isServerException) {
				JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
			}
			Register();
		} else if (e.getSource() == btLogin) {
			// some corrections needed to be done here
			this.dispose();
			new LoginFrame(isChinese);
		} else if (e.getSource() == jmi1) {
			this.dispose();
			// change language types
			if (isChinese) {
				isChinese = false;
				new RegisterFrame(isChinese, socket, br, ps, isServerException);
			} else {
				isChinese = true;
				new RegisterFrame(isChinese, socket, br, ps, isServerException);
			}
		} else {
			int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit", JOptionPane.YES_NO_CANCEL_OPTION);
			if (check == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}

	}

	public void keyPressed(KeyEvent e) {
		// another event added in the name text area---press the enter key on
		// the keyboard which has the same effect with clicking on the
		// btRegister
		// button
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (isServerException) {
				JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
			}
			Register();
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

	public void windowOpened(WindowEvent e) {

	}

	// add a window closing event to give a make-sure message to the user
	// telling them if they really want to exit the program
	public void windowClosing(WindowEvent e) {
		int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit", JOptionPane.YES_NO_CANCEL_OPTION);
		if (check == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {

	}

	public void windowDeiconified(WindowEvent e) {

	}

	public void windowActivated(WindowEvent e) {

	}

	public void windowDeactivated(WindowEvent e) {

	}

	// in run method of thread, its main job is to get the feedback from the
	// server
	public void run() {
		try {
			while (true) {
				// read a message From Server in each time of while loop
				// to do different things according to the different messages
				// read each time from server
				String messageFromServer = br.readLine();
				if (messageFromServer == null) {
					JOptionPane.showMessageDialog(this, "server exception,sorry, you're forced to logout");
					System.exit(0);
				}
				if (messageFromServer.equals("successfully!")) {
					JOptionPane.showMessageDialog(this, "RegisterSuccessfully!");
				} else if (messageFromServer.equals("TwoPasswordsDifferent")) {
					JOptionPane.showMessageDialog(this, "Two passwords different!");
				} else if (messageFromServer.equals("NoCompletion")) {
					JOptionPane.showMessageDialog(this, "Fill in all information,please");
				} else if (messageFromServer.equals("AccountExisted")) {
					JOptionPane.showMessageDialog(this, "Account existing");
				} else if (messageFromServer.equals("error")) {
					JOptionPane.showMessageDialog(this, "error,try again!");
				} else if (messageFromServer.equals("logout")) {

					// waiting to add
					// int option = JOptionPane.showConfirmDialog(this,
					// "are you sure to exit");
					// br.close();
					// ps.close();
					// socket.close();
					// System.exit(0);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Register method to send pieces of information required during register to
	// server
	private void Register() {
		String account = new String(tfAccount.getText());
		String password1 = new String(pfPassword1.getPassword());
		String password2 = new String(pfPassword2.getPassword());
		String name = new String(tfName.getText());
		ps.println("register"); // send the login message to server
		ps.println(account); // send account to server
		ps.println(password1); // send password1 to server
		ps.println(password2); // send password2 to server
		ps.println(name); // send name to server

	}
}