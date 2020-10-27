if(selectionType() != 4){
    run("Select None");
    return;
}

getSelectionCoordinates(xCoordinates, yCoordinates);
getStatistics(area, mean, min, max, std, histogram);

xNew = xCoordinates[0];
yNew = yCoordinates[0];

print("mean: " + mean);

count = 1;
while(count < lengthOf(xCoordinates) && (getPixel(xNew, yNew) < mean - 50 || getPixel(xNew, yNew) > mean + 50)){
    xNew = xCoordinates[count];
    yNew = yCoordinates[count];
    count++;
}

print("x:" + xNew);
print("y:" + yNew);
print("pixel value: " + getPixel(xNew, yNew));

if(mean < 50){
}else if(mean > 125){
    setForegroundColor(125, 125, 125);
    floodFill(xNew, yNew);
    setForegroundColor(255, 255, 255);
} else {
    setForegroundColor(255, 255, 255);
    floodFill(xNew, yNew);
}

run("Select None");