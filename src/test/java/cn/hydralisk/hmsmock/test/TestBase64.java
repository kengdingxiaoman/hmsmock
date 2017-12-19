package cn.hydralisk.hmsmock.test;

import cn.hydralisk.hmsmock.util.CommonUtils;
import org.bouncycastle.util.encoders.Base64;

/**
 * 
 * @author master.yang
 * @version $Id: TestBase64.java, v 0.1 2015-1-8 下午2:43:32 master.yang Exp $
 */
public class TestBase64 {

    public static void main(String[] args) {
        String data = "932D506FFE62B5CD40A2AE7A59B5CCAF0927E422090B06E048" +
        		"F94E96CCA6441980BD145F5C78C1AD81B7E76020CB981B9E74AD1E23F" +
        		"4FE0E31FFB41CF3B678E33B9E62FB2C04DDDE840951581425BDB5E841" +
        		"7DA7A6897E6F28C3CD2746EE66D0ABB7A677E64F2C2A7558D138EE64F265";
        
        byte[] sensBase64 = Base64.encode(CommonUtils.hex2byte(data));

        String sensBase64Str = new String(sensBase64);

        System.out.println("sens after base64 : " + sensBase64Str);

        sensBase64Str = sensBase64Str.replace("+", "-");
        sensBase64Str = sensBase64Str.replace("/", "_");
        System.out.println("sens final : " + sensBase64Str);
        
    }
}
