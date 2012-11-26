package terry.tan.adb;

import java.awt.image.BufferedImage;
import java.io.IOException;

import terry.tan.utils.DateTime;
import terry.tan.utils.ParameterChecker;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

public class ADBHelper {
	
	private static boolean isADBInitialized = false;

	/**
	 * Initializes connected device list
	 * 
	 * @return An array of devices
	 */
	public static IDevice[] initDevices() {
		IDevice[] devices = null;
		try {
			devices = initDevices(null);
		} catch (TimeoutException e) {
			// Nothing to do
		}
		return devices;
	}

	/**
	 * Initializes connected device list with timeout. If timeout parameter is
	 * null, there is no timeout.
	 * 
	 * @param timeout
	 *            Timeout milliseconds
	 * @return An array of devices
	 * @throws TimeoutException
	 *             if specified timeout reaches
	 */
	public static IDevice[] initDevices(Long timeout) throws TimeoutException {
		AndroidDebugBridge adb = null;
		long startTime = DateTime.currentTime();
		IDevice[] devices = null;
		while (adb == null || !adb.hasInitialDeviceList() || devices == null || devices.length <= 0) {
			while (!isADBInitialized) {
				try {
					AndroidDebugBridge.init(false);
					isADBInitialized = true;
				} catch (Exception e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			if (adb == null) {
				adb = AndroidDebugBridge.getBridge();
			}
			
			if (adb == null) {
				adb = AndroidDebugBridge.createBridge();
			}
			
			if (adb != null) {
				devices = adb.getDevices();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (timeout != null) {
				long now = DateTime.currentTime();
				if ((now - startTime) >= timeout) {
					throw new TimeoutException();
				}
			}
		}
		return devices;
	}

	/**
	 * Gets screenshot of a specific device
	 * 
	 * @param device
	 *            A device
	 * @return An instance of BufferedImage representing the screenshot of the
	 *         speified device
	 * @throws IOException
	 *             in case of timeout on the connection.
	 * @throws AdbCommandRejectedException
	 *             if adb rejects the command
	 * @throws TimeoutException
	 *             in case of I/O error on the connection.
	 */
	public static BufferedImage getScreenshot(IDevice device, boolean landscape)
			throws TimeoutException, AdbCommandRejectedException, IOException {
		ParameterChecker.checkNull("device", device);

		BufferedImage screenshoot = null;
		RawImage rawImage = device.getScreenshot();
		if (rawImage != null) {
			int width = landscape ? rawImage.height : rawImage.width;
			int height = landscape ? rawImage.width : rawImage.height;
			screenshoot = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			int index = 0;
			int indexInc = rawImage.bpp >> 3;
			for (int y = 0; y < rawImage.height; y++) {
				for (int x = 0; x < rawImage.width; x++, index += indexInc) {
					int value = rawImage.getARGB(index);
					if (landscape)
						screenshoot.setRGB(y, rawImage.width - x - 1, value);
					else
						screenshoot.setRGB(x, y, value);

				}
			}
		}
		return screenshoot;
	}
}