package booker.statistics.dao;

import booker.util.dataModel.SuperTwoDimensionModel;
import booker.util.dataModel.TwoDimensionModel;

import java.time.LocalDateTime;
import java.util.List;

public interface ManagerStatisticsDao {

    /**
     * 场馆的同比增长率（月）
     *
     * @return 同比增长率
     */
    List<Double> venueIncomeIncreaseByMonth(int year);

    /**
     * 场馆的同比增长率（季度）
     *
     * @return 同比增长率
     */
    List<Double> venueIncomeIncreaseByQuarter(int year);

    /**
     * 节目的同比增长率
     *
     * @param year 年份
     * @return 同比增长率
     */
    List<Double> programIncomeIncreaseByMonth(int year);

    /**
     * 节目的同比增长率
     *
     * @param year 年份
     * @return 同比增长率
     */
    List<Double> programIncomeIncreaseByQuarter(int year);

    /**
     * 场馆的平均收入（月）
     *
     * @param year 年份
     * @return 收入
     */
    List<SuperTwoDimensionModel> averageVenueIncomeByMonth(int year);

    /**
     * 场馆的平均收入（季度）
     *
     * @param year 年份
     * @return 收入
     */
    List<SuperTwoDimensionModel> averageVenueIncomeByQuarter(int year);

    /**
     * 节目的平均收入（月）
     *
     * @param year 年份
     * @return 收入
     */
    List<SuperTwoDimensionModel> averageProgramIncomeByMonth(int year);

    /**
     * 节目的平均收入（季度）
     *
     * @param year 年份
     * @return 收入
     */
    List<SuperTwoDimensionModel> averageProgramIncomeByQuarter(int year);

    /**
     * 场馆的周转(所有的场馆)
     *
     * @param year 年份
     * @return 周转天数
     */
    double turnover(int venueID, int year);

    /**
     * 获取场馆的总收入
     *
     * @param venueID 场馆ID
     * @return 总收入
     */
    double countVenueIncome(int venueID);

    /**
     * 场馆经营效益与节目效益的关系
     *
     * @return 关系
     */
    List<SuperTwoDimensionModel> statisByProgramIncome();

    /**
     * 活跃会员与地域的关系
     *
     * @return 关系
     */
    List<TwoDimensionModel> countMemberArea();
}
