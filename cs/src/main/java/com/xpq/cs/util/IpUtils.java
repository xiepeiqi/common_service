package com.xpq.cs.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * @date 2018年8月9日 @time 下午4:20:06
 * @author xiepeiqi
 */
public class IpUtils {

	/**
	 * 获取服务器本地的ip地址
	 * @return
	 * @date 2018年8月9日 @time 下午4:20:03
	 * @author xiepeiqi
	 */

	public static String getLocalIp() {
		InetAddress ip = null;
		String localIp = "127.0.0.1";
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				ip = (InetAddress) ni.getInetAddresses().nextElement();
				if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					localIp = ip.getHostAddress();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localIp;
	}

	/**
	 * 获取客户机的ip地址
	 * @param request
	 * @return
	 * @date 2018年8月9日 @time 下午4:19:58
	 * @author xiepeiqi
	 */
	public static String getClientIp(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;

	}

	public static long transIP2Int(String ip) {

		String[] temp = ip.split("\\.");
		int[] ipInt = new int[temp.length];

		for (int i = 0; i < temp.length; i++) {
			ipInt[i] = new Integer(temp[i]).intValue();
		}

		long ipNum = (long) ipInt[0] * 256 * 256 * 256 + ipInt[1] * 256 * 256 + ipInt[2] * 256 + ipInt[3];

		return ipNum;
	}

}
