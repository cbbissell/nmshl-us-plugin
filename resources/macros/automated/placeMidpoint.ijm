//setTool("point");
//waitForUser("Place a point in the middle of the muscle");
//while(selectionType() != 10){
//    setTool("point");
//    waitForUser("Place a point in the middle of the muscle");
//}

getDimensions(imageWidth, imageHeight, channels, slices, frames);
getSelectionBounds(xMid, yMid, width, height);

xArray = newArray();
yArray = newArray();

xOld = 0;
yOld = 0;

maxX = 0;
maxY = 0;
maxPoints = 0;
i = yMid + (yMid * 0.20);
j = xMid + (xMid * 0.20);

while(i <= (yMid + height) - (height * 0.20)){
    while(j <= (xMid + width) - (width * 0.20)){
        points = 0;
        //print(j + ", " + i);
        for(k = 0; k < 360; k++){
            for(l = 1; l < imageWidth; l++){
                rad = k * (PI/180);
                xMag = cos(rad);
                yMag = sin(rad);
                xNew = (l * xMag) + j;
                yNew = (l * yMag) + i;

                value = getPixel(xNew, yNew);
                if(xNew < 0 || xNew > imageWidth || yNew < 0 || yNew > imageHeight){
                    break;
                } else if(value == 125){
                    dist = sqrt(pow(xNew - xOld, 2) + pow(yNew - yOld, 2)) ;
                    if(dist > 5){
                        //drawLine(xMid, yMid, xNew, yNew);
                        //wait(1);

                        xOld = xNew;
                        yOld = yNew;
                        points++;
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        if(points > maxPoints){
            maxX = j;
            maxY = i;
            maxPoints = points;
        }
        //drawLine(0, 0, j, i);
        j += (width * 0.6) * 0.20;
    }

    j = xMid + (xMid * 0.20);
    i += (height * 0.6) * 0.20;
}

//drawLine(0, 0, maxX, maxY);
//print(maxPoints);

for(i = 0; i < 360; i++){
    for(j = 1; j < imageWidth; j++){
        rad = i * (PI/180);
        xMag = cos(rad);
        yMag = sin(rad);
        xNew = (j * xMag) + maxX;
        yNew = (j * yMag) + maxY;

        value = getPixel(xNew, yNew);
        if(xNew < 0 || xNew > imageWidth || yNew < 0 || yNew > imageHeight){
            break;
        } else if(value == 125){
            dist = sqrt(pow(xNew - xOld, 2) + pow(yNew - yOld, 2)) ;
            if(dist > 5){
                //drawLine(xMid, yMid, xNew, yNew);
                //wait(1);

                tempXArray = newArray(100, xNew);
                tempXArray = Array.slice(tempXArray, 1, 2);
                xArray = Array.concat(xArray, tempXArray);

                tempYArray = newArray(100, yNew);
                tempYArray = Array.slice(tempYArray, 1, 2);
                yArray = Array.concat(yArray, tempYArray);
                xOld = xNew;
                yOld = yNew;
                break;
            } else {
                break;
            }
        }
    }
}

//print("xArray:");
//Array.print(xArray);
//print("yArray:");
//Array.print(yArray);
makeSelection("polygon", xArray, yArray, maxPoints);
selectImage(1);
makeSelection("polygon", xArray, yArray, maxPoints);