package booker.util.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class FileHelper {

    /**
     * 读取levelToDiscount.properties
     *
     * @return Map<String, Double>
     */
    public Map<String, Double> readLevelToDiscount() {
        Map<String, Double> map = new TreeMap<>();
        try {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("levelToDiscount.properties");
            properties.load(inputStream);
            for (Object o : properties.keySet()) {
                String key = (String) o;
                double discount = Double.parseDouble(properties.getProperty(key));
                map.put(key, discount);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 读取levelToMarks.properties
     *
     * @return Map<Integer, String>
     */
    public Map<Integer, String> readLevelToMarks() {
        Map<Integer, String> map = new TreeMap<>();
        try {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("levelToMarks.properties");
            properties.load(inputStream);
            for (Object o : properties.keySet()) {
                String key = (String) o;
                Integer marks = Integer.parseInt(properties.getProperty(key));
                map.put(marks, key);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 读取邮箱协议配置信息
     *
     * @return Properties 配置信息
     */
    public Properties readMail(){
        try {
            Properties properties = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mail.properties");
            properties.load(inputStream);
            inputStream.close();
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
