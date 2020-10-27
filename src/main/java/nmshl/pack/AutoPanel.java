//AutoPanel.java 
//Describes panel that displays controls for the automatic processing of Echo Intensity on Whole Sweep

package nmshl.pack;

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Toolbar;
import ij.plugin.Macro_Runner;
import nmshl.pack.*;
import java.io.*;

public class AutoPanel extends JPanel implements ActionListener {
    private JButton selectBtn;
    private JButton cutBtn;
    private JButton midpointBtn;
    private JButton calcBtn;
    private int choice = -1;

    public AutoPanel() {    	
        //construct components 
        selectBtn = new JButton ("<html><center>Selection Tool");
        cutBtn = new JButton ("<html><center>Cut Tool");
        midpointBtn = new JButton ("<html><center>Place Midpoint");
        calcBtn = new JButton ("<html><center>Calculate");
        
        //set margins for each component
        selectBtn.setMargin(new Insets(0, 0, 0, 0));
        cutBtn.setMargin(new Insets(0, 0, 0, 0));
        midpointBtn.setMargin(new Insets(0, 0, 0, 0));
        calcBtn.setMargin(new Insets(0, 0, 0, 0));
                
        //sets fonts for all buttons
        selectBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        cutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        midpointBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        calcBtn.setFont(new Font("Arial", Font.PLAIN, 12));
                
        //create custom color and set the components to that color
        Color lightGray = new Color(234, 234, 234);
        selectBtn.setBackground(lightGray);
        cutBtn.setBackground(lightGray);
        midpointBtn.setBackground(lightGray);
        calcBtn.setBackground(lightGray);

        // set action commands for all buttons
        selectBtn.setActionCommand("select");
        cutBtn.setActionCommand("cut");
        midpointBtn.setActionCommand("midpoint");
        calcBtn.setActionCommand("calc");
        
        //Listen for actions from the buttons
        selectBtn.addActionListener(this);
        cutBtn.addActionListener(this);
        midpointBtn.addActionListener(this);
        calcBtn.addActionListener(this);
        
        JPanel p1 = new JPanel(); 
        
        //Sets size of each button
        selectBtn.setPreferredSize(new Dimension(167, 15));
        cutBtn.setPreferredSize(new Dimension(167, 15));
        midpointBtn.setPreferredSize(new Dimension(167, 15));
        calcBtn.setPreferredSize(new Dimension(167, 15));
        
        BorderLayout layout1 = new BorderLayout();
        layout1.setHgap(1);
        layout1.setVgap(1);
        setLayout(layout1);
        
        FlowLayout layout2 = new FlowLayout(FlowLayout.LEADING);
        layout2.setHgap(0);
        layout2.setVgap(0);
        p1.setLayout(layout2);
        
    	p1.setSize(new Dimension(669,50));
        
        p1.add(selectBtn);
        p1.add(cutBtn);
        p1.add(midpointBtn);
    	
    	add(p1, BorderLayout.NORTH);
    	add(calcBtn, BorderLayout.CENTER);
    	setPreferredSize(new Dimension(501,57));
    	
    	setVisible(true);
    }
    
    /*
     * Defines the different actions performed when any of the buttons are pressed
     */
    public void actionPerformed(ActionEvent e) {
        if ("select".equals(e.getActionCommand())){
        	if(choice == -1) {
        		selectBtn.setText("<html><center>Selecting...");
        	} else {
        		selectBtn.setText("<html><center>Selection Tool");
        	}
        	choice = 0;
        	
        } else if ("cut".equals(e.getActionCommand())){
        	if(choice == -1) {
        		cutBtn.setText("<html><center>Cutting...");
        	} else {
        		cutBtn.setText("<html><center>Cut Tool");
        	}
        	choice = 1;
        	
        } else if ("midpoint".equals(e.getActionCommand())){
        	choice = 2;
        	
        } else if ("calc".equals(e.getActionCommand())){
        	choice = 3;
        }
    }
    
    /*
     * Selects the "wand" tool and waits for the user to make a selection. 
     * Then calls a macro that does nothing if the selection is black, switches gray values to white, or switches white values to gray.
     * Continues to run until the selectBtn is pressed again, or the AutoPanel is closed.
     */
    public void selectBodies() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	disableBtns();
    	selectBtn.setEnabled(true);
    	
    	IJ.setTool(Toolbar.WAND);
		ImagePlus imagePlus = (IJ.getImage());
		while(selectBtn.getText().equals("<html><center>Selecting...")) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(imagePlus.getRoi() != null) {
				macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
									 	 + File.separator + "fillROI.ijm", "");		
			}
		}
		
		enableBtns();
		choice = -1;
    }
    
    /*
     * When toggled, runs macro that allows the user to color over unwanted pieces of the processed image.
     * Continues to run until the selectBtn is pressed again, or the AutoPanel is closed.
     */
    public void cutBodies() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	disableBtns();
    	cutBtn.setEnabled(true);
    	
    	macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
			 	 + File.separator + "doCut.ijm", "");
    	
    	while(cutBtn.getText().equals("<html><center>Cutting...")) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	Toolbar.setForegroundColor(new Color(255, 255, 255));
		enableBtns();
		choice = -1;
    }
    
    /*
     * Calls a macro that prompts the user to place a point on the midpoint of the muscle.
     * Macro takes the midpoint and draws vectors extending from this midpoint in all directions,
     * until the pixel underneath the vector has a grayscale value of 125. At which point the point is recorded.
     * Once all degrees have been checked, a selection containing all points found is created.
     * This selection is meant to outline the muscle desired to be measured.
     */
    public void placeMidpoint() {
    	disableBtns();
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	String midpoint = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
    											   + File.separator + "placeMidpoint.ijm", "");	
    	
    	enableBtns();
    	choice = -1;
    }
    
    public void doCalculate() {
    	
    	
    	choice = -1;
    }
    
    /*
     * Returns the value of choice
     */
    public int getChoice() {
    	return choice;
    }
    
    /*
     * Calls setEnabled(false) for all buttons in the panel
     */
    public void disableBtns() {
    	 selectBtn.setEnabled(false);
    	 cutBtn.setEnabled(false);
    	 midpointBtn.setEnabled(false);
    	 calcBtn.setEnabled(false);
    }
    
    /*
     * Calls setEnabled(true) for all buttons in the panel
     */
    public void enableBtns() {
    	selectBtn.setEnabled(true);
   	 	cutBtn.setEnabled(true);
   	 	midpointBtn.setEnabled(true);
   	 	calcBtn.setEnabled(true);   	
    }
    
    /*
     * Resets necessary names and variables prior to close
     */
    public void handleClose() {
    	selectBtn.setText("<html><center>Selection Tool");
    	cutBtn.setText("<html><center>Cut Tool");
    	choice = -2;
    }
}
