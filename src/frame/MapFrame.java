package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import util.FramesConnection;
import util.GUIUtil;

public class MapFrame extends JFrame
		implements ActionListener, ItemListener, MouseListener, MouseMotionListener, WindowListener, Runnable {

	private JComboBox jcbComboBox = new JComboBox();
	private JButton mainButton = new JButton("back");
	private JButton shrinkBtn = new JButton("shrink");
	private JButton zoomBtn = new JButton("zoom");
	private JButton exitButton = new JButton("exit");
	private ImageIcon imageIcon5;
	private ImageIcon imageIcon6;
	boolean flag = true;
	JLabel imageJLabel;
	private int jbx1 = 624;
	private int jby1 = 392;
	private int dx1 = 121;
	private int dy1 = 33;
	private int jbx2 = 890;
	private int jby2 = 372;
	private int dx2 = 95;
	private int dy2 = 21;
	private int x;
	private int y;
	FramesConnection fc = null;

	public MapFrame(FramesConnection fc) {
		this.fc = fc;
		this.setTitle("Campus Map");
		this.setLayout(new BorderLayout());
		imageIcon5 = new ImageIcon("image/maps/Ldpic5.jpg");
		imageJLabel = new JLabel(imageIcon5);

		JScrollPane jsScrollPane = new JScrollPane();
		jsScrollPane.setLayout(new ScrollPaneLayout());
		jsScrollPane.getViewport().add(imageJLabel);
		this.add(jsScrollPane, BorderLayout.CENTER);
		// jButton.setLocation(624, 392);
		JPanel myJPanel = new JPanel();
		myJPanel.setLayout(new GridLayout(1, 5));
		myJPanel.add(mainButton);
		myJPanel.add(exitButton);
		myJPanel.add(shrinkBtn);
		myJPanel.add(zoomBtn);
		jcbComboBox.addItem("classic");
		jcbComboBox.addItem("cute");
		myJPanel.add(jcbComboBox);
		this.add(myJPanel, BorderLayout.NORTH);
		mainButton.addActionListener(this);
		exitButton.addActionListener(this);
		shrinkBtn.addActionListener(this);
		zoomBtn.addActionListener(this);
		jcbComboBox.addItemListener(this);
		imageJLabel.addMouseListener(this);
		imageJLabel.addMouseMotionListener(this);
		this.addWindowListener(this);
		this.setSize(700, 700);
		GUIUtil.toCenter(this);
		this.setResizable(false);
		this.getContentPane().setBackground(new Color(167, 184, 203));
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		new Thread(this).start();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (jcbComboBox.getSelectedItem().equals(jcbComboBox.getItemAt(0))) {
				imageIcon5 = new ImageIcon("image/maps/Ldpic5.jpg");
				Image originalImage = imageIcon5.getImage();
				imageJLabel.setIcon(new ImageIcon(originalImage));
				jbx1 = 624;
				jby1 = 392;
				dx1 = 121;
				dy1 = 33;
				jbx2 = 890;
				jby2 = 372;
				dx2 = 95;
				dy2 = 21;
				flag = true;
			} else {
				imageIcon6 = new ImageIcon("image/maps/Ldpic6.jpg");
				Image originalImage = imageIcon6.getImage();
				imageJLabel.setIcon(new ImageIcon(originalImage));
				jbx1 = 316;
				jby1 = 334;
				dx1 = 57;
				dy1 = 22;
				flag = false;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mainButton) {
			fc.getPs().println("MapGoMainFrame");
		} else if (e.getSource() == exitButton) {
			int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (check == JOptionPane.YES_OPTION) {
				System.exit(0);
				try {
					fc.getBr().close();
					fc.getPs().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		} else if (e.getSource() == shrinkBtn) {
			scaleImage(0.5);
		} else if (e.getSource() == zoomBtn) {
			scaleImage(2);
		}

	}

	private void scaleImage(double d) {
		Image originalImage = null;
		if (flag) {
			if (d == 2.0) {
				jbx1 = jbx1 * 2;
				jby1 = jby1 * 2;
				dx1 = dx1 * 2;
				dy1 = dy1 * 2;
				jbx2 = jbx2 * 2;
				jby2 = jby2 * 2;
				dx2 = dx2 * 2;
				dy2 = dy2 * 2;
			} else {
				jbx1 = 624;
				jby1 = 392;
				dx1 = 121;
				dy1 = 33;
				jbx2 = 890;
				jby2 = 372;
				dx2 = 95;
				dy2 = 21;
			}
			originalImage = imageIcon5.getImage();

		} else {
			if (d == 2.0) {
				jbx1 = jbx1 + 223;
				jby1 = jby1 + 206;
				dx1 = dx1 + 60;
				dy1 = dy1 + 33;
			} else {
				jbx1 = 316;
				jby1 = 334;
				dx1 = 57;
				dy1 = 22;
			}
			originalImage = imageIcon6.getImage();
		}
		Image scaledImage;
		scaledImage = originalImage.getScaledInstance((int) (originalImage.getWidth(null) * d),
				(int) (originalImage.getHeight(null) * d), Image.SCALE_DEFAULT);
		imageJLabel.setIcon(new ImageIcon(scaledImage));

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2 && (jbx1 < e.getX() && e.getX() < jbx1 + dx1)
				&& (jby1 < e.getY() && e.getY() < jby1 + dy1)) {
			new TB3Plan();

		} else if (e.getClickCount() == 2 && (jbx2 < e.getX() && e.getX() < jbx2 + dx2)
				&& (jby2 < e.getY() && e.getY() < jby2 + dy2)) {
			new TB4Plan();
		}
	}

	public void mousePressed(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		JLabel jLabel = (JLabel) e.getSource();
		imageJLabel.setLocation(jLabel.getX() + e.getX() - x, jLabel.getY() + e.getY() - y);

	}

	public void mouseMoved(MouseEvent e) {
		// System.out.println(e.getX());
		// System.out.println(e.getY());
		// System.out.println(e.getXOnScreen());
		// System.out.println(e.getYOnScreen());
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		int check = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confimation to exit",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (check == JOptionPane.YES_OPTION) {
			System.exit(0);
			try {
				fc.getBr().close();
				fc.getPs().close();
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

	public void run() {
		try {
			while (isVisible()) {
				String messageFromServer = fc.getBr().readLine();
				if (messageFromServer == null) {
					JOptionPane.showMessageDialog(this, "server exception,sorry, you're forced to logout");
					System.exit(0);
				}
				if (messageFromServer.equals("MapGoMainFrameDone")) {
					this.dispose();
					new OperationFrame(fc);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
