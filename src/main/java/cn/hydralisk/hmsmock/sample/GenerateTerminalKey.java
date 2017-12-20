package cn.hydralisk.hmsmock.sample;

import cn.hydralisk.hmsmock.service.CommandGenerateKey;
import cn.hydralisk.hmsmock.util.CommonUtils;

/**
 * 生成TMK，也就是终端主密钥
 * created by yangyebo 2017-12-20 上午8:38
 */
public class GenerateTerminalKey{

    public static void main(String[] args) {
        //String requestData = "D107 16 01 00 41 16";

        String requestData = "D1071001000b10";

        CommandGenerateKey commandGenerateKey = new CommandGenerateKey();
        byte[] requestResult = commandGenerateKey.execute(CommonUtils.hex2byte(requestData));

        System.out.println(CommonUtils.byte2hex(requestResult));
    }
}
