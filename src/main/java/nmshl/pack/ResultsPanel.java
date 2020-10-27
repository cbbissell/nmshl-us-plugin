//ResultsPanel.java 
//Describes panel that displays a table containing the measured data

package nmshl.pack;

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;
import nmshl.pack.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import ij.io.OpenDialog;
import ij.io.Opener;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.*;

public class ResultsPanel extends JPanel implements ActionListener {
    private JButton importBtn;
    private JButton exportBtn;
    private JLabel placementLabel1;
    private JLabel placementLabel2;
    private JButton skipBtn;
    private JLabel placementAlert;
    private String[] defaultTable;
    private JScrollPane scrollPane;
    private JTable dataTable;
    private JButton averageBtn;
    private JButton renameBtn;
    private double currentProgress = 0;
    
    private boolean doSkip = false;

    public ResultsPanel() {    	
    	//Read the contents of default_table.txt, splits the file at each comma, and stores the split values in the defaultTable array
    	String contents = "";
    	String currentDirectory = System.getProperty("user.dir");
    	try {
    		contents = new String(Files.readAllBytes(Paths.get(currentDirectory + File.separator + "resources" + File.separator + "data" + File.separator + "default_table.txt")));
    	} catch (Exception e) {
    		System.err.print(e);
    	}
    	defaultTable = contents.split(",");
    	
    	
        //construct components
        importBtn = new JButton ("<html><center>Import Results");
        exportBtn = new JButton ("<html><center>Export Results");
        placementLabel1 = new JLabel ("<html><center>Next value to be placed: ");
        placementLabel2 = new JLabel ("<html><center>no value");
        skipBtn = new JButton ("<html><center>skip value");
        placementAlert = new JLabel ("	            	Place values in table to proceed");
        renameBtn = new JButton ("<html><center>Rename Images");
        renameBtn.setToolTipText ("Rename all images in selected folder to match each image's label");
        averageBtn = new JButton ("<html><center>Average Values");
        averageBtn.setToolTipText ("Average all columns with an \"AVG\" in their title");
        
        //gets the number of rows and columns from the default table
        String[] rowSplit = (contents.split("\n")); //splits file at each new line to get number of columns
    	String[] columnSplit = rowSplit[0].split(","); //splits a single line by commas to get number of rows
    	int cols = columnSplit.length - 1;
    	int rows = rowSplit.length;
    	
    	//creates new data table using the dimensions found from the default table file
        dataTable = new JTable(rows, cols);        
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //sets the default width for all of the columns
        for(int i = 0; i < cols; i++) {
        	dataTable.getColumnModel().getColumn(i).setPreferredWidth(110);
        }
        
        //set margins for each component
        importBtn.setMargin(new Insets(0, 0, 0, 0));
        exportBtn.setMargin(new Insets(0, 0, 0, 0));
        skipBtn.setMargin(new Insets(0, 0, 0, 0));
        dataTable.setRowMargin(0);
        renameBtn.setMargin(new Insets(0, 0, 0, 0));
        averageBtn.setMargin(new Insets(0, 0, 0, 0));
        
        //sets fonts for all buttons
        importBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        exportBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        placementLabel1.setFont(new Font("Arial", Font.PLAIN, 12));
        placementLabel2.setFont(new Font("Arial", Font.PLAIN, 12));
        skipBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        placementAlert.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        renameBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        averageBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        
        //create custom color and set the components to that color
        Color lightGray = new Color(234, 234, 234);
        importBtn.setBackground(lightGray);
        exportBtn.setBackground(lightGray);
        placementLabel1.setBackground(lightGray);
        placementLabel2.setBackground(lightGray);
        skipBtn.setBackground(lightGray);
        placementAlert.setBackground(lightGray);
        placementAlert.setForeground(Color.RED);
        dataTable.setBackground(lightGray);
        dataTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        renameBtn.setBackground(lightGray);
        averageBtn.setBackground(lightGray);

        // set action commands for all buttons
        importBtn.setActionCommand("import");
        exportBtn.setActionCommand("export");
        skipBtn.setActionCommand("skip");
        renameBtn.setActionCommand("rename");
        averageBtn.setActionCommand("average");
        
        //Listen for actions from the buttons
        importBtn.addActionListener(this);
        exportBtn.addActionListener(this);
        skipBtn.addActionListener(this);
        renameBtn.addActionListener(this);
        averageBtn.addActionListener(this);
        
        //Fills in the dataTable with the values from the default table
        int tempCount = 0;
        for(int i = 0; i < dataTable.getRowCount(); i++) {
        	for(int j = 0; j < dataTable.getColumnCount(); j++) {
        		dataTable.setValueAt(defaultTable[tempCount], i, j);
        		tempCount++;
        	}
        }
        
        
        JPanel mainPane = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        
        //setup Scroll Pane and data table within the scroll pane
        scrollPane = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.setDragEnabled(false);    
        
        importBtn.setPreferredSize(new Dimension(167, 15));
        exportBtn.setPreferredSize(new Dimension(167, 15));
        placementLabel1.setPreferredSize(new Dimension(150, 17));
        placementLabel2.setPreferredSize(new Dimension(150, 17));
        skipBtn.setPreferredSize(new Dimension(70, 17));
        placementAlert.setPreferredSize(new Dimension(250,17));
        renameBtn.setPreferredSize(new Dimension(167, 15));
        averageBtn.setPreferredSize(new Dimension(167, 15));
        
        BorderLayout layout1 = new BorderLayout();
        layout1.setHgap(1);
        layout1.setVgap(1);
        setLayout(layout1);
        
        FlowLayout layout2 = new FlowLayout(FlowLayout.LEADING);
        layout2.setHgap(0);
        layout2.setVgap(0);
        p1.setLayout(layout2);
        p2.setLayout(layout2);
        
        BoxLayout layout3 = new BoxLayout(mainPane, BoxLayout.Y_AXIS);
        mainPane.setLayout(layout3);
        
    	p1.setSize(new Dimension(669, 50));
    	p2.setSize(new Dimension(669,50));
        
        p1.add(importBtn, BorderLayout.CENTER);
        p1.add(exportBtn);
        p1.add(averageBtn);
        p1.add(renameBtn);
        
        p2.add(placementLabel1);
        p2.add(placementLabel2);
        p2.add(skipBtn);
        p2.add(placementAlert);
    	
    	mainPane.add(p1, BorderLayout.NORTH);
    	mainPane.add(p2);
    	add(scrollPane);
    	
    	add(mainPane, BorderLayout.NORTH);
    	add(scrollPane, BorderLayout.CENTER);
    	setPreferredSize(new Dimension(668,57 + (rows * 18)));
    	
    	skipBtn.setEnabled(false);
    	placementAlert.setVisible(false);
    	
    	setVisible(true);
    }
    
    /*
     * Defines the different actions performed when any of the buttons are pressed
     */
    public void actionPerformed(ActionEvent e) {
        if ("import".equals(e.getActionCommand())){
        	importResults();
        } else if ("export".equals(e.getActionCommand())){
        	exportResults();
        } else if ("skip".equals(e.getActionCommand())){
        	doSkip = true;
        } else if ("rename".equals(e.getActionCommand())) {
        	renameFiles();
        } else if("average".equals(e.getActionCommand())) {
        	averageColumns();
        }
    }
    
    /*
     * Prompts user to select a text file, imports text file contents into the data table
     */
    private void importResults() {
    	//Open new window requesting the selection of a file to import
    	OpenDialog openFile = new OpenDialog("Choose a file");
		String fileDirectory = openFile.getDirectory();
		String fileName = openFile.getFileName();
		if(fileDirectory == null) {
			System.err.println("null file directory");
			return;
		}
		fileName = (fileDirectory + fileName);
		
		//Read the contents of the selected file, splits the file at each comma, and stores the split values in the defaultTable array
		String[] importedTable;
		int rows;
		int cols;
		String contents = "";
    	try {
    		contents = new String(Files.readAllBytes(Paths.get(fileName)));
    	} catch (Exception e) {
    		System.err.print(e);
    	}
    	importedTable = contents.split(",");
    	String[] rowSplit = (contents.split("\n")); //splits file at each new line to get number of columns
    	String[] columnSplit = rowSplit[0].split(","); //splits a single line by commas to get number of rows
    	cols = columnSplit.length - 1;
    	rows = rowSplit.length;
    	
    	int tempCount = 0;
    	for(int i = 0; i < rows; i++) {
    		for(int j = 0; j < cols; j++) {
        		dataTable.setValueAt(importedTable[tempCount], i, j);
        		tempCount++;
    		}
    	}
    }
    
    /*
     * Exports the contents of the data table to a .txt file that can be imported to Excel.
     * Saves exported file to the folder containing the most recently opened image, and to the desktop if no images have been opened
     */
    public void exportResults() {
    	String imagePath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "newResults.txt";
    	
    	if(!(NMSHLPlugin.getImagePath() == null)) {
    		imagePath = NMSHLPlugin.getImagePath() + File.separator + "newResults.txt";
    	}
    	
    	//pulls all values from the data table and stores it in content
        String content = "";
        for(int i = 0; i < dataTable.getRowCount(); i++) {
        	for(int j = 0; j < dataTable.getColumnCount(); j++) {
        		content += dataTable.getValueAt(i, j);
        		content += ",";
        	}
        }
        
        //checks if file exists, if it already exists then the user is asked if they want to overwrite
        boolean doOverwrite = false;
        File file = new File(imagePath);
        if (file.exists()) {
        	String ObjButtons[] = {"Yes","No"};
        	int PromptResult = JOptionPane.showOptionDialog(null, 
                    "File already exists. Would you like to overwrite?", "Overwrite?", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
                    ObjButtons,ObjButtons[1]);
        	if(PromptResult==0) {
        		doOverwrite = true;
            }
        } else {
        	String ObjButtons[] = {"OK"};
        	int PromptResult = JOptionPane.showOptionDialog(null, 
                    "File successfully exported.", "Success",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
                    ObjButtons,ObjButtons[0]);
        	doOverwrite = true;
        }
        
        //if the user wants to overwrite then the file is created and data from dataTable is written to new file
        if(doOverwrite) {
        	try {
        		file.createNewFile();
        		System.out.println("File is created!");
        		System.out.println(file.exists());
        	} catch (Exception e) {
        		System.err.println(e);
        	}
        
        	//write the data from dataTable to the new file
        	Path path = Paths.get(imagePath);
        	try {
        		Files.write(path, content.getBytes());
        	} catch (Exception e) {
        		System.err.println(e);
        	}
        }
    }
    
    /*
     * Finds all columns in the dataTable that contain "AVG". Calculates average values from matching columns. 
     * Places averaged values in each corresponding row of the table.
     */
    public void averageColumns() {
    	for(int i = 0; i < dataTable.getColumnCount(); i++) { //loops through all columns searching for "AVG" columns
    		String temp1 = dataTable.getValueAt(0, i).toString(); //String containing the name of the current "AVG" column name
    		
    		if(temp1.contains("AVG")) { 
    			String searchTerm = temp1.substring(0, temp1.length() - 3); //creates a substring of the "AVG" column without the "AVG" tag
    			boolean found = false;
    			
    			for(int j = 0; j < i && !found; j++) { //checks all columns that are before the average column to find corresponding columns
    				String temp2 = dataTable.getValueAt(0, j).toString();
    				if(!temp2.equals(temp1) && temp2.contains(searchTerm)) {
    					found = true;
    					
    					for(int k = 1; k < dataTable.getRowCount(); k++) { //loops through each of the groups of rows that need to be averaged
    						int count = 0;
    						double average = 0;
    						
    						for(int l = j; l < i; l++) { //loops through the data columns containing the values
    							if(Double.parseDouble(dataTable.getValueAt(k, l).toString()) != 0) { //ignores cell positions that are equal to 0
    								average += Double.parseDouble(dataTable.getValueAt(k, l).toString()); 
    								count++;
    							} 
    						}
    						if(count > 0) { //only calculates average if some values have been added
    							average = average / count;
    							dataTable.setValueAt((Object)(average), k, i);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    /*
     * Prompts user to select a folder of images. Iterates through all images, reading the names off of the images and changing the file name to match
     */
    public void renameFiles() {
    	String newFileName;
    	JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        int result = f.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION || f.getSelectedFile() == null) {
            return;
        }
        
        System.out.println("Selected File: " + f.getSelectedFile());
        
        File[] files = f.getSelectedFile().listFiles();
		
        for(int i = 0; i < files.length; i++) {
       		currentProgress = ((double)(i + 1.0) / (double)(files.length));
        	System.out.println((i+1) + " / " + files.length + " = " +  currentProgress);
        	
        	boolean hasP = false; 
        	int index = (files[i].getName().lastIndexOf('.')); //Get the extension of the current image being processed
        	String extension = (files[i].getName()).substring(index);
        	if((files[i].getName()).substring(index-1, index).equals("P") || (files[i].getName()).contains("P_")) {
        		hasP = true; //Checks if the current image has been previously processed
        	}
        	
        	if(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".tif") || extension.equals(".tiff")) {
        	
        		Opener fileOpener = new Opener(); 
        		ImagePlus img = fileOpener.openImage("" + files[i]); //open the image in ImageJ
        	
        		newFileName = NMSHLPlugin.readImageName(img); //reread name off of image just in case it is named incorrectly
    		
        		if(WindowManager.getImageCount() > 0) {
        			ImagePlus imagePlus = (WindowManager.getCurrentImage());
        			imagePlus.changes = false;
        			imagePlus.close();
        		}
    		    
        		File tempFile;
        		if(hasP) { //Generates a new file name including the processed name, processed modifier, and file extension
        			tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "P_(0)" + extension);
        			newFileName += "P_";
        		} else {
               		tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "(0)" + extension);
        		}
    		
        		if(!files[i].getName().equals(tempFile.getName())) { //checks if file name is already correct before renaming
        			int j = 1;
        			while(tempFile.exists()) { //loops through values j to account for images with the same name on the image
        				tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "(" + j + ")" + extension);
        				j++;
        			}
        			files[i].renameTo(tempFile);
        		}
        		
        		NMSHLPlugin.measurementPanel.setAnswered(true);
        	} 
        } 
                
        //System.out.println(f.getCurrentDirectory());
        //System.out.println(f.getSelectedFile());
    }
    
    /*
     * Returns the row and column numbers for where a value should be placed based on the calculation done and image name
     */
    private int[] autoGetPlacement(String calc) {
    	int[] coords = new int[2];
    	NMSHLPlugin.readImageName();
    	
    	String newCalc = "";    	
    	if(calc.contains("Muscle Thickness")) {
    		newCalc = "Thick";
    	} else if(calc.contains("Pennation Angle")) {
    		newCalc = "Pen";
    	} else if(calc.contains("Total Area")) {
    		newCalc = "Area";
    	} else if(calc.contains("Total Mean")) {
    		
    	} else if(calc.contains("Medial Area")) {
    		
    	} else if(calc.contains("Medial E.I.")) {
    		
    	} else if(calc.contains("Lateral Area")) {
    		
    	} else if(calc.contains("Lateral E.I.")) {
    		
    	} else if(calc.contains("Echo Intensity")) {
    		newCalc = "EI";
    	}
    	
    	System.out.println("NEW CALC: " + newCalc);
    	
    	coords[1] = autoGetColumn(newCalc);
    	coords[0] = autoGetRow(newCalc);
    	    	
    	return(coords);
    }
    
    /*
     * Returns the column number for where a value should be placed based off of the image name and the measurement performed
     */
    private int autoGetColumn(String calc) {
    	int rowIndex = autoGetRow(calc);
    	
    	String[] muscleNameArray = NMSHLPlugin.muscleNameArray; 
    	int pos = -1;
    	
    	ArrayList<String> columns = new ArrayList<String>();    	
    	for(int i = 1; i < dataTable.getColumnCount(); i++) { //fills an array list with all column names
    		columns.add(dataTable.getValueAt(0, i).toString());
    	}
    	
    	if(!NMSHLPlugin.muscleNameArray[1].equals("TA")) { //TA images are excluded here and filtered in the else statement below
    		for(int i = 0; i < columns.size(); i++) {
    			String currentCol = columns.get(i);
    			if(!dataTable.getValueAt(rowIndex, findCol(currentCol)).toString().contains("TA") && rowIndex >= 0 && Double.parseDouble((dataTable.getValueAt(rowIndex, findCol(currentCol)).toString())) != 0) {
    				columns.remove(i);
    				i--;
    			} else if(currentCol.contains("AVG")) { //filters out all columns containing "AVG"
    				columns.remove(i);
    				i--;
    			} else if(muscleNameArray[1] != null && !currentCol.contains(muscleNameArray[1])){ //filters out all wrong muscle columns
    				columns.remove(i);
    				i--;
    			} else if(muscleNameArray[3] != null && !columns.get(i).contains(muscleNameArray[3])){ //filters out all non "CS" columns
    				columns.remove(i);
    				i--;
    			} else if(muscleNameArray[3] == null && columns.get(i).contains("CS")){ //filters out all columns containing "CS"
    				columns.remove(i);
    				i--;
    			} else if(columns.get(i).contains("EL")){ //filters out all "EL" (elastography) columns
    				columns.remove(i);
    				i--;
    			} else if(calc.equals("") || !columns.get(i).contains(calc)) {
    				columns.remove(i);
    				i--;
    			}
    		}

    		System.out.println("Columns: " + columns);
    	} else { //TA image filtering occurs here
    		for(int i = 0; i < columns.size(); i++) {
    			String currentCol = columns.get(i);
    			if(dataTable.getValueAt(rowIndex, findCol(currentCol)).toString().contains("TA") || dataTable.getValueAt(rowIndex, findCol(currentCol)).toString().contains("TA")) {
    				columns.remove(i);
    				i--;
    			} else if(Double.parseDouble((dataTable.getValueAt(rowIndex, findCol(currentCol)).toString())) != 0) { //excludes columns that already contain values
    				columns.remove(i);
    				i--;
    			} else if(currentCol.contains("AVG")) { //filters out all columns containing "AVG"
    				columns.remove(i);
    				i--;
    			} else if(muscleNameArray[1] != null && !currentCol.contains(muscleNameArray[1])){ //filters out all wrong muscle columns
    				columns.remove(i);
    				i--;
    			} else if(muscleNameArray[3] != null && !columns.get(i).contains(muscleNameArray[3])){ //filters out all non "CS" columns
    				columns.remove(i);
    				i--;
    			} else if(muscleNameArray[3] == null && columns.get(i).contains("CS")){ //filters out all columns containing "CS"
    				columns.remove(i);
    				i--;
    			} else if(columns.get(i).contains("EL")){ //filters out all "EL" (elastography) columns
    				columns.remove(i);
    				i--;
    			} else if(calc.equals("") || !columns.get(i).contains(calc)) {
    				columns.remove(i);
    				i--;
    			}
    		}
    		System.out.println("Columns: " + columns);
    		//TODO
    	}
    	
    	if(columns.size() > 0) {
    		pos = findCol(columns.get(0));
    	}
    	return(pos);
    }
    
    /*
     * Returns the row number for where a value should be placed based off of the image name and the measurement performed
     */
    private int autoGetRow(String calc) {
    	String[] muscleNameArray = NMSHLPlugin.muscleNameArray; 
    	int pos = -1;
    	
    	ArrayList<String> rows = new ArrayList<String>();
    	for(int i = 1; i < dataTable.getRowCount(); i++) { //fills an array list with all column names
    		rows.add(dataTable.getValueAt(i, 0).toString());
    	}
    	
    	if(!NMSHLPlugin.muscleNameArray[1].equals("TA")) {
    		for(int i = 0; i < rows.size(); i++) {
    			String currentRow = rows.get(i);
    			if(muscleNameArray[0] != null && !currentRow.contains(muscleNameArray[0])) { //filters out all rows of the wrong side
    				rows.remove(i);
    				i--;
    			} else if (muscleNameArray[2] != null && !currentRow.contains(muscleNameArray[2])) { //filters out all rows of the wrong percentage
    				rows.remove(i);
    				i--;
    			}
    		}
    		System.out.println("Rows: " + rows);
    	} else {
    		String percentAdj; //percentage adjusted to match up with the first column of rows
    		if(muscleNameArray[2].equals("20")) {
    			percentAdj = "25";
    		} else if(muscleNameArray[2].equals("40")) {
    			percentAdj = "50";
    		} else {
    			percentAdj = "75";
    		}
    		System.out.println("Adjusted: " + percentAdj);
    		
    		for(int i = 0; i < rows.size(); i++) {    			
    			String currentRow = rows.get(i);
    			if(muscleNameArray[0] != null && !currentRow.contains(muscleNameArray[0])) { //filters out all rows of the wrong side
    				rows.remove(i);
    				i--;
    			} else if (muscleNameArray[2] != null && !currentRow.contains(percentAdj)) { //filters out all rows of the wrong percentage
    				rows.remove(i);
    				i--;
    			}
    		}
    		System.out.println("Rows: " + rows);
    	}
    	
    	if(rows.size() > 0) {
    		pos = findRow(rows.get(0));
    	}
    	return(pos);
    }
    
    /*
     * Returns the index of the column that contains the first occurrence of string "name"
     */
    private int findCol(String name) {
    	for(int i = 0; i < dataTable.getColumnCount(); i++) {
    		for(int j = 0; j < dataTable.getRowCount(); j++) {
    			if(dataTable.getValueAt(j, i).toString().equals(name)) {
        			return(i);
        		}
    		}
    	}
    	return(-1);
    }
    
    /*
     * Returns the index of the column that contains the first occurrence of string "name"
     */
    private int findRow(String name) {
    	for(int i = 0; i < dataTable.getColumnCount(); i++) {
    		for(int j = 0; j < dataTable.getRowCount(); j++) {
    			if(dataTable.getValueAt(j, i).toString().equals(name)) {
        			return(j);
        		}
    		}
    	}
    	return(-1);
    }
    
    /*
     * Method called whenever a calculation is completed. First, attempts to automatically place value into the data table. 
     * If no proper location is found, it prompts the user to place the value in the data table or skip placing the value.
     */
    public void storeValue(double[] valueArr, String[] typeArr) {
    	skipBtn.setEnabled(true);
    	placementAlert.setVisible(true);
    	placementLabel2.setForeground(Color.RED);
    	for(int i = 0; i < typeArr.length; i++) {
    		int[] coords = autoGetPlacement(typeArr[i]);
    		System.out.println("Coords : " + coords[0] + ", " + coords[1]);
    		if(coords[0] != -1 && coords[1] != -1) { //checks if a proper location has been found
    			dataTable.setValueAt(valueArr[i], coords[0], coords[1]); //places value
    		} else { //if no proper location is found, then it prompts the user to place the value
    			placementLabel2.setText(typeArr[i] + " | " + valueArr[i]);
    			dataTable.getSelectionModel().clearSelection();
    			while (dataTable.getSelectionModel().isSelectionEmpty()) { //wait for user to select a cell on the data table
    				try {
    					Thread.sleep(300);
    					if(doSkip == true) {
    						doSkip = false;
    						break;
    					} else if (!dataTable.getSelectionModel().isSelectionEmpty()) {
    						dataTable.setValueAt(valueArr[i], dataTable.getSelectedRow(), dataTable.getSelectedColumn());
    					}
    				} catch (Exception e) {
    					System.err.println(e);
    				}
    			}
    			dataTable.repaint();
    		}
    	}
    	dataTable.getSelectionModel().clearSelection(); //resets various GUI elements
    	placementLabel2.setText("no value");
    	placementLabel2.setForeground(Color.BLACK);
    	placementAlert.setVisible(false);
    	skipBtn.setEnabled(false);
    	importBtn.setEnabled(true);
        exportBtn.setEnabled(true);
        dataTable.repaint();
        
        if(WindowManager.getImageCount() > 0) { //If an image is open, closes it after data has been placed
        	ImagePlus imagePlus = (WindowManager.getCurrentImage());
        	imagePlus.changes = false;
        	imagePlus.close();
        }
    }
    
}
