package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.FramesConnection;

public class DishInfoFrame extends JFrame implements ActionListener, FocusListener, WindowListener, KeyListener, Runnable {
	private JPanel dishPanel = null;
	private JLabel dishimageLabel = null;
	private JLabel dishnameLabel = null;
	private JLabel dishpriceLabel = null;
	private JLabel dishlikeLabel = null;
	private int likeNumber;
	private JButton dishlikeButton = null;

	private JLabel dishInfoTitileLabel = null;

	private JTextArea dishInfoArea = null;
	private JScrollPane dishInfoScollPane = null;
	private String dishInfo = null;

	private JTextArea comments = null;
	private JScrollPane commentScrollPane = null;

	private JTextField commentTextField = null;

	private JButton back = new JButton("Back");
	private JButton exit = new JButton("Exit");
	private boolean isChinese;
	private boolean isServerException = false;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private Thread thread = null;
	private String account = null;
	private int dishcode = 0;

	public DishInfoFrame(FramesConnection fc, int dishcode) {
		this.isChinese = fc.isChinese();
		this.isServerException = fc.isServerException();
		this.socket = fc.getSocket();
		this.br = fc.getBr();
		this.ps = fc.getPs();
		this.account = fc.getAccount();
		if (isServerException) {
			JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
		}
		this.dishcode = dishcode;

		dishPanel = new JPanel();
		dishPanel.setLayout(null);
		dishPanel.setBackground(Color.white);
		dishPanel.setPreferredSize(new Dimension(480, 800));

		dishimageLabel = new JLabel();
		dishimageLabel.setBounds(110, 0, 150, 90);
		dishPanel.add(dishimageLabel);

		dishnameLabel = new JLabel();
		dishnameLabel.setBounds(270, 0, 120, 30);
		dishnameLabel.setFont(new java.awt.Font("Dialog", 1, 23));
		dishPanel.add(dishnameLabel);

		dishpriceLabel = new JLabel();
		dishpriceLabel.setBounds(270, 30, 120, 30);
		dishpriceLabel.setForeground(Color.RED);
		dishPanel.add(dishpriceLabel);

		likeNumber = 40;
		dishlikeLabel = new JLabel();
		dishlikeLabel.setBounds(270, 60, 120, 30);
		dishPanel.add(dishlikeLabel);

		dishlikeButton = new JButton("like");
		dishlikeButton.setBounds(390, 60, 60, 25);
		dishPanel.add(dishlikeButton);

		dishInfoTitileLabel = new JLabel();
		dishInfoTitileLabel.setBounds(0, 100, 280, 30);
		dishInfoTitileLabel.setFont(new java.awt.Font("Dialog", 1, 23));
		dishPanel.add(dishInfoTitileLabel);

		dishInfoArea = new JTextArea();
		dishInfoArea.setLineWrap(true);
		dishInfoArea.setEditable(false);
		dishInfoArea.setSelectedTextColor(Color.RED);
		dishInfoArea.setFont(new java.awt.Font("Dialog", 1, 15));
		dishInfoArea.setCaretPosition(dishInfoArea.getText().length());// ?
		dishInfoScollPane = new JScrollPane(dishInfoArea);
		dishInfoScollPane.setBounds(0, 150, 480, 300);
		dishPanel.add(dishInfoScollPane);

		dishInfo = " ";
		dishInfoArea.append(dishInfo);

		comments = new JTextArea("以下是聊天记录:\n");
		comments.setEditable(false);
		comments.setSelectedTextColor(Color.RED);
		comments.setFont(new java.awt.Font("Dialog", 1, 15));
		comments.setCaretPosition(comments.getText().length());
		comments.setLineWrap(true);
		commentScrollPane = new JScrollPane(comments);
		commentScrollPane.setBounds(0, 460, 480, 170);
		dishPanel.add(commentScrollPane);

		commentTextField = new JTextField("comments her:");
		commentTextField.setBackground(Color.yellow);
		commentTextField.setBounds(0, 625, 480, 25);
		commentTextField.setFont(new java.awt.Font("Dialog", 1, 13));
		dishPanel.add(commentTextField);

		back.setBounds(120, 650, 120, 25);
		dishPanel.add(back);
		exit.setBounds(240, 650, 120, 25);
		dishPanel.add(exit);

		this.add(dishPanel);
		this.setSize(480, 700);
		this.setLocation(500, 0);
		this.setTitle(" ");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);

		dishlikeButton.addActionListener(this);
		back.addActionListener(this);
		exit.addActionListener(this);
		commentTextField.addFocusListener(this);
		commentTextField.addKeyListener(this);

		this.addWindowListener(this);
		thread = new Thread(this);
		thread.start();
		initializeDishInfo(this.dishcode);
		displayDishComments();
	}

	private void initializeDishInfo(int dishcode) {
		ps.println("initializeDishInfo");
		ps.println(dishcode);

	}

	private void displayDishComments() {
		ps.println("displayDishComments");
		ps.println(dishcode);
	}

	public void run() {

		try {
			while (isVisible()) {
				String messageFromServer = br.readLine();
				if (messageFromServer == null) {
					JOptionPane.showMessageDialog(this, "server exception,sorry, you're forced to logout");
					System.exit(0);
				}
				if (messageFromServer.equals("sendCommentDone")) {
					appendComment();
				} else if (messageFromServer.equals("DishInfoFrameBack")) {
					this.dispose();
					new CanteenFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("displayDCDone")) {
					initializeDC();
				} else if (messageFromServer.equals("ClickDishLikeButtonDone")) {
					JOptionPane.showMessageDialog(this, "Thumb-up Given Successfully!");
					changeDishLikeLabel();
				} else if (messageFromServer.equals("displayDishInfoDone")) {
					setDishInfo();
				} else if (messageFromServer.equals("error")) {
					JOptionPane.showMessageDialog(this, " Server Exception", "error", JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setDishInfo() {
		try {

			String canteenNameString = br.readLine();
			String dishName = br.readLine();
			likeNumber = Integer.parseInt(br.readLine());
			double price = Double.parseDouble(br.readLine());
			String introduction = br.readLine();
			String dishFaceFileName = br.readLine();
			this.setTitle(canteenNameString);
			setIcon(dishFaceFileName, dishimageLabel);
			dishnameLabel.setText(dishName);
			dishpriceLabel.setText("￥" + price);
			dishlikeLabel.setText("好评数： " + likeNumber);
			dishInfoTitileLabel.setText(dishName + "的简介：");
			dishInfoArea.setText(introduction);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void changeDishLikeLabel() {
		try {
			likeNumber = Integer.parseInt(br.readLine());
			dishlikeLabel.setText("好评数： " + likeNumber);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeDC() {
		try {
			String str = br.readLine();
			while (str.equals("start")) {
				String comment = br.readLine();
				if (comment.equals("over")) {
					return;
				}
				comments.append(comment + "\n");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void appendComment() {
		try {
			String comment = br.readLine();
			comments.append(comment + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == back) {
			ps.println("DishInfoGoCanteenFrame");
		} else if (e.getSource() == exit) {
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
		} else if (e.getSource() == dishlikeButton) {
			ps.println("ClickDishLikeButton");
			ps.println(dishcode);
			ps.println(likeNumber);

		}
	}

	public void focusGained(FocusEvent e) {
		commentTextField.setText("");
	}

	public void focusLost(FocusEvent e) {
		commentTextField.setText("comments her:");
	}

	// for testing
	public static void main(String[] args) {
		// try {
		// UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
		// } catch (UnsupportedLookAndFeelException e) {
		// e.printStackTrace();
		// }
		// boolean isChinese = true; // true for Chinese and false for English
		// boolean isServerException = false;
		// Socket socket = null;
		// BufferedReader br = null;
		// PrintStream ps = null;
		//
		// try {
		// socket = new Socket("127.0.0.1", 9999);
		// br = new BufferedReader(new
		// InputStreamReader(socket.getInputStream()));
		// ps = new PrintStream(socket.getOutputStream());
		// DishInfoFrame cTiantian = new DishInfoFrame(new
		// FramesConnection(isChinese, isServerException, socket, br, ps, "cc"),
		// 106);
		// } catch (Exception e) {
		// isServerException = true;
		// System.out.println("exception, open your server, please!");
		// e.printStackTrace();
		// }
	}

	public void setIcon(String filename, JLabel jbJLabel) {
		ImageIcon imageIcon = new ImageIcon(filename);
		Image temp = imageIcon.getImage().getScaledInstance(jbJLabel.getWidth(), jbJLabel.getHeight(), imageIcon.getImage().SCALE_DEFAULT);
		imageIcon = new ImageIcon(temp);
		jbJLabel.setIcon(imageIcon);
	}

	public void windowClosing(WindowEvent e) {
		int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit", JOptionPane.YES_NO_CANCEL_OPTION);
		if (check == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// if a sendMessage event happens, print a message to tell
			// the server
			ps.println("sendComment");
			ps.println(dishcode);
			ps.println(account);
			ps.println(account + " saying: " + commentTextField.getText());
			commentTextField.setText("");
		}

	}

	public void windowOpened(WindowEvent e) {

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

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

}