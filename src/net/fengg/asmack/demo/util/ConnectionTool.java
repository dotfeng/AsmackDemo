package net.fengg.asmack.demo.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

/**
 * asmack加载的静态方法
 * 
 */
public class ConnectionTool {
	private static ConnectionConfiguration connConfig;
	private static XMPPConnection con;

	private static final String IP = "192.168.1.16";
	private static final int PORT = 5222;
	
	// 静态加载ReconnectionManager ,重连后正常工作
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openConnection() {
		try {
			connConfig = new ConnectionConfiguration(IP, PORT);

			// 设置登录状态为离线
			connConfig.setSendPresence(false);
//			connConfig.setSASLAuthenticationEnabled(false);
			connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); 
			// 断网重连
			connConfig.setReconnectionAllowed(true);
			con = new XMPPConnection(connConfig);
			con.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static XMPPConnection getConnection() {
		if (con == null || !con.isConnected()) {
			openConnection();
		}
		return con;
	}

	public static void closeConnection() {
		con.disconnect();
		con = null;
	}
	
	public static String getIp() {
		return IP;
	}
	
	public static int getPort() {
		return PORT;
	}
}
