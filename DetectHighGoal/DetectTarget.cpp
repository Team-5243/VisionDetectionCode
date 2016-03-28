/*
 * DetectTarget.cpp
 *
 *  Created on: Mar 28, 2016
 *      Author: CvilleRobotics-2
 */

#include "DetectTarget.h"

int main(int agc, const char* argv[]){

	Mat img, thresholded,output;
	//OPEN tmp.jpg with open CV save to img;
	img = Commentabove;
	thresholded = ThresholdImage(img);

	findTarget(img, thresholded, targets);
	CalculateDist(targets);

	return 0;

}
void CalculateDist(Target& targets){
	double targetHeight= 4;//Get info from will
	int height = targets.VerticalTarget.height;
	targets.targetDistance = Y_IMAGE_RES * targetHeight/(height*12*2*tan(VIEW_ANGLE * PI / (180*2)));
}
void findTarget(Mat original, Mat thresholded, Target& target){

	vector<Vec4i> hierarchy;
	vector<vector<Point>> contours;

	//find rectangles
	findContours(thresholded, contours, hiearchy, RETR_EXTERNAL,CHAIN_APPROX_SIMPLE );

	unsigned int contourMin =6;
	for(vector<vector<Point>>::iterator it = contours.begin)
}





