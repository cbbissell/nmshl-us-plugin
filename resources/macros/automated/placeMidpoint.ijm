setTool("point");
waitForUser("Place a point in the middle of the muscle");
while(selectionType() != 10){
    setTool("point");
    waitForUser("Place a point in the middle of the muscle");
}

getDimensions(imageWidth, imageHeight, channels, slices, frames);
getSelectionBounds(xMid, yMid, width, height);

count = 0;
xArray = newArray();
yArray = newArray();

for(i = 0; i < 360; i++){
    for(j = 1; j < imageWidth; j++){
        rad = i * (PI/180);
        xMag = cos(rad);
        yMag = sin(rad);
        xNew = (j * xMag) + xMid;
        yNew = (j * yMag) + yMid;

        value = getPixel(xNew, yNew);
        if(xNew < 0 || xNew > imageWidth || yNew < 0 || yNew > imageHeight){
            break;
        } else if(value == 125){
            drawLine(xMid, yMid, xNew, yNew);
            wait(1);
            tempXArray = newArray(100, xNew);
            tempXArray = Array.slice(tempXArray, 1, 2);
            xArray = Array.concat(xArray, tempXArray);

            tempYArray = newArray(100, yNew);
            tempYArray = Array.slice(tempYArray, 1, 2);
            yArray = Array.concat(yArray, tempYArray);
            count ++;
            break;
        }
    }
    i++;
}

//print("xArray:");
//Array.print(xArray);
//print("yArray:");
//Array.print(yArray);
makeSelection("polygon", xArray, yArray, count);