package booker.statistics.controller;

import booker.statistics.service.UserStatisticsService;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/statistics/user")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    @Autowired
    public UserStatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }


    @GetMapping("/")
    public String statisticsHome() {
        return "statistics/userStatistics";
    }

    /**
     * 【请求】获取用户的消费价格区间
     */
    @PostMapping(value = "/req_memberConsumptionRange", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqMemberConsumptionRang(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userID = String.valueOf(session.getAttribute("user_mail"));
        double[] range = userStatisticsService.countCostRange(userID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("range", range);
        return jsonObject.toString();
    }

    /**
     * 【请求】根据单位获取消费数据
     */
    @PostMapping(value = "/req_memberConsumptionByUnitTime", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqMemberConsumptionByUnitTime(@RequestParam("unit_time") String unitTime, @RequestParam("unit_time_year") String year, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userID = String.valueOf(session.getAttribute("user_mail"));
        UnitTime unit = UnitTime.getEnum(unitTime);
        List<TwoDimensionModel> result = null;
        if (unit.equals(UnitTime.ALL) && year.equals("全部")) {
            result = userStatisticsService.getDetailPrice(userID);
        } else if (unit.equals(UnitTime.ALL) && !year.equals("全部")) {
            result = userStatisticsService.costByUnitTime(userID, Integer.parseInt(year));
        } else if (!unit.equals(UnitTime.ALL) && year.equals("全部")) {
            result = userStatisticsService.costByUnitTime(userID, unit);
        } else {
            result = userStatisticsService.costByUnitTime(userID, unit, Integer.parseInt(year));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取会员的最喜爱节目信息， 常住地
     */
    @PostMapping(value = "/req_memberFavorite", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqMemberFavorite(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userID = String.valueOf(session.getAttribute("user_mail"));
        Map<String, Integer> pty = userStatisticsService.countPty(userID);
        Map<String, Integer> area = userStatisticsService.countCity(userID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pty", pty);
        jsonObject.put("area", area);
        return jsonObject.toString();
    }
}
