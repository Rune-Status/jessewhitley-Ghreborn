package Ghreborn.net;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;

import Ghreborn.Config;
import Ghreborn.Connection;

public class HostList {
	
	private static HostList list = new HostList();
	
	public static HostList getHostList() {
		return list;
	}
	
	private Map<String, Integer> connections = new HashMap<String, Integer>();
	
	public synchronized boolean add(Channel session) {
		String addr = ((InetSocketAddress) session.getRemoteAddress())
				.getAddress().getHostAddress();
		Integer amt = connections.get(addr);
		if (amt == null) {
			amt = 1;
		} else {
			amt += 1;
		}
		if (amt > Config.IPS_ALLOWED || Connection.isIpBanned(addr)) {
			return false;
		} else {
			connections.put(addr, amt);
			return true;
		}
	}

	public void remove(Channel session) {
		String addr = ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress();
		Integer amt = connections.get(addr);
		if(amt == null)
			return;
		amt -= 1;
		if(amt <= 0)
			connections.remove(addr);
		else
			connections.put(addr, amt);
	}

}
