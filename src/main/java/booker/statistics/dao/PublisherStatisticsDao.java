package booker.statistics.dao;

import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.util.dataModel.TwoDimensionModel;

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
     * 获取同一场馆类型,不同节目的座位类型价格区间(1,0,0)
     *
     * @param venueID 场馆ID
     * @return
     */
    Map<String, Double[]> getSmallVenueSeatPriceRange(int venueID);

    /**
     * 获取同一场馆,相同节目类型座位类型价格区间(1,0,3)
     *
     * @param venueID 场馆ID
     * @return
     */
    Map<String, Double[]> getSmallVenueSeatPriceRange(int venueID, String programType);

    /**
     * 统计一个节目的各种座位类型的价格
     *
     * @param programID
     * @return
     */
    Map<String, Double> countProgramRange(ProgramID programID);

}
