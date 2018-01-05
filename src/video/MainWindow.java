/**
 * this package includes some classes to achieve the video player function in the mainframe
 */
package video;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * a class to display the video player interface
 * 
 * @author group 12
 * 
 */
public class MainWindow extends JFrame implements ActionListener, WindowListener {

	private JPanel contentPane;

	// declare a component form the third software named VLC which is a
	// video-palyer software
	// and provide API for java to develop
	EmbeddedMediaPlayerComponent playerComponent;
	private JPanel panel;
	private JButton btnPlay;
	private JButton btnPause;
	private JButton btnStop;
	private JButton backButton;
	private JPanel controlPanel;
	private JProgressBar progress;
	private JSlider slider;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 616, 466);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel videopane = new JPanel();
		contentPane.add(videopane, BorderLayout.CENTER);
		videopane.setLayout(new BorderLayout(0, 0));

		playerComponent = new EmbeddedMediaPlayerComponent();
		videopane.add(playerComponent);

		panel = new JPanel();
		videopane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		controlPanel = new JPanel();
		panel.add(controlPanel);

		backButton = new JButton("Back");
		controlPanel.add(backButton);

		btnStop = new JButton("Stop");
		controlPanel.add(btnStop);

		btnPlay = new JButton("Play");
		controlPanel.add(btnPlay);

		btnPause = new JButton("Pause");
		controlPanel.add(btnPause);
		slider = new JSlider();
		slider.setValue(100);
		slider.setMaximum(120);

		/**
		 * add some listeners to some components though internal classes
		 */
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PlayMain.setVol(slider.getValue());
			}
		});
		controlPanel.add(slider);

		progress = new JProgressBar();
		progress.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				PlayMain.jumpTo((float) x / progress.getWidth());
			}
		});
		progress.setStringPainted(true);
		panel.add(progress, BorderLayout.NORTH);
		btnPause.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				PlayMain.pause();
			}
		});
		btnPlay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				PlayMain.play();
			}
		});
		btnStop.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				PlayMain.stop1();
			}
		});
		backButton.addActionListener(this);
		this.addWindowListener(this);
	}

	// a method to get the EmbeddedMediaPlayer by calling a method of the
	// playerComponent
	public EmbeddedMediaPlayer getMediaPlayer() {
		return playerComponent.getMediaPlayer();
	}

	public JProgressBar getProgressBar() {
		return progress;
	}

	public void actionPerformed(ActionEvent e) {
		PlayMain.stop1();
		this.setVisible(false);
	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		PlayMain.stop1();
		this.setVisible(false);

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

}
