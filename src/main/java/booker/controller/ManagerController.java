package booker.controller;

import booker.model.Program;
import booker.model.Settlement;
import booker.model.Ticket;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.model.id.SettlementID;
import booker.model.id.TicketID;
import booker.service.*;
import booker.util.enums.state.TicketState;
import booker.util.enums.state.VenueState;
import booker.util.exception.*;
import booker.util.formatter.VenueIDFormatter;
import booker.util.infoCarrier.ManagerAccount;
import booker.util.infoCarrier.ProgramShowInfo;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ticketBookerManager")
public class ManagerController {

    private final VenueService venueService;

    private final ProgramService programService;

    private final ExternalBalanceService externalBalanceService;

    private final SettlementService settlementService;

    private final TicketService ticketService;

    @Autowired
    public ManagerController(VenueService venueService, ProgramService programService, ExternalBalanceService externalBalanceService, SettlementService settlementService, TicketService ticketService) {
        this.venueService = venueService;
        this.programService = programService;
        this.externalBalanceService = externalBalanceService;
        this.settlementService = settlementService;
        this.ticketService = ticketService;
    }

    @GetMapping("/home")
    public String home() {
        return "manager/manager";
    }

    @GetMapping("/ticket/check")
    public String check() {
        return "manager/checkTicket";
    }

    @GetMapping("/on-site/pay")
    public String pay() {
        return "manager/onSitePayment";
    }

    /**
     * 【请求】获取未审核的场馆
     */
    @PostMapping(value = "/req_getUnVerifyVenue", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetUnVerifyVenue(HttpServletRequest request, HttpServletResponse response) {
        List<Venue> venues = venueService.getVenueByState(VenueState.Unapproved);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("venues", venues);
        jsonObject.put("venues_size", venues.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】单个场馆审核
     */
    @PostMapping(value = "/req_verify", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqVerify(@RequestParam("venueID") String venueID, @RequestParam("state") String state,
                     HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            boolean result = venueService.verify(venueID, VenueState.getEnum(URLDecoder.decode(state, "UTF-8")));
            if (result) {
                jsonObject.put("result", "1;审核成功");
            } else {
                jsonObject.put("result", "1;审核失败");
            }
            return jsonObject.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            jsonObject.put("result", "1;审核失败");
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】批量场馆审核通过
     */
    @PostMapping(value = "/req_allPass", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqAllPass(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Integer.class);
        JSONObject jsonObject = new JSONObject();
        try {
            List<Integer> ids = objectMapper.readValue(id, javaType);
            boolean result = venueService.verify(ids);
            jsonObject.put("result", "1;审核成功");
            return jsonObject.toString();
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("result", "1;审核失败");
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】获取未结算记录
     */
    @PostMapping(value = "/req_getUnSettlementRecord", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetUnSettlementRecord(HttpServletRequest request, HttpServletResponse response) {
        List<Settlement> settlements = settlementService.getUnsettledRecord();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("settlements", settlements);
        jsonObject.put("settlements_size", settlements.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】结算记录
     */
    @PostMapping(value = "/req_settle", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSettle(@RequestParam("store_account") String storeAccount, @RequestParam("venue_account") String venueAccount,
                     @RequestParam("settlement_time") String settlementTime, HttpServletRequest request, HttpServletResponse response) {
        LocalDateTime localDateTime = LocalDateTime.parse(settlementTime.replace(' ', 'T'));
        SettlementID settlementID = new SettlementID();
        settlementID.setSettlement_time(localDateTime);
        settlementID.setStoreAccount(storeAccount);
        settlementID.setVenueAccount(venueAccount);
        List<SettlementID> settlements = new ArrayList<>();
        settlements.add(settlementID);
        JSONObject jsonObject = new JSONObject();
        try {
            boolean result = settlementService.settleAccounts(settlements);
            if (result) {
                jsonObject.put("result", "1;结算成功");
            } else {
                jsonObject.put("result", "-1;结算失败");
            }
            return jsonObject.toString();
        } catch (UserNotExistException | BalanceInsufficientException | PasswordInvalidException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】一键结算记录
     */
    @PostMapping(value = "/req_allSettle", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqAllSettle(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            boolean result = settlementService.settleAccounts();
            if (result) {
                jsonObject.put("result", "1;结算成功");
            } else {
                jsonObject.put("result", "-1;结算失败");
            }
            return jsonObject.toString();
        } catch (UserNotExistException | BalanceInsufficientException | PasswordInvalidException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】检票登记
     */
    @PostMapping(value = "/req_checkTicket", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqCheckTicket(@RequestParam("time") String time, @RequestParam("venueID") String venueID,
                          @RequestParam("raw") String raw, @RequestParam("col") String col,
                          HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        try {
            ProgramID programID = new ProgramID();
            String date[] = time.split("\\s");
            String temp_time[] = date[1].split(":");
            LocalDateTime reserveDate = LocalDateTime.of(LocalDate.parse(date[0]), LocalTime.of(Integer.parseInt(temp_time[0]), Integer.parseInt(temp_time[1]), 0));
            programID.setReserve_time(reserveDate);
            programID.setVenueID(VenueIDFormatter.deFormate(venueID));
            TicketID ticketID = new TicketID();
            ticketID.setProgramID(programID);
            ticketID.setRaw_num(Integer.parseInt(raw));
            ticketID.setCol_num(Integer.parseInt(col));
            ticketService.check(ticketID);
            jsonObject.put("result", "1;检票成功");
            return jsonObject.toString();
        } catch (TicketNotExistException | TicketCheckedException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        } catch (Exception e) {
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 【请求】（现场缴费）查找符合条件的节目信息
     */
    @PostMapping(value = "/req_getPrograms", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetPrograms(@RequestParam("time") String time, @RequestParam("key") String key,
                          HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String date[] = time.split("\\s");
        String temp_time[] = date[1].split(":");
        LocalDateTime reserveDate = LocalDateTime.of(LocalDate.parse(date[0]), LocalTime.of(Integer.parseInt(temp_time[0]), Integer.parseInt(temp_time[1]), 0));
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.searchPrograms(key, reserveDate));
        jsonObject.put("programs", programs);
        jsonObject.put("programs_size", programs.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】（现场缴费）获取单个节目详情
     */
    @PostMapping(value = "/req_getChooseInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetChooseInfo(@RequestParam("address") String address, @RequestParam("time") String time, @RequestParam("type") String type,
                            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime localDateTime = LocalDateTime.parse(time.replace(' ', 'T'));
        Program program = programService.getOneProgram(localDateTime, address, type);
        List<Ticket> tickets = ticketService.getProgramTicket(program.getProgramID());
        session.setAttribute("programID", program.getProgramID());
        String[] seatMap = getSeatMap(program.getVenue().getCol_num(), program.getVenue().getRaw_num(), tickets);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seatMap", seatMap);
        return jsonObject.toString();
    }

    /**
     * 【请求】（现场缴费）支付票价
     */
    @PostMapping(value = "/req_pay", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqPay(@RequestParam("seats") String seats, @RequestParam("total_price") String totalPrice,
                  HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        JSONObject jsonObject = new JSONObject();
        try {
            ProgramID programID = (ProgramID) session.getAttribute("programID");
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, String.class);
            List<String> need_seats = objectMapper.readValue(seats, javaType);
            List<TicketID> ticketIDS = new ArrayList<>();
            for (String seat : need_seats) {
                TicketID ticketID = new TicketID();
                ticketID.setProgramID(programID);
                ticketID.setRaw_num(Integer.parseInt(seat.split("排")[0]));
                ticketID.setCol_num(Integer.parseInt(seat.split("排")[1].replace("座", "")));
                ticketIDS.add(ticketID);
            }
            externalBalanceService.onSitePay(ManagerAccount.getAccount(), Double.parseDouble(totalPrice));
            ticketService.updateTicketsState(ticketIDS, TicketState.AlreadyPaid);
            jsonObject.put("result", "1;支付成功");
            return jsonObject.toString();
        } catch (UserNotExistException | IOException e) {
            e.printStackTrace();
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }
    }

    /**
     * 计算座位表
     *
     * @param col     列数量
     * @param raw     行数量
     * @param tickets 所有票
     * @return String[]
     */
    private String[] getSeatMap(int col, int raw, List<Ticket> tickets) {
        String[][] seatMap = new String[raw][col];
        for (Ticket ticket : tickets) {
            if (ticket.getTicketState() == TicketState.PendingReservation) {
                seatMap[ticket.getTicketID().getRaw_num() - 1][ticket.getTicketID().getCol_num() - 1] = "a";
            } else {
                seatMap[ticket.getTicketID().getRaw_num() - 1][ticket.getTicketID().getCol_num() - 1] = "u";
            }
        }

        String[] result = new String[raw];
        for (int i = 0; i < raw; i++) {
            StringBuilder builder = new StringBuilder();
            for (String seat : seatMap[i]) {
                builder.append(seat);
            }
            result[i] = builder.toString();
        }
        return result;
    }

    /**
     * 过滤不需要的场馆信息
     *
     * @param programs 未设置为null的循环
     * @return 已为null的结果
     */
    private List<ProgramShowInfo> getProgramsShowInfo(List<Program> programs) {
        List<ProgramShowInfo> result = new ArrayList<>();
        for (Program program : programs) {
            result.add(new ProgramShowInfo(program));
        }
        return result;
    }
}
