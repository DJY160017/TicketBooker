package booker.task;

import java.util.Random;

public class CodeTask {

    /**
     * 生成验证码
     *
     * @return String
     */
    public String createCode(){
        return formate(createRandomCode());
    }

    /**
     * 生成6位以内的书随机数
     *
     * @return int
     */
    private int createRandomCode(){
        Random rand=new Random();
        return rand.nextInt(1000000);
    }

    /**
     * 将6位以内的随机数format为6位
     *
     * @param num 随机数
     * @return String
     */
    private String formate(int num){
        StringBuilder str_num = new StringBuilder(String.valueOf(num));
        for(int i = str_num.length();i<6;i++){
            str_num.insert(0, "0");
        }
        return str_num.toString();
    }

}
