package booker.util.enums.state;

public enum ProgramState {

    AlreadyPassed("已通过"),
    Invalid("已失效");

    private String repre;

    ProgramState(String repre) {
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
    public static ProgramState getEnum(String a) {
        for (ProgramState thisEnum : ProgramState.values()) {
            if (thisEnum.repre.equals(a)) {
                return thisEnum;
            }
        }
        return null;
    }
}
