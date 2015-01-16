package net.fengg.asmack.demo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class CommonMethod {

	static MyConnectionListener connectionListener;

	public static boolean login(String account, String password) {
		try {
			SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			ConnectionTool.getConnection().login(account, password);
			// connectionListener = new MyConnectionListener();
			// ConnectionTool.getConnection().addConnectionListener(connectionListener);
			// Presence presence = new Presence(Presence.Type.available);
			// ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean logout() {
		try {
			updateTypeToUnAvailable();
			// ConnectionTool.getConnection().removeConnectionListener(connectionListener);
			ConnectionTool.closeConnection();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 注册用户
	 * 
	 * @param regUserName
	 * @param regUserPwd
	 * @return
	 */
	public static boolean createAccount(String regUserName, String regUserPwd) {
		try {
			ConnectionTool.getConnection().getAccountManager()
					.createAccount(regUserName, regUserPwd);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除当前用户
	 * 
	 * @return
	 */
	public static boolean deleteAccount() {
		try {
			ConnectionTool.getConnection().getAccountManager().deleteAccount();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除修改密码
	 * 
	 * @param connection
	 * @return
	 */
	public static boolean changePassword(String pwd) {
		try {
			ConnectionTool.getConnection().getAccountManager()
					.changePassword(pwd);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param mode
	 *   Free to chat. chat,
	 * 
	 *   Available (the default). available,
	 * 
	 *   Away. away,
	 * 
	 *   Away for an extended period of time. xa,
	 * 
	 *   Do not disturb. dnd
	 * 
	 * @return
	 */
	public static boolean updateMode(Mode mode) {
		try {
			Presence presence = new Presence(Presence.Type.available);
			presence.setMode(mode);
			ConnectionTool.getConnection().sendPacket(presence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param type
	 *   The user is available to receive messages (default). available,
	 * 
	 *   The user is unavailable to receive messages. unavailable,
	 * 
	 *   Request subscription to recipient's presence. subscribe,
	 * 
	 *   Grant subscription to sender's presence. subscribed,
	 * 
	 *   Request removal of subscription to sender's presence. unsubscribe,
	 * 
	 *   Grant removal of subscription to sender's presence. unsubscribed,
	 * 
	 *   The presence packet contains an error message. error
	 * 
	 * @return
	 */
	public static boolean updateType(Type type) {
		try {
			Presence presence = new Presence(type);
			ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateTypeToAvailable() {
		try {
			Presence presence = new Presence(Presence.Type.available);
			ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean updateTypeToUnAvailable() {
		try {
			Presence presence = new Presence(Presence.Type.unavailable);
			ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean updateTypeToUnAvailableToSomeone(String userName) {
		try {
			Presence presence = new Presence(Presence.Type.unavailable);
			presence.setTo(userName);
			ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean updateTypeToAvailableToSomeone(String userName) {
		try {
			Presence presence = new Presence(Presence.Type.available);
			presence.setTo(userName);
			ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改心情
	 * 
	 * @param status
	 */
	public static boolean changeStatus(String status) {
		try {
			Presence presence = new Presence(Presence.Type.available);
			presence.setStatus(status);
			ConnectionTool.getConnection().sendPacket(presence);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 更改用户状态
	 */
	public static void setPresence(int code) {
		Presence presence;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available);
			ConnectionTool.getConnection().sendPacket(presence);
			Log.v("state", "设置在线");
			break;
		case 1:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.chat);
			ConnectionTool.getConnection().sendPacket(presence);
			Log.v("state", "设置Q我吧");
			System.out.println(presence.toXML());
			break;
		case 2:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.dnd);
			ConnectionTool.getConnection().sendPacket(presence);
			Log.v("state", "设置忙碌");
			System.out.println(presence.toXML());
			break;
		case 3:
			presence = new Presence(Presence.Type.available);
			presence.setMode(Presence.Mode.away);
			ConnectionTool.getConnection().sendPacket(presence);
			Log.v("state", "设置离开");
			System.out.println(presence.toXML());
			break;
		case 4:
			Roster roster = ConnectionTool.getConnection().getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(ConnectionTool.getConnection().getUser());
				presence.setTo(entry.getUser());
				ConnectionTool.getConnection().sendPacket(presence);
				System.out.println(presence.toXML());
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(ConnectionTool.getConnection().getUser());
			presence.setTo(StringUtils.parseBareAddress(ConnectionTool
					.getConnection().getUser()));
			ConnectionTool.getConnection().sendPacket(presence);
			Log.v("state", "设置隐身");
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable);
			ConnectionTool.getConnection().sendPacket(presence);
			Log.v("state", "设置离线");
			break;
		default:
			break;
		}
	}

	public static List<RosterEntry> searchAllFriends() {
		List<RosterEntry> entries = new ArrayList<RosterEntry>();
		Collection<RosterEntry> roscol = ConnectionTool.getConnection()
				.getRoster().getEntries();
		Iterator<RosterEntry> iter = roscol.iterator();

		while (iter.hasNext()) {
			entries.add(iter.next());
		}
		return entries;
	}

	public static List<RosterGroup> searchAllGroup() {
		List<RosterGroup> groups = new ArrayList<RosterGroup>();
		Collection<RosterGroup> roscol = ConnectionTool.getConnection()
				.getRoster().getGroups();
		Iterator<RosterGroup> iter = roscol.iterator();
		while (iter.hasNext()) {
			groups.add(iter.next());
		}
		return groups;
	}

	public static List<RosterEntry> getGroupFriends(String group) {
		List<RosterEntry> entries = new ArrayList<RosterEntry>();
		RosterGroup rosgrou = ConnectionTool.getConnection().getRoster()
				.getGroup(group);
		Collection<RosterEntry> roscol = rosgrou.getEntries();
		Iterator<RosterEntry> iter = roscol.iterator();
		while (iter.hasNext()) {
			entries.add(iter.next());
		}
		return entries;
	}

	public static boolean addNewGroup(String group) {
		try {
			ConnectionTool.getConnection().getRoster().createGroup(group);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean addGroupFriend(String friend, String group) {
		try {
			Roster roster = ConnectionTool.getConnection().getRoster();
			roster.createEntry(friend.contains("@")? friend : getUserId(friend), friend,
					null == group? null : new String[] { group });
			Presence subscribed = new Presence(Presence.Type.subscribed);
			subscribed.setTo(getUserId(friend));
			ConnectionTool.getConnection().sendPacket(subscribed);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean addFriend(String friend) {
		return addGroupFriend(friend, null);
	}

	public static boolean removeUser(String userName) {
		try {
			Roster roster = ConnectionTool.getConnection().getRoster();
			if (userName.contains("@")) {
				userName = userName.split("@")[0];
			}
			RosterEntry entry = roster.getEntry(userName);
			System.out.println("删除好友:" + userName);
			System.out.println("User: " + (roster.getEntry(userName) == null));
			roster.removeEntry(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public static ArrayList<String> searchUsers(String user) {
		ArrayList<String> users = new ArrayList<String>();
		UserSearchManager usm = new UserSearchManager(
				ConnectionTool.getConnection());
		Form searchForm = null;
		try {
			searchForm = usm.getSearchForm("search."
					+ ConnectionTool.getConnection().getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", user);
			ReportedData data = usm.getSearchResults(answerForm, "search."
					+ ConnectionTool.getConnection().getServiceName());
			// column:jid,Username,Name,Email
			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				// row.getValues("Username").next().toString());
				// Log.d("Name", row.getValues("Name").next().toString());
				// Log.d("Email", row.getValues("Email").next().toString());
				users.add(row.getValues("jid").next().toString());
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return users;
	}

	public static List<Message> getOffLine() {
		List<Message> msglist = new ArrayList<Message>();
		try {
			OfflineMessageManager offlineMessageManager = new OfflineMessageManager(
					ConnectionTool.getConnection());
			Iterator<Message> it = offlineMessageManager.getMessages();
			while (it.hasNext()) {
				Message message = it.next();
				msglist.add(message);
			}
			updateTypeToAvailable();
			offlineMessageManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return msglist;
	}

	/**
	 * VCard
	 */
	public static VCard getVCard(String user) {
		VCard card = new VCard();
		try {
			card.load(ConnectionTool.getConnection(), user);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return card;
	}

	public static byte[] getAvatar(String user) {
		VCard card = new VCard();
		try {
			card.load(ConnectionTool.getConnection(), user);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return card.getAvatar();
	}

	/**
	 * 修改用户头像
	 * 
	 * @param connection
	 * @param f
	 * @throws XMPPException
	 * @throws IOException
	 */
	public static void changeAvatar(File f) {
		try {
			VCard vcard = new VCard();
			vcard.load(ConnectionTool.getConnection());

			byte[] bytes;

			bytes = Common.getBytesFromFile(f);
			vcard.setAvatar(bytes);

			vcard.save(ConnectionTool.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendBroadcastRoster(Context context, String txt) {
		Intent intent = new Intent();
		intent.setAction("roster_receiver");
		intent.putExtra("msg", txt);
		context.sendBroadcast(intent);
	}

	private static void sendBroadcastMsg(Context context, String from,
			String txt) {
		Intent intent = new Intent();
		intent.setAction("msg_receiver");
		intent.putExtra("from", from);
		intent.putExtra("msg", txt);
		context.sendBroadcast(intent);
	}

	private static void sendBroadcastFile(Context context, String filepath,
			double progress) {
		Intent intent = new Intent();
		intent.setAction("file_receiver");
		intent.putExtra("path", filepath);
		intent.putExtra("progress", progress);
		context.sendBroadcast(intent);
	}

	public static void getOnLine(final Context context) {
		ChatManager cm = ConnectionTool.getConnection().getChatManager();
		cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) {
				chat.addMessageListener(new MessageListener() {
					@Override
					public void processMessage(Chat chat2, Message message) {
						Log.d("connectMethod:getOnLine()", message.getBody()
								+ message.getFrom());
						sendBroadcastMsg(context, message.getFrom(),
								message.getBody());
					}
				});
			}
		});
	}

	public static void getRoster(final Context context) {
		ConnectionTool.getConnection().getRoster()
				.addRosterListener(new RosterListener() {
					@Override
					public void entriesAdded(Collection<String> arg0) {
						System.out.println("--------EE:" + "entriesAdded");
					}

					@Override
					public void entriesDeleted(Collection<String> arg0) {
						System.out.println("--------EE:" + "entriesDeleted");
					}

					@Override
					public void entriesUpdated(Collection<String> arg0) {
						System.out.println("--------EE:" + "entriesUpdated");
					}

					@Override
					public void presenceChanged(Presence presence) {
						System.out.println("--------EE:" + "presenceChanged");
						sendBroadcastRoster(context, presence.getFrom()
								+ presence.getType() + presence.getStatus());
					}

				});
	}

	public static void getOnLineFile(Context context) {
		FileTransferManager fileTransferManagernew = new FileTransferManager(
				ConnectionTool.getConnection());
		FileTransferListener filter = new ChatFileTransferListener(context);
		fileTransferManagernew.addFileTransferListener(filter);
	}

	static class ChatFileTransferListener implements FileTransferListener {
		Context context;

		public ChatFileTransferListener(Context context) {
			this.context = context;
		}

		@Override
		public void fileTransferRequest(FileTransferRequest request) {
			try {
				final File insFile = new File(
						Environment.getExternalStorageDirectory() + "/"
								+ request.getFileName());
				final IncomingFileTransfer infiletransfer = request.accept();
				infiletransfer.recieveFile(insFile);

				new Thread() {
					@Override
					public void run() {
						long startTime = System.currentTimeMillis();
						while (!infiletransfer.isDone()) {
							double progress = infiletransfer.getProgress();
							progress *= 100;
							System.out.println("status="
									+ infiletransfer.getStatus());
							System.out.println(progress);
							sendBroadcastFile(context,
									insFile.getAbsolutePath(), progress);
						}
						System.out
								.println("used "
										+ ((System.currentTimeMillis() - startTime) / 1000)
										+ " seconds  ");
					}
				}.start();
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendTalkMsg(String to, String msg) {
		Chat chat = ConnectionTool.getConnection().getChatManager()
				.createChat(to, null/*
									 * new MessageListener() { public void
									 * processMessage(Chat chat, Message
									 * message) {
									 * 
									 * if (message != null && message.getBody()
									 * != null) { System.out.println("收到消息:" +
									 * message.getBody()); //
									 * 可以在这进行针对这个用户消息的处理，但是这里我没做操作，看后边聊天窗口的控制 }
									 * 
									 * } }
									 */);
		try {
			chat.sendMessage(msg);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	public static void sendTalkFile(String to, String filepath) {
		FileTransferManager fileTransferManager = new FileTransferManager(
				ConnectionTool.getConnection());
		OutgoingFileTransfer outgoingFileTransfer = fileTransferManager
				.createOutgoingFileTransfer(to + "/Spark 2.6.3");
		File insfile = new File(filepath);
		try {
			outgoingFileTransfer.sendFile(insfile, "descr");

			// new Thread(){
			// @Override
			// public void run(){
			// long startTime = System.currentTimeMillis();
			// while(!outgoingFileTransfer.isDone()){
			// double progress = outgoingFileTransfer.getProgress();
			// progress*=100;
			// System.out.println("status="+outgoingFileTransfer.getStatus());
			// System.out.println(progress);
			// sendBroadcastFile(context, filepath, progress);
			// }
			// System.out.println("used "+((System.currentTimeMillis()-startTime)/1000)+" seconds  ");
			// }
			// }.start();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建房间
	 * 
	 * @param roomName
	 *            房间名称
	 */
	public static void createRoom(String roomName) {
		try {
			// 创建一个MultiUserChat
			MultiUserChat muc = new MultiUserChat(
					ConnectionTool.getConnection(), roomName + "@conference."
							+ ConnectionTool.getConnection().getServiceName());
			// 创建聊天室
			muc.create(roomName); // roomName房间的名字
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单。
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(ConnectionTool.getConnection().getUser());// 用户JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", false);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", false);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			// 进入是否需要密码
			// submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
			// true);
			// 设置进入密码
			// submitForm.setAnswer("muc#roomconfig_roomsecret", "password");
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// 允许用户注册房间
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加入会议室
	 * 
	 * @param user
	 *            昵称
	 * @param password
	 *            会议室密码
	 * @param roomsName
	 *            会议室名
	 * @param connection
	 */
	public static MultiUserChat joinMultiUserChat(String user, String password,
			String roomsName) {
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			MultiUserChat muc = new MultiUserChat(
					ConnectionTool.getConnection(), roomsName + "@conference."
							+ ConnectionTool.getConnection().getServiceName());
			// 聊天室服务将会决定要接受的历史记录数量
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxStanzas(0);
			// history.setSince(new Date());
			// 用户加入聊天室
			muc.join(user, password, history,
					SmackConfiguration.getPacketReplyTimeout());
			System.out.println("会议室加入成功........");
			return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("会议室加入失败........");
			return null;
		}
	}

	/**
	 * 查询会议室成员名字
	 * 
	 * @param muc
	 */
	public static List<String> findMulitUser(MultiUserChat muc) {
		List<String> listUser = new ArrayList<String>();
		Iterator<String> it = muc.getOccupants();
		// 遍历出聊天室人员名称
		while (it.hasNext()) {
			// 聊天室成员名字
			String name = StringUtils.parseResource(it.next());
			listUser.add(name);
		}
		return listUser;
	}

	/**
	 * 获取服务器上所有会议室
	 * 
	 * @return
	 * @throws XMPPException
	 */
	public static List<HostedRoom> getConferenceRoom() throws XMPPException {
		List<HostedRoom> list = new ArrayList<HostedRoom>();
		ServiceDiscoveryManager.getInstanceFor(ConnectionTool.getConnection());
		Collection<HostedRoom> hostrooms = MultiUserChat.getHostedRooms(
				ConnectionTool.getConnection(), ConnectionTool.getConnection()
						.getServiceName());
		for (HostedRoom k : hostrooms) {
			list.add(k);
			// for (HostedRoom j :
			// MultiUserChat.getHostedRooms(ConnectionTool.getConnection(),
			// k.getJid())) {
			// RoomInfo info2 =
			// MultiUserChat.getRoomInfo(ConnectionTool.getConnection(),
			// j.getJid());
			// if (j.getJid().indexOf("@") > 0) {

			// FriendRooms friendrooms = new FriendRooms();
			// friendrooms.setName(j.getName());//聊天室的名称
			// friendrooms.setJid(j.getJid());//聊天室JID
			// friendrooms.setOccupants(info2.getOccupantsCount());//聊天室中占有者数量
			// friendrooms.setDescription(info2.getDescription());//聊天室的描述
			// friendrooms.setSubject(info2.getSubject());//聊天室的主题
			// list.add(friendrooms);
			// }
			// }
		}
		return list;
	}

	/**
	 * 會議室信息監聽事件
	 * 
	 * @author Administrator
	 * 
	 */
	public class MultiListener implements PacketListener {
		@Override
		public void processPacket(Packet packet) {
			Message message = (Message) packet;
			// // 接收来自聊天室的聊天信息
			// String time = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			// MucHistory mh = new MucHistory();
			// mh.setUserAccount(account);
			// String from = StringUtils.parseResource(message.getFrom());
			// String fromRoomName = StringUtils.parseName(message.getFrom());
			// mh.setMhRoomName(fromRoomName);
			// mh.setFriendAccount(from);
			// mh.setMhInfo(message.getBody());
			// mh.setMhTime(time);
			// mh.setMhType("left");

		}
	}

	/**
	 * 会议室状态监听事件
	 * 
	 * @author Administrator
	 * 
	 */
	class ParticipantStatus implements ParticipantStatusListener {

		@Override
		public void adminGranted(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void adminRevoked(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void banned(String arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void joined(String participant) {
			System.out.println(StringUtils.parseResource(participant)
					+ " has joined the room.");
		}

		@Override
		public void kicked(String arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void left(String participant) {
			// TODO Auto-generated method stub
			System.out.println(StringUtils.parseResource(participant)
					+ " has left the room.");

		}

		@Override
		public void membershipGranted(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void membershipRevoked(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void moderatorGranted(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void moderatorRevoked(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void nicknameChanged(String participant, String newNickname) {
			System.out.println(StringUtils.parseResource(participant)
					+ " is now known as " + newNickname + ".");
		}

		@Override
		public void ownershipGranted(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void ownershipRevoked(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void voiceGranted(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void voiceRevoked(String arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 判断OpenFire用户的状态 strUrl : url格式 -
	 * http://my.openfire.com:9090/plugins/presence
	 * /status?jid=user1@SERVER_NAME&type=xml 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 -
	 * 用户离线 说明 ：必须要求 OpenFire加载 presence 插件，同时设置任何人都可以访问
	 */
	public int IsUserOnLine(String user) {
		String url = "http://" + ConnectionTool.getIp()
				+ ":9090/plugins/presence/status?" + "jid=" + user + "@"
				+ ConnectionTool.getIp() + "&type=xml";
		int shOnLineState = 0; // 不存在
		try {
			URL oUrl = new URL(url);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader oIn = new BufferedReader(new InputStreamReader(
						oConn.getInputStream()));
				if (null != oIn) {
					String strFlag = oIn.readLine();
					oIn.close();
					System.out.println("strFlag" + strFlag);
					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						shOnLineState = 2;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						shOnLineState = 0;
					} else if (strFlag.indexOf("priority") >= 0
							|| strFlag.indexOf("id=\"") >= 0) {
						shOnLineState = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shOnLineState;
	}
	
	static String getUserId(String name) {
		return name + "@" + ConnectionTool.getIp();
	}
}