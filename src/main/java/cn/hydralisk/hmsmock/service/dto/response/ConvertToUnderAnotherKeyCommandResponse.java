package cn.hydralisk.hmsmock.service.dto.response;

import cn.hydralisk.hmsmock.util.ByteArrayBuffer;

/**
 * 
 * @author master.yang
 * @version $Id: ConvertToUnderAnotherKeyCommandResponse.java, v 0.1 2014-10-11 上午11:43:14 master.yang Exp $
 */
public class ConvertToUnderAnotherKeyCommandResponse extends CommandResponse {

    private byte newKeyLength;
    private byte[] newKey;
    private byte[] kcv;

    /** 
     * @see cn.hydralisk.hmsmock.service.dto.response.CommandResponse#toByteArray()
     */
    @Override
    public byte[] toByteArray() {
        ByteArrayBuffer responseData = new ByteArrayBuffer();
        responseData.append(newKeyLength);
        responseData.append(newKey);
        responseData.append(kcv);
        return responseData.toByteArray();
    }

    //getter and setter
    public byte getNewKeyLength() {
        return newKeyLength;
    }

    public void setNewKeyLength(byte newKeyLength) {
        this.newKeyLength = newKeyLength;
    }

    public byte[] getNewKey() {
        return newKey;
    }

    public void setNewKey(byte[] newKey) {
        this.newKey = newKey;
    }

    public byte[] getKcv() {
        return kcv;
    }

    public void setKcv(byte[] kcv) {
        this.kcv = kcv;
    }
}
