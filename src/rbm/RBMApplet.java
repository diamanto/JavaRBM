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

public class RBMApplet extends JApplet {

	int numHidden = 0;
	double[][] trainingSet = new double[10][256];
	int count = 0;

	public void init() {

	}

	public RBMApplet() {

		final JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		
		final JButton btnTrain = new JButton("Train");

		JPanel pnlResult = new JPanel();
		pnlResult.setBackground(Color.WHITE);

		JPanel pnlDrawTable = new JPanel();
		pnlDrawTable.setBackground(Color.WHITE);

		JButton btnClear = new JButton("Clear");

		final JLabel lblNewLabel = new JLabel("no:");

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				lblNewLabel.setText("10/" + count);
				if (count == 10) {
					btnTrain.setEnabled(true);
				}
			}
		});
		pnlResult.setLayout(new CardLayout(0, 0));

		Display canvas2 = new Display();
		btnClear.addActionListener(canvas2);
		btnAdd.addActionListener(canvas2);
		pnlDrawTable.add(canvas2);
		btnTrain.setEnabled(false);
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTrain.setEnabled(false);
				count = 0;

				RBM rbm = new RBM(256, 25, 0.01);

				rbm.train(trainingSet, 1000);
				Picture image = rbm.featuresToPicture();
				BufferedImage bi = image.getImage();
				ImagePanel im = new ImagePanel(bi);
				panel.add(im);
				rbm.getEnergy();
				revalidate();

			}
		});
		pnlDrawTable.setLayout(new CardLayout(0, 0));
		panel.setLayout(new CardLayout(0, 0));

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTrain.setEnabled(true);
				trainingSet = new double[10][256];
				count = 0;
				lblNewLabel.setText("no:");
				panel.removeAll();
				panel.revalidate();
				revalidate();
			}
		});
		
		JLabel lblEnergy = new JLabel("Energy:");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(pnlDrawTable, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addComponent(btnTrain, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
									.addGap(10)
									.addComponent(pnlResult, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
									.addGap(58)
									.addComponent(panel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblEnergy)))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(pnlDrawTable, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(lblNewLabel)
							.addGap(11)
							.addComponent(btnAdd)
							.addGap(11)
							.addComponent(btnClear)
							.addGap(11)
							.addComponent(btnTrain))
						.addComponent(pnlResult, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 256, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addComponent(btnReset)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblEnergy)
					.addContainerGap(79, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);

	}

	private class Display extends JPanel 
	implements MouseListener, MouseMotionListener, ActionListener {

		Image OSI;  // The off-screen image (created in checkOSI()).

		int widthOfOSI, heightOfOSI;  // Current width and height of OSI.  These
		// are checked against the size of the applet,
		// to detect any change in the panel's size.
		// If the size has changed, a new OSI is created.
		// The picture in the off-screen image is lost
		// when that happens.


		/* The following variables are used when the user is sketching a
         curve while dragging a mouse. */

		private int mouseX, mouseY;   // The location of the mouse.

		private int prevX, prevY;     // The previous location of the mouse.)

		private boolean dragging;     // This is set to true when the user is drawing.

		private Graphics dragGraphics;  // A graphics context for the off-screen image,
		// to be used while a drag is in progress.


		Display() {
			// Constructor.  When this component is first created, it is set to
			// listen for mouse events and mouse motion events from
			// itself.  The initial background color is white.
			addMouseListener(this);
			addMouseMotionListener(this);
			setBackground(Color.white);
		}

		private void repaintRect(int x1, int y1, int x2, int y2) {
			// Call repaint on a rectangle that contains the points (x1,y1)
			// and (x2,y2).  (Add a 1-pixel border along right and bottom 
			// edges to allow for the pen overhang when drawing a line.)
			int x, y;  // top left corner of rectangle that contains the figure
			int w, h;  // width and height of rectangle that contains the figure
			if (x2 >= x1) {  // x1 is left edge
				x = x1;
				w = x2 - x1;
			}
			else {          // x2 is left edge
				x = x2;
				w = x1 - x2;
			}
			if (y2 >= y1) {  // y1 is top edge
				y = y1;
				h = y2 - y1;
			}
			else {          // y2 is top edge.
				y = y2;
				h = y1 - y2;
			}
			repaint(x,y,w+1,h+1);
		}


		private void checkOSI() {
			// This method is responsible for creating the off-screen image. 
			// It should be called before using the OSI.  It will make a new OSI if
			// the size of the panel changes.
			if (OSI == null || widthOfOSI != getSize().width || heightOfOSI != getSize().height) {
				// Create the OSI, or make a new one if panel size has changed.
				OSI = null;  // (If OSI already exists, this frees up the memory.)
				OSI = createImage(getSize().width, getSize().height);
				widthOfOSI = getSize().width;
				heightOfOSI = getSize().height;
				Graphics OSG = OSI.getGraphics();  // Graphics context for drawing to OSI.
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
					System.out.println("szivas");
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

			prevX = evt.getX();  // Save mouse coordinates.
			prevY = evt.getY();

			dragGraphics = OSI.getGraphics();
			dragGraphics.setColor(Color.black);

			dragging = true;  // Start drawing.

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

		private static final long serialVersionUID = 7952119619331504986L;
		private BufferedImage image;

		public ImagePanel() { }

		public ImagePanel(BufferedImage image) {
			this.image = image;
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
			g.dispose();
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
		}

	}
}
