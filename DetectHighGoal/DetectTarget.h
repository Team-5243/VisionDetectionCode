/*
 * DetectTarget.h
 *
 *  Created on: Mar 28, 2016
 *      Author: CvilleRobotics-2
 */

#ifndef DETECTTARGET_H_
#define DETECTTARGET_H_

#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <ctime>
#include <iostream>
#include <sstream>

	void initializeParams(ProgParams& params);
	Mat ThreholdImage(Mat img);
	void findTarget(Mat original, Mat thresholded, Target& targets);
	void NullTargets(Target& targts);

//Thresholds
	int minR = 0;
	int maxR = 0;
	int minG = 80;
	int maxG = 255;
	int minB = 0;
	int maxB = 0;

	//Ratio ranges. Used to see if target is horizantal or verticle
	double MinHRatio = 1.5;
	double MaxHRatio =6.6;

	double MinVRatio = 1.5;
	double MaxVRatio = 8.5;
	Target target;
	Mat frame;

struct Target{
public:
		double HorizontalAngle;
		double VerticalLeftAngle;
        double VerticalRightAngle;
		double Horizontal_W_H_Ratio;
		double Horizontal_H_W_Ratio;
		double VerticalLeft_W_H_Ratio;
		double VerticalLeft_H_W_Ratio;
        double VerticalRight_W_H_Ratio;
		double VerticalRight_H_W_Ratio;

		Point HorizontalCenter;
		Point VerticalLeftCenter;
		Point VerticalRightCenter;

		bool HorizGoal;
		bool VertLeftGoal;
		bool VertRightGoal;
		bool HotGoal;

	};






#endif /* DETECTTARGET_H_ */
