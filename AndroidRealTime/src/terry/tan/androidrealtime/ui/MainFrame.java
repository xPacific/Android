package terry.tan.androidrealtime.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import terry.tan.adb.ADBHelper;
import terry.tan.delegate.IAction;

import com.android.ddmlib.IDevice;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static String NO_DEVICES_CONNECTED = "NO DEVICES CONNECTED";
	private ImageDisplayer imageDisplayer;
	private IPaintWhenNoImageToDraw defaultDrawer = new IPaintWhenNoImageToDraw() {
		@Override
		public void paint(ImageDisplayer displayer, Graphics g) {
			paintWhenNoScreenshoot(displayer, g);
		}
	};
	private ActionBar actionBar;

	/**
	 * Used to toggle paused
	 */
	private boolean isPaused = false;

	/**
	 * Set this field to true if window gets closing
	 */
	private boolean stopRefresh = false;

	/**
	 * Frequency should be customizable
	 */
	private long refreshFreq = 0;

	/**
	 * Used to implement rotate screenshot functionality
	 */
	private boolean landscape = false;

	/**
	 * Used to implement switch device functionality
	 */
	private IDevice[] connectedDevices;
	private IDevice currentDevice;

	public MainFrame() {
		initializeUI();
		registerListeners();
		initializeThreads();
	}

	private void initializeUI() {
		this.setMinimumSize(new Dimension(400, 300));
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(
				JRootPane.INFORMATION_DIALOG);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		imageDisplayer = new ImageDisplayer();
		imageDisplayer.setDefaultDrawer(defaultDrawer);
		this.setContentPane(imageDisplayer);
		// this.getContentPane().add(imageDisplayer);

		actionBar = new ActionBar();
		actionBar.setAlwaysOnTop(true);
		actionBar.pack();
		actionBar.setVisible(true);
	}

	private void registerListeners() {
		this.addHierarchyBoundsListener(new HierarchyBoundsListener() {

			@Override
			public void ancestorResized(HierarchyEvent e) {
				// TODO Auto-generated method stub
				// imageDisplayer.scaleToImageWidthHeightRadio();
			}

			@Override
			public void ancestorMoved(HierarchyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		actionBar.addRotateScreenshotListener(new IAction<Boolean>() {

			@Override
			public void invoke(Boolean landscape) {
				MainFrame.this.landscape = landscape;
			}
		});
	}

	private void initializeThreads() {
		Thread refreshThread = new Thread() {
			@Override
			public void run() {
				refreshScreenshot();
			}
		};
		refreshThread.start();
	}

	private void refreshScreenshot() {
		while (!stopRefresh) {
			if (currentDevice == null) {
				initiDevices();
			}

			checkWhetherNeedToPause();
			tryToRefreshScreenshot();
			sleepForAWhile();
		}
	}

	private void initiDevices() {
		connectedDevices = ADBHelper.initDevices();
		if (connectedDevices != null && connectedDevices.length > 0) {
			// By default, set the first device as current device
			currentDevice = connectedDevices[0];
		}
	}

	private void tryToRefreshScreenshot() {
		if (currentDevice != null) {
			try {
				BufferedImage screenshot = ADBHelper.getScreenshot(
						currentDevice, landscape);
				refreshUI(screenshot);
			} catch (Exception e) {
				e.printStackTrace();
				clearCurrentDeviceAndScreenshot();
			}
		}
	}

	private void clearCurrentDeviceAndScreenshot() {
		currentDevice = null;
		imageDisplayer.setImage(null);
	}

	private void refreshUI(final BufferedImage screenshot) {
		if (screenshot != null) {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						imageDisplayer.setImage(screenshot);
					}
				});
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void checkWhetherNeedToPause() {
		while (isPaused) {
			sleepForAWhile();
		}
	}

	private void sleepForAWhile() {
		try {
			if (refreshFreq > 0) {
				Thread.sleep(refreshFreq);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void paintWhenNoScreenshoot(ImageDisplayer displayer, Graphics g) {
		Font font = new Font("Arial", Font.BOLD | Font.ITALIC, 20);
		FontMetrics metrics = g.getFontMetrics(font);
		Rectangle2D rect = metrics.getStringBounds(NO_DEVICES_CONNECTED, g);
		int textWidth = (int) Math.ceil(rect.getWidth());
		int textHeight = (int) Math.ceil(rect.getHeight());

		int displayerWidth = displayer.getWidth();
		int displayerHeight = displayer.getHeight();
		if (displayerWidth < textWidth) {
			displayerWidth = textWidth;
		}

		if (displayerHeight < textHeight) {
			displayerHeight = textHeight;
		}

		int x = (displayerWidth - textWidth) / 2 + metrics.getAscent();
		int y = (displayerHeight - textHeight) / 2;
		displayer.setSize(displayerWidth, displayerHeight);
		g.drawString(NO_DEVICES_CONNECTED, x, y);
	}
}