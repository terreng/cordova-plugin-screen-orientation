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
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;

public class CDVOrientation extends CordovaPlugin implements SensorEventListener {
    
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
	

	/*
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
		
		OrientationEventListener orientationEventListener = new OrientationEventListener(cordova.getActivity())
		{
        @Override
        public void onOrientationChanged(int orientation)
			{
				Log.d(TAG, "orientation = " + orientation);
				executeGlobalJavascript("onRotationUpdate("+Integer.toString(orientation)+")");
			}
		}; 
	
		orientationEventListener.enable();
    }*/
	/*
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			executeGlobalJavascript("onRotationUpdate('land')");
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			executeGlobalJavascript("onRotationUpdate('port')");
		}
	}*/
	/*
	private static final int THRESHOLD = 40;
	public static final int PORTRAIT = 0;
	public static final int LANDSCAPE = 270;
	public static final int REVERSE_PORTRAIT = 180;
	public static final int REVERSE_LANDSCAPE = 90;
	private int lastRotatedTo = 0;
	
	@Override
	public void onOrientationChanged(int orientation) {
		int newRotateTo = lastRotatedTo;
		if(orientation >= 360 + PORTRAIT - THRESHOLD && orientation < 360 ||
				orientation >= 0 && orientation <= PORTRAIT + THRESHOLD)
			newRotateTo = 0;
		else if(orientation >= LANDSCAPE - THRESHOLD && orientation <= LANDSCAPE + THRESHOLD)
			newRotateTo = 90;
		else if(orientation >= REVERSE_PORTRAIT - THRESHOLD && orientation <= REVERSE_PORTRAIT + THRESHOLD)
			newRotateTo = 180;
		else if(orientation >= REVERSE_LANDSCAPE - THRESHOLD && orientation <= REVERSE_LANDSCAPE + THRESHOLD)
			newRotateTo = -90;
		if(newRotateTo != lastRotatedTo) {
			executeGlobalJavascript("onRotationUpdate("+Integer.toString(newRotateTo)+")");
			lastRotatedTo = newRotateTo;
		}
	}*/
	
	/*public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
	
	Context context = cordova.getActivity().getApplicationContext();
	
	OrientationEventListener orientationListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_UI) {
        public void onOrientationChanged(int orientation) {
            executeGlobalJavascript("onRotationUpdate("+Integer.toString(orientation)+")");
        }
    };
	
	orientationListener.enable();
    }*/
	
	/*
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
		
		Context context = cordova.getActivity();
		
		SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	private double pitch, tilt, azimuth;
	
	public void onSensorChanged(SensorEvent event) {
		//Get Rotation Vector Sensor Values
		double[] g = convertFloatsToDoubles(event.values.clone());
	
		//Normalise
		double norm = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2] + g[3] * g[3]);
		g[0] /= norm;
		g[1] /= norm;
		g[2] /= norm;
		g[3] /= norm;
	
		//Set values to commonly known quaternion letter representatives
		double x = g[0];
		double y = g[1];
		double z = g[2];
		double w = g[3];
	
		//Calculate Pitch in degrees (-180 to 180)
		double sinP = 2.0 * (w * x + y * z);
		double cosP = 1.0 - 2.0 * (x * x + y * y);
		pitch = Math.atan2(sinP, cosP) * (180 / Math.PI);
	
		//Calculate Tilt in degrees (-90 to 90)
		double sinT = 2.0 * (w * y - z * x);
		if (Math.abs(sinT) >= 1)
			tilt = Math.copySign(Math.PI / 2, sinT) * (180 / Math.PI);
		else
			tilt = Math.asin(sinT) * (180 / Math.PI);
	
		//Calculate Azimuth in degrees (0 to 360; 0 = North, 90 = East, 180 = South, 270 = West)
		double sinA = 2.0 * (w * z + x * y);
		double cosA = 1.0 - 2.0 * (y * y + z * z);
		azimuth = Math.atan2(sinA, cosA) * (180 / Math.PI);
		
		executeGlobalJavascript("onRotationUpdate("+Integer.toString((int) tilt)+")");
	}
	
	private double[] convertFloatsToDoubles(float[] input)
	{
		if (input == null)
			return null;
	
		double[] output = new double[input.length];
	
		for (int i = 0; i < input.length; i++)
			output[i] = input[i];
	
		return output;
	}
	
	public boolean flatEnough(double degreeTolerance) {
		return tilt <= degreeTolerance && tilt >= -degreeTolerance && pitch <= degreeTolerance && pitch >= -degreeTolerance;
	}*/
	
	/*public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
	
	Context context = cordova.getActivity().getApplicationContext();
	
	OrientationEventListener orientationListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_UI) {
        public void onOrientationChanged(int orientation) {
            executeGlobalJavascript("onRotationUpdate("+Integer.toString(orientation)+")");
        }
    };
	
	orientationListener.enable();
    }*/
	
	/*
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			executeGlobalJavascript("onRotationUpdate('land')");
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			executeGlobalJavascript("onRotationUpdate('port')");
		}
	}*/
	
	/*private static final int THRESHOLD = 40;
	public static final int PORTRAIT = 0;
	public static final int LANDSCAPE = 270;
	public static final int REVERSE_PORTRAIT = 180;
	public static final int REVERSE_LANDSCAPE = 90;
	private int lastRotatedTo = 0;
	
	@Override
	public void onOrientationChanged(int orientation) {
		int newRotateTo = lastRotatedTo;
		if(orientation >= 360 + PORTRAIT - THRESHOLD && orientation < 360 ||
				orientation >= 0 && orientation <= PORTRAIT + THRESHOLD)
			newRotateTo = 0;
		else if(orientation >= LANDSCAPE - THRESHOLD && orientation <= LANDSCAPE + THRESHOLD)
			newRotateTo = 90;
		else if(orientation >= REVERSE_PORTRAIT - THRESHOLD && orientation <= REVERSE_PORTRAIT + THRESHOLD)
			newRotateTo = 180;
		else if(orientation >= REVERSE_LANDSCAPE - THRESHOLD && orientation <= REVERSE_LANDSCAPE + THRESHOLD)
			newRotateTo = -90;
		if(newRotateTo != lastRotatedTo) {
			executeGlobalJavascript("onRotationUpdate("+Integer.toString(newRotateTo)+")");
			lastRotatedTo = newRotateTo;
		}
	}*/
	
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
		
		Context context = cordova.getActivity();
		
		SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	private double pitch, tilt, azimuth;
	
	public void onSensorChanged(SensorEvent event) {
		//Get Rotation Vector Sensor Values
		double[] g = convertFloatsToDoubles(event.values.clone());
	
		//Normalise
		double norm = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2] + g[3] * g[3]);
		g[0] /= norm;
		g[1] /= norm;
		g[2] /= norm;
		g[3] /= norm;
	
		//Set values to commonly known quaternion letter representatives
		double x = g[0];
		double y = g[1];
		double z = g[2];
		double w = g[3];
	
		//Calculate Pitch in degrees (-180 to 180)
		double sinP = 2.0 * (w * x + y * z);
		double cosP = 1.0 - 2.0 * (x * x + y * y);
		pitch = Math.atan2(sinP, cosP) * (180 / Math.PI);
	
		//Calculate Tilt in degrees (-90 to 90)
		double sinT = 2.0 * (w * y - z * x);
		if (Math.abs(sinT) >= 1)
			tilt = Math.copySign(Math.PI / 2, sinT) * (180 / Math.PI);
		else
			tilt = Math.asin(sinT) * (180 / Math.PI);
	
		//Calculate Azimuth in degrees (0 to 360; 0 = North, 90 = East, 180 = South, 270 = West)
		double sinA = 2.0 * (w * z + x * y);
		double cosA = 1.0 - 2.0 * (y * y + z * z);
		azimuth = Math.atan2(sinA, cosA) * (180 / Math.PI);
		
		executeGlobalJavascript("onRotationUpdate("+Integer.toString((int) tilt)+")");
	}
	
	private double[] convertFloatsToDoubles(float[] input)
	{
		if (input == null)
			return null;
	
		double[] output = new double[input.length];
	
		for (int i = 0; i < input.length; i++)
			output[i] = input[i];
	
		return output;
	}
	
	public boolean flatEnough(double degreeTolerance) {
		return tilt <= degreeTolerance && tilt >= -degreeTolerance && pitch <= degreeTolerance && pitch >= -degreeTolerance;
	}


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        
        Log.d(TAG, "execute action: " + action);
		
		executeGlobalJavascript("onRotationUpdate(-200)");
        
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