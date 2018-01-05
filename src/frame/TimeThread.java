package frame;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeThread extends Thread {

	private OperationFrame of;

	public TimeThread(OperationFrame of) {
		this.of = of;
	}

	public void run() {
		while (true) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			of.setTitle(sdf.format(date));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
