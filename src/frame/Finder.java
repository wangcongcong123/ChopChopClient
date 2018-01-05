package frame;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Finder extends JDialog implements ActionListener {

	private JLabel dishName = new JLabel("DishName(Optional):");
	private JTextField dishTextField = new JTextField(6);

	private JLabel price = new JLabel("Price Intervel(RMB):");
	private JTextField dishPriceTextField1 = new JTextField(2);
	private JTextField dishPriceTextField2 = new JTextField(2);
	private JLabel canteenName = new JLabel("Canteen(Optional):");

	private JComboBox CanteenComboBox = new JComboBox();

	private ButtonGroup bgSelect = new ButtonGroup();
	private JRadioButton sortedRadioButton1 = new JRadioButton("SortedByLike", true);
	private JRadioButton sortedRadioButton2 = new JRadioButton("SortedByPrice", false);

	private JButton searchButton = new JButton("Search");
	private JButton backButton = new JButton("Back");
	private FinderBean fb;

	public Finder(JFrame jf, FinderBean fb) {
		super(jf, true);
		this.fb = fb;
		this.setLayout(null);
		dishName.setBounds(10, 10, 260, 30);
		dishName.setFont(new java.awt.Font("Dialog", 1, 18));
		dishTextField.setBounds(200, 10, 160, 30);
		this.add(dishName);
		this.add(dishTextField);

		price.setBounds(10, 40, 300, 30);
		price.setFont(new java.awt.Font("Dialog", 1, 18));
		dishPriceTextField1.setBounds(200, 40, 60, 30);
		dishPriceTextField2.setBounds(300, 40, 60, 30);
		this.add(price);
		this.add(dishPriceTextField1);
		this.add(dishPriceTextField2);

		canteenName.setBounds(10, 70, 190, 30);
		canteenName.setFont(new java.awt.Font("Dialog", 1, 18));
		CanteenComboBox.addItem("天天餐厅");
		CanteenComboBox.addItem("美食园");
		CanteenComboBox.addItem("奥运餐厅");
		CanteenComboBox.addItem("三四餐厅");
		CanteenComboBox.setBounds(200, 70, 160, 30);
		this.add(canteenName);
		this.add(CanteenComboBox);

		bgSelect.add(sortedRadioButton1);
		bgSelect.add(sortedRadioButton2);

		sortedRadioButton1.setBounds(10, 100, 240, 30);
		sortedRadioButton1.setFont(new java.awt.Font("Dialog", 1, 18));
		sortedRadioButton2.setBounds(250, 100, 240, 30);
		sortedRadioButton2.setFont(new java.awt.Font("Dialog", 1, 18));

		this.add(sortedRadioButton1);
		this.add(sortedRadioButton2);
		ImageIcon imageIcon = new ImageIcon("image/backgrounds/sx.jpg");
		Image temp = imageIcon.getImage().getScaledInstance(400, 200, imageIcon.getImage().SCALE_DEFAULT);
		imageIcon = new ImageIcon(temp);
		JLabel extra = new JLabel(imageIcon);
		extra.setBounds(0, 130, 480, 243);
		this.add(extra);

		searchButton.setBounds(380, 370, 80, 40);
		backButton.setBounds(300, 370, 80, 40);
		this.add(searchButton);
		this.add(backButton);
		this.setLayout(null);
		this.setSize(480, 450);
		this.setLocation(200, 160);
		this.setTitle("Search Frame");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		backButton.addActionListener(this);
		searchButton.addActionListener(this);
		this.setResizable(false);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backButton) {
			this.dispose();
		} else if (e.getSource() == searchButton) {

			try {
				String dishName = dishTextField.getText();

				double price1 = Double.parseDouble(dishPriceTextField1.getText());
				double price2 = Double.parseDouble(dishPriceTextField2.getText());
				if (price1 > price2) {
					JOptionPane.showMessageDialog(this, "The first price should be less than the second price");
					return;
				}
				String canteenName = CanteenComboBox.getSelectedItem().toString();
				String sorting = null;
				if (sortedRadioButton1.isSelected()) {
					sorting = sortedRadioButton1.getText();
				} else {
					sorting = sortedRadioButton2.getText();
				}
				fb.setDishName(dishName);
				fb.setPrice1(price1);
				fb.setPrice2(price2);
				fb.setCanteenName(canteenName);
				fb.setSorting(sorting);
				fb.setSearch(true);
				this.dispose();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Input valid price numbers,please");
			}

		}
	}

}
