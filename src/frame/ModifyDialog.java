package frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import util.GUIUtil;

public class ModifyDialog extends JDialog implements ActionListener {
	private JLabel accountJLabel = new JLabel("account:");
	private JLabel account = new JLabel();

	private JLabel oldpassword = new JLabel("OldPassword:");
	private JPasswordField oldpasswordField = new JPasswordField(10);
	private JLabel newpassword1 = new JLabel("NewPassword1:");
	private JPasswordField newpassword1Field = new JPasswordField(10);
	private JLabel newpassword2 = new JLabel("NewPassword2:");
	private JPasswordField newpassword2Field = new JPasswordField();

	private JLabel nameJLabel = new JLabel("name:");
	private JTextField nameTextField = new JTextField(10);

	private JButton updateButton = new JButton("Update");
	private JButton backButton = new JButton("Back");
	private UserBean ub;
	private String password = null;

	public ModifyDialog(JFrame jf, UserBean ub, String acc, String password) {
		super(jf, true);
		this.setTitle("Modify User Info");
		this.password = password;
		this.ub = ub;
		this.setLayout(new GridLayout(6, 2));
		account.setText(acc);
		this.add(accountJLabel);
		this.add(account);
		this.add(oldpassword);
		this.add(oldpasswordField);
		this.add(newpassword1);
		this.add(newpassword1Field);
		this.add(newpassword2);
		this.add(newpassword2Field);
		this.add(nameJLabel);
		this.add(nameTextField);
		this.add(backButton);
		this.add(updateButton);

		backButton.addActionListener(this);
		updateButton.addActionListener(this);
		this.setSize(400, 400);
		GUIUtil.toCenter(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			this.dispose();
		} else if (e.getSource() == updateButton) {

			try {
				String oldpassword = String.valueOf(oldpasswordField.getPassword());
				if (!oldpassword.equals(password)) {
					JOptionPane.showMessageDialog(this, "fill in the correct old password,please");
					return;
				}
				String newpassword1 = String.valueOf(newpassword1Field.getPassword());
				String newpassword2 = String.valueOf(newpassword2Field.getPassword());
				if (newpassword1.isEmpty() || newpassword2.isEmpty()) {
					JOptionPane.showMessageDialog(this, "fill in your new password,please");
					return;
				}
				if (!newpassword1.equals(newpassword2)) {
					JOptionPane.showMessageDialog(this, "password1  should be equal to password2");
					return;
				}
				String Name = nameTextField.getText();
				if (Name.trim().isEmpty()) {
					JOptionPane.showMessageDialog(this, "fill in name,please");
					return;
				}
				ub.setPassword(newpassword1);
				ub.setName(Name);
				ub.setModified(true);
				this.dispose();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Input valid price numbers,please");
			}

		}

	}

}
