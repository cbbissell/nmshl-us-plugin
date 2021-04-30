//ResultsPanel.java 
//Describes panel that displays a table containing the measured data

package nmshl.pack;

import java.awt.*;  
import java.awt.event.*;
import javax.swing.*;

import ij.plugin.Macro_Runner;
import nmshl.pack.*;
import java.io.*;
import java.nio.file.*;
import java.text.DecimalFormat;
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
    private JButton placeBtn;
    private JLabel placementAlert;
    private String[] defaultTable;
    private JScrollPane scrollPane;
    private JTable dataTable;
    private JButton averageBtn;
	private JButton maskBtn;
    private JButton renameBtn;
    volatile private double currentProgress = 0;
    
    volatile private boolean doPlace = false;
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
		skipBtn = new JButton ("<html><center>Skip");
		placeBtn = new JButton ("<html><center>Place");
		renameBtn = new JButton ("<html><center>Rename Images");
		maskBtn = new JButton ("<html><center>Create Masks");
		averageBtn = new JButton ("<html><center>Average Values");

		addBtn(importBtn, "Import data table from an existing file", "import");
		addBtn(exportBtn, "Export current data table to a new file", "export");
		addBtn(skipBtn, "Skip the current value being placed", "skip");
		addBtn(placeBtn, "Place the current value in the stated column and row", "place");
		addBtn(renameBtn, "Renames the images in a folder to match the names on images", "rename");
		addBtn(maskBtn, "Mask the images in selected folder. Requires that each image be processed", "mask");
		addBtn(averageBtn, "Average all columns with an \"AVG\" in their title", "average");

        placementLabel1 = new JLabel ("<html><font color='white'>__</font><font color='red'>row</font>  |  <font color='blue'>column</font></html>");
        placementLabel2 = new JLabel ("<html><center>no value");
        placementAlert = new JLabel ("<html><center>Skip or place value");

		placementLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        placementAlert.setHorizontalAlignment(SwingConstants.CENTER);
        
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

        dataTable.setRowMargin(0);

        //sets fonts for components
        placementLabel1.setFont(new Font("Arial", Font.PLAIN, 12));
        placementLabel2.setFont(new Font("Arial", Font.PLAIN, 12));
        placementAlert.setFont(new Font("Arial", Font.PLAIN, 12));
        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
        
        //create custom color and set the components to that color
        Color lightGray = new Color(234, 234, 234);
        placementLabel1.setBackground(lightGray);
        placementLabel2.setBackground(lightGray);
        placementAlert.setBackground(lightGray);
        placementAlert.setForeground(Color.RED);
        dataTable.setBackground(lightGray);
        dataTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //Fills in the dataTable with the values from the default table
        int tempCount = 0;
        for(int i = 0; i < dataTable.getRowCount(); i++) {
        	for(int j = 0; j < dataTable.getColumnCount(); j++) {
				if (defaultTable[tempCount].equals("0")) {
					defaultTable[tempCount] = "";
				}
        		dataTable.setValueAt(defaultTable[tempCount], i, j);
        		tempCount++;
        	}
        }
        
        //setup Scroll Pane and data table within the scroll pane
        scrollPane = new JScrollPane(dataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.setDragEnabled(false);    
        
//        importBtn.setPreferredSize(new Dimension(167, 15));
//        exportBtn.setPreferredSize(new Dimension(167, 15));
//        placementLabel1.setPreferredSize(new Dimension(150, 17));
//        placementLabel2.setPreferredSize(new Dimension(150, 17));
//        skipBtn.setPreferredSize(new Dimension(70, 17));
//        placeBtn.setPreferredSize(new Dimension(131, 17));
//        placementAlert.setPreferredSize(new Dimension(150,17));
//        renameBtn.setPreferredSize(new Dimension(167, 15));
//        averageBtn.setPreferredSize(new Dimension(167, 15));

		JPanel mainPane = new JPanel();
		mainPane.setMaximumSize(new Dimension(672, 50));
		JPanel topPane = new JPanel();
		JPanel midPane = new JPanel();
		JPanel botPane = new JPanel();

		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.setHgap(1);
		gridLayout1.setVgap(1);

		topPane.setLayout(gridLayout1);
		topPane.add(importBtn);
		topPane.add(exportBtn);
		topPane.add(averageBtn);
		topPane.add(renameBtn);
		topPane.add(maskBtn);

		midPane.setLayout(gridLayout1);
		midPane.add(placeBtn);
		midPane.add(placementLabel2);
		midPane.add(skipBtn);
		midPane.add(placementAlert);

		botPane.setLayout(gridLayout1);
		botPane.add(placementLabel1);

		GridLayout gridLayout2 = new GridLayout(3,1, 1, 1);

		mainPane.setLayout(gridLayout2);
		mainPane.add(topPane);
		mainPane.add(midPane);
		mainPane.add(botPane);

		BoxLayout layout1 = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout1);

		mainPane.setAlignmentX(LEFT_ALIGNMENT);
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		add(mainPane);
		add(scrollPane);

		setPreferredSize(new Dimension(668,86 + (rows * 19)));
    	
    	skipBtn.setEnabled(false);
    	placeBtn.setEnabled(false);
    	placementAlert.setVisible(false);
    	
    	setVisible(true);
    }

    private void addBtn(JButton btn, String tooltip, String actionCommand){
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
        if ("import".equals(e.getActionCommand())){
        	importResults();
        } else if ("export".equals(e.getActionCommand())){
        	exportResults();
        } else if ("skip".equals(e.getActionCommand())){
        	doSkip = true;
        } else if ("place".equals(e.getActionCommand())){
        	doPlace = true;
        } else if ("rename".equals(e.getActionCommand())) {
			renameFiles();
		} else if ("mask".equals(e.getActionCommand())) {
        	maskFiles();
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
			String ObjButtons[] = {"OK"};
			int PromptResult = JOptionPane.showOptionDialog(null,
					"No file selected.", "Failed",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
					ObjButtons,ObjButtons[0]);
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
				System.out.println("changed:" + dataTable.getValueAt(i, j).toString());
        		tempCount++;
    		}
    	}
		String ObjButtons[] = {"OK"};
		int PromptResult = JOptionPane.showOptionDialog(null,
				"File successfully imported.", "Success",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
				ObjButtons,ObjButtons[0]);
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
        		if(dataTable.getValueAt(i, j).toString() == null || dataTable.getValueAt(i, j).toString().isEmpty()){
					content += " ";
				} else {
					content += dataTable.getValueAt(i, j);
				}
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
				String ObjButtons[] = {"OK"};
				int PromptResult = JOptionPane.showOptionDialog(null,
						"File successfully exported.", "Success",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
						ObjButtons,ObjButtons[0]);
        		System.out.println("File is created!");

//        		System.out.println(file.exists());
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
			System.out.println("checking: " + dataTable.getValueAt(0, i).toString());
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
    							if((dataTable.getValueAt(k, l).toString() != null && !dataTable.getValueAt(k, l).toString().isEmpty())) { //ignores cell positions that are equal to 0
    								try{
    									if(Double.parseDouble(dataTable.getValueAt(k, l).toString()) != 0) {
											average += Double.parseDouble(dataTable.getValueAt(k, l).toString());
											count++;
										}
									} catch(NumberFormatException e) {
										//Not a double in cell, skip this cell
									}
    							} 
    						}
    						if(count > 0) { //only calculates average if some values have been added
    							DecimalFormat df = new DecimalFormat("#.####");
    							average = average / count;
    							dataTable.setValueAt((Object)(df.format(average)), k, i);
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
    	renameBtn.setEnabled(false);
    	JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        
        int result = f.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION || f.getSelectedFile() == null) {
        	renameBtn.setEnabled(true);
            return;
        }
        
        System.out.println("Selected File: " + f.getSelectedFile());
        
        File[] files = f.getSelectedFile().listFiles();  
        
        new Thread(new Runnable() {
        	@Override
        	public void run() {
        		doRenameLoop(f, files);
        	}
    	}).start();
        
    	new Thread(new Runnable() {
        	@Override
        	public void run() {
        		updateRenameBtn();
        	}
    	}).start();
                
        //System.out.println((i+1) + " / " + files.length + " = " +  currentProgress);    // TODO
    	//setRenameText((i+1) + " / " + files.length + " = " +  currentProgress);
        //setRenameText("" + currentProgress);
        
        //System.out.println(f.getCurrentDirectory());
        //System.out.println(f.getSelectedFile());
    }
    
    private void doRenameLoop(JFileChooser f, File[] files) {
    	String newFileName;
		for(int i = 0; i < files.length; i++) {
       		currentProgress = ((double)(i + 1.0) / (double)(files.length));

        	int index = (files[i].getName().lastIndexOf('.')); //Get the extension of the current image being processed
        	String extension = (files[i].getName()).substring(index);
        	
        	if(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".tif") || extension.equals(".tiff")) {
        	
        		Opener fileOpener = new Opener(); 
        		ImagePlus img = fileOpener.openImage("" + files[i]); //open the image in ImageJ
        	
        		newFileName = "" + (i+1) + "--" + NMSHLPlugin.readImageName(img); //reread name off of image just in case it is named incorrectly
    		
        		if(WindowManager.getImageCount() > 0) {
        			ImagePlus imagePlus = (WindowManager.getCurrentImage());
        			imagePlus.changes = false;
        			imagePlus.close();
        		}

        		File tempFile;
        		if((files[i].getName()).contains("P1")) {
					if((files[i].getName()).contains("P2")) {
						tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "P3" + extension);
					} else {
						tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "P2" + extension);
					}
				} else if ((files[i].getName()).contains("P")){
					tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "P1" + extension);
				} else {
					tempFile = new File(f.getSelectedFile() + File.separator + newFileName + extension);
				}

        		if(!files[i].getName().equals(tempFile.getName())) { //checks if file name is already correct before renaming

        			/*
        			int j = 1;
        			while(tempFile.exists()) { //loops through values j to account for images with the same name on the image
        				tempFile = new File(f.getSelectedFile() + File.separator + newFileName + "(" + j + ")" + extension);
        				j++;
        			}
        			*/

        			tempFile = new File(f.getSelectedFile() + File.separator + newFileName + extension);
        			files[i].renameTo(tempFile);
        		}
        		
        		NMSHLPlugin.measurementPanel.setAnswered(true);
        	} 
        } 
		JOptionPane.showMessageDialog(this, "Done. " + files.length + " files renamed.");
    }
    
    private void updateRenameBtn() {
    	while(currentProgress < 1) {
    		try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		renameBtn.setText("" + (int)(currentProgress * 100) + "%");
    	}
    	renameBtn.setText("<html><center>Rename Images");
    	renameBtn.setEnabled(true);
    }


	/*
	 * Prompts user to select a folder of images. Iterates through all images, creating a mask of the processed area
	 */
	public void maskFiles() {
		maskBtn.setEnabled(false);
		JFileChooser f = new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = f.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION || f.getSelectedFile() == null) {
			maskBtn.setEnabled(true);
			return;
		}

		System.out.println("Selected File: " + f.getSelectedFile());

		File[] files = f.getSelectedFile().listFiles();

		new Thread(new Runnable() {
			@Override
			public void run() {
				doMaskLoop(f, files);
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				updateMaskBtn();
			}
		}).start();
	}

	private void doMaskLoop(JFileChooser f, File[] files) {
//		boolean hasMaskFolder = false;
//		for(int i = 0; i < files.length; i++) {
//			if(files[i].getName().contains("Masks")){
//				hasMaskFolder = true;
//				System.out.println(files[i].getName());
//				System.out.println("no mask folder");
//			}
//		}
//		System.out.println("hasMaskFolder: " + hasMaskFolder);
//		if(!hasMaskFolder){
//			String path = files[1].getPath();
//			File file = new File(files[1].getPath().substring(0,files[1].getPath().length() - files[1].getName().length()) + File.separator + "Masks");
//						//NEED TRY CATCH HERE
//			System.out.println(file.getPath());
//			boolean madeFile = file.mkdir();
//			if(madeFile){
//				System.out.println("New dir made");
//			} else {
//				System.out.println("No dir made");
//			}
//		}

		int count = 0;
		String file1Name;
		String file2Name;
		for(int i = 0; i < files.length; i++) {
			currentProgress = ((double)(i + 1.0) / (double)(files.length));

			int index = (files[i].getName().lastIndexOf('.')); //Get the extension of the current image being processed
			String extension = (files[i].getName()).substring(index);
			if(!files[i].getName().contains("P") && !files[i].getName().contains("Masks")){
				if(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".tif") || extension.equals(".tiff")) {
					String processedImg = null;
					File tempFile = null;
					for(int j = 0; j < files.length; j++) {
						if(files[j].getName().contains(files[i].getName().substring(0,index)) && files[j].getName().contains("P")){
							processedImg = files[j].getName();
							tempFile = files[j];
						}
					}
					System.out.println("1: " + files[i].getName());
					System.out.println("2: " + processedImg);
					if(processedImg != null){
						Macro_Runner macroRunner = new Macro_Runner();
						String currentDirectory = System.getProperty("user.dir");
						String result = macroRunner.runMacroFile(currentDirectory + File.separator+ "resources" + File.separator + "macros" + File.separator
										+ "Mask Img.ijm", files[i] + "|" + tempFile);
						count++;
					}

				}
			}

			NMSHLPlugin.measurementPanel.setAnswered(true);

		}
		JOptionPane.showMessageDialog(this, "Done. " + count + " files masked.");
	}

	private void updateMaskBtn() {
		while(currentProgress < 1) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			maskBtn.setText("" + (int)(currentProgress * 100) + "%");
		}
		maskBtn.setText("<html><center>Create Masks");
		maskBtn.setEnabled(true);
	}

    /*
     * Returns the row and column numbers for where a value should be placed based on the calculation done and image name
     */
    private int[] autoGetPlacement(String calc) {
    	int[] coords = new int[2];
    	
    	String newCalc = "";    	
    	if(calc.contains("Muscle Thickness")) {
    		newCalc = "Thick";
    	} else if(calc.contains("Pennation Angle")) {
    		newCalc = "Pen";
    	} else if(calc.contains("Total Area")) {
    		newCalc = "Area";
    	} else if(calc.contains("Elastography")) {
			newCalc = "EL";
		} else if(calc.contains("Echo Intensity")) {
			newCalc = "EI";
		} else if(calc.contains("Medial Area")) {
			newCalc = "Area";
			NMSHLPlugin.muscleNameArray[1] = "MG";
    	} else if(calc.contains("Medial E.I.")) {
    		newCalc = "EI";
			NMSHLPlugin.muscleNameArray[1] = "MG";
    	} else if(calc.contains("Lateral Area")) {
			newCalc = "Area";
			NMSHLPlugin.muscleNameArray[1] = "LG";
    	} else if(calc.contains("Lateral E.I.")) {
			newCalc = "EI";
			NMSHLPlugin.muscleNameArray[1] = "LG";
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
		System.out.println("Cols1: " + columns);

		for(int i = 0; i < columns.size(); i++) { //loops through colums and removes columns containing "AVG"
			String currentCol = columns.get(i);
			if(currentCol.contains("AVG")) { //filters out all columns containing "AVG"
				columns.remove(i);
				i--;
			} else if(rowIndex >= 0 && (!((dataTable.getValueAt(rowIndex, findCol(currentCol)).toString()).equals(" "))
					 				&& !((dataTable.getValueAt(rowIndex, findCol(currentCol)).toString()).isEmpty()))) {
				columns.remove(i);
				i--;
			}
		}
		System.out.println("Cols2: " + columns);

		if(muscleNameArray[1] != null && !muscleNameArray[1].isEmpty()){
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(!currentCol.contains(muscleNameArray[1])){
					columns.remove(i);
					i--;
				}
			}
		}
		System.out.println("Cols3: " + columns);
		System.out.println(muscleNameArray[3]);

		if(muscleNameArray[3] != null && !muscleNameArray[3].isEmpty()){
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(!currentCol.contains(muscleNameArray[3])){
					if(!(muscleNameArray[3].equals("CSA") && muscleNameArray[1] != null && muscleNameArray[1].equals("TA") && muscleNameArray[4] != null && muscleNameArray[4].equals("Area"))){
						if(!currentCol.contains("Area")) {
							System.out.println("test");
							columns.remove(i);
							i--;
						}
					} else {
						System.out.println("test");
						columns.remove(i);
						i--;
					}

				}
			}
		} else {
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(currentCol.contains("CSA") && !muscleNameArray[1].equals("Ga")){
					columns.remove(i);
					i--;
				}
			}
		}
		System.out.println("Cols4: " + columns);

		if(muscleNameArray[4] != null && !muscleNameArray[4].isEmpty()){
			for(int i = 0; i < columns.size(); i++) {
				System.out.println("4: " + columns.toString());
				String currentCol = columns.get(i);
				if(!currentCol.contains(muscleNameArray[4])){
					columns.remove(i);
					i--;
				}
			}
		} else {
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(currentCol.contains("90")){
					columns.remove(i);
					i--;
				}
			}
		}
		System.out.println("Cols5: " + columns);
		System.out.println(muscleNameArray[5]);

		if(muscleNameArray[5] != null && !muscleNameArray[5].isEmpty() && !muscleNameArray[1].equals("Ga")){
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(!currentCol.contains(muscleNameArray[5]) && (!muscleNameArray[1].equals("TA") || calc.equals("EL"))){
					columns.remove(i);
					i--;
				}
			}
		} else {
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(currentCol.contains("LO") && (!muscleNameArray[1].equals("TA") || calc.equals("EL"))){
					columns.remove(i);
					i--;
				}
			}
		}
		System.out.println("Cols6: " + columns);
		System.out.println("calc: " + calc);
		if(calc != null && !calc.isEmpty()){
			for(int i = 0; i < columns.size(); i++) {
				String currentCol = columns.get(i);
				if(!currentCol.contains(calc)){
					columns.remove(i);
					i--;
				}
			}
		}
		System.out.println("Cols7: " + columns);

		System.out.println(columns.toString());
    	
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
    	
    	if(NMSHLPlugin.muscleNameArray[1] != null && !NMSHLPlugin.muscleNameArray[1].equals("TA")) {
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

    	if(rows.size() > 0 && rows.size() < 2) {
    		pos = findRow(rows.get(0));
    	} else {
    		pos = -1;
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
    			System.out.println("Valid Location Found!");
    			int taLoc = -1;
    			for(int j = 0; j < dataTable.getColumnCount(); j++){
    				if((dataTable.getValueAt(1, j).toString()).contains("TA")){
    					taLoc = j;
    					break;
					}
				}
    			if(taLoc != -1 && coords[1] > taLoc){
					placementLabel1.setText("<html><font color='white'>__</font><font color='red'>" + dataTable.getValueAt(coords[0], taLoc).toString()
											+ "</font>  |  <font color='blue'>" + dataTable.getValueAt(0, coords[1]).toString() + "</font></html>");
				} else {
					placementLabel1.setText("<html><font color='white'>__</font><font color='red'>" + dataTable.getValueAt(coords[0], 0).toString()
											+ "</font>  |  <font color='blue'>" + dataTable.getValueAt(0, coords[1]).toString() + "</font></html>");
				}
    			//placeBtn.setText("Place:  " + dataTable.getValueAt(0, coords[1]).toString());
				//placementLabel1.setText("coord")
    			placeBtn.setEnabled(true);
    		}
    		
    		placementLabel2.setText(typeArr[i] + " | " + valueArr[i]);
			dataTable.getSelectionModel().clearSelection();
			while (dataTable.getSelectionModel().isSelectionEmpty()) { //wait for user to select a cell on the data table
				try {
					Thread.sleep(300);
					if(doSkip) {
						doSkip = false;
						break;
					} else if (!dataTable.getSelectionModel().isSelectionEmpty()) {
						dataTable.setValueAt(valueArr[i], dataTable.getSelectedRow(), dataTable.getSelectedColumn());
					} else if (doPlace) {
						doPlace = false;
						dataTable.setValueAt(valueArr[i], coords[0], coords[1]); //places value automatically
						break;
					}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
			dataTable.repaint();
    		   		
    		
    		//placeBtn.setText("<html><center>Place:");
			placementLabel1.setText("<html><font color='white'>__</font><font color='red'>row</font>  |  <font color='blue'>column</font></html>");
    		placeBtn.setEnabled(false);
    		
    		
    		/*
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
    		} */
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
