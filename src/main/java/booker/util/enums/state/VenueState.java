package booker.util.enums.state;

public enum VenueState {

    Unapproved("未审批"),
    NotThrough("未通过"),
    AlreadyPassed("已通过"),
    AlreadyReserved("已预定"),
    Invalid("已失效");

    private String repre;

    VenueState(String repre) {
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
    public static VenueState getEnum(String a) {
        for (VenueState thisEnum : VenueState.values()) {
            if (thisEnum.repre.equals(a)) {
                return thisEnum;
            }
        }
        return null;
    }
}
