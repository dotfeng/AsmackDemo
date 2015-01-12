package net.fengg.asmack.demo.util;

import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

/**
 * 连接监听类
 * 
 * 
 */
public class MyConnectionListener implements ConnectionListener {
	private Timer tExit;
	private String username;
	private String password;
	private int logintime = 2000;

	@Override
	public void connectionClosed() {
		ConnectionTool.closeConnection();
		tExit = new Timer();
		tExit.schedule(new timetask(), logintime);
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		boolean error = e.getMessage().equals("stream:error (conflict)");
		if (!error) {
			ConnectionTool.closeConnection();
			tExit = new Timer();
			tExit.schedule(new timetask(), logintime);
		}
	}

	class timetask extends TimerTask {
		@Override
		public void run() {
			if (username != null && password != null) {
				if (CommonMethod.login(username, password)) {
					Log.i("MyConnectionListener", "登录成功");
				} else {
					Log.i("MyConnectionListener", "重新登录");
					tExit.schedule(new timetask(), logintime);
				}
			}
		}
	}

	@Override
	public void reconnectingIn(int arg0) {
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
	}

	@Override
	public void reconnectionSuccessful() {
	}

}

