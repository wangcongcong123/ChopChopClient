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

public class TB3Plan extends JFrame implements ActionListener {

	private JButton button1 = new JButton("floor1");
	private JButton button2 = new JButton("floor2");
	private JButton button3 = new JButton("floor3");
	private JButton button4 = new JButton("floor4");
	private JButton button5 = new JButton("floor5");
	JScrollPane jsScrollPane = new JScrollPane();
	JPanel myJPanel = new JPanel();
	private ImageIcon imageIcon;

	JLabel imageJLabel;

	public TB3Plan() {
		this.setTitle("Teaching Buiding 3 Plan");
		this.setLayout(new BorderLayout());
		jsScrollPane.setLayout(new ScrollPaneLayout());
		imageIcon = new ImageIcon("image/maps/3-1.png");
		imageJLabel = new JLabel(imageIcon);
		jsScrollPane.getViewport().add(imageJLabel);

		this.add(jsScrollPane, BorderLayout.CENTER);

		myJPanel.setLayout(new GridLayout(5, 1));
		myJPanel.add(button1);
		myJPanel.add(button2);
		myJPanel.add(button3);
		myJPanel.add(button4);
		myJPanel.add(button5);
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1) {
			changeFloor("image/maps/3-1.png");
		} else if (e.getSource() == button2) {
			changeFloor("image/maps/3-2.png");
		} else if (e.getSource() == button3) {
			changeFloor("image/maps/3-3.png");
		} else if (e.getSource() == button4) {
			changeFloor("image/maps/3-4.png");
		} else if (e.getSource() == button5) {
			changeFloor("image/maps/3-5.png");
		}
	}

	public void changeFloor(String filename) {
		imageIcon = new ImageIcon(filename);
		Image originalImage = imageIcon.getImage();
		imageJLabel.setIcon(new ImageIcon(originalImage));
	}

}
