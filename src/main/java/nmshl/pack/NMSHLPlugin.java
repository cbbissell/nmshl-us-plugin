package nmshl.pack;

import ij.*;
import ij.plugin.*;
import ij.process.*;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import javax.swing.*;
import java.io.File;

public class NMSHLPlugin extends ImageJ implements PlugIn {
	static ImageJ plug;
	boolean moreMeasurements = true;
	static String fileName;
	static JFrame measurementFrame = new JFrame ("Measurement Types");
	static ChooseMeasurementPanel measurementPanel = new ChooseMeasurementPanel();
	static JFrame tableFrame = new JFrame ("Results Table");
	static ResultsPanel tablePanel = new ResultsPanel();
	static String imagePath = null;
	boolean firstRun = true;
	static int choice;
	static String imageName;
	static boolean runAgain = true;
	
	static String sideValue;
	static String muscleType;
	static String percentValue;
	static String csValue;
	static String angleValue;
	static String[] muscleNameArray = new String[] {sideValue, muscleType, percentValue, csValue, angleValue};
	
	static boolean autoFrameClosed = true; //tells whether or not the autoFrame is currently open or closed
	
	public void run(String arg) {
		/*	//Re-add this section if a file selection window is ever desired
		OpenDialog openFile = new OpenDialog("Choose a file");
		String fileDirectory = openFile.getDirectory();
		fileName = openFile.getFileName();
		if(fileDirectory == null) {
			System.err.println("null file directory");
			return;
		}
		
		System.out.println(fileDirectory + fileName);
		
		Opener fileOpener = new Opener();
		fileOpener.open(fileDirectory + fileName);
		*/
		
		measurementPanel.resetPanel();
		
		if(firstRun) { //Performs certain setup functions if this is the first call of run()
			measurementFrame.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
			measurementFrame.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			    	handleClosing();
			    }
			});
			
			tableFrame.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
			tableFrame.addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			    	handleClosing();
			    }
			});
			
			measurementFrame.add(measurementPanel, BorderLayout.CENTER);
			measurementPanel.disableAllBtn();
			measurementFrame.pack();
			
			tableFrame.add(tablePanel, BorderLayout.CENTER);
			tableFrame.pack();
			
			Dimension plugDimension = plug.getSize();
			Point plugPoint = plug.getLocation();
			measurementFrame.setResizable(false);
			measurementFrame.setLocation((int)(plugPoint.x) + (int)(plugDimension.width),  (int)(plugPoint.y));
			//measurementFrame.setMaximumSize(new Dimension(200, 200)); //size of measurementFrame randomly increased during runtime, setting maximum size to smaller than the panel seems to fix that
			
			Dimension measureDimension = measurementFrame.getSize();
			Point measurePoint = measurementFrame.getLocation();
			tableFrame.setLocation((int)(measurePoint.x),  (int)(plugPoint.y) + (int)(measureDimension.height));
			tableFrame.setMinimumSize(new Dimension(686, 100));
						
			measurementFrame.setVisible (true);   
			tableFrame.setVisible (true); 
			
			firstRun = false;
		}
		
		while(WindowManager.getImageCount() == 0) { //Waits for user to open an image
			if((this.getFrames()).length == 0){
				System.exit(0);
			}
		}
		
		imagePath = IJ.getDirectory("image");
		
		
		imageName = readImageName();
		choice = askMeasurement();
		
        switch(choice) {
        case 0:
        	m0();
        	break;
        case 1:
        	m1();
        	break;
        case 2:
        	m2();
        	break;
        case 3:
        	m3();
        	break;
        case 4:
        	m4();
        	break;
        case 5:
        	m5();
        	break;
        default: 
        	break;
        }
        
        //reset the muscleNameArray
        for(int i = 0; i < muscleNameArray.length; i++) {
        	muscleNameArray[i] = null;
        }
	}
	
	public void windowClosing(WindowEvent e) {
		handleClosing();
		super.windowClosing(e);
	}
	
	/*
	 * Prompts the user to export the data table prior to closing any of the windows
	 */
	private void handleClosing() {
		String ObjButtons[] = {"Yes","No"};
        int PromptResult = JOptionPane.showOptionDialog(null, 
            "Export results table before closing?", "Export?", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
            ObjButtons,ObjButtons[1]);
        if(PromptResult==0) {
        	tablePanel.exportResults();
        	System.exit(0);          
        } else {
        	System.exit(0);
        }
	}
	
	/*
	 * Creates a modal window that asks the user to select a measurement type
	 */
	public int askMeasurement() { 
		measurementPanel.enableAllBtn();
        measurementFrame.toFront();
        while (!measurementPanel.isAnswered());
        measurementPanel.disableAllBtn();
        int choice = measurementPanel.getChosenBtn0();
        return(choice);       
	}
	
	/*
	 * Describes the measuring protocol for "Muscle Thickness and Pennation Angle for MG, LG, and TA"
	 */
	public void m0() {
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result =  macroRunner.runMacroFile(currentDirectory + File.separator + "resources" + File.separator + "macros" + File.separator + "Muscle Thickness and Pennation Angle for MG, LG, and TA.ijm", "");
		if(result != null && !result.equals("[aborted]")) {
			String[] splitResults = result.split(" ");
			String[] types = new String[splitResults.length];
			double[] dResults = new double[splitResults.length];
			for(int i = 0; i < splitResults.length; i++) { //Creates two arrays to be used by the storeValue() method in ResultsPanel
				dResults[i] = Double.parseDouble(splitResults[i]); //first array contains the results values as doubles
				if(i == 0) {
					types[i] = "Muscle Thickness"; //second array is filled with the names of the measurements taken
				} else {
					types[i] = "Pennation Angle" + i; 
				}
			}	
			tablePanel.storeValue(dResults, types);
		}
	}
	
	/*
	 * Describes the measuring protocol for "Elastography". Not functional currently as the Elastography button is never placed in the panel
	 */
	public void m1() {
		measurementPanel.disableAllBtn();
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result = macroRunner.runMacroFile(currentDirectory + File.separator + "resources" + File.separator + "macros" + File.separator + "Elastography.ijm", "");
	}
	
	/*
	 * Describes the measuring protocol for "Total Area Sweep of Gastroc and the TA"
	 */
	public void m2() {
		measurementPanel.disableAllBtn();
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result = macroRunner.runMacroFile(currentDirectory + File.separator + "resources" + File.separator + "macros" + File.separator + "Total Area Sweep of Gastroc and the TA.ijm", "");
		if(result != null && !result.equals("[aborted]")) { //Creates two arrays to be used by the storeValue() method in ResultsPanel
			String[] splitResults = result.split(" ");
			String[] types = new String[] {"Total Area", "Total Mean"};
			double[] dResults = new double[splitResults.length];
			for(int i = 0; i < splitResults.length; i++) {
				dResults[i] = Double.parseDouble(splitResults[i]);
			}
			tablePanel.storeValue(dResults, types);
		}
	}
	
	/*
	 * Describes the measuring protocol for "Echo intensity for MG and LG on Whole Sweep"
	 */
	public void m3() {
		measurementPanel.disableAllBtn();
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "Echo intensity for MG and LG on Whole Sweep.ijm", "");
		if(result != null && !result.equals("[aborted]")) { //Creates two arrays to be used by the storeValue() method in ResultsPanel
			String[] splitResults = result.split(" ");
			String[] types = new String[] {"Medial Area", "Medial E.I.", "Lateral Area", "Lateral E.I."};
			double[] dResults = new double[splitResults.length];
			for(int i = 0; i < splitResults.length; i++) {
				dResults[i] = Double.parseDouble(splitResults[i]);
			}
			tablePanel.storeValue(dResults, types);
		}
	}
	
	/*
	 * Describes the measuring protocol for "Cross-Sectional Echo Intensity"
	 */
	public void m4() {
		measurementPanel.disableAllBtn();
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result = macroRunner.runMacroFile(currentDirectory + File.separator + "resources" + File.separator + "macros" + File.separator + "Cross-Sectional Echo Intensity.ijm", "");
		if(result != null && !result.equals("[aborted]")) { //Creates two arrays to be used by the storeValue() method in ResultsPanel
			tablePanel.storeValue(new double[] {Double.parseDouble(result)}, new String[] {"Echo Intensity"});
		}
	}	
	
	/*
	 * Performs semi-automatic measurement of Cross-Sectional Echo Intensity.
	 * Opens a new AutoPanel that contains controls for semi-automated processing. 
	 */
	public void m5() {
		measurementPanel.disableAllBtn();
		autoFrameClosed = false;
		JFrame autoFrame = new JFrame ("Cross-Sectional - Auto");
		AutoPanel autoPanel = new AutoPanel();
		
		autoFrame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	System.out.println("exited");
		    	autoPanel.handleClose();
		    	autoFrameClosed = true;
		    }
		});	
		
		autoFrame.add(autoPanel, BorderLayout.CENTER);
		autoFrame.pack();
		Point tablePoint = tableFrame.getLocation();
		Dimension tableDimension = tableFrame.getSize();
		autoFrame.setLocation((int)(tablePoint.x) + ((int)(tableDimension.width) - 315),  (int)(tablePoint.y) + (int)(tableDimension.height));
		autoFrame.setResizable(false);
		autoFrame.setVisible (true);	
		
		for(int i = 0; i < muscleNameArray.length; i++) {
			System.out.print(muscleNameArray[i] + ", ");
		}
		System.out.println("");
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator + "automated" 
												 + File.separator + "preprocess.ijm", "");
		
		while(autoFrameClosed == false) { //holds the thread until the user makes a selection in the AutoPanel
			while(autoPanel.getChoice() == -1) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			switch(autoPanel.getChoice()) {
			case 0:
				autoPanel.selectBodies();
				break;
			case 1:
				autoPanel.cutBodies();
				break;
			case 2:
				autoPanel.placeMidpoint();
				break;
			case 3:
				autoPanel.doCalculate();
				break;
			case 4:
				autoPanel.addFilter();
				break;
			case 5:
				autoPanel.subFilter();
				break;
			}
		}
	}

	/*
	 * Describes the measuring protocol for "Echo Intensity for TA"
	 */
	public void m6() {
		measurementPanel.disableAllBtn();
		Macro_Runner macroRunner = new Macro_Runner();
		String currentDirectory = System.getProperty("user.dir");
		String result = macroRunner.runMacroFile(currentDirectory + File.separator + "resources" + File.separator + "macros" + File.separator + "Total Area Sweep of Gastroc and the TA.ijm", "");
		if(result != null && !result.equals("[aborted]")) { //Creates two arrays to be used by the storeValue() method in ResultsPanel
			String[] splitResults = result.split(" ");
			String[] types = new String[] {"Total Area", "Total Mean"};
			double[] dResults = new double[splitResults.length];
			for(int i = 0; i < splitResults.length; i++) {
				dResults[i] = Double.parseDouble(splitResults[i]);
			}
			tablePanel.storeValue(dResults, types);
		}
	}
	
	/*
	 * Returns the value of runAgain
	 */
	private static boolean runAgainCheck() {
		return(runAgain);
	}
	
	/*
	 * Returns the value of imagePath
	 */
	public static String getImagePath() {
		return imagePath;
	}
	
	/*
	 * Method that allows for calling readImageName(ImagePlus) without a parameter
	 */
	public static String readImageName() {
		return(readImageName(IJ.getImage()));
	}
	
	/*
	 * Takes in an array of pixels from the image currently being processed and find the correct set of letters
	 * that matches the array of pixels
	 */
	public static String readImageName(ImagePlus img) { 
		String imageName = "";
		int[] pixelRow = new int[500];
		String stringRow = "";
		float currentPixel;
		ColorProcessor newProcessor = (ColorProcessor)(img.getProcessor());
		int[] rgb = new int[3];
		
		for(int i = 100; i < 600; i++) { //loops through a single line of pixels
			rgb = newProcessor.getPixel(i, 738, (int[])(newProcessor.getPixels()));
			currentPixel = newProcessor.getPixelValue(i, 738);
			if(currentPixel > 50 && (rgb[0] > (rgb[1] * 0.65) && rgb[2] > (rgb[1] * 0.65))) { //stores the value of the pixel as 1 if it is above the threshold]
				//System.out.println(i + ": " + currentPixel + " R: " + rgb[0] + " G: " + rgb[1] + " B: " + rgb[2]);
				pixelRow[i-100] = 1;
			} else {
				pixelRow[i-100] = 0; //stores the value of the pixel as 0 if it is below the threshold
			}
		}
		
		
		/*
		for(int i = 100; i < 600; i++) { //loops through a single line of pixels
			currentPixel = newProcessor.getPixelValue(i, 738);
			if(currentPixel > 100) { //stores the value of the pixel as 1 if it is above the threshold
				pixelRow[i-100] = 1;
			} else {
				pixelRow[i-100] = 0; //stores the value of the pixel as 0 if it is below the threshold
			}
		} */
		for(int i = 0; i < 500; i++) { //Loops through pixelRow[] to combine all values into a single string
			stringRow += String.valueOf(pixelRow[i]);
			//System.out.print(String.valueOf(pixelRow[i]));
		}
		//System.out.println("");
		
		int wordCount = 0;
		for(int i = 0; i < 500; i++) { // TODO NEED TO ADD ERROR HANDLING HERE
			int startIndex;
			int endIndex;
			int zeroCount = 0;
			
			if(pixelRow[i] == 1) { //finds the next index of a 1 to start the word
				startIndex = i;
				endIndex = i;
				i++;
				
				while(zeroCount < 18 && i < 500) { //loops through the rest of the pixels, stops the word if there are too many 0 values
					if(pixelRow[i] == 1) {
						endIndex = i + 1;
						zeroCount = 0;
					} else {
						zeroCount++;
					}
					i++;
				}
				imageName += recognizeLetter(stringRow.substring(startIndex, endIndex));
				wordCount++;
				/*
				if(wordCount >= 6) {
					//System.out.println(imageName);
					if((muscleNameArray[1] != null && muscleNameArray[1].contentEquals("TA")) && muscleNameArray[4] == null) {
						muscleNameArray[4] = "120";
					}
					return(imageName);
				} */
			}
		}
		//System.out.println(imageName);
		if((muscleNameArray[1] != null && muscleNameArray[1].contentEquals("TA")) && muscleNameArray[4] == null) {
			muscleNameArray[4] = "120";
		}
		System.out.println(imageName);
		return(imageName);
	}
	
	/*
	 * Compares the string "binarySeries" to multiple possible series that represent certain sets of characters.
	 * These characters were decided upon when creating a naming convention.
	 * If the binarySeries is not recognized, returns null
	 */
	private static String recognizeLetter(String binarySeries) {
		System.out.println(binarySeries);
		switch(binarySeries) {
        case "111": //L
        	muscleNameArray[0] = "L";
        	return("L_");
        //case "11111111111": //R
        case "1111111111": //R
        	muscleNameArray[0] = "R";
        	return("R_");
        //case "111100000001111000001111111111": //MG
        case "111100000001110000001111111111": //MG
        	muscleNameArray[1] = "MG";
        	return("MG_");
        case "1110000000000000001111111111": //LG
        	muscleNameArray[1] = "LG";
        	return("LG_");
        //case "11111111111111111000001111": //TA
        case "1111111111111110000000111": //TA
        	muscleNameArray[1] = "TA";
        	return("TA_");
        case "11100000000000000111111111": //LO
        	
        	return("EL_");
        //case "111100000011": //25
        case "11000000011": //25
        	muscleNameArray[2] = "25";
        	return("25_");
        //case "11000000000000011100000111": //50
        case "11000000000000011100000011": //50
        	muscleNameArray[2] = "50";
        	return("50_");
        //case "1110000011": //75
        case "1100000011": //75
        	muscleNameArray[2] = "75";
        	return("75_");
        //case "11110000011100000111": //20
        case "1100000011100000011": //20
        	muscleNameArray[2] = "20";
        	return("20_");
        //case "1111100000011100000111": //40
        case "1111000000011100000011": //40
        	muscleNameArray[2] = "40";
        	return("40_");
        case "11100000000000011100000011": //60
        	muscleNameArray[2] = "60";
        	return("60_");
        //case "1111111111100001111111111": //CS
        case "1111111111000001111111111": //CS
        	return("CS_");
        //case "1111111111100001111111111000000001111": //CSA
        case "1111111111000001111111111000000000111": //CSA
        	muscleNameArray[3] = "CSA";
        	return("CSA_");
        //case "111111111110000011111111111": //BR
        case "11111111110000001111111111": //BR
        	
        	return("BR_");
        //case "1111111111110000111": //EL
        case "1111111111100000111": //EL
        	
        	return("EL_");
        //case "111000001110000011100000111": //90
        case "110000000110000011100000011": //90
        	muscleNameArray[4] = "90";
        	return("90_");
        //case "1111111111110001111111111111": //EI
        case "111111111110000011111111111": //EI
        	
        	return("EI_");
        //case "111111111111111101110000000111": //TH
        case "11111111111111100111000000011": //TH
        	
        	return("TH_");
        case "1111111111111111101110000000111": //TH // for some reason the "T" varies by one pixel from time to time
        	
        	return("TH_");
        case "1100011": //1
        	
        	return("1_");
        case "11": //2
        	
        	return("2_");
        case "1111111111100001110000000010000001111111111": //INS
        	
        	return("INS_");
        default: 
        	System.out.println("Not Recognized: " + binarySeries);
        	return("null_");
        }
	}

	public void setupMainFrame() {
		this.exitWhenQuitting(true);
	}
	
	/*
	 * Sets up the basic ImageJ window and sets parameters for some of the other panels.
	 * Performs run() loop.
	 */
	public static void main(final String... args) {
		plug = new NMSHLPlugin();
		plug.exitWhenQuitting(true);
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        plug.setLocation((center.x) - (int)(center.x * 0.9), (center.y) - (int)(center.y * 0.9));
		
		do {
			((PlugIn)(plug)).run("");
			if(WindowManager.getImageCount() > 0) {
	        	ImagePlus imagePlus = (WindowManager.getCurrentImage());
	        	imagePlus.changes = false;
	        	imagePlus.close();
	        }
		} while(runAgainCheck());
		System.exit(0);
	}
}
