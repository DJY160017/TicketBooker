package booker.util.dataModel;

import java.io.Serializable;

/**
 * 超二维数据模型
 */
public class SuperTwoDimensionModel<T1, T2, T3> implements Serializable {

    /**
     * 横轴标签
     */
    private T1 tag;

    /**
     * 纵轴数据1
     */
    private T2 data1;

    /**
     * 纵轴数据2
     */
    private T3 data2;

    public SuperTwoDimensionModel() {
    }

    public SuperTwoDimensionModel(T1 tag, T2 data1, T3 data2) {
        this.tag = tag;
        this.data1 = data1;
        this.data2 = data2;
    }

    public T1 getTag() {
        return tag;
    }

    public void setTag(T1 tag) {
        this.tag = tag;
    }

    public T2 getData1() {
        return data1;
    }

    public void setData1(T2 data1) {
        this.data1 = data1;
    }

    public T3 getData2() {
        return data2;
    }

    public void setData2(T3 data2) {
        this.data2 = data2;
    }
}
