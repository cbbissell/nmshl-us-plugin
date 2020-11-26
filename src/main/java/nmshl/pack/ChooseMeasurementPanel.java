//ChooseMeasurementPanel Panel.java 
//Describes panel that displays the measurement selection

package nmshl.pack;

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.nio.file.*;
import ij.io.OpenDialog;
import ij.gui.*;

public class ChooseMeasurementPanel extends JPanel implements ActionListener {
	private JButton m0Btn;
    private JButton m1Btn;
    private JButton m2Btn;
    private JButton m3Btn;
    private JButton m4Btn;
    private JButton m5Btn;
    
    private volatile boolean answered0 = false;
    private int chosenBtn0 = -1;

    public ChooseMeasurementPanel() {  
    	    	
        //construct components
        m0Btn = new JButton ("<html><center>Muscle Thickness and<p>Pennation Angle<p>for MG, LG, and TA");
        m1Btn = new JButton ("<html><center>Elastography");
        m2Btn = new JButton ("<html><center>Total Area Sweep of<p>Gastroc and the TA<p>OR<p>Echo Intensity for TA");
        m3Btn = new JButton ("<html><center>Echo intensity for MG<p>and LG on Whole<p>Sweep");
        m4Btn = new JButton ("<html><center>Cross-Sectional Echo<p>Intensity");
        m5Btn = new JButton ("<html><center>AUTO: <p>Cross-Sectional <p>Echo Intensity");
        
        //set margins for each component
        m0Btn.setMargin(new Insets(0, 0, 0, 0));
        m1Btn.setMargin(new Insets(0, 0, 0, 0));
        m2Btn.setMargin(new Insets(0, 0, 0, 0));
        m3Btn.setMargin(new Insets(0, 0, 0, 0));
        m4Btn.setMargin(new Insets(0, 0, 0, 0));
        m5Btn.setMargin(new Insets(0, 0, 0, 0));
        
        //sets fonts for all buttons
        m0Btn.setFont(new Font("Arial", Font.PLAIN, 12));
        m1Btn.setFont(new Font("Arial", Font.PLAIN, 12));
        m2Btn.setFont(new Font("Arial", Font.PLAIN, 12));
        m3Btn.setFont(new Font("Arial", Font.PLAIN, 12));
        m4Btn.setFont(new Font("Arial", Font.PLAIN, 12));
        m5Btn.setFont(new Font("Arial", Font.PLAIN, 12));
        
        //create custom color and set the components to that color
        Color lightGray = new Color(234, 234, 234);
        m0Btn.setBackground(lightGray);
        m1Btn.setBackground(lightGray);
        m2Btn.setBackground(lightGray);
        m3Btn.setBackground(lightGray);
        m4Btn.setBackground(lightGray);
        m5Btn.setBackground(lightGray);

        //adjust size and set layout
        //setPreferredSize (new Dimension (669, 68));
        setLayout (null);
        
        //Creates tooltips for each button to display when the buttons are hovered over
        m0Btn.setToolTipText ("Muscle Thickness and Pennation Angle for MG, LG, and TA");
        m1Btn.setToolTipText ("Elastography");
        m2Btn.setToolTipText ("Total Area Sweep of Gastroc and the TA / Echo Intensity for TA");
        m3Btn.setToolTipText ("Echo intensity for MG and LG on Whole Sweep");
        m4Btn.setToolTipText ("Cross-Sectional Echo Intensity");
        m5Btn.setToolTipText ("Auto Testing");
        
        // set action commands for all buttons
        m0Btn.setActionCommand("m0");
        m1Btn.setActionCommand("m1");
        m2Btn.setActionCommand("m2");
        m3Btn.setActionCommand("m3");
        m4Btn.setActionCommand("m4");
        m5Btn.setActionCommand("m5");
        
        //Listen for actions from the buttons
        m0Btn.addActionListener(this);
        m1Btn.addActionListener(this);
        m2Btn.addActionListener(this);
        m3Btn.addActionListener(this);
        m4Btn.addActionListener(this);
        m5Btn.addActionListener(this);
        
        /*
        FlowLayout newLayout = new FlowLayout(FlowLayout.LEFT);
        newLayout.setVgap(1);
        newLayout.setHgap(1);
        setLayout(newLayout);
        */
        
        GridLayout newLayout = new GridLayout();
        newLayout.setVgap(1);
        newLayout.setHgap(1);
        setLayout(newLayout);
        
        //m0Btn.setPreferredSize(new Dimension (169,70));
        //m2Btn.setPreferredSize(new Dimension (169,70));
        //m3Btn.setPreferredSize(new Dimension (169,70));
        //m4Btn.setPreferredSize(new Dimension (169,70));
        //m5Btn.setPreferredSize(new Dimension (169,70));
        
        setPreferredSize(new Dimension(675,70));
        
        add(m4Btn);
        add(m2Btn);
        add(m3Btn);
        add(m0Btn);
        add(m5Btn);
    }
    
    /*
     * Defines the different actions performed when any of the buttons are pressed
     */
    public void actionPerformed(ActionEvent e) {
        if ("m0".equals(e.getActionCommand())) {
        	chosenBtn0 = 0;
        	answered0 = true;
            m0Btn.setEnabled(false);
            m1Btn.setEnabled(true);
            m2Btn.setEnabled(true);
            m3Btn.setEnabled(true);
            m4Btn.setEnabled(true);
            m5Btn.setEnabled(true);
        } else  if ("m1".equals(e.getActionCommand())){
        	chosenBtn0 = 1;
        	answered0 = true;
            m0Btn.setEnabled(true);
            m1Btn.setEnabled(false);
            m2Btn.setEnabled(true);
            m3Btn.setEnabled(true);
            m4Btn.setEnabled(true);
            m5Btn.setEnabled(true);
        } else  if ("m2".equals(e.getActionCommand())){
        	chosenBtn0 = 2;
        	answered0 = true;
            m0Btn.setEnabled(true);
            m1Btn.setEnabled(true);
            m2Btn.setEnabled(false);
            m3Btn.setEnabled(true);
            m4Btn.setEnabled(true);
            m5Btn.setEnabled(true);
        } else  if ("m3".equals(e.getActionCommand())){
        	chosenBtn0 = 3;
        	answered0 = true;
            m0Btn.setEnabled(true);
            m1Btn.setEnabled(true);
            m2Btn.setEnabled(true);
            m3Btn.setEnabled(false);
            m4Btn.setEnabled(true);
            m5Btn.setEnabled(true);
        } else  if ("m4".equals(e.getActionCommand())){
        	chosenBtn0 = 4;
        	answered0 = true;
            m0Btn.setEnabled(true);
            m1Btn.setEnabled(true);
            m2Btn.setEnabled(true);
            m3Btn.setEnabled(true);
            m4Btn.setEnabled(false);
            m5Btn.setEnabled(true);
        } else if("m5".equals(e.getActionCommand())){
        	chosenBtn0 = 5;
        	answered0 = true;
            m0Btn.setEnabled(true);
            m1Btn.setEnabled(true);
            m2Btn.setEnabled(true);
            m3Btn.setEnabled(true);
            m4Btn.setEnabled(true);
            m5Btn.setEnabled(false);
        }
    }
    
    /*
     * Returns the value fo answered0
     */
    public boolean isAnswered() {
    	return(answered0);
    }
    
    /*
     * Sets answered0 equal to value
     */
    public void setAnswered(boolean value) {
    	answered0 = value;
    }
    
    /*
     * Calls setEnabled(false) for all buttons in the panel
     */
    public void disableAllBtn() {
    	m0Btn.setEnabled(false);
        m1Btn.setEnabled(false);
        m2Btn.setEnabled(false);
        m3Btn.setEnabled(false);
        m4Btn.setEnabled(false);
        m5Btn.setEnabled(false);
    }
    
    /*
     * Calls setEnabled(true) for all buttons in the panel
     */
    public void enableAllBtn() {
    	m0Btn.setEnabled(true);
        m1Btn.setEnabled(true);
        m2Btn.setEnabled(true);
        m3Btn.setEnabled(true);
        m4Btn.setEnabled(true);
        m5Btn.setEnabled(true);
    }
    
    /*
     * Returns the value of chosenBtn0
     */
    public int getChosenBtn0() {
    	return(chosenBtn0);
    }
    
    /*
     * Resets local variables
     */
    public void resetPanel() {
    	chosenBtn0 = -1;   
    	answered0 = false;
    }
}
