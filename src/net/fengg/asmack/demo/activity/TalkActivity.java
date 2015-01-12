package net.fengg.asmack.demo.activity;

import net.fengg.asmack.demo.util.CommonMethod;
import net.fengg.asmack.demo.util.DemoApplication;
import net.fengg.asmack.demo.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class TalkActivity extends BaseActivity implements OnClickListener {

	// 广播接受者
	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("msg_receiver".equals(intent.getAction())) {
				if (null != intent.getExtras()) {
					if(intent.getExtras().getString("from").contains(DemoApplication.FromName)) {
						((EditText) findViewById(R.id.et_talk)).setText(intent
								.getExtras().getString("msg"));
					} else {
						showToast(intent.getExtras().getString("from") + "\n" +
								intent.getExtras().getString("msg"));
					}
				}
			} else if ("file_receiver".equals(intent.getAction())) {
				if (null != intent.getExtras()) {
					((EditText) findViewById(R.id.et_talk)).setText(intent
							.getExtras().getString("path"));
					((TextView) findViewById(R.id.tv_progress)).setText(intent
							.getExtras().getDouble("progress") + "");
				}
			} else if ("roster_receiver".equals(intent.getAction())) {
				showToast(intent.getExtras().getString("msg"));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talks);
		regisBroadcast();
		initView();
	}

	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("msg_receiver");
		filter.addAction("file_receiver");
		this.registerReceiver(this.receiver, filter);
	}

	private void initView() {
		findViewById(R.id.btn_sendmsg).setOnClickListener(this);
		findViewById(R.id.btn_sendimg).setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		DemoApplication.FromName = "";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 发送文本消息
		case R.id.btn_sendmsg:
			String txt = ((EditText) findViewById(R.id.et_talk)).getText()
					.toString();
			if (0 < txt.length()) {
				CommonMethod.sendTalkMsg(DemoApplication.FromName, txt);
			}
			break;
		// 发送图片
		case R.id.btn_sendimg:
			String filepath = Environment.getExternalStorageDirectory()
					.getPath() + "/";
			filepath += ((EditText) findViewById(R.id.et_talk)).getText()
					.toString();
			CommonMethod.sendTalkFile(DemoApplication.FromName, filepath);
			break;

		default:
			break;
		}
	}
}
