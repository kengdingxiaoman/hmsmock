package cn.hydralisk.hmsmock.service.dto.request;

import cn.hydralisk.hmsmock.constants.enums.KeyLength;

/**
 * 
 * @author master.yang
 * @version $Id: ConvertToUnderAnotherKeyCommandRequest.java, v 0.1 2014-10-11 下午1:54:25 master.yang Exp $
 */
public class ConvertToUnderAnotherKeyCommandRequest extends CommandRequest {

    private KeyLength originalKeyLength;
    private KeyLength newKeyLength;
    private byte[] zmkIndex;

    //getter and setter
    public KeyLength getOriginalKeyLength() {
        return originalKeyLength;
    }

    public void setOriginalKeyLength(KeyLength originalKeyLength) {
        this.originalKeyLength = originalKeyLength;
    }

    public KeyLength getNewKeyLength() {
        return newKeyLength;
    }

    public void setNewKeyLength(KeyLength newKeyLength) {
        this.newKeyLength = newKeyLength;
    }

    public byte[] getZmkIndex() {
        return zmkIndex;
    }

    public void setZmkIndex(byte[] zmkIndex) {
        this.zmkIndex = zmkIndex;
    }
}
