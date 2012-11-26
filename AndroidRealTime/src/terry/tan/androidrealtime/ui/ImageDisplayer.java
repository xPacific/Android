package terry.tan.androidrealtime.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import terry.tan.uiutils.UIUtils;

@SuppressWarnings("serial")
public class ImageDisplayer extends JPanel {

	private BufferedImage image;
	private IPaintWhenNoImageToDraw defaultDrawer;

	public void setImage(BufferedImage image) {
		this.image = image;
		this.repaint();
		if (image != null) {
			scaleToImageWidthHeightRadio();
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void scaleToImageWidthHeightRadio() {
		if (image != null) {
			double imageWidth = (double) image.getWidth();
			double imageHeight = (double) image.getHeight();

			double currentWidth = (double) this.getWidth();
			double currentHeight = (double) this.getHeight();

			if (imageWidth == 0 || imageHeight == 0 || currentWidth == 0
					|| currentHeight == 0) {
				scaleToImageSize();
			}

			double imageRadio = imageWidth / imageHeight;
			double preferableWidth = imageRadio * currentHeight;
			double preferableHeight = currentHeight;
			UIUtils.scaleToPreferableDimension(preferableWidth,
					preferableHeight, this);
		}
	}

	public void scaleToImageSize() {
		if (image != null) {
			UIUtils.scaleToPreferableDimension((double) image.getWidth(),
					(double) image.getHeight(), this);
		}
	}

	public void setDefaultDrawer(IPaintWhenNoImageToDraw defaultDrawer) {
		this.defaultDrawer = defaultDrawer;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image != null) {
			g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		} else {
			if (defaultDrawer != null) {
				defaultDrawer.paint(this, g);
			}
		}
	}
}