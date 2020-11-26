// Automatic Cross-Sectional Echo Intensity for MG

wait(700);
run("Duplicate...", " ");
rename("preprocessed_img");

run("Minimum...", "radius=1");
//run("Gaussian Blur...", "sigma=2");
//run("Convolve...", "text1=[-1 -1 -1 -1 -1 -1 -1\n-1 -1 -1 -1 -1 -1 -1\n1 1 1 1 1 1 1\n1 1 1 1 1 1 1\n1 1 1 1 1 1 1\n0 0 0 0 0 0 0\n0 0 0 0 0 0 0\n] normalize");

run("Gaussian Blur...", "sigma=3.5");
//setMinAndMax(10, 200);
//run("Convolve...", "text1=[-1 -1 -1 -1 -1\n-1 -1 -1 -1 -1\n1 1 1 1 1\n1 1 1 1 1\n1 1 1 1 1\n0 0 0 0 0\n0 0 0 0 0] normalize");
run("Convolve...", "text1=[-1 -1 -1 -1 -1 -1 -1\n-1 -1 -1 -1 -1 -1 -1\n1 1 1 1 1 1 1\n1 1 1 1 1 1 1\n1 1 1 1 1 1 1\n0 0 0 0 0 0 0\n0 0 0 0 0 0 0\n] normalize");
run("Convolve...", "text1=[0 0 0 0 0 0 0\n0 0 0 0 0 0 0\n1 1 1 1 1 1 1\n1 1 1 1 1 1 1\n1 1 1 1 1 1 1\n-1 -1 -1 -1 -1 -1 -1\n-1 -1 -1 -1 -1 -1 -1\n] normalize");
run("Gaussian Blur...", "sigma=3");
run("Convolve...", "text1=[-1 -1 1 1 1 0 0\n-1 -1 1 1 1 0 0\n-1 -1 1 1 1 0 0\n-1 -1 1 1 1 0 0\n-1 -1 1 1 1 0 0] normalize");
run("Convolve...", "text1=[0 0 1 1 1 -1 -1\n0 0 1 1 1 -1 -1\n0 0 1 1 1 -1 -1\n0 0 1 1 1 -1 -1\n0 0 1 1 1 -1 -1] normalize");

//setMinAndMax(10, 160);

//run("Gaussian Blur...", "sigma=2");

//run("Gamma...", "value=1.85");
run("Gamma...", "value=2.05");
run("Enhance Contrast...", "saturated=5");
run("Despeckle");
//run("Gaussian Blur...", "sigma=3");
//run("AND...", "value=11110000");

run("Subtract...", "value=5");
run("Make Binary");

run("Remove Outliers...", "radius=4 threshold=50 which=Bright");
