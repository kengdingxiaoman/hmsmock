package cn.hydralisk.hmsmock.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 * @author master.yang
 * @version $Id: FileUtils.java, v 0.1 2014-8-15 上午11:44:30 master.yang Exp $
 */
public abstract class FileUtils {

    public static String getMasterKey() {
        List<String> strs = readToList("lmk");
        return strs.get(0);
    }

    public static List<String> getIpWhiteList() {
        return readToList("whitelist");
    }

    public static List<String> getContent(String fileUrl) {
        return readToList(fileUrl);
    }

    private static List<String> readToList(String fileUrl) {
        try {
            List<String> contents = new ArrayList<String>();
            FileReader fileReader = new FileReader(fileUrl);
            BufferedReader bufferedReader = new BufferedReader((fileReader));
            String lineContent = null;
            while ((lineContent = bufferedReader.readLine()) != null) {
                contents.add(lineContent);
            }
            fileReader.close();
            bufferedReader.close();
            return contents;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
