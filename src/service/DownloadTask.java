package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import db.ThreadInfoRepository;
import db.ThreadInfoRepositoryImpl;
import entity.FileInfo;
import entity.ThreadInfo;

public class DownloadTask {

	private FileInfo fileInfo;
	private Context context;
	private ThreadInfoRepository dao;
	public static boolean isPasue= false;


	public DownloadTask(Context context, FileInfo fileInfo) {
		this.fileInfo = fileInfo;
		this.context = context;
		dao = new ThreadInfoRepositoryImpl(context);
	}

	public void download() {
		// ��ѯ���ݿ����Ƿ����
		List<ThreadInfo> threadInfos = dao.getThreadInfos(fileInfo.getDownloadUrl());
		ThreadInfo threadInfo = null;
		if (threadInfos == null || threadInfos.isEmpty()) {
			threadInfo = new ThreadInfo(0, fileInfo.getDownloadUrl(), 0, fileInfo.getLength(), 0);
			dao.insert(threadInfo);
		} else {
			threadInfo = threadInfos.get(0);
		}

		// ���������߳�
		new DownloadThread(threadInfo).start();
	}

	class DownloadThread extends Thread {

		private ThreadInfo threadInfo;

		public DownloadThread(ThreadInfo threadInfo) {
			this.threadInfo = threadInfo;
		}

		@Override
		public void run() {
			URL url = null;
			HttpURLConnection conn = null;
			InputStream in = null;
			RandomAccessFile raf = null;
			try {
				url = new URL(threadInfo.getDownloadUrl());
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");

				int start = threadInfo.getFinished();
				int end = threadInfo.getEnd();
				// ���ֶ�ȡ����start��ʼ��ȡ
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end);

				File file = new File(Environment.getExternalStorageDirectory() + "/download/" + fileInfo.getFileName());
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(start);// д�ļ�ʱ����x���ֽڣ�����д��

				int finished = start;
				in = conn.getInputStream();
				
				long time = System.currentTimeMillis();
				
				if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
					byte[] data = new byte[1024*5];
					int len = -1;
					while ((len = in.read(data)) != -1) {
						
						raf.write(data, 0, len);

						finished = finished + len;
						Log.d("finished", "finished="+finished);
						int progress = Integer.valueOf(finished * 100/ fileInfo.getLength());
						Intent intent = new Intent();
						intent.setAction(DownloadService.ACTION_UPDATE);
						intent.putExtra(DownloadService.FINISHED, progress);
						// ���͹㲥
						context.sendBroadcast(intent);
						
						if(isPasue){
							//��ͣ,�����ȱ��浽���ݿ��У��´ο�ʼʱ���������ж�ȡ����
							dao.update(threadInfo.getDownloadUrl(), threadInfo.getId(), finished);
							return;
						}
						
					}

					// ������ɣ�ɾ�����ݿ��еļ�¼
					dao.delete(threadInfo.getDownloadUrl(), threadInfo.getId());
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (raf != null) {
						raf.close();
					}
					if (in != null) {
						in.close();
					}
					if (conn != null) {
						conn.disconnect();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

}
