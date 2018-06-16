package booker.statistics.service;

import booker.util.dataModel.SuperTwoDimensionModel;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;

import java.util.List;

public interface ManagerStatisticsService {

    /**
     * 场馆的同比增长率（月，季度）
     *
     * @param unitTime 单位时间
     * @return 同比增长率
     */
    List<TwoDimensionModel> venueIncomeIncrease(UnitTime unitTime, int year);

    /**
     * 节目的额同比增长率
     *
     * @param unitTime 单位时间
     * @return 同比增长率
     */
    List<TwoDimensionModel> programIncomeIncrease(UnitTime unitTime, int year);

    /**
     * 场馆的平均收入（月，季度）
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 收入
     */
    List<TwoDimensionModel> averageVenueIncome(UnitTime unitTime, int year);

    /**
     * 节目的平均收入（月，季度）
     *
     * @param unitTime 单位时间
     * @param year     年份
     * @return 收入
     */
    List<TwoDimensionModel> averageProgramIncome(UnitTime unitTime, int year);

    /**
     * 场馆的周转(所有的场馆)
     *
     * @param unitTime
     * @param year
     * @return
     */
    double turnover(UnitTime unitTime, int year);

    /**
     * 场馆经营效益与地域的关系
     *
     * @return 关系
     */
    List<TwoDimensionModel> statisByArea();

    /**
     * 场馆经营效益与规模的关系
     *
     * @return 关系
     */
    List<TwoDimensionModel> statisBySize();

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
