package cn.hydralisk.hmsmock.service;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.constants.enums.KeyLength;
import cn.hydralisk.hmsmock.constants.enums.ZoneMasterKey;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.service.dto.request.ConvertToUnderAnotherKeyCommandRequest;
import cn.hydralisk.hmsmock.service.dto.response.ConvertToUnderAnotherKeyCommandResponse;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.ThreeDesUtils;

/**
 * 将一个密钥加密的key转换为另一个密钥加密的结果
 * @author master.yang
 * @version $Id: CommandConvertToUnderDifferentKey.java, v 0.1 2014-10-11 上午10:33:54 master.yang Exp $
 */
public class CommandConvertToUnderAnotherKey extends CommonCommand {

    private static final int ORIGINAL_KEY_LENGTH_INDEX = 2;

    private static final int NEW_KEY_LENGTH_INDEX = 3;

    private static final int ZMK_INDEX_BEGIN_INDEX = 4;

    private static final int ZMK_INDEX_END_INDEX = 5;

    private static final int KEY_TYPE_INDEX = 6;

    /** 
     * @see cn.hydralisk.hmsmock.service.CommonCommand#execute(byte[])
     */
    @Override
    public byte[] execute(byte[] requestData) {
        ByteArrayBuffer command = new ByteArrayBuffer();
        command.append(requestData);

        KeyLength originalKeyLength = extractOriginalKeyLength(command);
        KeyLength newKeyLength = extractNewKeyLength(command);

        byte[] zmkIndex = extractZmkIndex(command);

        ConvertToUnderAnotherKeyCommandRequest commandRequest = new ConvertToUnderAnotherKeyCommandRequest();
        commandRequest.setOriginalKeyLength(originalKeyLength);
        commandRequest.setNewKeyLength(newKeyLength);
        commandRequest.setZmkIndex(zmkIndex);

        ConvertToUnderAnotherKeyCommandResponse commandResponse = new ConvertToUnderAnotherKeyCommandResponse();
        if (CommonUtils.isEquals(zmkIndex, HmsConstants.NOT_USE_ZMK_MARK)) {
            commandResponse = convertWorkKeyUnderLmk(command, commandRequest);
        } else {
            commandResponse = convertTmkeyUnderLmk(command, commandRequest);
        }
        return commandResponse.toByteArray();
    }

    private KeyLength extractOriginalKeyLength(ByteArrayBuffer command) {
        if (ORIGINAL_KEY_LENGTH_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_LENGTH);
        }
        byte originalKeyLength = command.subArray(ORIGINAL_KEY_LENGTH_INDEX);
        if (!KeyLength.isSupportKeyLength(originalKeyLength)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_KEY_LENGTH);
        }
        return KeyLength.getMatchedKeyLength(originalKeyLength);
    }

    private KeyLength extractNewKeyLength(ByteArrayBuffer command) {
        if (NEW_KEY_LENGTH_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_LENGTH);
        }
        byte newKeyLength = command.subArray(NEW_KEY_LENGTH_INDEX);
        if (!KeyLength.isSupportKeyLength(newKeyLength)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_KEY_LENGTH);
        }
        return KeyLength.getMatchedKeyLength(newKeyLength);
    }

    private byte[] extractZmkIndex(ByteArrayBuffer command) {
        if (ZMK_INDEX_END_INDEX >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_ZMK_INDEX);
        }
        byte[] zmkIndex = command.subArray(ZMK_INDEX_BEGIN_INDEX, ZMK_INDEX_END_INDEX);
        if (!ZoneMasterKey.isExistentZmk(zmkIndex)) {
            throw new CommonException(RespErrorByte.UNSUPPORT_ZMK_INDEX);
        }
        return zmkIndex;
    }

    /**
     * 将tmkey under zmk 转换为 tmkey under lmk
     * 
     * @param command
     * @param commandRequest
     * @return
     */
    private ConvertToUnderAnotherKeyCommandResponse convertTmkeyUnderLmk(ByteArrayBuffer command,
                                                                         ConvertToUnderAnotherKeyCommandRequest commandRequest) {
        //byte keyType = extractKeyType(command);

        byte[] tmkeyUnderZmk = obtainTmkeyUnderZmk(command, commandRequest);

        int kcvLengthBeginIndex = KEY_TYPE_INDEX
                                  + commandRequest.getOriginalKeyLength().getLength() + 1;
        byte[] originalKcv = obtainKcv(command, commandRequest, kcvLengthBeginIndex);

        byte[] zmkey = CommonUtils.hex2byte(ZoneMasterKey.getMatchedZoneMasterKey(
            commandRequest.getZmkIndex()).getValue());

        byte[] tmkey = ThreeDesUtils.decryptKey(zmkey, tmkeyUnderZmk);

        byte[] tmkeyKcv = ThreeDesUtils.encryptKey(tmkey, ThreeDesUtils.KCV_CHECK_VALUE);
        
        if (originalKcv != null) { //kcv有传才校验
            if (!isKcvStartWith(originalKcv, tmkeyKcv)) {
                throw new CommonException(RespErrorByte.KCV_NOT_MATCHED_REQUIRED);
            }
        }

        byte[] tmkeyUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), tmkey);

        ConvertToUnderAnotherKeyCommandResponse commandResponse = new ConvertToUnderAnotherKeyCommandResponse();
        commandResponse.setNewKeyLength(CommonUtils
            .convert2DataShortHexLength(tmkeyUnderLmk.length));
        commandResponse.setNewKey(tmkeyUnderLmk);
        commandResponse.setKcv(tmkeyKcv);
        return commandResponse;
    }

    private byte[] obtainTmkeyUnderZmk(ByteArrayBuffer command,
                                       ConvertToUnderAnotherKeyCommandRequest commandRequest) {
        int tmkUnderZmkEndIndex = KEY_TYPE_INDEX
                                  + commandRequest.getOriginalKeyLength().getLength();
        if (tmkUnderZmkEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_ORIGINAL_KEY);
        }
        return command.subArray(KEY_TYPE_INDEX + 1, tmkUnderZmkEndIndex);
    }

    private byte[] obtainKcv(ByteArrayBuffer command,
                             ConvertToUnderAnotherKeyCommandRequest commandRequest,
                             int kcvLengthIndex) {
        if (kcvLengthIndex >= command.length()) {
            System.out.println("no kcv length. no need check");
            return null; //可以不传密钥
        }

        byte kcvLength = command.subArray(kcvLengthIndex);
        if (kcvLength == 0x00) {
            System.out.println("kcv length is 0. no need check");
            return null; //KCV长度为0，不校验
        }

        int kcvBeginIndex = kcvLengthIndex + 1;
        int kcvEndIndex = kcvLengthIndex + CommonUtils.convert2DecimalLength(kcvLength);
        if (kcvEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_KCV);
        }
        return command.subArray(kcvBeginIndex, kcvEndIndex);
    }

    /**
     * 将workkey under tmk 转换为workkey under lmk
     * 
     * @param command
     * @param commandRequest
     * @return
     */
    private ConvertToUnderAnotherKeyCommandResponse convertWorkKeyUnderLmk(ByteArrayBuffer command,
                                                                           ConvertToUnderAnotherKeyCommandRequest commandRequest) {
        //byte keyType = extractKeyType(command);
        byte[] tmkeyUnderLmk = obtainTmkeyUnderLmk(command, commandRequest);

        byte[] workkeyUnderTmkey = extractWorkkeyUnderTmkey(command, commandRequest);

        int kcvLengthBeginIndex = KEY_TYPE_INDEX + commandRequest.getNewKeyLength().getLength()
                                  + commandRequest.getOriginalKeyLength().getLength() + 1;
        byte[] kcv = obtainKcv(command, commandRequest, kcvLengthBeginIndex);

        byte[] tmkey = ThreeDesUtils.decryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), tmkeyUnderLmk);

        byte[] workkey = ThreeDesUtils.decryptKey(tmkey, workkeyUnderTmkey);

        byte[] workkeyKcv = ThreeDesUtils.encryptKey(workkey, ThreeDesUtils.KCV_CHECK_VALUE);

        if (kcv != null) { //kcv有传才校验
            if (!isKcvStartWith(kcv, workkeyKcv)) {
                throw new CommonException(RespErrorByte.KCV_NOT_MATCHED_REQUIRED);
            }
        }

        byte[] workkeyUnderLmk = ThreeDesUtils.encryptKey(
            CommonUtils.hex2byte(HmsConstants.LOCAL_MASTER_KEY), workkey);

        ConvertToUnderAnotherKeyCommandResponse commandResponse = new ConvertToUnderAnotherKeyCommandResponse();
        commandResponse.setNewKeyLength(CommonUtils
            .convert2DataShortHexLength(workkeyUnderLmk.length));
        commandResponse.setNewKey(workkeyUnderLmk);
        commandResponse.setKcv(workkeyKcv);
        return commandResponse;
    }

    private byte[] obtainTmkeyUnderLmk(ByteArrayBuffer command,
                                       ConvertToUnderAnotherKeyCommandRequest commandRequest) {
        int tmkeyUnderLmkEndIndex = KEY_TYPE_INDEX + commandRequest.getNewKeyLength().getLength();
        if (tmkeyUnderLmkEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_TMK_UNDER_LMK);
        }
        return command.subArray(KEY_TYPE_INDEX + 1, tmkeyUnderLmkEndIndex);
    }

    private byte[] extractWorkkeyUnderTmkey(ByteArrayBuffer command,
                                            ConvertToUnderAnotherKeyCommandRequest commandRequest) {
        int wordKeyUnderTmkeyEndIndex = KEY_TYPE_INDEX
                                        + commandRequest.getNewKeyLength().getLength()
                                        + commandRequest.getOriginalKeyLength().getLength();
        if (wordKeyUnderTmkeyEndIndex >= command.length()) {
            throw new CommonException(RespErrorByte.ABSENCE_OF_WORKKEY);
        }
        return command.subArray(KEY_TYPE_INDEX + commandRequest.getNewKeyLength().getLength() + 1,
            wordKeyUnderTmkeyEndIndex);
    }

    private boolean isKcvStartWith(byte[] givenKcv, byte[] workkeyKcv) {
        int compareLength = givenKcv.length;
        for (int i = 0; i < compareLength; i++) {
            if (givenKcv[i] != workkeyKcv[i]) {
                return false;
            }
        }
        return true;
    }

    //    private byte extractKeyType(ByteArrayBuffer command) {
    //        if (KEY_TYPE_INDEX >= command.length()) {
    //            throw new CommonException(RespErrorByte.ABSENCE_OF_KEY_TYPE);
    //        }
    //        byte keyType = command.subArray(KEY_TYPE_INDEX);
    //        if (!KeyType.isSupportKeyType(keyType)) {
    //            throw new CommonException(RespErrorByte.UNSUPPORT_KEY_TYPE);
    //        }
    //        return keyType;
    //    }
}
