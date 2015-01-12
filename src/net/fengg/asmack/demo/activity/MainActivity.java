package net.fengg.asmack.demo.activity;

import java.lang.ref.WeakReference;

import net.fengg.asmack.demo.util.CommonMethod;

import org.jivesoftware.smack.SmackAndroid;

import net.fengg.asmack.demo.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener {
	Message msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SmackAndroid.init(getApplicationContext());
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			login(((TextView)findViewById(R.id.et_name)).getText().toString(),
					((TextView)findViewById(R.id.et_pwd)).getText().toString());
			break;
		case R.id.btn_regis:
			reg(((TextView)findViewById(R.id.et_name)).getText().toString(),
					((TextView)findViewById(R.id.et_pwd)).getText().toString());
			break;
		default:
			break;
		}
	}
	
	private void initView() {
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_regis).setOnClickListener(this);
	}

	private void login(final String name, final String pswd) {
		new Thread() {
			public void run() {
				msg = new Message();
				boolean result = false;
				try {
					result = CommonMethod.login(name , pswd);
		        }catch (Exception e) {
		            e.printStackTrace();
		        }finally {
		        	if(result) {
		        		msg.obj = "登录成功";
		        		msg.what = 1;
		        	} else {
		        		msg.what = 0;
		        		msg.obj = "登录失败";
		        	}
		        	insHandler.sendMessage(msg);
		        }
			};
		}.start();
	}
	
	private void reg(final String name, final String pswd) {
		new Thread() {
			public void run() {
				msg = new Message();
				boolean result = false;
				try {
					result = CommonMethod.createAccount(name , pswd);
		        }catch (Exception e) {
		            e.printStackTrace();
		        }finally {
		        	msg.what = 0;
		        	if(result) {
		        		msg.obj = "成功";
		        	} else {
		        		msg.obj = "失败";
		        	}
		        	insHandler.sendMessage(msg);
		        }
			};
		}.start();
	}
	
	MainHandler insHandler = new MainHandler(this);
	
	static class MainHandler extends Handler {
		WeakReference<MainActivity> mActivity;

		MainHandler(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity theActivity = mActivity.get();
			switch (msg.what) {
			case 0:
				theActivity.showToast((String) msg.obj);
				break;
			case 1:
				theActivity.openActivity(FriendsActivity.class);
				break;
			}
		}
	};
}
