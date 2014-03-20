package rbm;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import picture_processing.Picture;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.DropMode;
import javax.swing.JTextArea;

public class RBMApplet extends JApplet {
	
	private double[][] trainingSet = new double[10][256];
	private double[][] testSet = new double[1][256];
	private int count = 0;
	private double energy;
	private RBM rbm;

	public RBMApplet() {

		final JPanel panel = new JPanel();
		final JButton btnTrain = new JButton("Train");
		final JLabel lblEnergy = new JLabel("Energy:");
		panel.setBackground(Color.WHITE);
		final JPanel pnlResult = new JPanel();
		pnlResult.setBackground(Color.WHITE);
		final JPanel pnlDrawTable = new JPanel();
		pnlDrawTable.setBackground(Color.WHITE);
		final JButton btnClear = new JButton("Clear");
		final JLabel lblNewLabel = new JLabel("no:");
		final JButton btnAdd = new JButton("Add");
		final Display canvas2 = new Display();
		final JButton btnReset = new JButton("Reset");
		final JButton btnCheckDigit = new JButton("Add to check");
		final JLabel lblIsSame = new JLabel("N/A");
		JButton btnCheck = new JButton("Check");

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				lblNewLabel.setText("10/" + count);
				if (count == 10) {
					btnTrain.setEnabled(true);
				}
			}
		});
		pnlResult.setLayout(new CardLayout(0, 0));


		btnClear.addActionListener(canvas2);
		btnAdd.addActionListener(canvas2);
		btnCheckDigit.addActionListener(canvas2);

		pnlDrawTable.add(canvas2);
		btnTrain.setEnabled(false);
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTrain.setEnabled(false);
				count = 0;

				rbm = new RBM(256, 25, 0.01);

				rbm.train(trainingSet, 5000);
				Picture image = rbm.featuresToPicture();
				Picture image2 = rbm.getVisible();
				ImagePanel im = new ImagePanel(image.getImage());
				ImagePanel im2 = new ImagePanel(image2.getImage());
				panel.add(im);
				pnlResult.add(im2);
				energy = rbm.getEnergy();
				lblEnergy.setText("Energy: " + energy);
				
				revalidate();

			}
		});
		pnlDrawTable.setLayout(new CardLayout(0, 0));
		panel.setLayout(new CardLayout(0, 0));


		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTrain.setEnabled(false);
				trainingSet = new double[10][256];
				count = 0;
				energy = 0;
				lblIsSame.setText("");
				lblEnergy.setText("Energy: ");
				lblNewLabel.setText("no:");
				panel.removeAll();
				panel.revalidate();
				trainingSet = new double[10][256];
				revalidate();
			}
		});

		btnCheckDigit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rbm.saveValues();
			}
		});
		
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < 256; i++) {
					if (i % 16 == 0) {
						System.out.println();
					}
					System.out.print((int) testSet[0][i]);
				}
				rbm.train(testSet, 1);
				double newEnergy = rbm.getEnergy();
				lblIsSame.setText("Energy: " + newEnergy);
				
				Picture image2 = rbm.getVisible();
				ImagePanel im2 = new ImagePanel(image2.getImage());
				pnlResult.removeAll();
				pnlResult.add(im2);
				rbm.restoreValues();
				
				revalidate();
			}
		});
		
		JLabel lblDetectedfeatures = new JLabel("Detected features:");
		
		JLabel lblDrawingArea = new JLabel("Drawing area:");
		
		JLabel lblReconstruction = new JLabel("Reconstruction:");
		
		JTextArea txtrInstructionsDraw = new JTextArea();
		txtrInstructionsDraw.setText("Instructions:\r\n1. Draw the same digit 10 times, click 'Add' after each\r\n2. Train the RBM, click 'Train'\r\n3. See the detected features\r\n4. Draw a digit to check how close it is to your digit\r\nclick 'Add to check' then 'Check'");
		

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnTrain, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(lblIsSame)
									.addComponent(lblEnergy)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(pnlDrawTable, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
											.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
											.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
											.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
											.addComponent(lblDrawingArea))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(lblReconstruction)
											.addComponent(pnlResult, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
											.addComponent(btnCheckDigit)
											.addComponent(btnCheck))))
								.addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDetectedfeatures)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtrInstructionsDraw, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(23)))
					.addGap(33))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDrawingArea)
						.addComponent(lblReconstruction)
						.addComponent(lblDetectedfeatures))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(pnlDrawTable, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
									.addGap(12)
									.addComponent(lblNewLabel)
									.addGap(11)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnAdd)
										.addComponent(btnCheckDigit))
									.addGap(11)
									.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnClear)
										.addComponent(btnCheck)))
								.addComponent(pnlResult, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
							.addGap(34)
							.addComponent(btnTrain)
							.addGap(7)
							.addComponent(btnReset)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblEnergy)
							.addGap(20)
							.addComponent(lblIsSame)
							.addContainerGap(42, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
							.addComponent(txtrInstructionsDraw, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(30))))
		);
		getContentPane().setLayout(groupLayout);

	}

	private class Display extends JPanel 
	implements MouseListener, MouseMotionListener, ActionListener {

		Image OSI;
		int widthOfOSI, heightOfOSI;
		private int mouseX, mouseY;
		private int prevX, prevY;
		private boolean dragging;
		private Graphics dragGraphics;

		Display() {
			addMouseListener(this);
			addMouseMotionListener(this);
			setBackground(Color.white);
		}

		private void repaintRect(int x1, int y1, int x2, int y2) {
			int x, y;
			int w, h;
			if (x2 >= x1) {
				x = x1;
				w = x2 - x1;
			}
			else {
				x = x2;
				w = x1 - x2;
			}
			if (y2 >= y1) {
				y = y1;
				h = y2 - y1;
			}
			else {
				y = y2;
				h = y1 - y2;
			}
			repaint(x,y,w+1,h+1);
		}


		private void checkOSI() {
			if (OSI == null || widthOfOSI != getSize().width || heightOfOSI != getSize().height) {
				OSI = null;
				OSI = createImage(getSize().width, getSize().height);
				widthOfOSI = getSize().width;
				heightOfOSI = getSize().height;
				Graphics OSG = OSI.getGraphics();
				OSG.setColor(Color.white);
				OSG.fillRect(0, 0, widthOfOSI, heightOfOSI);
				OSG.dispose();
			}
		}

		public void paintComponent(Graphics g) {
			checkOSI();
			g.drawImage(OSI, 0, 0, this);
		}


		public void actionPerformed(ActionEvent evt) {
			if (evt.getActionCommand().equals("Add")) {
				int w = OSI.getWidth(null);
				int h = OSI.getHeight(null);
				int[] pixels = new int[w * h];
				PixelGrabber pg = new PixelGrabber(OSI, 0, 0, w, h, pixels, 0, w);
				try {
					pg.grabPixels();
				} catch (InterruptedException e1) {
					System.out.println("oh");
				}

				for (int i = 0; i < pixels.length; i++) {
					pixels[i] = pixels[i] < -1 ? 1 : 0;			
				}


				if (count < 10) {
					trainingSet[count] =
							PictureUtils.convertTo256(pixels);
					count++;
				}
			} 
			
			if (evt.getActionCommand().equals("Add to check")) {
				int w = OSI.getWidth(null);
				int h = OSI.getHeight(null);
				int[] pixels = new int[w * h];
				PixelGrabber pg = new PixelGrabber(OSI, 0, 0, w, h, pixels, 0, w);
				try {
					pg.grabPixels();
				} catch (InterruptedException e1) {
					System.out.println("oh");
				}

				for (int i = 0; i < pixels.length; i++) {
					pixels[i] = pixels[i] < -1 ? 1 : 0;			
				}
				
				testSet[0] = PictureUtils.convertTo256(pixels);
				

			}

			checkOSI();
			Graphics g = OSI.getGraphics();
			g.setColor(getBackground());
			g.fillRect(0,0,getSize().width,getSize().height);
			g.dispose();
			repaint();
		}


		public void mousePressed(MouseEvent evt) {

			if (dragging == true)
				return;

			prevX = evt.getX(); 
			prevY = evt.getY();

			dragGraphics = OSI.getGraphics();
			dragGraphics.setColor(Color.black);

			dragging = true;

		} 


		public void mouseReleased(MouseEvent evt) {

			if (dragging == false)
				return;  // Nothing to do because the user isn't drawing.
			dragging = false;
			mouseX = evt.getX();
			mouseY = evt.getY();
			dragGraphics.drawLine(prevX,prevY,mouseX,mouseY);
			repaintRect(prevX,prevY,mouseX,mouseY);
			dragGraphics.dispose();
			dragGraphics = null;
		}


		public void mouseDragged(MouseEvent evt) {

			if (dragging == false)
				return;  // Nothing to do because the user isn't drawing.

			mouseX = evt.getX(); 
			mouseY = evt.getY(); 

			// A CURVE is drawn as a series of LINEs.
			dragGraphics.drawLine(prevX,prevY,mouseX,mouseY);
			repaintRect(prevX,prevY,mouseX,mouseY);

			prevX = mouseX;  // Save coords for the next call to mouseDragged or mouseReleased.
			prevY = mouseY;

		} 


		public void mouseEntered(MouseEvent evt) { }
		public void mouseExited(MouseEvent evt) { }
		public void mouseClicked(MouseEvent evt) { }
		public void mouseMoved(MouseEvent evt) { }

	}

	public class ImagePanel extends JPanel {

		private BufferedImage image;

		public ImagePanel(BufferedImage image) {
			this.image = image;
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
			g.dispose();
		}
	}
}
