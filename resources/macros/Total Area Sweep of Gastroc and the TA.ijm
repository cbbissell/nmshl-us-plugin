// Total Area Sweep of Gastroc and the TA

//Automates certain aspects of the ultrasound standard operating procedure
//for measuring total area sweep of gastroc and the TA.

    run("Clear Results");
    a = true;

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

        waitForUser("Draw a polygon around the Medial and Lateral Gastroc or TA.");

        z = 0;
        while(z == 0){
            if(selectionType() == -1){ //forces the user to make a selection
                waitForUser("Draw a polygon around the Medial and Lateral Gastroc or TA.");
            } else {
                z = 1;
            }
        }

        run("Measure");
        area = getResult("Area", 0);
        mean = getResult("Mean", 0);
        updateResults();
        setForegroundColor(255, 255, 255);
        autoUpdate(false);
        a = getBoolean("Redraw?"); //sets loop requirement equal to user input

        if(a == 0){
            getSelectionBounds(x, y, width, height);
            run("Draw", "slice");
            makeText("Area: " + area + "\nE.I.: " + mean, x + (width / 2), y + (height / 2));
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
            //close();
	    result = "" + area + " " + mean;
	    return result;
        } else {
            run("Clear Results");
        }
    }

