//AutoPanel.java 
//Describes panel that displays controls for the automatic processing of Echo Intensity on Whole Sweep

package nmshl.pack;

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Toolbar;
import ij.plugin.Macro_Runner;
import ij.process.ByteProcessor;
import nmshl.pack.*;
import java.io.*;

public class AutoPanel extends JPanel implements ActionListener {
    private JButton selectBtn;
    private JButton cutBtn;
    private JButton midpointBtn;
    private JButton calcBtn;
    private JButton addBtn;
    private JButton subBtn;
    private int choice = -1;

    public AutoPanel() {    	
        //construct components 
        selectBtn = new JButton ("<html><center>Selection Tool");
        cutBtn = new JButton ("<html><center>Cut Tool");
        midpointBtn = new JButton ("<html><center>Place Midpoint");
        calcBtn = new JButton ("<html><center>Calculate");
        addBtn = new JButton ("<html><center>+");
        subBtn = new JButton ("<html><center>-");
        
        //set margins for each component
        selectBtn.setMargin(new Insets(0, 0, 0, 0));
        cutBtn.setMargin(new Insets(0, 0, 0, 0));
        midpointBtn.setMargin(new Insets(0, 0, 0, 0));
        calcBtn.setMargin(new Insets(0, 0, 0, 0));
        addBtn.setMargin(new Insets(0, 0, 0, 0));
        subBtn.setMargin(new Insets(0, 0, 0, 0));
                
        //sets fonts for all buttons
        selectBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        cutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        midpointBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        calcBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        addBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        subBtn.setFont(new Font("Arial", Font.PLAIN, 16));
                
        //create custom color and set the components to that color
        Color lightGray = new Color(234, 234, 234);
        selectBtn.setBackground(lightGray);
        cutBtn.setBackground(lightGray);
        midpointBtn.setBackground(lightGray);
        calcBtn.setBackground(lightGray);
        addBtn.setBackground(lightGray);
        subBtn.setBackground(lightGray);

        // set action commands for all buttons
        selectBtn.setActionCommand("select");
        cutBtn.setActionCommand("cut");
        midpointBtn.setActionCommand("midpoint");
        calcBtn.setActionCommand("calc");
        addBtn.setActionCommand("add");
        subBtn.setActionCommand("sub");
        
        //Listen for actions from the buttons
        selectBtn.addActionListener(this);
        cutBtn.addActionListener(this);
        midpointBtn.addActionListener(this);
        calcBtn.addActionListener(this);
        addBtn.addActionListener(this);
        subBtn.addActionListener(this);
                
        //Sets size of each button
        selectBtn.setPreferredSize(new Dimension(100, 40));
        cutBtn.setPreferredSize(new Dimension(100, 40));
        midpointBtn.setPreferredSize(new Dimension(100, 40));
        calcBtn.setPreferredSize(new Dimension(100, 40));
        addBtn.setPreferredSize(new Dimension(49, 19));
        subBtn.setPreferredSize(new Dimension(49, 19));
        
        JPanel p1 = new JPanel(); 
        JPanel p2 = new JPanel(); 
        
        BorderLayout layout1 = new BorderLayout();
        layout1.setHgap(1);
        layout1.setVgap(1);
        setLayout(layout1);
        
        FlowLayout layout2 = new FlowLayout(FlowLayout.LEADING);
        layout2.setHgap(0);
        layout2.setVgap(0);
        p1.setLayout(layout2);
        p2.setLayout(layout2);
        
        p1.add(selectBtn);
        p1.add(cutBtn);
        p1.add(midpointBtn);
        
        p2.add(addBtn);
        p2.add(subBtn);
    	
    	add(p1, BorderLayout.NORTH);
    	add(p2, BorderLayout.CENTER);
    	add(calcBtn, BorderLayout.WEST);
    	setPreferredSize(new Dimension(300,60));
    	
    	setVisible(true);
    }
    
    /*
     * Defines the different actions performed when any of the buttons are pressed
     */
    public void actionPerformed(ActionEvent e) {
        if ("select".equals(e.getActionCommand())){
        	resetBtnText();
        	if(choice != 0) {
        		selectBtn.setText("<html><center>Selecting...");
        	}
        	choice = 0;
        	
        } else if ("cut".equals(e.getActionCommand())){
        	resetBtnText();
        	if(choice != 1) {
        		cutBtn.setText("<html><center>Cutting...");
        	}
        	choice = 1;
        	
        } else if ("midpoint".equals(e.getActionCommand())){
        	choice = 2;
        	
        } else if ("calc".equals(e.getActionCommand())){
        	choice = 3;
        	
        } else if ("add".equals(e.getActionCommand())){
        	choice = 4;
        	
        } else if ("sub".equals(e.getActionCommand())){
        	choice = 5;
        	
        }
    }
    
    private void resetBtnText() {
    	selectBtn.setText("<html><center>Selection Tool");
    	cutBtn.setText("<html><center>Cut Tool");
    }
    
    /*
     * Selects the "wand" tool and waits for the user to make a selection. 
     * Then calls a macro that does nothing if the selection is black, switches gray values to white, or switches white values to gray.
     * Continues to run until the selectBtn is pressed again, or the AutoPanel is closed.
     */
    public void selectBodies() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	selectBtn.setEnabled(false);
    	
    	IJ.setTool(Toolbar.WAND);
		ImagePlus imagePlus = (IJ.getImage());
		
		while(choice == 0 && selectBtn.getText().equals("<html><center>Selecting...")) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
									 	 + File.separator + "fillROI.ijm", "");		
		}
		
		selectBtn.setText("<html><center>Selection Tool");
		selectBtn.setEnabled(true);
		IJ.setTool(Toolbar.CROSSHAIR);
		if(choice == 0) {
			choice = -1;
		}
    }
    
    /*
     * When toggled, runs macro that allows the user to color over unwanted pieces of the processed image.
     * Continues to run until the selectBtn is pressed again, or the AutoPanel is closed.
     */
    public void cutBodies() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	cutBtn.setEnabled(false);
    	
    	macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
			 	 + File.separator + "doCut.ijm", "");
    	
    	while(choice == 1 && cutBtn.getText().equals("<html><center>Cutting...")) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	Toolbar.setForegroundColor(new Color(255, 255, 255));
    	cutBtn.setText("<html><center>Cut Tool");
    	cutBtn.setEnabled(true);
    	IJ.setTool(Toolbar.CROSSHAIR);
    	if(choice == 1) {
    		choice = -1;
    	}
    }
    
    /*
     * Calls a macro that prompts the user to place a point on the midpoint of the muscle.
     * Macro takes the midpoint and draws vectors extending from this midpoint in all directions,
     * until the pixel underneath the vector has a grayscale value of 125. At which point the point is recorded.
     * Once all degrees have been checked, a selection containing all points found is created.
     * This selection is meant to outline the muscle desired to be measured.
     */
    public void placeMidpoint() {
    	String[] titles = WindowManager.getImageTitles();
    	for(int i = 0; i < titles.length; i++) {
    		System.out.println(titles[i]);
    	}
    	
    	//ImagePlus recentImg = WindowManager.getImage(titles[titles.length-1]);
    	ImagePlus recentImg = WindowManager.getImage("preprocessed_img");
    	if(recentImg == null) {
    		choice = -1;
			return;
    	}
    	WindowManager.setTempCurrentImage(recentImg);
    	
    	ByteProcessor newProcessor;
    	try {
    		newProcessor = (ByteProcessor)(IJ.getImage().getProcessor());
		} catch (Exception e) {
			choice = -1;
			return;
		}
    	
    	//ByteProcessor newProcessor = (ByteProcessor)(IJ.getImage().getProcessor());
    	int height = newProcessor.getHeight();
    	int width = newProcessor.getWidth();
    	int[] xVals = new int[height*2];
    	int[] yVals = new int[height*2];
    	int count = 0;
    	
    	for(int i = 0; i < height; i++) {
    		int firstX = -1;
        	int firstY = -1;
        	int secondX = -1;
        	int secondY = -1;
    		for(int j = 0; j < width; j++) {
    			if(newProcessor.getPixel(j, i) > 10 && newProcessor.getPixel(j, i) < 200)  {
    				if(newProcessor.getPixel(j + 1, i) < 10) {
    					firstX = j;
    					firstY = i;
    					j++;
    					while(j < width) {
    						if(newProcessor.getPixel(j, i) > 10 && newProcessor.getPixel(j, i) < 200) {
    							secondX = j;
    							secondY = i;
    							j = width;
    						} else {
    							j++;
    						}
    					}
    				}
    			}
    			
    			/*
    			if(newProcessor.getPixel(j, i) > 200) {
        			System.out.print("1");
    			} else if (newProcessor.getPixel(j, i) > 10) {
        			System.out.print("2");
    			} else {
    				System.out.print("0");
    			} */
    		}
    		if(firstX != -1 && secondX != -1) {
    			xVals[count] = firstX;
    			xVals[count+1] = secondX;
    			yVals[count] = firstY;
    			yVals[count+1] = secondY;
    			count += 2;
    			//newProcessor.set(firstX, firstY, 255);
    			//newProcessor.set(secondX, secondY, 255);
    		}
    	}
    	
    	int minX = xVals[0];
    	int maxX = xVals[0];
    	int minY = yVals[0];
    	int maxY = yVals[0];
    	
    	for(int i = 1; i < count; i++) {
    		if(xVals[i] < minX) {
    			minX = xVals[i];
    		} else if(xVals[i] > maxX) {
    			maxX = xVals[i];
    		}
    		if(yVals[i] < minY) {
    			minY = yVals[i];
    		} else if(yVals[i] > maxY) {
    			maxY = yVals[i];
    		}
    	}
    	
    	//newProcessor.set((xVals[(int)(0)]), yVals[(int)(0)], 100);
    	IJ.makeRectangle(minX, minY, maxX - minX, maxY - minY);
    	//IJ.makePoint((xVals[(int)(0)]), yVals[(int)(0)]);
    	
    			
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	String midpoint = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
    											   + File.separator + "placeMidpoint.ijm", "");
    	
    	choice = -1;
    	
    	/*
    	resetBtnText();
    	disableBtns();
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	String midpoint = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
    											   + File.separator + "placeMidpoint.ijm", "");	
    	
    	enableBtns();
    	choice = -1;
    	*/
    }
    
    public void doCalculate() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	String result = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
    											   + File.separator + "calculate.ijm", "");	
    	
    	if(result != null && !result.equals("[aborted]")) {
			NMSHLPlugin.tablePanel.storeValue(new double[] {Double.parseDouble(result)}, new String[] {"Echo Intensity"});
		}
    	choice = -1;
    }
    
    public void addFilter() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	String result = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
    											   + File.separator + "addFilter.ijm", "");	

    	choice = -1;
    }
    
    public void subFilter() {
    	String currentDirectory = System.getProperty("user.dir");
    	Macro_Runner macroRunner = new Macro_Runner();
    	String result = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
    											   + File.separator + "subFilter.ijm", "");	

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
    	 addBtn.setEnabled(false);
    	 subBtn.setEnabled(false);
    }
    
    /*
     * Calls setEnabled(true) for all buttons in the panel
     */
    public void enableBtns() {
    	selectBtn.setEnabled(true);
   	 	cutBtn.setEnabled(true);
   	 	midpointBtn.setEnabled(true);
   	 	calcBtn.setEnabled(true); 
   	 	addBtn.setEnabled(true);
   	 	subBtn.setEnabled(true);
    }
    
    /*
     * Resets necessary names and variables prior to close
     */
    public void handleClose() {
    	if(WindowManager.getImageCount() > 0) {
        	ImagePlus imagePlus = (WindowManager.getCurrentImage());
        	imagePlus.close();
        }
    	selectBtn.setText("<html><center>Selection Tool");
    	cutBtn.setText("<html><center>Cut Tool");
    	choice = -2;
    }
}
