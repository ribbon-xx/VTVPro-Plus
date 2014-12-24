package mdn.vtvsport.object;

public class ScheduleInfo {
	private String id;
	private String time;
	private String title;
	private String content;
	private String channelId;
	
	public ScheduleInfo() {
		this.id = "";
		this.time = "";
		this.title = "";
		this.content = "";
		this.channelId = "";
	}
	
	public ScheduleInfo(String time, String title, String content) {
		this.time = time;
		this.title = title;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
