package com.cmnt.dbpick.common.constant;

import com.cmnt.dbpick.common.utils.DateUtil;

import java.security.MessageDigest;
import java.util.Random;

public class FileConstants {


	private static final Random RANDOM = new Random();

	/**
	 * obs bucket config
	 */

	public static final String OBS_BUCKET_CMNT_LIVE = "cmnt-live";
	public static final String OBS_BUCKET_CMNT_LIVE_TEST = "cmnt-live-test2";

	/**
	 * obs file puth  config
	 */
	public static final String OBS_FILE_LIVE_COVER = "live_cover";

	/**
	 * md5算法
	 * @param dataStr
	 * @return
	 */
	public static String md5Encrypt(String dataStr) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(dataStr.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 随机生成一份随机数，避免上传的图片重复
	 */

	public static String generatorFileName() {
		String s = new StringBuilder().append(DateUtil.getTimeStrampSeconds()).append(RANDOM.nextInt()).toString();
		return md5Encrypt(s);
	}

}
