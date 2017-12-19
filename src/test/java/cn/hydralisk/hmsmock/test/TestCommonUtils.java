package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonUtils;

public class TestCommonUtils {

	public static void main(String[] args) {
		String randomHex = CommonUtils.genRandomHexStr(32);

		System.out.println(randomHex);

		System.out.println(Integer.valueOf("0634", 16).toString());
	}
}
