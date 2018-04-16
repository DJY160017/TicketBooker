package booker.controller;

import booker.model.BankAccount;
import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.service.ExternalBalanceService;
import booker.service.ProgramService;
import booker.service.VenueService;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.VenueState;
import booker.util.exception.*;
import booker.util.formatter.VenueIDFormatter;
import booker.util.infoCarrier.TicketInitInfo;
import booker.task.MD5Task;
import booker.task.ValidateTask;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/venue")
public class VenueController {

    private final VenueService venueService;

    private final ExternalBalanceService externalBalanceService;

    @Autowired
    public VenueController(VenueService venueService, ExternalBalanceService externalBalanceService) {
        this.venueService = venueService;
        this.externalBalanceService = externalBalanceService;
    }

    @GetMapping("/home")
    public String home() {
        return "venue/venueHome";
    }

    @GetMapping("/application")
    public String venueApplication() {
        return "venue/apply/venueApplication";
    }

    @GetMapping("/addPlan")
    public String venueAdd() {
        return "venue/add/venueAdd";
    }

    @GetMapping("/book")
    public String venueBook() {
        return "venue/add/venueBook";
    }

    @GetMapping("/plan")
    public String venuePlan() {
        return "venue/plan/venuePlan";
    }

    @GetMapping("/venueInfoManage")
    public String venueInfo() {
        return "venue/venueInfo";
    }

    /**
     * 【请求】场馆登录
     * produces少一点都不行
     */
    @PostMapping(value = "/req_log_in", produces = "text/json;charset=UTF-8;application/json")
    public @ResponseBody
    String reqLogIn(@RequestParam("venueID") String venueID, @RequestParam("password") String password,
                    HttpServletRequest request, HttpServletResponse response) {
        boolean result = false;
        JSONObject jsonObject = new JSONObject();
        try {
            result = venueService.login(venueID, password);
            HttpSession session = request.getSession(false);
            session.setAttribute("venueID", venueID);
            session.setAttribute("user_type", "venue");
        } catch (VenueNotExistException | PasswordInvalidException e) {
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }

        if (result) {
            jsonObject.put("result", "1;登录成功");
        } else {
            jsonObject.put("result", "-1;服务器开了一个小差。。请稍后重试");
        }
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆登出
     */
    @PostMapping(value = "/req_log_out", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqLogOut(HttpServletRequest request, HttpServletResponse response) {
        // 限制进入
        HttpSession session = request.getSession(false);
        session.setAttribute("venueID", null);
        session.setAttribute("user_type", null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1");
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆信息
     */
    @PostMapping(value = "/req_getVenueInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetVenueInfo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        Venue venue = venueService.getVenue(venueID);
        BankAccount bankAccount = externalBalanceService.getUserAccount(String.valueOf(VenueIDFormatter.deFormate(venueID)));
        session.setAttribute("venueState", venue.getVenueState());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venue", venue);
        if (bankAccount != null) {
            jsonObject.put("account", bankAccount.getAccount());
        } else {
            jsonObject.put("account", "null");
        }
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆场馆状态
     */
    @PostMapping(value = "/req_getVenueState", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetVenueState(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        VenueState venueState = (VenueState) session.getAttribute("venueState");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venueState", venueState);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆申请
     */
    @PostMapping(value = "/req_venueApply", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueApply(@RequestParam("name") String name, @RequestParam("password") String password,
                         @RequestParam("raw") String raw, @RequestParam("col") String col, @RequestParam("price") String price,
                         @RequestParam("address") String address, HttpServletRequest request, HttpServletResponse response) {
        Venue venue = new Venue();
        venue.setName(name);
        venue.setPassword(MD5Task.encodeMD5(password));
        venue.setRaw_num(Integer.parseInt(raw));
        venue.setCol_num(Integer.parseInt(col));
        venue.setPrice(Double.parseDouble(price));
        venue.setAddress(address);
        venue.setVenueState(VenueState.Unapproved);
        JSONObject jsonObject = new JSONObject();
        try {
            int venueID = venueService.apply(venue);
            jsonObject.put("result", "1;申请成功");
            jsonObject.put("venueID", VenueIDFormatter.formate(venueID));
            return jsonObject.toString();
        } catch (VenueExistedException | PositiveIntegerException | PriceException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】场馆信息修改
     */
    @PostMapping(value = "/req_venueInfoModify", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueInfoModify(@RequestParam("name") String name, @RequestParam("raw") String raw, @RequestParam("col") String col,
                              @RequestParam("price") String price, @RequestParam("address") String address, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        session.setAttribute("venueState", VenueState.Unapproved);
        Venue venue = venueService.getVenue(venueID);
        venue.setName(name);
        venue.setRaw_num(Integer.parseInt(raw));
        venue.setCol_num(Integer.parseInt(col));
        venue.setPrice(Double.parseDouble(price));
        venue.setAddress(address);
        venue.setVenueState(VenueState.Unapproved);
        JSONObject jsonObject = new JSONObject();
        venueService.modifyVenue(venue);
        jsonObject.put("result", "1;修改成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆密码修改
     */
    @PostMapping(value = "/req_venueModifyPassword", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueModifyPassword(@RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        Venue venue = venueService.getVenue(venueID);
        venue.setPassword(MD5Task.encodeMD5(password));
        JSONObject jsonObject = new JSONObject();
        venueService.modifyVenue(venue);
        jsonObject.put("result", "1;修改成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆银行卡号预留
     */
    @PostMapping(value = "/req_addVenueAccount", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqAddVenueAccount(@RequestParam("account") String account, HttpServletRequest request, HttpServletResponse response) {
        ValidateTask validateTask = new ValidateTask();
        JSONObject jsonObject = new JSONObject();
        try {
            validateTask.accountDetector(account);
            HttpSession session = request.getSession(false);
            String venueID = String.valueOf(session.getAttribute("venueID"));
            BankAccount bankAccount = new BankAccount();
            bankAccount.setUserID(String.valueOf(VenueIDFormatter.deFormate(venueID)));
            bankAccount.setAccount(account);
            boolean result = externalBalanceService.addBankAccount(bankAccount);
            if (result) {
                jsonObject.put("result", "1;预留成功");
            } else {
                jsonObject.put("result", "-1;预留失败");
            }
            return jsonObject.toString();
        } catch (AccountInvalidException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】查找场馆初始信息
     */
    @PostMapping(value = "/req_venueSearchInit", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVenueInit(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        int maxPage = venueService.getMaxPage("全部", "", 10);
        List<Venue> venues = venueService.getVenues("全部", "", 1, 10);
        session.setAttribute("venue_max_page", maxPage);
        session.setAttribute("venue_pageNow", 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venues", venues);
        jsonObject.put("venues_size", venues.size());
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目信息
     */
    @PostMapping(value = "/req_venueInfoByPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetVenueByPage(@RequestParam("area") String area, @RequestParam("pageNow") String pageNow,
                             @RequestParam("key") String key, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        List<Venue> venues = venueService.getVenues(area, key, Integer.parseInt(pageNow), 10);
        session.setAttribute("venue_pageNow", Integer.parseInt(pageNow));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venues", venues);
        jsonObject.put("venues_size", venues.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目信息,根据条件
     */
    @PostMapping(value = "/req_search", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSearchVenue(@RequestParam("area") String area, @RequestParam("key") String key,
                          HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        int maxPage = venueService.getMaxPage(area, key, 10);
        List<Venue> venues = venueService.getVenues(area, key, 1, 10);
        session.setAttribute("venue_pageNow", 1);
        session.setAttribute("venue_max_page", maxPage);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venues", venues);
        jsonObject.put("venues_size", venues.size());
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取场馆信息
     */
    @PostMapping(value = "/req_getOneVenue", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetOneVenue(@RequestParam("address") String address, HttpServletRequest request, HttpServletResponse response) {
        Venue venue = venueService.getOneVenue(address);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venue", venue);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目的最大页数
     */
    @PostMapping(value = "/req_getMaxPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetMaxPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String maxPage = String.valueOf(session.getAttribute("venue_max_page"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目的当前页数
     */
    @PostMapping(value = "/req_getPageNow", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetPageNow(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String pageNow = String.valueOf(session.getAttribute("venue_pageNow"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNow", pageNow);
        return jsonObject.toString();
    }

    /**
     * 【请求】预定场馆
     */
    @PostMapping(value = "/req_book", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqBook(@RequestParam("startTime") String start_time, @RequestParam("endTime") String end_time,
                   @RequestParam("name") String name, @RequestParam("type") String type, @RequestParam("low_price") String lowPrice,
                   @RequestParam("reserve_time") String reserve_time, @RequestParam("introduction") String introduction,
                   @RequestParam("raw_rule") String rawRule, @RequestParam("col_rule") String colRule, @RequestParam("address") String address,
                   HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        JSONObject jsonObject = new JSONObject();
        try {
            String userID = String.valueOf(session.getAttribute("user_mail"));
            Venue venue = venueService.getOneVenue(address);
            LocalDateTime startDate = LocalDateTime.of(LocalDate.parse(start_time), LocalTime.of(0, 0, 0));
            LocalDateTime endDate = LocalDateTime.of(LocalDate.parse(end_time), LocalTime.of(0, 0, 0));
            String date[] = reserve_time.split("\\s");
            String time[] = date[1].split(":");
            LocalDateTime reserveDate = LocalDateTime.of(LocalDate.parse(date[0]), LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0));
            ProgramID programID = new ProgramID();
            programID.setVenueID(venue.getVenueID());
            programID.setReserve_time(reserveDate);
            Program program = new Program();
            program.setProgramID(programID);
            program.setName(name);
            program.setProgramType(type);
            program.setVenue(venue);
            program.setIntroduction(introduction);
            program.setStart_time(startDate);
            program.setEnd_time(endDate);
            program.setCaterer(userID);
            TicketInitInfo ticketInitInfo = new TicketInitInfo();
            ticketInitInfo.setProgram(program);
            ticketInitInfo.setPrice(Double.parseDouble(lowPrice));
            ticketInitInfo.setRaw_num(venue.getRaw_num());
            ticketInitInfo.setCol_num(venue.getCol_num());
            ticketInitInfo.setRaw_price_rule(createRule(rawRule));
            ticketInitInfo.setCol_price_rule(createRule(colRule));
            session.setAttribute("ticketInitInfo", ticketInitInfo);
            jsonObject.put("result", "1;成功");
            return jsonObject.toString();
        } catch (RuleInvalidException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 用于解析rule
     *
     * @param rule 规则
     * @return Map<Integer                               [                               ]                                                               ,                                                                                                                               Double>
     * @throws RuleInvalidException 规则格式不正确
     */
    private Map<Integer[], Double> createRule(String rule) throws RuleInvalidException {
        String needRule = rule.replaceAll("；", ";");
        needRule = needRule.replaceAll("：", ":");
        needRule = needRule.replaceAll("元", "");
        ValidateTask validateTask = new ValidateTask();
        if (needRule.lastIndexOf(";") != needRule.length() - 1) {
            throw new RuleInvalidException();
        }
        needRule = needRule.substring(0, needRule.length() - 1);
        String rules[] = needRule.split(";");
        try {
            Map<Integer[], Double> map = new HashMap<>();
            for (String ruleItem : rules) {
                String item[] = ruleItem.split(":");
                String first = item[0].split("-")[0];
                String last = item[0].split("-")[1];
                validateTask.positiveIntegerDetector(first);
                validateTask.positiveIntegerDetector(last);
                Integer[] needLocation = new Integer[2];
                needLocation[0] = Integer.parseInt(first);
                needLocation[1] = Integer.parseInt(last);
                if (needLocation[1] < needLocation[0]) {
                    throw new RuleInvalidException();
                }
                validateTask.priceDetector(item[1]);
                map.put(needLocation, Double.parseDouble(item[1]));
            }
            return map;
        } catch (Exception e) {
            throw new RuleInvalidException();
        }
    }
}
