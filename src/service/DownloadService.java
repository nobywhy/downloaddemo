package service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import entity.FileInfo;

public class DownloadService extends Service {

	private String dir = Environment.getExternalStorageDirectory()+"/download";
	private static String TAG = DownloadService.class.getSimpleName();
	public static final String ACTION_START="ACTION_START";
	public static final String ACTION_PASUE="ACTION_PASUE";
	
	private final int MSG_INIT = 1;
	
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==MSG_INIT){
				FileInfo fileInfo = (FileInfo) msg.obj;
				Log.d(TAG, "length="+fileInfo.getLength());
				
				
				
				
			}
		}
		
	};
	
	
	@Override
	public void onCreate() {
		Log.d(TAG, "DownloadService onCreate()");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "DownloadService onStartCommand()");
		if(intent.getAction().equals(ACTION_START)){
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			Log.d(TAG, "start: "+fileInfo);
			
			new InitThread(DownloadService.this,fileInfo).start();
			
		}
		if(intent.getAction().equals(ACTION_PASUE)){
			FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
			Log.d(TAG, "stop: "+fileInfo);
		}
		
		
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 初始化线程，获取文件大小，创建文件
	 * @author lw
	 *
	 */
	class InitThread extends Thread{

		private FileInfo fileInfo;
		InitThread(Context context,FileInfo fileInfo){
			this.fileInfo = fileInfo;
		}
		
		
		@Override
		public void run() {
			
			URL url = null;
			HttpURLConnection connection = null;
			RandomAccessFile raf = null;
			try {
				url = new URL(fileInfo.getDownloadUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.setReadTimeout(3000);
				connection.setRequestMethod("GET");
				//获取文件大小
				int length = -1;
				if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
					length = connection.getContentLength();
				}
				if(length<=0){
					return;
				}
				
				//创建文件
				File fileDir = new File(dir);
				if(!fileDir.exists()){
					fileDir.mkdir();
				}
				File file = new File(dir,fileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.setLength(length);
				fileInfo.setLength(length);
				
				//通过handler传递参数给service
				Message message = handler.obtainMessage(MSG_INIT, fileInfo);
				handler.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				connection.disconnect();
                try {
                    if (raf != null) raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
			}
			
		}
		
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "DownloadService onDestroy()");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
