package booker.statistics.controller;

import booker.model.Venue;
import booker.service.VenueService;
import booker.statistics.service.PublisherStatisticsService;
import booker.util.helper.VenueSizeHelper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/statistics/publisher")
public class PublisherStatisticsController {

    private final VenueService venueService;

    private final PublisherStatisticsService publisherStatisticsService;

    @Autowired
    public PublisherStatisticsController(VenueService venueService, PublisherStatisticsService publisherStatisticsService) {
        this.venueService = venueService;
        this.publisherStatisticsService = publisherStatisticsService;
    }

    @GetMapping("/")
    public String statisticsHome() {
        return "statistics/publisherStatistics";
    }

    /**
     * 【请求】获取数据
     */
    @PostMapping(value = "/req_seatPriceDecide", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSeatPriceDecide(@RequestParam("address") String address, @RequestParam("type") String type,
                              @RequestParam("venue_key") int venueKey, @RequestParam("size_key") int sizeKey,
                              @RequestParam("type_key") int typeKey, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(venueKey + " " + sizeKey + " " + typeKey);
        Venue venue = venueService.getOneVenue(address);
        List<Map<String, Double[]>> result = null;
        if (venueKey == 1 && sizeKey == 0 && typeKey == 0) {
            result = publisherStatisticsService.getSmallVenueSeatPriceRange(venue.getVenueID());
        } else if (venueKey == 0 && sizeKey == 2 && typeKey == 0) {
            String size = VenueSizeHelper.getSize(venue.getCol_num(), venue.getRaw_num());
            result = publisherStatisticsService.getSmallSizeSeatPriceRange(size);
        } else if (venueKey == 1 && sizeKey == 0 && typeKey == 3) {
            result = publisherStatisticsService.getSmallVenueSeatPriceRange(venue.getVenueID(), type);
        } else if (venueKey == 0 && sizeKey == 2 && typeKey == 3) {
            String size = VenueSizeHelper.getSize(venue.getCol_num(), venue.getRaw_num());
            result = publisherStatisticsService.getSmallSizeSeatPriceRange(size, type);
        }
        Map<String, Double[]> need_result = new HashMap<>();
        for (Map<String, Double[]> map : result) {
            for (String key : map.keySet()) {
                String real_key = key.split("/")[1];
                if (!need_result.keySet().contains(real_key)) {
                    need_result.put(real_key, map.get(key));
                } else {
                    Double[] prices = map.get(key);
                    Double[] pre_prices = need_result.get(real_key);
                    pre_prices[0] = (pre_prices[0] + prices[0]) / 2.0;
                    pre_prices[1] = (pre_prices[1] + prices[1]) / 2.0;
                    need_result.put(real_key, pre_prices);
                }
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", need_result);
        return jsonObject.toString();
    }
}
