package drawing_interface;

import javax.swing.*;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;
 
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
 
public class Drawtable extends JPanel 
                           implements ActionListener {
    private JTextArea drawArea;
    private JButton button1, button2;
    //private MyDrawPanel drawPanel;
    final static String newline = "\n";
 
    public Drawtable() {
        super(new GridBagLayout());
        GridBagLayout gridbag = (GridBagLayout)getLayout();
        GridBagConstraints c = new GridBagConstraints();
 
        JLabel label = null;
 
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        label = new JLabel("Draw here please");
        gridbag.setConstraints(label, c);
        add(label);
 
        c.weighty = 1.0;
        drawArea = new JTextArea();
        drawArea.setEditable(false);
        JScrollPane topScrollPane = new JScrollPane(drawArea);
        Dimension preferredSize = new Dimension(200, 75);
        topScrollPane.setPreferredSize(preferredSize);
        gridbag.setConstraints(topScrollPane, c);
        add(topScrollPane);
 
        c.weightx = 0.0;
        c.weighty = 0.0;
       
 
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 1;
        c.insets = new Insets(10, 10, 0, 10);
        button1 = new JButton("Blah blah blah");
        gridbag.setConstraints(button1, c);
        add(button1);
 
        c.gridwidth = GridBagConstraints.REMAINDER;
        button2 = new JButton("You don't say!");
        gridbag.setConstraints(button2, c);
        add(button2);
 
        button1.addActionListener(this);
        button2.addActionListener(this);
 
 
        setPreferredSize(new Dimension(450, 450));
        setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(
                                                1,1,2,2,Color.black),
                                BorderFactory.createEmptyBorder(5,5,5,5)));
    }
 
    public void actionPerformed(ActionEvent e) {
        drawArea.append(e.getActionCommand() + newline);
        drawArea.setCaretPosition(drawArea.getDocument().getLength());
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MultiListener");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new Drawtable();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
 
class Eavesdropper implements ActionListener {
    JTextArea myTextArea;
    public Eavesdropper(JTextArea ta) {
        myTextArea = ta;
    }
 
    public void actionPerformed(ActionEvent e) {
        myTextArea.append(e.getActionCommand()
                        + Drawtable.newline);
        myTextArea.setCaretPosition(myTextArea.getDocument().getLength());
    }
    
	private class MyDrawPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private DrawingTest drawingTest;

		public MyDrawPanel(DrawingTest drawingTest) {
			this.drawingTest = drawingTest;
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.BLUE);
			for (int i = 1; i < drawingTest.getPoints().size(); i++) {
				Point p1 = drawingTest.getPoints().get(i - 1);
				Point p2 = drawingTest.getPoints().get(i);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}
}