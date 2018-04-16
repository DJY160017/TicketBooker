package booker.util.infoCarrier;

import booker.model.Program;

import java.util.Map;

public class TicketInitInfo {

    /**
     * 座位行数
     */
    private int raw_num;

    /**
     * 座位列数
     */
    private int col_num;

    /**
     * 票的底价
     */
    private double price;

    /**
     * 行方向加价规则
     */
    private Map<Integer[], Double> raw_price_rule;

    /**
     * 列方向加价规则
     */
    private Map<Integer[], Double> col_price_rule;

    /**
     * 节目信息
     */
    private Program program;

    public int getRaw_num() {
        return raw_num;
    }

    public void setRaw_num(int raw_num) {
        this.raw_num = raw_num;
    }

    public int getCol_num() {
        return col_num;
    }

    public void setCol_num(int col_num) {
        this.col_num = col_num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<Integer[], Double> getRaw_price_rule() {
        return raw_price_rule;
    }

    public void setRaw_price_rule(Map<Integer[], Double> raw_price_rule) {
        this.raw_price_rule = raw_price_rule;
    }

    public Map<Integer[], Double> getCol_price_rule() {
        return col_price_rule;
    }

    public void setCol_price_rule(Map<Integer[], Double> col_price_rule) {
        this.col_price_rule = col_price_rule;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
