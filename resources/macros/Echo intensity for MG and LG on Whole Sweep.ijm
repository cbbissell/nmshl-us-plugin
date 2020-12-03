// Echo Intensity for MG and LG

//Automates certain aspects of the ultrasound standard operating procedure
//for measuring Echo Intensity for MG and LG.

    run("Clear Results");
    a = true;
    medialArea = 0;
    lateralArea = 0;
    result = "";

    setTool("line");

    waitForUser("Draw a line to calibrate scale of image");
    x = 0;
    while(x == 0){
        if(selectionType() == -1){ //forces the user to make a selection
            waitForUser("Draw a line to calibrate scale of image");
        } else {
            x = 1;
        }
     }

    run("Set Scale...", "known=4 unit=cm");

    while(a){
            setTool("polygon");

            waitForUser("Draw a polygon around the medial");

            z = 0;
            while(z == 0){
                if(selectionType() == -1){ //forces the user to make a selection
                    waitForUser("Draw a polygon around the medial");
                } else {
                    z = 1;
                }
            }

            run("Measure");
            medialArea = getResult("Area", 0);
            medialMean = getResult("Mean", 0);
            updateResults();
            setForegroundColor(255, 255, 255);
            autoUpdate(false);
            a = getBoolean("Redraw?"); //sets loop requirement equal to user input

            if(a == 0){
		        result += "" + medialArea;
		        result += " " + medialMean;
                getSelectionBounds(x, y, width, height);
                run("Draw", "slice");
                makeText("Area: " + medialArea + "\nE.I.: " + medialMean, x + (width / 2), y + (height / 2));
                run("Draw", "slice");
            } else {
                run("Clear Results");
            }
        }

    a = true;
    while(a){
            setTool("polygon");

            waitForUser("Draw a polygon around the lateral");

            z = 0;
            while(z == 0){
                if(selectionType() == -1){ //forces the user to make a selection
                    waitForUser("Draw a polygon around the lateral");
                } else {
                    z = 1;
                }
            }

            run("Measure");
            lateralArea = getResult("Area", 1);
            lateralMean = getResult("Mean", 1);
            updateResults();
            setForegroundColor(255, 255, 255);
            autoUpdate(false);
            a = getBoolean("Redraw?"); //sets loop requirement equal to user input

            if(a == 0){
                getSelectionBounds(x, y, width, height);
                run("Draw", "slice");
                makeText("Area: " + lateralArea + "\nE.I.: " + lateralMean, x + (width / 2), y + (height / 2));
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
                //run("Clear Results");
		result += " " + lateralArea;
		result += " " + lateralMean;
                //close();
		return result;
            } else {
                IJ.deleteRows(1,1);
            }
        }