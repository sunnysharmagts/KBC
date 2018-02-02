package com.mysterio.bm;

public class ScreenDimension {
	
	private int mDeviceHeight, mDeviceWidth;
	private static ScreenDimension sDimension = null;
	//private Constructor
	private ScreenDimension() { 
		mDeviceHeight = mDeviceWidth = 0;
	}
	
	public int getDeviceHeight(){
		return mDeviceHeight;
	}
	
	public int getDeviceWidth(){
		return mDeviceWidth;
	}
	
	public void setDeviceHeight(int mDeviceHeight){
		this.mDeviceHeight = mDeviceHeight;
	}
	
	public void setDeviceWidth(int mDeviceWidth){
		this.mDeviceWidth = mDeviceWidth;
	}
	
	public static synchronized ScreenDimension getSingletonObject(){
		if(sDimension == null)
			sDimension = new ScreenDimension();
		
		return sDimension;
	}
}
