package video;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class PlayMain extends Thread {

	// static MainWindow frame;
	private static MainWindow frame;

	public static void main(String[] args) {
		new PlayMain();
	}

	public PlayMain() {
		// these below codes are from the third software's documentations which
		// tell us how to configure
		// try {
		// UIManager.setLookAndFeel(new SeaGlassLookAndFeel());
		//
		// } catch (UnsupportedLookAndFeelException e1) {
		// e1.printStackTrace();
		// }
		// different OSs has different configurations
		if (RuntimeUtil.isMac()) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");
		} else if (RuntimeUtil.isWindows()) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		} else if (RuntimeUtil.isNix()) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib");
		}
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		frame = new MainWindow();
		frame.setVisible(true);
		String options[] = { "--subsdec-encoding=GB18030" };
		// add a preparedmedia file
		frame.getMediaPlayer().prepareMedia("bjut.ifox", options);
		// open a thread to play the video
		new Thread(this).start();
	}

	public void run() {
		try {
			while (true) {
				long total = frame.getMediaPlayer().getLength();
				long curr = frame.getMediaPlayer().getTime();
				float percent = (float) curr / total;
				frame.getProgressBar().setValue((int) (percent * 100));
				// refresh the ProgressBar every 100 mills
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// a method to play video
	public static void play() {
		frame.getMediaPlayer().play();
	}

	// a method to pause video
	public static void pause() {
		frame.getMediaPlayer().pause();
	}

	// a method to stop video
	public static void stop1() {
		frame.getMediaPlayer().stop();
	}

	// a method to jump video
	public static void jumpTo(float to) {
		frame.getMediaPlayer().setTime((long) (to * frame.getMediaPlayer().getLength()));
	}

	public static void setVol(int v) {
		frame.getMediaPlayer().setVolume(v);
	}

	public static void exit() {
		frame.getMediaPlayer().release();
		System.exit(0);
	}
}
