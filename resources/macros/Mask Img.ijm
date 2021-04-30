setBatchMode(true)

input = getArgument();
index = indexOf(input, "|");

file1 = substring(input, 0, index);
file2 = substring(input, index+1, lengthOf(input));

open(file1)
file1Name = File.name;
file1NameX = File.nameWithoutExtension;
open(file2)
file2Name = File.name;
file2NameX = File.nameWithoutExtension;

fileDir = File.directory;

imageCalculator("Subtract create", file2Name, file1Name);
selectWindow("Result of " + file2Name);
file3Name = "" + file1NameX + "bMASK.tif"
rename(file3Name);

run("Make Binary");
run("Dilate");
run("Dilate");
run("Dilate");
run("Dilate");
run("Fill Holes");
run("Erode");
run("Erode");
run("Erode");
run("Erode");

save(fileDir + file3Name);

imageCalculator("AND create", file3Name, file1Name);
selectWindow("Result of " + file3Name);
file4Name = "" + file1NameX + "sMASK.tif"
rename(file4Name);

save(fileDir + file4Name);

selectWindow(file1Name);
close();
selectWindow(file2Name);
close();
selectWindow(file3Name);
close();
selectWindow(file4Name);
close();