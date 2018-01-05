package frame;

import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.GUIUtil;

public class ShowDialog extends JDialog {
	JLabel face = new JLabel("Face:");
	JLabel faceJLabel = new JLabel();
	JLabel accountJLabel = new JLabel("Account:");
	JLabel accountInfoJLabel = new JLabel();
	JLabel nameJLabel = new JLabel("Name:");
	JLabel nameInfoJLabel = new JLabel();

	public ShowDialog(JFrame jf, String account, String name, String outfilename) {
		super(jf, true);
		this.setTitle("User Information");
		this.setLayout(new GridLayout(3, 2));
		ImageIcon imageIcon = new ImageIcon(outfilename);
		Image temp = imageIcon.getImage().getScaledInstance(50, 50, imageIcon.getImage().SCALE_DEFAULT);
		faceJLabel.setIcon(new ImageIcon(temp));
		accountInfoJLabel.setText(account);
		nameInfoJLabel.setText(name);
		this.add(face);
		this.add(faceJLabel);
		this.add(accountJLabel);
		this.add(accountInfoJLabel);
		this.add(nameJLabel);
		this.add(nameInfoJLabel);
		this.setSize(400, 400);
		GUIUtil.toCenter(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}