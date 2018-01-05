package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import util.GUIUtil;

public class AboutOurApp extends JDialog {
	private JTextArea jta = new JTextArea();
	private JScrollPane jspJScrollPane = new JScrollPane();

	public AboutOurApp(JFrame jf) {
		super(jf, true);

		readFile(jta);
		jta.setLineWrap(true);
		jta.setEditable(false);
		jspJScrollPane.getViewport().add(jta, BorderLayout.CENTER);
		this.add(jspJScrollPane);
		this.setTitle("About");
		this.setSize(new Dimension(500, 500));
		GUIUtil.toCenter(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void readFile(JTextArea jta2) {

		try {
			FileInputStream fis = new FileInputStream("about.txt");
			byte input[] = new byte[21];
			while (fis.read(input) != -1) {
				String inputString = new String(input);
				jta.append(inputString);
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
