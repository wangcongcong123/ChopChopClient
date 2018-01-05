package frame;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.FramesConnection;
import util.GUIUtil;

public class LoginFrame extends JFrame implements MouseListener, KeyListener, WindowListener, Runnable {
	// private JLabel lbAccount;
	private JTextField tfAccount;
	// private JLabel lbPassword;
	private JMenuBar jmb = new JMenuBar();
	private JMenu jm;
	private JMenuItem jmi1;
	private JMenuItem jmi2;
	private JMenuItem jmi3;
	// private JButton btLanguage;
	private JLabel Login = new JLabel();
	private JLabel Register = new JLabel();
	private JPasswordField pfPassword;
	private boolean isChinese;
	private boolean isServerException = false;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private Thread thread = null;
	private String account;
	private String password;

	public LoginFrame(boolean version) {

		this.setLayout(null);
		// version is used to check if language is Chinese or not
		tfAccount = new JTextField(400);
		tfAccount.setBounds(240, 165, 400, 50);
		pfPassword = new JPasswordField(300);
		pfPassword.setBounds(240, 260, 400, 50);
		if (version) {
			isChinese = true;
			this.setTitle("登录");
			jm = new JMenu("设置");
			jmi1 = new JMenuItem("语言");
			jmi1.setToolTipText("English");
			jmi2 = new JMenuItem("字体");
			jmi3 = new JMenuItem("关于");
			jm.add(jmi1);
			jm.add(jmi2);
			jm.add(jmi3);
			pfPassword.addKeyListener(this);
			pfPassword.setEchoChar('*');
			// lbPassword = new JLabel("输入密码");
			// lbPassword.setForeground(Color.black);
		} else {
			// English
			// create objects of the components on the LoginFrame
			isChinese = false;
			this.setTitle("Login");
			jm = new JMenu("Setup");
			jmi1 = new JMenuItem("Language");
			jmi1.setToolTipText("中文");
			jmi2 = new JMenuItem("Font");
			jmi3 = new JMenuItem("About");
			jm.add(jmi1);
			jm.add(jmi2);
			jm.add(jmi3);
			pfPassword.addKeyListener(this);
			pfPassword.setEchoChar('*');

		}
		Login.setBounds(220, 350, 200, 50);
		Register.setBounds(440, 350, 200, 50);
		setIcon("image/labelicons/LOGIN1.PNG", Login);
		setIcon("image/labelicons/REGISTER1.PNG", Register);
		GUIUtil.SetBackground(this, "image/backgrounds/LOGINFRAME.PNG");
		this.setJMenuBar(jmb);
		jmb.add(jm);
		// this.add(lbAccount);
		this.add(tfAccount);
		// this.add(lbPassword);
		this.add(pfPassword);
		this.add(Login);
		this.add(Register);
		// this.add(Exit);
		this.setSize(850, 600);
		GUIUtil.toCenter(this);
		// set the DefaultCloseOperation of the frame be DO_NOTHING_ON_CLOSE
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.getContentPane().setBackground(new Color(167, 184, 203));
		Login.addMouseListener(this);
		Register.addMouseListener(this);
		// Exit.addMouseListener(this);
		jmi1.addMouseListener(this);
		// add a window event for the frame
		this.addWindowListener(this);
		try {
			// create a socket connecting to the server
			socket = new Socket("127.0.0.1", 9999);
			// get the input and output stream form the server
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ps = new PrintStream(socket.getOutputStream());
			// start a thread to listen the information form the server in this
			// class
			thread = new Thread(this);
			thread.start();
			// check if the server is exceptional
		} catch (Exception e) {
			isServerException = true;
			JOptionPane.showMessageDialog(this, "The server exception, restart your programme after starting your server", "error", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}

	}

	public void setIcon(String filename, JLabel jbJLabel) {
		ImageIcon imageIcon = new ImageIcon(filename);
		Image temp = imageIcon.getImage().getScaledInstance(jbJLabel.getWidth(), jbJLabel.getHeight(), imageIcon.getImage().SCALE_DEFAULT);
		imageIcon = new ImageIcon(temp);
		jbJLabel.setIcon(imageIcon);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		// another event added in the password text area-press the enter key on
		// the keyboard which has the same effect with cliking on the btlogin
		// button
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (isServerException) {
				JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
			}
			login();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	// the main method is generated for testing
	public static void main(String[] args) {
		// new LoginFrame(false);
	}

	// in run method of thread, its main job is to get the feedback from the
	// server
	public void run() {
		try {
			while (isVisible()) {
				// read a message From Server in each time of while loop
				// to do different things according to the different messages
				// read each time from server
				String messageFromServer = br.readLine();
				if (messageFromServer == null) {
					JOptionPane.showMessageDialog(this, "server exception,sorry, you're forced to logout");
					System.exit(0);
				}
				if (messageFromServer.equals("successfully")) {
					JOptionPane.showMessageDialog(this, "successfully");
					this.dispose();
					new OperationFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("RegisterFrame")) {
					this.dispose();
					new RegisterFrame(isChinese, socket, br, ps, isServerException);
				} else if (messageFromServer.equals("PasswordWrong")) {
					JOptionPane.showMessageDialog(this, "either password or account is wrong, try again!");
				} else if (messageFromServer.equals("error")) {
					JOptionPane.showMessageDialog(this, "error,try again!");
				} else if (messageFromServer.equals("AlreadyLogined")) {
					JOptionPane.showMessageDialog(this, "Hei,bully~you already logined and cannot login again");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// login method to send pieces of information required during login to
	// server
	private void login() {
		account = new String(tfAccount.getText());
		password = new String(pfPassword.getPassword());
		ps.println("login"); // send the login message to server
		ps.println(account); // send account to server
		ps.println(password); // send password to server
	}

	public void windowOpened(WindowEvent e) {

	}

	// add a window closing event to give a make-sure message to the user
	// telling them if they really want to exit the program
	public void windowClosing(WindowEvent e) {
		int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit", JOptionPane.YES_NO_CANCEL_OPTION);
		if (check == JOptionPane.YES_OPTION) {
			System.exit(0);
			try {
				br.close();
				ps.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == Login) {
			// if the server doesn't open , giving a message to remind the user
			if (isServerException) {
				JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
			}
			// if an Actionevent happened to the btLogin, execute the login
			// method declared in this class
			login();
		} else if (e.getSource() == Register) {
			// if clicking on the btRigster button, sending a message to the
			// server to inform it
			ps.println("GoRegisterFrame");
		} else if (e.getSource() == jmi1) {
			this.dispose();
			// change language types
			if (isChinese) {
				isChinese = false;
				new LoginFrame(isChinese);
			} else {
				isChinese = true;
				new LoginFrame(isChinese);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == Login) {
			setIcon("image/labelicons/LOGIN2.PNG", Login);
		} else if (e.getSource() == Register) {
			setIcon("image/labelicons/REGISTER2.PNG", Register);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (e.getSource() == Login) {
			setIcon("image/labelicons/LOGIN1.PNG", Login);
		} else if (e.getSource() == Register) {
			setIcon("image/labelicons/REGISTER1.PNG", Register);
		}
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

}
