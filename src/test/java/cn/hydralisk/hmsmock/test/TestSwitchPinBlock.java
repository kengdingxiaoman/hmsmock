package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.service.CommandSwitchPinBlock;
import cn.hydralisk.hmsmock.util.CommonUtils;

public class TestSwitchPinBlock {

	public static void main(String[] args) {
		byte[] requestData = new byte[] { (byte) 0xd1, (byte) 0x24,
				(byte) 0x10, (byte) 0x8d, (byte) 0x1d, (byte) 0xbf,
				(byte) 0x1e, (byte) 0x9a, (byte) 0x70, (byte) 0x50,
				(byte) 0xed, (byte) 0x3b, (byte) 0xfa, (byte) 0xaa,
				(byte) 0x3b, (byte) 0xcd, (byte) 0xcc, (byte) 0x60,
				(byte) 0xba, (byte) 0x10, (byte) 0xeb, (byte) 0x0a,
				(byte) 0x8b, (byte) 0x76, (byte) 0x4c, (byte) 0x0b,
				(byte) 0x89, (byte) 0x5a, (byte) 0xe0, (byte) 0xe6,
				(byte) 0x90, (byte) 0x7a, (byte) 0x8d, (byte) 0xf4,
				(byte) 0xa7, (byte) 0xfa, (byte) 0x01, (byte) 0x01,
				(byte) 0xa4, (byte) 0x2b, (byte) 0x90, (byte) 0x97,
				(byte) 0xe5, (byte) 0xfd, (byte) 0x3f, (byte) 0x4e,
				(byte) 0x06, (byte) 0x22, (byte) 0x84, (byte) 0x80,
				(byte) 0x03, (byte) 0x80, (byte) 0x49, (byte) 0x79,
				(byte) 0x65, (byte) 0x76, (byte) 0x3b, (byte) 0x06,
				(byte) 0x22, (byte) 0x84, (byte) 0x80, (byte) 0x03,
				(byte) 0x80, (byte) 0x49, (byte) 0x79, (byte) 0x65,
				(byte) 0x76, (byte) 0x3b };

		System.out.println(CommonUtils.byte2hexHexSytle(requestData));
		
		CommandSwitchPinBlock commandSwitchPinBlock = new CommandSwitchPinBlock();
		byte[] responseData = commandSwitchPinBlock.execute(requestData);
		System.out.println(CommonUtils.byte2hexHexSytle(responseData));
	}
}
