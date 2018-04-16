package booker.util.enums.state;

public enum SettlementState {
    Unsettled("未结算"),
    AlreadySettled("已结算"),
    Unfinished("未完成");

    private String repre;

    SettlementState(String repre) {
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
    public static SettlementState getEnum(String a) {
        for (SettlementState thisEnum : SettlementState.values()) {
            if (thisEnum.repre.equals(a)) {
                return thisEnum;
            }
        }
        return null;
    }
}
