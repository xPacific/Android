package terry.tan.androidrealtime.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import terry.tan.delegate.IAction;
import terry.tan.utils.ParameterChecker;

import com.android.ddmlib.IDevice;

@SuppressWarnings("serial")
public class ActionBar extends JFrame {

	private IDevice[] devices;
	private IDevice currentDevice;
	private boolean landscape;
	
	private final Set<IAction<Long>> frequencyChangedListeners = new LinkedHashSet<IAction<Long>>();
	private final Set<IAction<Boolean>> rotateScreenshotListeners = new LinkedHashSet<IAction<Boolean>>();
	private final Set<IAction<IDevice>> currentDeviceChangedListeners = new LinkedHashSet<IAction<IDevice>>();
	
	private JTextField freqTxtField;
	private JToggleButton rotateScreenButton;
	
	public ActionBar() {
		initializeUI();
		registerListeners();
	}
	
	private void initializeUI() {
		FlowLayout flowLayout = new FlowLayout();
		setLayout(flowLayout);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
		
		freqTxtField = new JTextField();
		freqTxtField.setMinimumSize(new Dimension(50, 20));
		freqTxtField.setVisible(false);
		this.add(freqTxtField);
		
		rotateScreenButton = new JToggleButton();
		rotateScreenButton.setText("Landscape");
		this.add(rotateScreenButton);
	}
	
    private void registerListeners() {
    	rotateScreenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				landscape = !landscape;
				rotateScreenshot(landscape);
			}
		});
	}
	
	public void addFrequencyChangedListener(IAction<Long> frequencyChangedListener) {
		ParameterChecker.checkNull("frequencyChangedListener", frequencyChangedListener);
		synchronized (frequencyChangedListeners) {
			frequencyChangedListeners.add(frequencyChangedListener);
		}
	}
	
	public void addRotateScreenshotListener(IAction<Boolean> rotateScreenshotListener) {
		ParameterChecker.checkNull("rotateScreenshotListener", rotateScreenshotListener);
		synchronized (rotateScreenshotListeners) {
			rotateScreenshotListeners.add(rotateScreenshotListener);
		}
	}
	
	public void addCurrentDeviceChangedListener(IAction<IDevice> currentDeviceChangedListener) {
		ParameterChecker.checkNull("currentDeviceChangedListener", currentDeviceChangedListener);
		synchronized (currentDeviceChangedListeners) {
			currentDeviceChangedListeners.add(currentDeviceChangedListener);
		}
	}
	
	public void refreshDeviceList(IDevice[] devices) {
		this.devices = devices;
		// TODO: Refresh UI
	}
	
	private void fireFrequencyChanged(long frequency) {
		synchronized (frequencyChangedListeners) {
			for (IAction<Long> action : frequencyChangedListeners) {
				action.invoke(frequency);
			}
		}
	}
	
	private void rotateScreenshot(Boolean landscape) {
		synchronized (rotateScreenshotListeners) {
			for (IAction<Boolean> action : rotateScreenshotListeners) {
				action.invoke(landscape);
			}
		}
	}
	
	private void fireCurrentDeviceChanged(IDevice device) {
		synchronized (currentDeviceChangedListeners) {
			for (IAction<IDevice> action : currentDeviceChangedListeners) {
				action.invoke(device);
			}
		}
	}
}
