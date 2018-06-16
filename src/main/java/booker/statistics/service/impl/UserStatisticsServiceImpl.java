package booker.statistics.service.impl;

import booker.dao.ProgramDao;
import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.statistics.dao.UserStatisticsDao;
import booker.statistics.service.UserStatisticsService;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserStatisticsService")
public class UserStatisticsServiceImpl implements UserStatisticsService {

    private final UserStatisticsDao userStatisticsDao;

    private final ProgramDao programDao;

    @Autowired
    public UserStatisticsServiceImpl(UserStatisticsDao userStatisticsDao, ProgramDao programDao) {
        this.userStatisticsDao = userStatisticsDao;
        this.programDao = programDao;
    }

    /**
     * 统计指定ID用户的花销
     *
     * @param userID   用户ID
     * @param unitTime 单位时间（月，季度，年）
     * @param year     指定年份
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String userID, UnitTime unitTime, int year) {
        List<TwoDimensionModel> result = null;
        if (unitTime.equals(UnitTime.MONTH)) {
            result = userStatisticsDao.costByMonth(userID, year);
        } else {
            result = userStatisticsDao.costByQuarter(userID, year);
        }
        return result;
    }

    /**
     * 统计指定ID用户的花销
     *
     * @param userID 用户ID
     * @return 以单位时间为单位的统计数据
     */
    @Override
    public List<TwoDimensionModel> costByUnitTime(String userID) {
        return userStatisticsDao.costByYear(userID);
    }

    /**
     * 统计指定用户观看过的节目类型数量（以便统计最爱节目类型）
     *
     * @param userID 用户ID
     * @return 每种节目类型观看的次数
     */
    @Override
    public Map<String, Integer> countPty(String userID) {
        List<ProgramID> ids = userStatisticsDao.getALlUserProgramID(userID);
        List<Program> programs = programDao.getAllProgram(ids);
        Map<String, Integer> result = new HashMap<>();
        for (Program program : programs) {
            if (!result.keySet().contains(program.getProgramType())) {
                result.put(program.getProgramType(), 0);
            }
            result.put(program.getProgramType(), result.get(program.getProgramType()) + 1);
        }
        return result;
    }

    /**
     * 统计用户观看节目的地点次数最多地点
     *
     * @param userID 用户ID
     * @return 可能的常住地
     */
    @Override
    public TwoDimensionModel countCity(String userID) {
        List<ProgramID> ids = userStatisticsDao.getALlUserProgramID(userID);
        List<Program> programs = programDao.getAllProgram(ids);
        Map<String, Integer> result = new HashMap<>();
        for (Program program : programs) {
            Venue venue = program.getVenue();
            String city = venue.getCity();
            if (!result.keySet().contains(city)) {
                result.put(city, 1);
            } else {
                result.put(city, result.get(city) + 1);
            }
        }

        String city = null;
        for (String key : result.keySet()) {
            if (city == null) {
                city = key;
                continue;
            }

            if (result.get(key) > result.get(city)) {
                city = key;
            }
        }
        return new TwoDimensionModel<>(city, result.get(city));
    }

    /**
     * 获取用户的每笔订单的详细交易价格
     *
     * @param userID 用户ID
     * @return
     */
    @Override
    public List<TwoDimensionModel> getDetailPrice(String userID) {
        return userStatisticsDao.getDetailPrice(userID);
    }

    /**
     * 获取客户的消费价格区间
     *
     * @param userID 用户ID
     * @return 价格区间
     */
    @Override
    public int[] countCostRange(String userID) {
        return userStatisticsDao.countCostRange(userID);
    }
}
