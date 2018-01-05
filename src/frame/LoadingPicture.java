package frame;

import java.awt.Container;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import util.GUIUtil;

public class LoadingPicture extends JFrame implements Runnable {
	protected AudioStream as;
	protected FileInputStream fileau;

	public LoadingPicture() {
		Container c = getContentPane();
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		c.add(jp);
		this.setSize(985, 680);
		GUIUtil.SetBackground(this, "image/backgrounds/Ldpic7.jpg");
		GUIUtil.toCenter(this);
		setUndecorated(true);
		this.setResizable(false);
		this.setVisible(true);
		Thread thread = new Thread(this);
		try {
			fileau = new FileInputStream("video/Imissyou.wav");
			as = new AudioStream(fileau);
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread.start();

	}

	public void run() {
		try {
			AudioPlayer.player.start(as);
			Thread.sleep(5000);
			fileau.close();
			as.close();
			this.dispose();
			new LoginFrame(false);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
