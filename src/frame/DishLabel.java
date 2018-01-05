package frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 * a class to configure the dishLabel in dishlist
 * 
 * @author group 12
 * 
 */
public class DishLabel extends JLabel implements ListCellRenderer {

	// declare two different borders
	private Border lineBorder = BorderFactory.createLineBorder(Color.blue, 1);
	private Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public DishLabel() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		Object[] cell = (Object[]) value;
		this.setIcon((Icon) cell[0]);
		this.setText((cell[1].toString()) + "  " + cell[2].toString() + " " + cell[3].toString());
		setToolTipText(cell[4].toString());
		if (isSelected) {
			this.setBackground(list.getSelectionBackground());
			this.setForeground(list.getSelectionForeground());
		} else {
			this.setBackground(list.getBackground());
			this.setForeground(list.getForeground());
		}
		if (cellHasFocus) {
			this.setBorder(lineBorder);
		} else {
			this.setBorder(emptyBorder);
			this.setForeground(list.getForeground());
		}
		this.setEnabled(list.isEnabled());
		this.setOpaque(true);
		return this;
	}

	private void jbInit() throws Exception {

		this.setFont(new java.awt.Font("Dialog", Font.PLAIN, 15));
	}
}
