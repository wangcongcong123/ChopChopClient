package frame;

import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.FaceFilter;
import util.FramesConnection;
import util.GUIUtil;
import video.PlayMain;

public class OperationFrame extends JFrame implements MouseListener, Runnable, WindowListener, ActionListener {

	private JLabel btPortrait = new JLabel();
	private JLabel lbGreet = new JLabel("<HTML><U>Hi, XXX</U></HTML>");
	private JLabel btMap = new JLabel();
	private JLabel btCanteen = new JLabel();
	private JLabel btCourseTable = new JLabel();

	private JComboBox cbHome = new JComboBox();
	private JPanel jp = new JPanel();
	private JLabel jbJLabel;
	private JLabel[] jbJlaJLabels = new JLabel[20];
	private int x = 80;
	private int y = 500;
	private int y1 = 500;
	private boolean isChinese; // true for Chinese and false for English
	private boolean isServerException = false;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private Thread thread = null;
	private String account;
	private boolean tag;// used for labeling the operations of uploading
	JMenuItem[] menuItem = new JMenuItem[5];
	private String name = null;
	private String password = null;
	private String outfacefile = null;
	UserBean ub = new UserBean();

	// or changing face
	public OperationFrame(FramesConnection fc) {
		createMenuBar();
		btPortrait.setBackground(Color.WHITE);
		btPortrait.setOpaque(true);
		btPortrait.addMouseListener(this);
		jbJLabel = new JLabel();
		jbJLabel.setBounds(590, 10, 75, 50);
		setIcon("image/labelicons/playerlabel1.png", jbJLabel);
		this.add(jbJLabel);
		Container c = getContentPane();
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		c.add(jp);
		jp.setLayout(null);
		cbHome.addItem("English");
		cbHome.addItem("中文");
		cbHome.setBounds(630, 430, 50, 25);

		lbGreet.setBounds(15, 50, 120, 60);
		btPortrait.setSize(60, 60);
		btPortrait.setLocation(10, 10);

		btMap.setLocation(300, 80);
		btMap.setSize(200, 50);

		btCourseTable.setLocation(300, 190);
		btCourseTable.setSize(200, 50);

		btCanteen.setLocation(300, 330);
		btCanteen.setSize(200, 50);
		setIcon("image/labelicons/f1.png", btMap);
		setIcon("image/labelicons/f2.png", btCourseTable);
		setIcon("image/labelicons/f3.png", btCanteen);
		jp.add(cbHome);
		jp.add(btCanteen);
		jp.add(btCourseTable);
		jp.add(btMap);
		jp.add(btPortrait);
		jp.add(lbGreet);
		GUIUtil.SetBackground(this, "image/backgrounds/background.jpg");
		this.add(jp);
		this.setSize(680, 500);
		GUIUtil.toCenter(this);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		jbJLabel.addMouseListener(this);
		btMap.addMouseListener(this);
		btCourseTable.addMouseListener(this);
		btCanteen.addMouseListener(this);
		jbJLabel.addMouseListener(this);
		this.addWindowListener(this);
		this.isChinese = fc.isChinese();
		this.isServerException = fc.isServerException();
		this.socket = fc.getSocket();
		this.br = fc.getBr();
		this.ps = fc.getPs();
		this.account = fc.getAccount();
		if (isServerException) {
			JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
		}

		displayFaceAndName();
		new Thread(this).start();
		new TimeThread(this).start();
	}

	private void displayFaceAndName() {
		ps.println("DisplayFaceAndName");
		ps.println(account);
	}

	public void setIcon(String filename, JLabel jbJLabel) {
		ImageIcon imageIcon = new ImageIcon(filename);
		Image temp = imageIcon.getImage().getScaledInstance(jbJLabel.getWidth(), jbJLabel.getHeight(), imageIcon.getImage().SCALE_DEFAULT);
		imageIcon = new ImageIcon(temp);
		jbJLabel.setIcon(imageIcon);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == jbJLabel) {
			new PlayMain();

		} else if (e.getSource() == btMap) {
			ps.println("GoMapFrame");
		} else if (e.getSource() == btCourseTable) {
			ps.println("GoCSFrame");
		} else if (e.getSource() == btCanteen) {
			ps.println("GoCanteenFrame");
		} else if (e.getSource() == btPortrait) {
			try {
				JFileChooser jfc = new JFileChooser();
				if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					boolean flag = new FaceFilter().accept(jfc.getSelectedFile());
					if (flag) {
						setIcon(jfc.getSelectedFile().toString(), btPortrait);
						if (tag) {
							ps.println("UploadFace");
							ps.println(account);
							ps.println(jfc.getSelectedFile().toString());
						} else {
							ps.println("UpdateFace");
							ps.println(account);
							ps.println(jfc.getSelectedFile().toString());
						}
					} else {
						JOptionPane.showMessageDialog(this, "error,the picture's format is wrong!");
					}
				}
			} catch (HeadlessException e1) {
				JOptionPane.showMessageDialog(this, "error,unable to download the picture!");
			}
		}

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btMap) {
			setIcon("image/labelicons/f1-1.png", btMap);
			btMap.setLocation(300, 90);
		} else if (e.getSource() == btCourseTable) {
			setIcon("image/labelicons/f2-2.png", btCourseTable);
			btCourseTable.setLocation(300, 200);
		} else if (e.getSource() == btCanteen) {
			setIcon("image/labelicons/f3-3.png", btCanteen);
			btCanteen.setLocation(300, 340);
		} else if (e.getSource() == jbJLabel) {
			setIcon("image/labelicons/playerlabel2.png", jbJLabel);
		}

	}

	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btMap) {
			setIcon("image/labelicons/f1.png", btMap);
			btMap.setLocation(300, 80);
		} else if (e.getSource() == btCourseTable) {
			setIcon("image/labelicons/f2.png", btCourseTable);
			btCourseTable.setLocation(300, 190);
		} else if (e.getSource() == btCanteen) {
			setIcon("image/labelicons/f3.png", btCanteen);
			btCanteen.setLocation(300, 330);
		} else if (e.getSource() == jbJLabel) {
			setIcon("image/labelicons/playerlabel1.png", jbJLabel);
		}
	}

	public void run() {

		try {
			while (isVisible()) {
				String messageFromServer = br.readLine();
				if (messageFromServer == null) {
					JOptionPane.showMessageDialog(this, "server exception,sorry, you're forced to logout");
					System.exit(0);
				}
				if (messageFromServer.equals("displayFaceAndNameDone")) {
					initializeFaceAndDone();
				} else if (messageFromServer.equals("MapFrame")) {
					this.dispose();
					new MapFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("CurriculumScheduleFrame")) {
					this.dispose();
					new CurriculumSchedule(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("Canteen_tiantianFrame")) {
					this.dispose();
					new CanteenFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("error")) {
					JOptionPane.showMessageDialog(this, "error!");
				} else if (messageFromServer.equals("UpdateFaceSucessfully")) {
					JOptionPane.showMessageDialog(this, "UpdateFaceSucessfully!");
				} else if (messageFromServer.equals("UploadFaceSucessfully")) {
					JOptionPane.showMessageDialog(this, "UploadFaceSucessfully!");// do
																					// something
																					// else
				} else if (messageFromServer.equals("ModifyUserInfoDone")) {
					JOptionPane.showMessageDialog(this, "updated successfully");
				}
			}

		} catch (

		IOException e)

		{
			e.printStackTrace();
		}

	}

	private void initializeFaceAndDone() {
		try {
			name = br.readLine();
			password = br.readLine();
			outfacefile = br.readLine();
			if (outfacefile.equals("null")) {
				tag = true;
			} else {
				tag = false;
			}
			// String outfacefile = "congcon.gif";
			// InputStream in = socket.getInputStream();
			// // 创建图片字节流
			// FileOutputStream fos = new FileOutputStream(outfacefile);
			// byte[] buf = new byte[1024];
			// int len = 0;
			// // 往字节流里写图片数据
			// while ((len = in.read(buf)) != -1) {
			// fos.write(buf, 0, len);
			// }
			// fos.close();
			// in.close();
			// br = new BufferedReader(new InputStreamReader(in));
			lbGreet.setText("<HTML><U>Hi, " + name + "</U></HTML>");
			setIcon(outfacefile, btPortrait);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		String[] menuArr = { "Setting(S)", "Help(H)" };
		String[][] menuItemArr = { { "Show(S)", "Modify(E)", "Login(Li)", "Logout(Lo)" }, { "About" } };
		int k = 0;
		for (int i = 0; i < menuArr.length; i++) {
			JMenu menu = new JMenu(menuArr[i]);
			ImageIcon imageIcon = new ImageIcon("image/menuicons/" + "m" + (i + 1) + ".png");
			Image temp = imageIcon.getImage().getScaledInstance(10, 10, imageIcon.getImage().SCALE_DEFAULT);
			imageIcon = new ImageIcon(temp);
			menu.setIcon(imageIcon);
			// menu.setIcon(new ImageIcon("m1.png"));
			for (int j = 0; j < menuItemArr[i].length; j++) {
				menuItem[k] = new JMenuItem(menuItemArr[i][j]);
				// menuItem[k].setIcon(new ImageIcon("m" + j + 3 + ".png"));
				if (i == 0) {
					imageIcon = new ImageIcon("image/menuicons/" + "m" + (j + 3) + ".png");
				} else {
					imageIcon = new ImageIcon("image/menuicons/" + "m" + (j + 7) + ".png");
				}
				temp = imageIcon.getImage().getScaledInstance(10, 10, imageIcon.getImage().SCALE_DEFAULT);
				imageIcon = new ImageIcon(temp);
				menuItem[k].setIcon(imageIcon);
				menuItem[k].addActionListener(this);
				menu.add(menuItem[k]);
				k++;
			}
			menuBar.add(menu);
		}
		this.setJMenuBar(menuBar);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuItem[0]) {
			new ShowDialog(this, account, name, outfacefile);
		} else if (e.getSource() == menuItem[1]) {
			ub = new UserBean();
			new ModifyDialog(this, ub, account, password);
			if (ub.isModified()) {
				ModifyUser();
			}
		} else if (e.getSource() == menuItem[2]) {
			System.out.println(menuItem[2].getText());
		} else if (e.getSource() == menuItem[3]) {
			System.out.println(menuItem[3].getText());
		} else if (e.getSource() == menuItem[4]) {

			new AboutOurApp(this);
		}
	}

	private void ModifyUser() {
		ps.println("ModifyUserInfo");
		ps.println(account);
		ps.println(ub.getPassword());
		ps.println(ub.getName());
		lbGreet.setText("<HTML><U>Hi, " + ub.getName() + "</U></HTML>");
		password = ub.getPassword();
	}

	public void windowOpened(WindowEvent e) {

	}

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

}
