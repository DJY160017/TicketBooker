package booker.util.enums.state;

public enum UnitTime {

    DAY("日"),
    MONTH("月份"),
    QUARTER("季度"),
    YEAR("年份"),
    ALL("全部");

    private String repre;

    UnitTime(String repre) {
        this.repre = repre;
    }

    /**
     * @return 该枚举相对应的文件中形式
     * <p>
     * enum TO String
     * 便于写入数据库
     */
    public String getRepre() {
        return repre;
    }

    /**
     * @return 该类型对应的枚举代码
     * <p>
     * String TO enum
     * 便于从数据库读入
     */
    public static UnitTime getEnum(String a) {
        for (UnitTime thisEnum : UnitTime.values()) {
            if (thisEnum.repre.equals(a)) {
                return thisEnum;
            }
        }
        return null;
    }
}
