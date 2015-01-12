package net.fengg.asmack.demo.service;

import net.fengg.asmack.demo.util.CommonMethod;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ConnectionService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		getOnLineMsg();
		getOnLineFile();
		getRoster();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void getOnLineMsg() {
		CommonMethod.getOnLine(this);
	}

	private void getOnLineFile() {
		CommonMethod.getOnLineFile(this);
	}
	
	private void getRoster() {
		CommonMethod.getRoster(this);
	}
}
