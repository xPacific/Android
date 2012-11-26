package terry.tan.androidrealtime;

import javax.swing.SwingUtilities;
import terry.tan.androidrealtime.ui.MainFrame;
import terry.tan.uiutils.UIUtils;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame mainFrame = new MainFrame();
				mainFrame.pack();
				UIUtils.makeControlInScreenCenter(mainFrame);
				mainFrame.setVisible(true);
			}
		});
	}
}
