package net.fengg.asmack.demo.bean;

/**
 * 离线消息类
 * 
 * @author jin
 * 
 */
public class OffLineBean {
	private String to;
	private String body;
	private String from;
	private String date;

	public OffLineBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OffLineBean(String to, String body, String from, String date) {
		super();
		this.to = to;
		this.body = body;
		this.from = from;
		this.date = date;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getTo() + getBody() + this.getFrom() + getDate();
	}

}
