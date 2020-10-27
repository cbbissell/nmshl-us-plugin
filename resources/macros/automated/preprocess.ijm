// Automatic Cross-Sectional Echo Intensity for MG

//makeRectangle(196, 100, 614, 627);
//run("Crop");
run("Gamma...", "value=1.85");
run("Enhance Contrast...", "saturated=5");
//run("Despeckle");
run("Gaussian Blur...", "sigma=3");
run("AND...", "value=11110000");

run("Subtract...", "value=5");
run("Make Binary");

run("Remove Outliers...", "radius=4 threshold=50 which=Bright");