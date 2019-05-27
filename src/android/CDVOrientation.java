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

package cordova.plugins.screenorientation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;

import android.view.OrientationEventListener;
import android.provider.Settings;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;

public class CDVOrientation extends CordovaPlugin {
    
    private static final String TAG = "YoikScreenOrientation"; 
    
    /**
     * Screen Orientation Constants
     */
    
    private static final String ANY = "any";
    private static final String PORTRAIT_PRIMARY = "portrait-primary";
    private static final String PORTRAIT_SECONDARY = "portrait-secondary";
    private static final String LANDSCAPE_PRIMARY = "landscape-primary";
    private static final String LANDSCAPE_SECONDARY = "landscape-secondary";
    private static final String PORTRAIT = "portrait";
    private static final String LANDSCAPE = "landscape";
	OrientationEventListener orientationEventListener;
	
	@Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
		
		orientationEventListener = new OrientationEventListener(cordova.getActivity())
		{
		private static final int THRESHOLD = 45;
		public static final int PORTRAIT = 0;
		public static final int LANDSCAPE = 270;
		public static final int REVERSE_PORTRAIT = 180;
		public static final int REVERSE_LANDSCAPE = 90;
		private int lastRotatedTo = 0;
			
        @Override
        public void onOrientationChanged(int orientation)
			{
				String islocked = "false";
				Context context = cordova.getActivity().getApplicationContext();
				if (android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) != 1) {
					islocked = "true";
				}
				
				int newRotateTo = lastRotatedTo;
				if(orientation >= 360 + PORTRAIT - THRESHOLD && orientation < 360 || orientation >= 0 && orientation <= PORTRAIT + THRESHOLD)
					newRotateTo = 0;
				else if(orientation >= LANDSCAPE - THRESHOLD && orientation <= LANDSCAPE + THRESHOLD)
					newRotateTo = 90;
				else if(orientation >= REVERSE_PORTRAIT - THRESHOLD && orientation <= REVERSE_PORTRAIT + THRESHOLD)
					newRotateTo = 180;
				else if(orientation >= REVERSE_LANDSCAPE - THRESHOLD && orientation <= REVERSE_LANDSCAPE + THRESHOLD)
					newRotateTo = -90;
				if(newRotateTo != lastRotatedTo) {
					executeGlobalJavascript("onRotationUpdate("+Integer.toString(newRotateTo)+","+islocked+")");
					lastRotatedTo = newRotateTo;
				}
			}
		}; 
	
		orientationEventListener.enable();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        
        Log.d(TAG, "execute action: " + action);
        
        // Route the Action
        if (action.equals("screenOrientation")) {
            return routeScreenOrientation(args, callbackContext);
        }
        
        // Action not found
        callbackContext.error("action not recognised");
        return false;
    }
	
    private void executeGlobalJavascript(final String jsString){
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + jsString);
            }
        });
    }
    
    private boolean routeScreenOrientation(JSONArray args, CallbackContext callbackContext) {
        
        String action = args.optString(0);
        
        String orientation = args.optString(1);
        
        Log.d(TAG, "Requested ScreenOrientation: " + orientation);
        
        Activity activity = cordova.getActivity();
        
        if (orientation.equals(ANY)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if (orientation.equals(LANDSCAPE_PRIMARY)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (orientation.equals(PORTRAIT_PRIMARY)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation.equals(LANDSCAPE)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (orientation.equals(PORTRAIT)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else if (orientation.equals(LANDSCAPE_SECONDARY)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        } else if (orientation.equals(PORTRAIT_SECONDARY)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
        
        callbackContext.success();
        return true;
        
        
    }
}