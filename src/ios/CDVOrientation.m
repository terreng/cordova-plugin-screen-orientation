/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

#import "CDVOrientation.h"
#import <Cordova/CDVViewController.h>
#import <objc/message.h>
#import <CoreMotion/CoreMotion.h>

@interface CDVOrientation () {}
@end

@implementation CDVOrientation

UIInterfaceOrientation orientationLast, orientationAfterProcess;
CMMotionManager *motionManager;
UIInterfaceOrientation orientationNew;

 - (void)initializeMotionManager{
    motionManager = [[CMMotionManager alloc] init];
    motionManager.accelerometerUpdateInterval = .3;
    motionManager.gyroUpdateInterval = .3;

    [motionManager startAccelerometerUpdatesToQueue:[NSOperationQueue currentQueue]
                                        withHandler:^(CMAccelerometerData  *accelerometerData, NSError *error) {
                                            if (!error) {
                                                [self outputAccelerationData:accelerometerData.acceleration];
                                            }
                                            else{
                                                NSLog(@"%@", error);
                                            }
                                        }];
}

    - (void)outputAccelerationData:(CMAcceleration)acceleration{

    NSString *rotatedeg = @"";

    if (acceleration.x >= 0.75 && orientationLast != UIInterfaceOrientationLandscapeLeft) {
        orientationNew = UIInterfaceOrientationLandscapeLeft;
        rotatedeg = @"-90";
    }
    else if (acceleration.x <= -0.75  && orientationLast != UIInterfaceOrientationLandscapeRight) {
        orientationNew = UIInterfaceOrientationLandscapeRight;
        rotatedeg = @"90";
    }
    else if (acceleration.y <= -0.75) {
        orientationNew = UIInterfaceOrientationPortrait;
        rotatedeg = @"0";
    }
    else if (acceleration.y >= 0.75) {
        orientationNew = UIInterfaceOrientationPortraitUpsideDown;
        rotatedeg = @"180";
    }
    else {
        return;
    }

    if (orientationNew == orientationLast)
        return;

    orientationLast = orientationNew;

    [self.commandDelegate evalJs:[NSString stringWithFormat:@"onRotationUpdate('%@',false)", rotatedeg]];
}

-(void)screenOrientation:(CDVInvokedUrlCommand *)command
{

    //[self.commandDelegate evalJs:@"alert('-200')"];

    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;

    CDVPluginResult* pluginResult;
    NSInteger orientationMask = [[command argumentAtIndex:0] integerValue];
    CDVViewController* vc = (CDVViewController*)self.viewController;
    NSMutableArray* result = [[NSMutableArray alloc] init];

    if (orientationMask == nil) {
        [self initializeMotionManager];
    }
    
    if(orientationMask & 1) {
        [result addObject:[NSNumber numberWithInt:UIInterfaceOrientationPortrait]];
    }
    //if(orientationMask & 2) {
    //    [result addObject:[NSNumber numberWithInt:UIInterfaceOrientationPortraitUpsideDown]];
    //}
    if(orientationMask & 4) {
        [result addObject:[NSNumber numberWithInt:UIInterfaceOrientationLandscapeRight]];
    }
    if(orientationMask & 8) {
        [result addObject:[NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft]];
    }
    
    SEL selector = NSSelectorFromString(@"setSupportedOrientations:");
    
    if([vc respondsToSelector:selector]) {
        if (orientationMask != 15 || [UIDevice currentDevice] == nil) {
            ((void (*)(CDVViewController*, SEL, NSMutableArray*))objc_msgSend)(vc,selector,result);
        }
        
        if ([UIDevice currentDevice] != nil){
            NSNumber *value = nil;
            if (orientationMask != 15) {
                if (!_isLocked) {
                    _lastOrientation = [UIApplication sharedApplication].statusBarOrientation;
                }
                UIInterfaceOrientation deviceOrientation = [UIApplication sharedApplication].statusBarOrientation;
                if(orientationMask == 8  || (orientationMask == 12  && !UIInterfaceOrientationIsLandscape(deviceOrientation))) {
                    value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeLeft];
                } else if (orientationMask == 4){
                    value = [NSNumber numberWithInt:UIInterfaceOrientationLandscapeRight];
                } else if (orientationMask == 1 || (orientationMask == 3 && !UIInterfaceOrientationIsPortrait(deviceOrientation))) {
                    value = [NSNumber numberWithInt:UIInterfaceOrientationPortrait];
                } else if (orientationMask == 2) {
                    value = [NSNumber numberWithInt:UIInterfaceOrientationPortraitUpsideDown];
                }
            } else {
                if (_lastOrientation != UIInterfaceOrientationUnknown) {
                    [[UIDevice currentDevice] setValue:[NSNumber numberWithInt:_lastOrientation] forKey:@"orientation"];
                    ((void (*)(CDVViewController*, SEL, NSMutableArray*))objc_msgSend)(vc,selector,result);
                    [UINavigationController attemptRotationToDeviceOrientation];
                }
            }
            if (value != nil) {
                _isLocked = true;
                [[UIDevice currentDevice] setValue:value forKey:@"orientation"];
            } else {
                _isLocked = false;
            }
        }
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    }
    else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_INVALID_ACTION messageAsString:@"Error calling to set supported orientations"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
}

@end
