// Muscle Thickness and Pennation Angle for MG, LG, and TA

//Automates certain aspects of the ultrasound standard operating procedure
//for measuring Muscle Thickness and Pennation Angle for MG, LG, and TA.

    run("Clear Results");
    a = true;
    length = 0;
    angle = 0;
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
        setTool("line");

        waitForUser("Draw a straight line from the top of the muscle to the bottom of the muscle");

        z = 0;
        while(z == 0){
            if(selectionType() == -1){ //forces the user to make a selection
                waitForUser("Draw a straight line from the top of the muscle to the bottom of the muscle");
            } else {
                z = 1;
            }
        }

        run("Measure");
        length = getResult("Length", 0);
        updateResults();
        setForegroundColor(255, 255, 255);
        autoUpdate(false);
        a = getBoolean("Redraw?"); //sets loop requirement equal to user input

        if(a == 0){
	    result += "" + length + " ";
            getSelectionCoordinates(x, y);
            run("Draw", "slice");
            makeText(length, 5 + ((x[0] + x[1])/2), ((y[0] + y[1])/2) - 25);
            run("Draw", "slice");
        } else {
            run("Clear Results");
        }
    }

    i = 1;
    a = true;


    while(a){ //loop deciding if enough angles have been drawn
        setTool("angle");
        waitForUser("Trace the pennation angle of the muscle");

        z = 0;
        while(z == 0){
            if(selectionType() == -1){ //forces the user to make a selection
                waitForUser("Trace the pennation angle of the muscle");
            } else {
                z = 1;
            }
        }

        run("Measure");
        angle = getResult("Angle", i);
        updateResults();
        setForegroundColor(255, 255, 255);
        autoUpdate(false);
        a = getBoolean("Draw more angles?"); //sets loop requirement equal to user input

        if(a == 0){
            getSelectionCoordinates(x, y);
            run("Draw", "slice");
            makeText(angle + fromCharCode(0x00B0), ((x[0] + x[2])/2), ((y[0] + y[2])/2));
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
                test1 = File.exists(path+t+"P.tif");
                test2 = File.exists(path+t+"P2.tif");
                if(test1 == 1) {
                    saveAs("tiff",path+t+"P2");
                } else if (test2 == 1) {
                    saveAs("tiff",path+t+"P3");
                } else {
                    saveAs("tiff",path+t+"P");
                }
            }
            //run("Clear Results");
            //close();
	    result += "" + angle;
            return result;
        } else {
	    result += "" + angle + " ";
            getSelectionCoordinates(x, y);
            run("Draw", "slice");
            makeText(angle + fromCharCode(0x00B0), ((x[0] + x[2])/2), ((y[0] + y[2])/2));
            run("Draw", "slice");
            i++;
        }
    }