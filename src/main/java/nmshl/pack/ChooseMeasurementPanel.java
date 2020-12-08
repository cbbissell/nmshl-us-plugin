//ChooseMeasurementPanel Panel.java 
//Describes panel that displays the measurement selection

package nmshl.pack;

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import ij.io.OpenDialog;
import ij.gui.*;

public class ChooseMeasurementPanel extends JPanel implements ActionListener {
    private ArrayList<JButton> btnList = new ArrayList<JButton>();
	private JButton m0Btn;
    private JButton m1Btn;
    private JButton m2Btn;
    private JButton m3Btn;
    private JButton m4Btn;
    private JButton m5Btn;
    private JButton m6Btn;
    
    private volatile boolean answered0 = false;
    private int chosenBtn0 = -1;

    public ChooseMeasurementPanel() {  
    	    	
        //construct components
        m0Btn = new JButton ("<html><center>Muscle Thickness and<p>Pennation Angle<p>for MG, LG, and TA");
        m1Btn = new JButton ("<html><center>Elastography");
        m2Btn = new JButton ("<html><center>Total Area Sweep <p>of Gastroc <p>and the TA");
        m3Btn = new JButton ("<html><center>Echo intensity for MG<p>and LG on Whole<p>Sweep");
        m4Btn = new JButton ("<html><center>Cross-Sectional Echo<p>Intensity");
        m5Btn = new JButton ("<html><center>AUTO: <p>Cross-Sectional <p>Echo Intensity");
        m6Btn = new JButton ("<html><center>Echo Intensity <p>for TA");

        addBtn(m0Btn, "Muscle Thickness and Pennation Angle for MG, LG, and TA", "m0");
        addBtn(m1Btn, "Elastography", "m1");
        addBtn(m2Btn, "Total Area Sweep of Gastroc and the TA / Echo Intensity for TA", "m2");
        addBtn(m3Btn, "Echo intensity for MG and LG on Whole Sweep", "m3");
        addBtn(m4Btn, "Cross-Sectional Echo Intensity", "m4");
        addBtn(m5Btn, "Auto Testing", "m5");
        addBtn(m6Btn, "Echo Intensity for TA", "m6");


        setLayout(null);
        
        GridLayout newLayout = new GridLayout();
        newLayout.setVgap(1);
        newLayout.setHgap(1);
        setLayout(newLayout);
        
        setPreferredSize(new Dimension(700,70));
        
        add(m4Btn);
        add(m2Btn);
        add(m6Btn);
        add(m3Btn);
        add(m0Btn);
        add(m1Btn);
        add(m5Btn);
    }

    private void addBtn(JButton btn, String tooltip, String actionCommand){
        btnList.add(btn);
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        Color lightGray = new Color(234, 234, 234);
        btn.setBackground(lightGray);
        btn.setToolTipText (tooltip);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(this);
    }
    /*
     * Defines the different actions performed when any of the buttons are pressed
     */
    public void actionPerformed(ActionEvent e) {
        if(!e.getActionCommand().equals(null)){
            chosenBtn0 = Integer.parseInt(e.getActionCommand().substring(1));
            answered0 = true;
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
    	for(JButton btn : btnList){
    	    btn.setEnabled(false);
        }
    }
    
    /*
     * Calls setEnabled(true) for all buttons in the panel
     */
    public void enableAllBtn() {
        for(JButton btn : btnList){
            btn.setEnabled(true);
        }
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
        disableAllBtn();
    	chosenBtn0 = -1;   
    	answered0 = false;
    }
}
