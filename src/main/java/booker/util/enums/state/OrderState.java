package booker.util.enums.state;

public enum OrderState {
    PendingReservation("待预定"),
    Unpaid("未支付"),
    AlreadyPaid("已支付"),
    Unsubscribed("已退订"),
    Invalid("已失效");

    private String repre;

    OrderState(String repre) {
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
    public static OrderState getEnum(String a) {
        for (OrderState thisEnum : OrderState.values()) {
            if (thisEnum.repre.equals(a)) {
                return thisEnum;
            }
        }
        return null;
    }
}
