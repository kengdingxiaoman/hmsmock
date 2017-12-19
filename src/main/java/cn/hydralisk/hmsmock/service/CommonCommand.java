package cn.hydralisk.hmsmock.service;

public abstract class CommonCommand implements Command {

    public abstract byte[] execute(byte[] requestData);

    public void print(byte b) {
        //System.out.println(CommonUtils.byte2hexSpacingWithBlank(new byte[] { b }));
    }

    public void print(byte[] b) {
        //System.out.println(CommonUtils.byte2hexSpacingWithBlank(b));
    }
}
