// Cross-Sectional Echo Intensity

//Automates certain aspects of the ultrasound standard operating procedure
//for measuring Cross-Sectional Echo Intensity.

    run("Clear Results");
    a = true;
    result = "";

    while(a){
            setTool("polygon");

            waitForUser("Outline area around medial, lateral or TA.");

            z = 0;
            while(z == 0){
                if(selectionType() == -1){ //forces the user to make a selection
                    waitForUser("Outline area around medial, lateral or TA.");
                } else {
                    z = 1;
                }
            }

            run("Measure");
            mean = getResult("Mean", 0);
            updateResults();
            setForegroundColor(255, 255, 255);
            autoUpdate(false);
            a = getBoolean("Redraw?"); //sets loop requirement equal to user input

            if(a == 0){
                        getSelectionBounds(x, y, width, height);
                        run("Draw", "slice");
                        makeText(mean, x + (width / 2), y + (height / 2));
                        run("Draw", "slice");
                        b = getBoolean("Save Image?");
                        if(b == 1){ //if the user wants to save image
                            path = getDirectory("image");
                            title = getTitle();
                            t = getTitle();
                            t = replace(t, ".jpg", "");
                            t = replace(t, ".jpeg", "");
                            t = replace(t, ".tif", "");
                            selectWindow(title);
                            count = 1;
                            while(File.exists(path+t+"P"+count+".tif")){
                                count++;
                            }

                            saveAs("tiff",path+t+"P"+count);
                        }
			result += "" + mean;
            //close();
			return result;
            } else {
                        run("Clear Results");
            }
    }