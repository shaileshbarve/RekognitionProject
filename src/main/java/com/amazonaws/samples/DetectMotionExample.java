package com.amazonaws.samples;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamPanel;

/**
 * Detect motion.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class DetectMotionExample extends JFrame implements Runnable {

	private static final long serialVersionUID = -585739158170333370L;

	private static final int INTERVAL = 100; // ms

	private ImageIcon motion = null;
	private ImageIcon nothing = null;
	private JLabel label = null;

	private Webcam webcam = Webcam.getDefault();
	private int threshold = 25;
	private int inertia = 1000; // how long motion is valid

	public DetectMotionExample() {

		label = new JLabel(nothing);

		Thread updater = new Thread(this, "updater-thread");
		updater.setDaemon(true);
		updater.start();

		setTitle("Rage Motion Detector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());

//		webcam.setViewSize(new Dimension(320, 240));
		webcam.setViewSize(new Dimension(640, 480));

		WebcamPanel panel = new WebcamPanel(webcam);

		add(panel);
		add(label);

		pack();
		setVisible(true);
	}

	public static void main(String[] args) throws InterruptedException {
		new DetectMotionExample();
	}
	
	public void stop() {
		if (webcam.isOpen())
			webcam.close();
	}

	@Override
	public void run() {

//		WebcamMotionDetector detector = new WebcamMotionDetector(webcam, threshold, inertia);
		WebcamMotionDetector detector = new WebcamMotionDetector(webcam);
		detector.setInterval(INTERVAL);
		detector.start();

		while (true) {

			Icon icon = label.getIcon();
			if (detector.isMotion()) {
//				if (icon != motion) 
				{
					try {
						S3Sample.SnapPhoto();
						S3Sample.SearchImagesinCollection();
						
						Thread.sleep(2 * 1000);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (icon != nothing) {
//					label.setIcon(nothing);
					JOptionPane.showMessageDialog(null, "No Motion detected");
				}
			}

			try {
				Thread.sleep(INTERVAL * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}