package net.fengg.asmack.demo.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import net.fengg.asmack.demo.adapter.SearchAdap;
import net.fengg.asmack.demo.util.CommonMethod;
import org.jivesoftware.smackx.packet.VCard;

import com.mustafaferhan.debuglog.DebugLog;

import net.fengg.asmack.demo.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();
	}

	private void initView() {
		ArrayList<String> users = null;
		if (null == (users = getIntent().getStringArrayListExtra("users"))) {
			this.finish();
		}

		ListView searlistview = (ListView) findViewById(R.id.lv_searchusers);
		SearchAdap adap = new SearchAdap(this, users);
		searlistview.setAdapter(adap);
		searlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View conView,
					int arg2, long arg3) {
				String user = ((SearchAdap.ViewHolder) conView.getTag()).fName
						.getText().toString();
				getVCard(user);
			}
		});
		searlistview.setOnItemLongClickListener(new OnItemLongClickListener() {
			private View cView = null;

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View conView,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				cView = conView;
				new AlertDialog.Builder(SearchActivity.this)
						.setTitle("陌生人")
						.setItems(new String[] { "打招呼", "加为好友" },
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 打招呼
										if (0 == which) {

										}
										// 发送好友请求
										else {
											String user = ((SearchAdap.ViewHolder) cView
													.getTag()).fName.getText()
													.toString();
											addFriend(user);
										}
									}
								}).show();
				return false;
			}
		});
	}

	private void getVCard(final String user) {
		new Thread() {
			public void run() {
				VCard card = CommonMethod.getVCard(user);
				DebugLog.d(card.getFirstName() + card.getNickName());
			};
		}.start();
	}

	private void addFriend(final String user) {
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean result = false;
				try {
					result = CommonMethod.addFriend(user);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					msg.what = 0;
					if (result) {
						msg.obj = "成功";
					} else {
						msg.obj = "失败";
					}
					insHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	SearchHandler insHandler = new SearchHandler(this);

	static class SearchHandler extends Handler {
		WeakReference<SearchActivity> mActivity;

		SearchHandler(SearchActivity activity) {
			mActivity = new WeakReference<SearchActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			SearchActivity theActivity = mActivity.get();
			switch (msg.what) {
			case 0:
				theActivity.showToast((String) msg.obj);
				break;
			case 1:
				break;
			}
		}
	}
}
