package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.FramesConnection;
import util.GUIUtil;

public class CanteenFrame extends JFrame implements FocusListener, ActionListener, WindowListener, Runnable {
	public JButton jb_tiantian = new JButton("Everyday Restaurant");
	public JButton jb_meishiyuan = new JButton("Delicacies Restaurant");
	public JButton jb_aoyun = new JButton("Olympic Restaurant ");
	public JButton jb_sansi = new JButton("ThreeAndFour Restaurant");
	public JButton jb_select = new JButton("Finder");

	private JTextArea taMsg = new JTextArea("Blow are chatting recordes:\n");
	// private JButton sendButton = new JButton("Send");
	private JScrollPane recordeJScrollPane = new JScrollPane();
	private JTextField chatTextField = new JTextField("Input your message here,please:");
	private JButton backmainButton = new JButton("Back");

	JPanel northPanel = new JPanel();
	JScrollPane dishJScrollPane = new JScrollPane();
	JPanel southPanel = new JPanel();

	DefaultListModel listModel = new DefaultListModel();
	JList dishList = null;
	private boolean isChinese;
	private boolean isServerException = false;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private Thread thread = null;
	private String account = null;
	private int dishcode = 0;
	private FinderBean fb;

	public CanteenFrame(FramesConnection fc) {
		this.isChinese = fc.isChinese();
		this.isServerException = fc.isServerException();
		this.socket = fc.getSocket();
		this.br = fc.getBr();
		this.ps = fc.getPs();
		this.account = fc.getAccount();
		if (isServerException) {
			JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
		}
		this.setTitle("tiantian");
		this.setLayout(new BorderLayout());

		northPanel.setLayout(new GridLayout(1, 5));
		northPanel.add(jb_tiantian);
		northPanel.add(jb_meishiyuan);
		northPanel.add(jb_aoyun);
		northPanel.add(jb_sansi);
		northPanel.add(jb_select);
		this.add(northPanel, BorderLayout.NORTH);
		// add elements to listModel
		listModel.addElement(new Object[] { new ImageIcon(" "), " ", " ", " ", " " });
		// create dishList though listModel

		dishList = new JList(listModel);
		// setCellRenderer for dishList by passing a parameter of DishLabel
		// object
		dishList.setCellRenderer(new DishLabel());

		// add the given dishList to the viewPort of dishJScrollPane
		dishJScrollPane.getViewport().add(dishList);
		this.add(dishJScrollPane, BorderLayout.CENTER);

		taMsg.setFont(new java.awt.Font("MingLiU", 1, 15));
		taMsg.setSelectedTextColor(Color.red);
		taMsg.setSize(400, 200);
		taMsg.setEditable(false);
		taMsg.setLineWrap(true);
		recordeJScrollPane.getViewport().add(taMsg);
		this.add(recordeJScrollPane, BorderLayout.EAST);

		southPanel.setLayout(new BorderLayout());
		chatTextField.setBackground(Color.yellow);
		chatTextField.setFont(new java.awt.Font("Bell MIT", 1, 13));
		southPanel.add(chatTextField, BorderLayout.CENTER);
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(1, 2));
		jPanel.add(backmainButton);
		// jPanel.add(sendButton);
		southPanel.add(jPanel, BorderLayout.SOUTH);

		this.add(southPanel, BorderLayout.SOUTH);

		this.setSize(1000, 600);
		// use a static tool method form GUIUtil class to have the frame be the
		// center of the
		// screen
		GUIUtil.toCenter(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);

		chatTextField.addFocusListener(this);
		jb_tiantian.addActionListener(this);
		jb_aoyun.addActionListener(this);
		jb_meishiyuan.addActionListener(this);
		jb_sansi.addActionListener(this);
		backmainButton.addActionListener(this);
		// sendButton.addActionListener(this);
		jb_select.addActionListener(this);
		this.addWindowListener(this);
		// set a anonymous inner listener class for chatTextField
		// which helps you send message by pressing enter key
		chatTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// if a sendMessage event happens, print a message to tell
					// the server
					ps.println("sendMessage");
					ps.println(account);
					ps.println(account + " saying: " + chatTextField.getText());
					chatTextField.setText("");

				}
			}
		});
		// add a window event for the frame

		thread = new Thread(this);
		thread.start();
		displayChatRecording();
		displayDishList("tiantian");
	}

	private void displayDishList(String canteen) {
		ps.println("displayDishList");
		ps.println(canteen);

	}

	private void displayChatRecording() {
		ps.println("displayChatRecording");

	}

	// the main function was created in order to test
	// it exists temporarily
	public static void main(String[] args) {
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
		// new CanteenFrame(new FramesConnection(isChinese, isServerException,
		// socket, br, ps, "cc"));
		// } catch (Exception e) {
		// isServerException = true;
		// System.out.println("exception, open your server, please!");
		// e.printStackTrace();
		// }
	}

	// implements action events for different components
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backmainButton) {
			ps.println("CanteenGoMainFrame");
		} else if (e.getSource() == jb_tiantian) {
			this.setTitle("tiantian");
			displayDishList("tiantian");
		} else if (e.getSource() == jb_meishiyuan) {
			this.setTitle("meishiyuan");
			displayDishList("meishiyuan");
		} else if (e.getSource() == jb_aoyun) {
			this.setTitle("aoyun");
			displayDishList("aoyun");
		} else if (e.getSource() == jb_sansi) {
			this.setTitle("sansi");
			displayDishList("sansi");
		} else if (e.getSource() == jb_select) {
			fb = new FinderBean();
			new Finder(this, fb);
			if (fb.isSearch()) {
				this.setTitle("Results");
				searchDish();
			}

		}

	}

	private void searchDish() {
		ps.println("searchDish");
		ps.println(fb.getDishName());
		ps.println(fb.getPrice1());
		ps.println(fb.getPrice2());
		ps.println(fb.getCanteenName());
		ps.println(fb.getSorting());

	}

	// execute run method to listen the message form server
	// do different things by different feedback from server
	public void run() {
		try {
			while (isVisible()) {
				String messageFromServer = br.readLine();
				if (messageFromServer == null) {
					JOptionPane.showMessageDialog(this, "server exception,sorry, you're forced to logout");
					System.exit(0);
				}
				if (messageFromServer.equals("sendMessageDone")) {
					appendMessage();
				} else if (messageFromServer.equals("CanteenFrameBack")) {
					this.dispose();
					new OperationFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("displayCRDone")) {
					initializeCR();
				} else if (messageFromServer.equals("displayDLDone")) {
					initializeDL();
				} else if (messageFromServer.equals("GoDishInfoFrameDone")) {
					this.dispose();
					new DishInfoFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account), dishcode);
				} else if (messageFromServer.equals("searchDLDone")) {
					showSearchDishlistResult();
				} else if (messageFromServer.equals("error")) {
					JOptionPane.showMessageDialog(this, " Server Exception", "error", JOptionPane.WARNING_MESSAGE);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showSearchDishlistResult() {
		try {
			String str = br.readLine();
			listModel.clear();
			while (str.equals("start")) {
				int dishcode = Integer.parseInt(br.readLine());
				if (dishcode == 1) {
					listModel.addElement(new Object[] { new ImageIcon(" "), "No Required Dishes Found", " ", " ", " " });
					dishList = new JList(listModel);
					dishList.setCellRenderer(new DishLabel());
					dishJScrollPane.getViewport().add(dishList);
					this.add(dishJScrollPane, BorderLayout.CENTER);
					return;
				}
				if (dishcode == 0) {
					createDishListByModel();
					return;
				}
				String dishName = br.readLine();
				int likeNumber = Integer.parseInt(br.readLine());
				double price = Double.parseDouble(br.readLine());
				String dishFaceFileName = br.readLine();
				// add elements to listModel
				listModel.addElement(new Object[] { new ImageIcon(dishFaceFileName), dishName, "价格：" + price + "元", "好评数：" + likeNumber, dishcode });
				// create dishList though listModel
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeDL() {
		try {
			String str = br.readLine();
			listModel.clear();
			while (str.equals("start")) {
				int dishcode = Integer.parseInt(br.readLine());
				if (dishcode == 0) {
					createDishListByModel();
					return;
				}
				String dishName = br.readLine();
				int likeNumber = Integer.parseInt(br.readLine());
				double price = Double.parseDouble(br.readLine());
				String dishFaceFileName = br.readLine();
				// add elements to listModel
				listModel.addElement(new Object[] { new ImageIcon(dishFaceFileName), dishName, "价格：" + price + "元", "好评数：" + likeNumber, dishcode });
				// create dishList though listModel
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createDishListByModel() {

		dishList = new JList(listModel);
		dishList.setCellRenderer(new DishLabel());
		dishJScrollPane.getViewport().add(dishList);
		this.add(dishJScrollPane, BorderLayout.CENTER);
		dishList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// if clicking on an item continuously two times, the following
				// event will happen
				if (e.getClickCount() == 2) {
					Object[] cell = (Object[]) dishList.getSelectedValue();
					dishcode = Integer.parseInt(cell[4].toString());
					ps.println("GoDishInfoFrame");
				}
			}

		});
	}

	private void initializeCR() {
		try {
			String str = br.readLine();
			while (str.equals("start")) {
				String message = br.readLine();
				if (message.equals("over")) {
					return;
				}
				taMsg.append(message + "\n");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void appendMessage() {

		try {
			String message = br.readLine();
			taMsg.append(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void windowClosing(WindowEvent e) {
		int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit", JOptionPane.YES_NO_CANCEL_OPTION);
		if (check == JOptionPane.YES_OPTION) {
			System.exit(0);
			try {
				br.close();
				ps.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void focusGained(FocusEvent e) {
		if (e.getSource() == chatTextField) {
			chatTextField.setText("");
		}

	}

	public void focusLost(FocusEvent e) {
		if (e.getSource() == chatTextField) {
			chatTextField.setText("Input your message here,please:");
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

}
