package terry.tan.uiutils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import terry.tan.utils.ParameterChecker;

public final class UIUtils {
	
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public static void makeControlInScreenCenter(Container control) {
		ParameterChecker.checkNull("control", control);
		
		Dimension screenSize = getScreenSize();
		int x = (screenSize.width - control.getWidth()) / 2;
		int y = (screenSize.height - control.getHeight()) / 2;
		control.setLocation(x, y);
	}
	
	public static void fullScreen(Container control) {
		ParameterChecker.checkNull("control", control);
		
		Dimension screenSize = getScreenSize();
		control.setSize(screenSize);
	}
	
	public static void scaleToPreferableDimension(double preferableWidth, double preferableHeight, Container control) {
		ParameterChecker.checkNull("control", control);
		
		Dimension screenSize = UIUtils.getScreenSize();

		if (preferableHeight == 0) {
			if (preferableWidth > screenSize.width) {
				preferableWidth = screenSize.getWidth();
			}
		} else {
			// If the size of scaled width or height exceeds screen's, adjust
			// width or height.
			if (preferableWidth > screenSize.getWidth()
					|| preferableHeight > screenSize.getHeight()) {
				double screenRadio = screenSize.getWidth() / screenSize.getHeight();
				double preferableRadio = preferableWidth / preferableHeight;

				// If image dimension is wider than screen, set screen's width
				// as the width to be set
				if (screenRadio < preferableRadio) {
					preferableWidth = screenSize.getWidth();
					preferableHeight = preferableWidth / preferableRadio;
					// If image dimension is higher than screen, set screen's
					// height as the height to be set
				} else if (screenRadio > preferableRadio) {
					preferableHeight = screenSize.getHeight();
					preferableWidth = preferableRadio * preferableHeight;
					// If image has the same width/height radio with screen, set
					// screen's dimension as dimension to set
				} else {
					preferableWidth = screenSize.getWidth();
					preferableHeight = screenSize.getHeight();
				}
			}
		}
		
		control.setSize((int) Math.ceil(preferableWidth), (int) Math.ceil(preferableHeight));
	}
	
	public static void makeTopCenterInParent(Container control) {
		ParameterChecker.checkNull("control", control);
		ParameterChecker.checkNull("control.getParent()", control.getParent());
		
		Container parent = control.getParent();
		int x = (parent.getWidth() - control.getWidth()) / 2;
		control.setLocation(x, 0);
	}
}
