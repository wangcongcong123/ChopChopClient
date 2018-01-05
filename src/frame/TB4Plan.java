package frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import util.GUIUtil;

public class TB4Plan extends JFrame implements ActionListener {

	private JButton button1 = new JButton("floor1");
	private JButton button2 = new JButton("floor2");
	private JButton button3 = new JButton("floor3");
	private JButton button4 = new JButton("floor4");
	private JButton button5 = new JButton("floor5");
	private JButton button6 = new JButton("floor6");
	JScrollPane jsScrollPane = new JScrollPane();
	JPanel myJPanel = new JPanel();
	private ImageIcon imageIcon;

	JLabel imageJLabel;

	public TB4Plan() {
		this.setTitle("Teaching Buiding 4 Plan");
		this.setLayout(new BorderLayout());
		jsScrollPane.setLayout(new ScrollPaneLayout());
		imageIcon = new ImageIcon("image/maps/4-1.png");
		imageJLabel = new JLabel(imageIcon);
		jsScrollPane.getViewport().add(imageJLabel);

		this.add(jsScrollPane, BorderLayout.CENTER);

		myJPanel.setLayout(new GridLayout(6, 1));
		myJPanel.add(button1);
		myJPanel.add(button2);
		myJPanel.add(button3);
		myJPanel.add(button4);
		myJPanel.add(button5);
		myJPanel.add(button6);
		this.add(myJPanel, BorderLayout.WEST);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		this.setSize(1200, 700);
		GUIUtil.toCenter(this);
		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	// imageIcon = new ImageIcon("video.jpg");
	// Image temp = imageIcon.getImage().getScaledInstance(
	// jbJLabel.getWidth(), jbJLabel.getHeight(),
	// imageIcon.getImage().SCALE_DEFAULT);
	// jbJLabel.setIcon(imageIcon);
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {
			changeFloor("image/maps/4-1.png");
		} else if (e.getSource() == button2) {
			changeFloor("image/maps/4-2.png");
		} else if (e.getSource() == button3) {
			changeFloor("image/maps/4-3.png");
		} else if (e.getSource() == button4) {
			changeFloor("image/maps/4-4.png");
		} else if (e.getSource() == button5) {
			changeFloor("image/maps/4-5.png");
		} else if (e.getSource() == button6) {
			changeFloor("image/maps/4-6.png");
		}
	}

	public void changeFloor(String filename) {
		imageIcon = new ImageIcon(filename);
		Image originalImage = imageIcon.getImage();
		imageJLabel.setIcon(new ImageIcon(originalImage));
	}

}
