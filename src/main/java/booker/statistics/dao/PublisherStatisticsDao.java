package booker.statistics.dao;

import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;

import java.util.List;
import java.util.Map;

public interface PublisherStatisticsDao {

    /**
     * 统计指定ID用户的花销/单位时间
     *
     * @param caterer 用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<Program> getPrograms(String caterer, int year);

    /**
     * 统计指定ID用户的花销/单位时间（年）
     *
     * @param caterer 用户ID
     * @return 以单位时间为单位的统计数据
     */
    List<Program> getPrograms(String caterer);

    /**
     * 根据场馆规模获取场馆
     *
     * @param size 规模
     * @return 场馆列表
     */
    List<Venue> getSmallSizeVenue(String size);

    /**
     * 统计一个节目的各种座位类型的价格
     *
     * @param programID
     * @return
     */
    Map<String, Double> countProgramRange(ProgramID programID);

}
