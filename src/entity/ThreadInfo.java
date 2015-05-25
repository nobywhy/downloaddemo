package entity;

import java.io.Serializable;

/**
 * 线程下载信息，记录下载进度，保存到数据库
 * 
 * @author Administrator
 * 
 */
public class ThreadInfo implements Serializable {

	private int id;
	private String downloadUrl;// 下载地址
	private int start;// 开始位置
	private int end;// 接收位置
	private int finished;// 完成多少

	public ThreadInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ThreadInfo(int id, String downloadUrl, int start, int end, int finished) {
		super();
		this.id = id;
		this.downloadUrl = downloadUrl;
		this.start = start;
		this.end = end;
		this.finished = finished;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "ThreadInfo [id=" + id + ", downloadUrl=" + downloadUrl + ", start=" + start + ", end=" + end + ", finished="
				+ finished + "]";
	}

}
