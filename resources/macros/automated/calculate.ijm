if(selectionType() == 2) {
    run("Measure");
    mean = getResult("Mean", nResults-1);
    area = getResult("Area", nResults-1);
    result = "";
    updateResults();
    setForegroundColor(255, 255, 255);
    autoUpdate(false);

    getSelectionBounds(x, y, width, height);
    run("Draw", "slice");
    makeText("EI: " + mean + "\nArea: " + area, x + (width / 2), y + (height / 2));
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
        test1 = File.exists(path+t+"P1.tif");
        test2 = File.exists(path+t+"P2.tif");
        if(test1 == 1) {
            saveAs("tiff",path+t+"P2");
        } else if (test2 == 1) {
            saveAs("tiff",path+t+"P3");
        } else {
            saveAs("tiff",path+t+"P1");
        }
    }

    //result += "" + mean + " " + area;
    result += "" + mean;
    return result;
}