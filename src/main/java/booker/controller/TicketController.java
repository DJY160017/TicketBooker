package booker.controller;

import booker.model.Program;
import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.service.ProgramService;
import booker.service.TicketService;
import booker.util.enums.state.TicketState;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

    private final ProgramService programService;

    @Autowired
    public TicketController(TicketService ticketService, ProgramService programService) {
        this.ticketService = ticketService;
        this.programService = programService;
    }

    @GetMapping("/choose")
    public String choose() {
        return "ticket/ticketChoose";
    }

    /**
     * 【请求】获取单个节目详情
     */
    @PostMapping(value = "/req_getChooseInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetChooseInfo(@RequestParam("address") String address, @RequestParam("time") String time, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime localDateTime = LocalDateTime.parse(time.replace(' ', 'T'));
        String programType = String.valueOf(session.getAttribute("programType"));
        Program program = programService.getOneProgram(localDateTime, address, programType);
        List<Ticket> tickets = ticketService.getProgramTicket(program.getProgramID());
        double low_price = ticketService.getProgramLowPrice(program.getProgramID());
        session.setAttribute("low_price", low_price);
        session.setAttribute("programID", program.getProgramID());
        String[] seatMap = getSeatMap(program.getVenue().getCol_num(), program.getVenue().getRaw_num(), tickets);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seatMap", seatMap);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取单个ticket
     */
    @PostMapping(value = "/req_getOneTicket", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetOneTicket(@RequestParam("col") String col, @RequestParam("raw") String raw, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        ProgramID programID = (ProgramID) session.getAttribute("programID");
        TicketID ticketID = new TicketID();
        ticketID.setProgramID(programID);
        ticketID.setCol_num(Integer.parseInt(col));
        ticketID.setRaw_num(Integer.parseInt(raw));
        Ticket ticket = ticketService.getOneTicket(ticketID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ticket", ticket);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取当前节目的最低价
     */
    @PostMapping(value = "/req_getLowPrice", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetLowPrice(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        double price = (double) session.getAttribute("low_price");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("low_price", price);
        return jsonObject.toString();
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
}
