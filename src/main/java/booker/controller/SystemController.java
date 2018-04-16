package booker.controller;

import booker.service.MemberService;
import booker.service.SettlementService;
import booker.service.TicketService;
import booker.service.VenueService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController {

    private final MemberService memberService;

    private final TicketService ticketService;

    private final SettlementService settlementService;

    private final VenueService venueService;

    @Autowired
    public SystemController(MemberService memberService, TicketService ticketService, SettlementService settlementService, VenueService venueService) {
        this.memberService = memberService;
        this.ticketService = ticketService;
        this.settlementService = settlementService;
        this.venueService = venueService;
    }

    @GetMapping("/error")
    public String getError() {
        return "errorPage";
    }

    /**
     * 【请求】用户数据中心
     */
    @PostMapping(value = "/req_userDataCenter", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqUserDataCenter(HttpServletRequest request, HttpServletResponse response) {
        int total_users = memberService.getSignUpUserNum();
        Map<String, Integer> levelMembers = memberService.getLevelMembers();
        Map<String, Integer> consume_three = ticketService.calculateConsume();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total_users", total_users);
        jsonObject.put("level", levelMembers);
        jsonObject.put("consume", consume_three);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆数据中心
     */
    @PostMapping(value = "/req_venueDataCenter", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueDataCenter(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Integer> state = venueService.getVenues();
        Map<String, Double> profit = settlementService.countVenueProfit(LocalDateTime.now().getYear());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", state);
        jsonObject.put("profit", profit);
        return jsonObject.toString();
    }
}
