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
	for(vector<vector<Point>>::iterator it = contours.begin();it != contours.end();){
        if (it-> size() < contourMin){
            it = contours.erase(it);
        }
        else{
            ++it;
        }
	}

	//Vector for min Area Boxes
	vector<RotatedRect> minRect(contours.size());

	NullTargets(targets);
	if(!contours.empty()&& !hierarchy.empty()){
        for(unsigned int i = 0; i < contours.size(); i++){
            //Capture corners of contour
            minRect[i] = minAreaRect(Mat(contours[i]));

            Rect box = minRect[i].boundingRect();
            double WHRatio = box.width/((double)box.height);
            double HWRatio = ((double) box.height) / box.width;

            //check if contour is vert
            if((HWRatio>MinVRatio) && (HWRatio<MaxVRatio)){
                if(targets.VertLeftGoal == true){
                    if(targets.VertLeftCenter.x<(box.x+box.width/2)){
                        targets.VertRightGoal = true;
                        targets.VerticalRightTarget = box;
                        targets.VerticalRightAngle = minRect[i].angle;
                        targets.VerticalRightCenter = Point(box.x+box.width/2, box.y+box.height/2);
                        targets.VerticalRight_H_W_Ratio = HWRatio;
                        targets.VerticalRight_W_H_Ratio = WHRatio;
                    }
                    else{
                        targets.VertRightGoal = targets.VertLeftGoal;
                        targets.VerticalRightTarget = targets.VerticalLeftTarget;
                        targets.VerticalRightAngle = targets.VerticalLeftAngle;
                        targets.VerticalRightCenter = targets.VerticalLeftCenter;
                        targets.VerticalRight_H_W_Ratio = targets.VerticalLeft_H_W_Ratio;
                        targets.VerticalRight_W_H_Ratio = targets.VerticalLeft_W_H_Ratio;

                        targets.VertLeftGoal = true;
                        targets.VerticalLeftTarget =box;
                        targets.VerticalLeftAngle = minRect[i].angle;
                        targets.VerticalLeftCenter = Point(box.x + box.width/2, box.y+box.height/2);
                        targets.VerticalLeft_H_W_Ratio = HWRatio;
                        targets.VerticalLeft_W_H_Ratio = WHRatio;
                    }
                }
                else{
                    targets.VertLeftGoal = true;
                    targets.VerticalLeftTarget =box;
                    targets.VerticalLeftAngle = minRect[i].angle;
                    targets.VerticalLeftCenter = Point(box.x + box.width/2, box.y+box.height/2);
                    targets.VerticalLeft_H_W_Ratio = HWRatio;
                    targets.VerticalLeft_W_H_Ratio = WHRatio;
                }
            }
            else if((WHRatio>MinHRatio) && WHRatio < MaxHRatio)){
                targets.HorizGoal = true;
                targets.HorizontalTarget = box;
                targets.HorizontalAngle = minRect[i].angle;
                targets.HorizontalCenter = Point(box.x + box.width/2, box.y+box.height/2);
                targets.Horizontal_H_W_Ratio = HWRatio;
                targets.Horizontal_W_H_Ratio = WHRatio;
            }

            if(targets.HorizGoal&&targets.VertGoalLeft&&targets.VertGoalRight){
                targets.HotGoal = true;

                //determine Left or Right
                int center = (targets.VerticalLeftCenter.x + targets.VerticalRightCenter.x)/2;
                int imageCenter = original.size().width/2;
                if(center < imageCenter){
                    //TURN RIGHT
                }else{
                    //TURN LEFT
                }
            }


        }//End for loop
	}//end If statement
	else{
        cout<<"No Contours" << endl;
        //DONT TURN
	}

}

//Filter
Mat ThresholdImage(Mat original){
    //Local Temp Image
    Mat thresholded;

    //Remove non green object;
    inRange(original, Scalar(minB, minG, minR), Sclar(maxB, maxG, maxR), thresholded);

    //smooth Edges
    blur(thresholded, thresholded, Size(3,3));

    //Additional filtering if needed
    //Canny(thresholded, thresholded, 100,100,3);
    //blur(thresholded, thresholded, Size(5,5));

    return thresholded;
}

void NullTargets(Target& target){
    target.HorizontalAngle =0.0;
    target.VerticalLeftAngle = 0.0;
    target.VerticalRightAngle = 0.0;
    target.Horizontal_H_W_Ratio = 0.0;
    target.Horizontal_W_H_Ratio = 0.0;
    target.VerticalRight_H_W_Ratio =0.0;
    target.VerticalRight_W_H_Ratio = 0.0;
    target.VerticalLeft_H_W_Ratio =0.0;
    target.VerticalLeft_W_H_Ratio = 0.0;
    target.targetDistance = 0.0;
    target.targetLeftOrRight = 0;

    target.HorizGoal = false;
    target.VertLeftGoal = false;
    target.VertRightGoal = false;
    target.HotGoal = false;
}







