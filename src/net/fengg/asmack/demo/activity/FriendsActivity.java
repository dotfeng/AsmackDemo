package net.fengg.asmack.demo.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import net.fengg.asmack.demo.adapter.FriendsAdap;
import net.fengg.asmack.demo.service.ConnectionService;
import net.fengg.asmack.demo.util.CommonMethod;
import net.fengg.asmack.demo.util.DemoApplication;

import org.jivesoftware.smack.RosterEntry;

import net.fengg.asmack.demo.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FriendsActivity extends BaseActivity implements OnClickListener {
	private List<RosterEntry> rosterEntries = new ArrayList<RosterEntry>();
	private FriendsAdap adap;

	// 广播接受者
	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if ("msg_receiver".equals(intent.getAction())) {
				if (null != intent.getExtras()) {
					showToast(intent.getExtras().getString("from") + "\n" +
							intent.getExtras().getString("msg"));
				}
			} else if ("file_receiver".equals(intent.getAction())) {
				if (null != intent.getExtras()) {
					((TextView) findViewById(R.id.tv_progress)).setText(intent
							.getExtras().getString("path") + "   " + intent
							.getExtras().getDouble("progress"));
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
		setContentView(R.layout.activity_friends);
		initView();
	}
	
	private void regisBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("msg_receiver");
		filter.addAction("file_receiver");
		filter.addAction("roster_receiver");
		this.registerReceiver(this.receiver, filter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		regisBroadcast();
		getFriends();
	}
	/**
	 * 在退出的时候注销
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		logout();
		//注销需停止service
		Intent intent = new Intent(this, ConnectionService.class);
		stopService(intent);
	}

	private void initView() {
		getOffLine();
		// 开启服务 监听服务器
		Intent intent = new Intent(this, ConnectionService.class);
		startService(intent);

		findViewById(R.id.btn_search).setOnClickListener(this);
		findViewById(R.id.btn_newgroup).setOnClickListener(this);
		findViewById(R.id.btn_add_friend).setOnClickListener(this);
		
		ListView frilistview = (ListView) findViewById(R.id.lv_friends);
		adap = new FriendsAdap(this, rosterEntries);
		frilistview.setAdapter(adap);
		frilistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View conView,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				DemoApplication.FromName = ((FriendsAdap.ViewHolder) conView
						.getTag()).fName.getText().toString();
				openActivity(TalkActivity.class);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search:
			searchUser();
			break;
		case R.id.btn_newgroup:
			createNewGroup();
			break;
		case R.id.btn_add_friend:
			addFriend();
			break;
		default:
			break;
		}
	}

	/**
	 * 搜索用户
	 */
	private void searchUser() {
		new Thread() {
			public void run() {
				String usr = ((EditText) findViewById(R.id.et_searchname))
						.getText().toString();
				ArrayList<String> users = CommonMethod.searchUsers(usr);
				if (users.isEmpty()) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = "没有用户";
					insHandler.sendMessage(msg);
					return;
				}

				Intent intent = new Intent(FriendsActivity.this,
						SearchActivity.class);
				intent.putStringArrayListExtra("users", users);
				startActivity(intent);
			};
		}.start();
	}

	private void logout() {
		new Thread() {
			public void run() {
				CommonMethod.logout();
			};
		}.start();
	}
	
	/**
	 * 获取所有好友的信息
	 */
	private void getFriends() {
		new Thread() {
			public void run() {
				rosterEntries.clear();
//				 获取所有好友，不分组
				 for (RosterEntry enty : CommonMethod.searchAllFriends()) {
					 rosterEntries.add(enty);
				 }

				// 获取所有好友，分组
//				for (RosterGroup group : CommonMethod.searchAllGroup()) {
//					for (RosterEntry enty : CommonMethod.getGroupFriends(group
//							.getName())) {
//						enty.setName(group.getName());
//						rosterEntries.add(enty);
//					}
//				}
				insHandler.sendEmptyMessage(1);
			};
		}.start();
	}

	private void createNewGroup() {
		new Thread() {
			public void run() {
				String group = ((EditText) findViewById(R.id.et_groupname))
						.getText().toString();
				Message msg = new Message();
				boolean result = false;
				try {
					result = CommonMethod.addNewGroup(group);
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

	private void addFriend() {
		new Thread() {
			public void run() {
				String friend = ((EditText) findViewById(R.id.et_friend_name))
						.getText().toString();
				String group = ((EditText) findViewById(R.id.et_group_name))
						.getText().toString();

				Message msg = new Message();
				boolean result = false;
				try {
					result = CommonMethod.addGroupFriend(friend, group);
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

	/**
	 * 离线消息
	 */
	private void getOffLine() {
		new Thread() {
			public void run() {
				List<org.jivesoftware.smack.packet.Message> msglist = CommonMethod
						.getOffLine();
				String m = "";
				for (org.jivesoftware.smack.packet.Message msg : msglist) {
					m += msg.getTo() + msg.getBody() + msg.getFrom();
				}
				if ("".equals(m)) {
					m = "没有离线消息";
				}
				Message msg = new Message();
				msg.what = 0;
				msg.obj = m;
				insHandler.sendMessage(msg);
			};
		}.start();
	}

	FriendsnHandler insHandler = new FriendsnHandler(this);

	static class FriendsnHandler extends Handler {
		WeakReference<FriendsActivity> mActivity;

		FriendsnHandler(FriendsActivity activity) {
			mActivity = new WeakReference<FriendsActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			FriendsActivity theActivity = mActivity.get();
			switch (msg.what) {
			case 0:
				theActivity.showToast((String) msg.obj);
				break;
			case 1:
				theActivity.adap.notifyDataSetChanged();
				break;
			}
		}
	};
}
