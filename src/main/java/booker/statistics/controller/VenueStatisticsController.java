package booker.statistics.controller;

import booker.statistics.service.VenueStatisticsService;
import booker.util.dataModel.TwoDimensionModel;
import booker.util.enums.state.UnitTime;
import booker.util.formatter.VenueIDFormatter;
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
@RequestMapping("/statistics/venue")
public class VenueStatisticsController {

    private final VenueStatisticsService venueStatisticsService;

    @Autowired
    public VenueStatisticsController(VenueStatisticsService venueStatisticsService) {
        this.venueStatisticsService = venueStatisticsService;
    }


    @GetMapping("/")
    public String statisticsHome() {
        return "statistics/venueStatistics";
    }

    /**
     * 【请求】获取用户的消费价格区间
     */
    @PostMapping(value = "/req_marketCompare", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqMarketCompare(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        int realID = VenueIDFormatter.deFormate(venueID);
        Map<String, Double> averagePriceIndex = venueStatisticsService.averagePriceIndex(realID);
        Map<String, Double> incomeIndex = venueStatisticsService.incomeIndex(realID);
        Map<String, Double> occupyIndex = venueStatisticsService.occupyIndex(realID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("平均价格指数", averagePriceIndex);
        jsonObject.put("收入指数", incomeIndex);
        jsonObject.put("市场占有率", occupyIndex);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆比较的数据
     */
    @PostMapping(value = "/req_venuePriceCompare", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenuePriceCompare(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        int realID = VenueIDFormatter.deFormate(venueID);
        double averageAreaPrice = venueStatisticsService.averageAreaPrice(realID);
        double averageSizePrice = venueStatisticsService.averageSizePrice(realID);
        double averageSizeAreaPrice = venueStatisticsService.averageSizeAreaPrice(realID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("相同地域平均价格", averageAreaPrice);
        jsonObject.put("同等规模平均价格", averageSizePrice);
        jsonObject.put("维度组合平均价格", averageSizeAreaPrice);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆预订率数据
     */
    @PostMapping(value = "/req_venueBookIndex", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueBookIndex(@RequestParam("unit_time") String unitTime, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        int realID = VenueIDFormatter.deFormate(venueID);
        UnitTime unitTime1 = UnitTime.getEnum(unitTime);
        List<TwoDimensionModel> twoDimensionModelList = venueStatisticsService.bookIndex(realID, unitTime1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", twoDimensionModelList);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆预定高峰时间数据
     */
    @PostMapping(value = "/req_venueTopBookIndex", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueTopBookIndex(@RequestParam("year") int year, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        int realID = VenueIDFormatter.deFormate(venueID);
        List<TwoDimensionModel> localDateTimes = venueStatisticsService.countTopTimeRange(realID, year);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", localDateTimes);
        return jsonObject.toString();
    }

}
