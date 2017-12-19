package cn.hydralisk.hmsmock.service;

public interface Command {

	public byte[] execute(byte[] requestData);
}
