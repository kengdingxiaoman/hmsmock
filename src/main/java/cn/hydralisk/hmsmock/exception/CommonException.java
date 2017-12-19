package cn.hydralisk.hmsmock.exception;

import cn.hydralisk.hmsmock.constants.CommonConstants;
import cn.hydralisk.hmsmock.util.CommonUtils;

public class CommonException extends RuntimeException {

	private static final long serialVersionUID = 7088804960598494831L;

	private RespErrorByte respErrorByte;

	public CommonException(RespErrorByte respErrorByte) {
		super();
		this.respErrorByte = respErrorByte;
	}

	public RespErrorByte getRespErrorByte() {
		return respErrorByte;
	}

	@Override
	public String getMessage() {
		StringBuffer message = new StringBuffer();
		message.append("错误码:")
				.append(CommonUtils.byte2hexHexSytle(respErrorByte
						.getErrorByte())).append(CommonConstants.COMMA)
				.append("错误描述：").append(respErrorByte.getErrorDesc());
		return message.toString();
	}
}
