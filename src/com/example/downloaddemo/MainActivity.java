package com.example.downloaddemo;

import service.DownloadService;
import entity.FileInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 多线程断点下载demo
 * 核心流程：
 * 	1、activity将要下载的文件信息传递给service
 * 	2、service启动一个thread去下载文件
 * 	3、thread下载时，需要将下载进度保存到数据库，将文件报错到SD卡，同时讲下载进度以广播的形式发送给activity
 * 	4、activity接收广播，更新进度条UI
 * @author lw
 *
 */
public class MainActivity extends Activity {

	private Button btn_start;
	private Button btn_pasue;
	private ProgressBar progressBar1;
	private TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_start = (Button) this.findViewById(R.id.btn_start);
		btn_pasue = (Button) this.findViewById(R.id.btn_pause);
		progressBar1 = (ProgressBar) this.findViewById(R.id.progressBar1);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		
		btn_start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FileInfo fileInfo = new FileInfo(1, "imooc.apk", "http://www.imooc.com/mobile/imooc.apk", 0, 0);
				tv_title.setText(fileInfo.getFileName());
				Intent intent = new Intent(MainActivity.this,DownloadService.class);
				intent.setAction(DownloadService.ACTION_START);
				intent.putExtra("fileInfo", fileInfo);
				startService(intent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
}
