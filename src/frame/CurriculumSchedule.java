package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import util.FramesConnection;
import util.GUIUtil;

public class CurriculumSchedule extends JFrame implements ActionListener, WindowListener, Runnable {
	public JButton addCourse = new JButton("Sava");
	public JButton exit = new JButton("Exit");
	public JButton reverse = new JButton("Back");
	private boolean isChinese; // true for Chinese and false for English
	private boolean isServerException = false;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintStream ps = null;
	private Thread thread = null;
	private String account;
	JTable lecTable = null;
	String[][] cellData = { { "8:00--8:50--9:55", "", "", "", "", "", "", "" }, { "9:55--10:45--13:30", "", "", "", "", "", "", "" }, { "13:30--14:20--15:25", "", "", "", "", "", "", "" }, { "15:25--16:15--18:00", "", "", "", "", "", "", "" }, { "18:00--18:50--19:55", "", "", "", " ", "", "", "" }, { "19:55--20:45", "", "", "", " ", "", "", "" } };
	String[] colums = { "Times", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
	private boolean tag;// used for labeling the operations of uploading or
						// changing curriculum schedule

	public JPanel southPanel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(addCourse);
		southPanel.add(reverse);
		southPanel.add(exit);
		return southPanel;

	}

	public CurriculumSchedule(FramesConnection fc) {
		lecTable = new JTable(cellData, colums);
		lecTable.setRowHeight(34);
		lecTable.setDragEnabled(false);
		//
		TableColumn firsetColumn = lecTable.getColumnModel().getColumn(0);
		firsetColumn.setPreferredWidth(180);
		firsetColumn.setMaxWidth(180);
		firsetColumn.setMinWidth(180);
		for (int i = 1; i < colums.length; i++) {
			TableColumn Column = lecTable.getColumnModel().getColumn(i);
			Column.setPreferredWidth(150);
			Column.setMaxWidth(150);
			Column.setMinWidth(150);
		}
		//
		lecTable.setPreferredScrollableViewportSize(new Dimension(1020, 250));
		JScrollPane scrollPane = new JScrollPane(lecTable);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.getContentPane().add(southPanel(), BorderLayout.SOUTH);
		this.setTitle("University Curriculum Schedule");
		addCourse.setSize(20, 20);
		exit.setSize(20, 20);
		reverse.setSize(20, 20);
		exit.addActionListener(this);
		addCourse.addActionListener(this);
		reverse.addActionListener(this);
		this.addWindowListener(this);
		this.setVisible(true);
		this.setSize(920, 300);
		GUIUtil.toCenter(this);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.isChinese = fc.isChinese();
		this.isServerException = fc.isServerException();
		this.socket = fc.getSocket();
		this.br = fc.getBr();
		this.ps = fc.getPs();
		this.account = fc.getAccount();
		if (isServerException) {
			JOptionPane.showMessageDialog(this, "The server exception, restart your programme", "error", JOptionPane.WARNING_MESSAGE);
		}
		new Thread(this).start();
		displayCurriculumSchedule();

	}

	private void displayCurriculumSchedule() {

		ps.println("displayCurriculumSchedule");
		ps.println(account);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == reverse) {
			ps.println("CSGoMainFrame");
		} else if (e.getSource() == addCourse) {
			addCourse_actionPerformed();
		} else {
			int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit", JOptionPane.YES_NO_CANCEL_OPTION);
			if (check == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	private void addCourse_actionPerformed() {
		if (tag) {
			ps.println("SavaCurriculumSchedule");
		} else {
			ps.println("UpdateCurriculumSchedule");
			ps.println(account);
		}
		TableModel tableModel = lecTable.getModel();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				if (tag) {
					ps.println(account);
					ps.println((i + 1) * 10 + j + 1);
					ps.println((String) tableModel.getValueAt(i, j));
				} else {
					ps.println((String) tableModel.getValueAt(i, j));
					ps.println((i + 1) * 10 + j + 1);
				}
				// System.out.println((String) tableModel.getValueAt(i, j));
			}
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
				if (messageFromServer.equals("CurriculumScheduleBack")) {
					this.dispose();
					new OperationFrame(new FramesConnection(isChinese, isServerException, socket, br, ps, account));
				} else if (messageFromServer.equals("SavaCurriculumScheduleDone")) {
					JOptionPane.showMessageDialog(this, "Saved!");
					tag = false;
				} else if (messageFromServer.equals("UpdateCurriculumScheduleDone")) {
					JOptionPane.showMessageDialog(this, "Updated!");
				} else if (messageFromServer.equals("displayCSDone")) {
					initializeCS();
				} else if (messageFromServer.equals("error")) {
					JOptionPane.showMessageDialog(this, "error!");
				} else if (messageFromServer.equals("logout")) {
					// ....
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initializeCS() {
		try {
			String cellData = br.readLine();
			if (cellData.equals("null")) {
				tag = true;
				return;
			} else {
				tag = false;
			}
			TableModel tableModel = lecTable.getModel();
			tableModel.setValueAt(cellData, 0, 0);
			for (int j = 1; j < 8; j++) {
				cellData = br.readLine();
				tableModel.setValueAt(cellData, 0, j);
			}
			for (int i = 1; i < 6; i++) {
				for (int j = 0; j < 8; j++) {
					cellData = br.readLine();
					tableModel.setValueAt(cellData, i, j);

				}
			}
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

	// the main function was created in order to test
	// it exists temporarily
	public static void main(String[] args) {
		// try {
		// UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
		// } catch (UnsupportedLookAndFeelException e) {
		// e.printStackTrace();
		// }
		//
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
		// CurriculumSchedule cs = new CurriculumSchedule(new
		// FramesConnection(isChinese, isServerException, socket, br, ps,
		// "cc"));
		// } catch (Exception e) {
		// isServerException = true;
		// System.out.println("exception, open your server, please!");
		// e.printStackTrace();
		// }
	}

}
