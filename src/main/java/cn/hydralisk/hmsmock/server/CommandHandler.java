package cn.hydralisk.hmsmock.server;

import cn.hydralisk.hmsmock.constants.HmsConstants;
import cn.hydralisk.hmsmock.exception.CommonException;
import cn.hydralisk.hmsmock.exception.RespErrorByte;
import cn.hydralisk.hmsmock.service.Command;
import cn.hydralisk.hmsmock.service.CommandFactory;
import cn.hydralisk.hmsmock.util.ByteArrayBuffer;
import cn.hydralisk.hmsmock.util.CommonUtils;
import cn.hydralisk.hmsmock.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;

/**
 * 处理请求
 * 
 * @author master.yang
 * 
 */
public class CommandHandler implements Runnable {

    private Socket socket;

    public CommandHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        InputStream socketIn = null;
        OutputStream socketOut = null;

        try {
            long identify = Calendar.getInstance().getTime().getTime();

            CommonUtils.println("================================================================");
            printlnWithTime(
                "New connection accepted " + socket.getInetAddress() + ":" + socket.getPort(),
                identify);

            socketIn = socket.getInputStream();
            socketOut = socket.getOutputStream();

            byte[] requestData = readRequestData(socketIn);
            printlnWithTime("Request Data: ", requestData, identify);

            if (!checkIpIsInWhiteList(identify)) {
                printlnWithTime("check ip white list failed!", identify);
                printlnWithTime("Response Data: ",
                    new byte[] { RespErrorByte.REQUEST_IP_NOT_IN_WHITE_LIST.getErrorByte() },
                    identify);

                socketOut.write(new byte[] { RespErrorByte.REQUEST_IP_NOT_IN_WHITE_LIST
                    .getErrorByte() });
                return;
            }

            byte[] responseData = executeCommand(requestData, identify);

            printlnWithTime("Response Data: ", responseData, identify);

            socketOut.write(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (socketIn != null) {
                    socketIn.close();
                }
                if (socketOut != null) {
                    socketOut.flush();
                    socketOut.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private byte[] readRequestData(InputStream socketIn) throws IOException {
        byte[] maxBuffer = new byte[HmsConstants.MAX_TRANSFER_DATA_LENGTH];
        int readedLength = socketIn.read(maxBuffer, 0, HmsConstants.MAX_TRANSFER_DATA_LENGTH);

        if (noRequestData(readedLength)) {
            return null;
        }

        byte[] data = new byte[readedLength];
        System.arraycopy(maxBuffer, 0, data, 0, readedLength);
        return data;
    }

    private boolean noRequestData(int dataLength) {
        return dataLength <= 0;
    }

    private boolean checkIpIsInWhiteList(long identify) {
        List<String> whiteIpList = FileUtils.getIpWhiteList();
        if (whiteIpList == null || whiteIpList.isEmpty()) {
            printlnWithTime("ip white list is empty, no need check!", identify);
            return true;
        }

        String requestIp = socket.getInetAddress().getHostAddress();
        for (String whiteIp : whiteIpList) {
            if (requestIp.equals(whiteIp)) {
                return true;
            }
        }

        return false;
    }

    private byte[] executeCommand(byte[] requestData, long identify) {
        try {
            Command command = CommandFactory.generateCommand(requestData);
            byte[] handleResult = command.execute(requestData);
            ByteArrayBuffer responseData = new ByteArrayBuffer(handleResult.length);
            responseData.append(HmsConstants.SUCCESS_RESP_BYTE);
            responseData.append(handleResult);
            return responseData.toByteArray();
        } catch (CommonException coEx) {
            printlnWithTime(coEx.getMessage(), identify);
            return generateCommonErrorResponseData(coEx);
        } catch (Exception ex) {
            ex.printStackTrace();
            return generateUnknownErrorResponseData();
        }
    }

    private byte[] generateCommonErrorResponseData(CommonException coEx) {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer();
        byteArrayBuffer.append(HmsConstants.FAILER_RESP_BYTE);
        byteArrayBuffer.append(coEx.getRespErrorByte().getErrorByte());
        return byteArrayBuffer.toByteArray();
    }

    private byte[] generateUnknownErrorResponseData() {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer();
        byteArrayBuffer.append(HmsConstants.FAILER_RESP_BYTE);
        byteArrayBuffer.append(RespErrorByte.UNKNOWN_ERROR.getErrorByte());
        return byteArrayBuffer.toByteArray();
    }

    private void printlnWithTime(String str, byte[] data, long identify) {
        printlnWithTime(str + CommonUtils.byte2hexHexSytle(data), identify);
    }

    private void printlnWithTime(String str, long identify) {
        CommonUtils.println(CommonUtils.getCurrentDateTime() + " [" + identify + "] " + str);
    }
}
