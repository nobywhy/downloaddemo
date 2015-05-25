package entity;

import java.io.Serializable;

public class FileInfo implements Serializable {
	private int id;
	private String fileName;// �ļ���
	private String downloadUrl;// ���ص�ַ
	private int length;// �ļ���С
	private int finished;// ��������˶���

	public FileInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileInfo(int id, String fileName, String downloadUrl, int length, int finished) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.downloadUrl = downloadUrl;
		this.length = length;
		this.finished = finished;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", fileName=" + fileName + ", downloadUrl=" + downloadUrl + ", length=" + length
				+ ", finished=" + finished + "]";
	}

}
