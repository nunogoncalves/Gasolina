package com.numicago.android.gasolina.utils;

import java.io.File;

import android.content.Context;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
            		android.os.Environment.getExternalStorageDirectory(), 
            		context.getString(context.getApplicationInfo().labelRes));
        else
            cacheDir = context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url, String fileName)
    {
    	String filename;
//        //I identify images by hashcode. Not a perfect solution, good for the demo.
//    	if(fileName != null && fileName.length() != 0) {
//    		filename = String.valueOf(fileName).replaceAll("[^a-zA-Z ]+","");
//    	} else {
    	filename=String.valueOf(url.hashCode());
//    	}
        File f = new File(cacheDir, filename + ".jpg");
        return f;
        
    }
    
    public void clear()
    {
        File[] files = cacheDir.listFiles();
        for(File f : files)
        	f.delete();
    }
}