package booker.util.dataModel;

import java.io.Serializable;

/**
 * 二维数据模型
 */
public class TwoDimensionModel<T1, T2> implements Serializable {

    /**
     * 横轴标签
     */
    private T1 tag;

    /**
     * 纵轴数据
     */
    private T2 data;

    public TwoDimensionModel() {
    }

    public TwoDimensionModel(T1 tag, T2 data) {
        this.tag = tag;
        this.data = data;
    }

    public T1 getTag() {
        return tag;
    }

    public void setTag(T1 tag) {
        this.tag = tag;
    }

    public T2 getData() {
        return data;
    }

    public void setData(T2 data) {
        this.data = data;
    }
}
