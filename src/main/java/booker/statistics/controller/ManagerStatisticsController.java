package booker.statistics.controller;

import booker.statistics.service.ManagerStatisticsService;
import booker.util.dataModel.SuperTwoDimensionModel;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/statistics/manager")
public class ManagerStatisticsController {

    private final ManagerStatisticsService managerStatisticsService;

    @Autowired
    public ManagerStatisticsController(ManagerStatisticsService managerStatisticsService) {
        this.managerStatisticsService = managerStatisticsService;
    }

    @GetMapping("/")
    public String statisticsHome() {
        return "statistics/managerStatistics";
    }

    /**
     * 【请求】获取场馆同比增长率
     */
    @PostMapping(value = "/req_systemVenueProfit", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSystemVenueProfit(@RequestParam("year") int year, HttpServletRequest request, HttpServletResponse response) {
        List<TwoDimensionModel> month_venueProfit = managerStatisticsService.venueIncomeIncrease(UnitTime.MONTH, year);
        List<TwoDimensionModel> quarter_venueProfit = managerStatisticsService.venueIncomeIncrease(UnitTime.QUARTER, year);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("month_profit", month_venueProfit);
        jsonObject.put("quarter_profit", quarter_venueProfit);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目同比增长率
     */
    @PostMapping(value = "/req_systemProgramProfit", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSystemProgramProfit(@RequestParam("year") int year, HttpServletRequest request, HttpServletResponse response) {
        List<TwoDimensionModel> month_programProfit = managerStatisticsService.programIncomeIncrease(UnitTime.MONTH, year);
        List<TwoDimensionModel> quarter_programProfit = managerStatisticsService.programIncomeIncrease(UnitTime.QUARTER, year);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("month_profit", month_programProfit);
        jsonObject.put("quarter_profit", quarter_programProfit);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆平均收入
     */
    @PostMapping(value = "/req_systemVenueAverageIncome", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSystemVenueAverageIncome(@RequestParam("unit_time") String unit_time, @RequestParam("year") int year, HttpServletRequest request, HttpServletResponse response) {
        UnitTime unitTime = UnitTime.getEnum(unit_time);
        List<SuperTwoDimensionModel> venueProfit = managerStatisticsService.averageVenueIncome(unitTime, year);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("profit", venueProfit);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目平均收入
     */
    @PostMapping(value = "/req_systemProgramAverageIncome", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSystemProgramAverageIncome(@RequestParam("unit_time") String unit_time, @RequestParam("year") int year, HttpServletRequest request, HttpServletResponse response) {
        UnitTime unitTime = UnitTime.getEnum(unit_time);
        List<SuperTwoDimensionModel> programProfit = managerStatisticsService.averageProgramIncome(unitTime, year);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("profit", programProfit);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取平台周转
     */
    @PostMapping(value = "/req_systemTurnover", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSystemTurnover(HttpServletRequest request, HttpServletResponse response) {
        List<Integer> years = Arrays.asList(2018, 2017, 2016);
        List<Double> turnover = new ArrayList<>();
        for (Integer year : years) {
            turnover.add(managerStatisticsService.turnover(year));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", turnover);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆经营效益与地域的关系
     */
    @PostMapping(value = "/req_venueProfitArea", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueProfitArea(HttpServletRequest request, HttpServletResponse response) {
        List<TwoDimensionModel> twoDimensionModels = managerStatisticsService.statisByArea();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", twoDimensionModels);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆经营效益与规模的关系
     */
    @PostMapping(value = "/req_venueProfitSize", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueProfitSize(HttpServletRequest request, HttpServletResponse response) {
        List<TwoDimensionModel> twoDimensionModels = managerStatisticsService.statisBySize();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", twoDimensionModels);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆经营效益与节目效益的关系
     */
    @PostMapping(value = "/req_venueProfitProgram", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueProfitProgram(HttpServletRequest request, HttpServletResponse response) {
        List<SuperTwoDimensionModel> twoDimensionModels = managerStatisticsService.statisByProgramIncome();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", twoDimensionModels);
        return jsonObject.toString();
    }

    /**
     * 【请求】活跃会员与地域的关系
     */
    @PostMapping(value = "/req_activeMemberArea", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqActiveMemberArea(HttpServletRequest request, HttpServletResponse response) {
        List<TwoDimensionModel> twoDimensionModels = managerStatisticsService.countMemberArea();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", twoDimensionModels);
        return jsonObject.toString();
    }
}
