package booker.service.impl;

import booker.dao.TicketDao;
import booker.model.Order;
import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.service.TicketService;
import booker.util.enums.state.TicketState;
import booker.util.exception.TicketCheckedException;
import booker.util.exception.TicketNotExistException;
import booker.util.infoCarrier.TicketInitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TicketService")
public class TicketServiceImpl implements TicketService {

    /**
     * dao对象
     */
    private final TicketDao ticketDao;

    /**
     * 行方向加价规则
     */
    private Map<Integer[], Double> raw_price_rule;

    /**
     * 列方向加价规则
     */
    private Map<Integer[], Double> col_price_rule;

    @Autowired
    public TicketServiceImpl(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    /**
     * 批量添加ticket
     *
     * @param ticketInitInfo 票添加的初始信息
     * @return boolean
     */
    @Override
    public boolean addTickets(TicketInitInfo ticketInitInfo) {
        raw_price_rule = ticketInitInfo.getRaw_price_rule();
        col_price_rule = ticketInitInfo.getCol_price_rule();
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i <=ticketInitInfo.getRaw_num(); i++) {
            for (int j = 1; j <=ticketInitInfo.getCol_num(); j++) {
                TicketID ticketID = new TicketID();
                ticketID.setProgramID(ticketInitInfo.getProgram().getProgramID());
                ticketID.setRaw_num(i);
                ticketID.setCol_num(j);
                Ticket ticket = new Ticket();
                ticket.setTicketID(ticketID);
                ticket.setTicketState(TicketState.PendingReservation);
                ticket.setPrice(calculatePrice(i, j, ticketInitInfo.getPrice()));
                tickets.add(ticket);
            }
        }
        return ticketDao.addTickets(tickets);
    }

    /**
     * 根据ticketID获取一张票
     *
     * @param ticketID 票ID
     * @return Ticket
     */
    @Override
    public Ticket getOneTicket(TicketID ticketID) {
        return ticketDao.getOneTicket(ticketID);
    }

    /**
     * 根据节目的最低价票
     *
     * @param programID 节目ID
     * @return double
     */
    @Override
    public double getProgramLowPrice(ProgramID programID) {
        return ticketDao.getProgramLowPrice(programID);
    }

    /**
     * 根据节目的时间，地址，状态获取ticket list
     *
     * @param programID   节目ID
     * @param ticketState 票的状态
     * @return List<Ticket>
     */
    @Override
    public List<Ticket> getProgramTicket(ProgramID programID, TicketState ticketState) {
        return ticketDao.getProgramTicket(programID, ticketState);
    }


    /**
     * 根据节目ID，状态获取ticket list
     *
     * @param programID 节目ID
     * @return List<Ticket>
     */
    @Override
    public List<Ticket> getProgramTicket(ProgramID programID) {
        return ticketDao.getProgramTicket(programID);
    }

    /**
     * 根据ticketID修改状态
     *
     * @param ticketID    票ID
     * @param ticketState 状态
     * @return boolean
     */
    @Override
    public boolean updateTicketState(TicketID ticketID, TicketState ticketState) {
        return ticketDao.updateTicketState(ticketID, ticketState);
    }

    /**
     * 根据ticketID批量修改状态
     *
     * @param ticketIDs   id列表
     * @param ticketState 状态
     * @return boolean
     */
    @Override
    public boolean updateTicketsState(List<TicketID> ticketIDs, TicketState ticketState) {
        return ticketDao.updateTicketsState(ticketIDs, ticketState);
    }

    /**
     * 检票登记
     *
     * @param ticketID 票ID
     * @return 检票结果
     */
    @Override
    public boolean check(TicketID ticketID) throws TicketNotExistException, TicketCheckedException {
        Ticket ticket = ticketDao.getOneTicket(ticketID);
        if (ticket == null) {
            throw new TicketNotExistException();
        }
        if (ticket.isCheck()) {
            throw new TicketCheckedException();
        }
        ticket.setCheck(true);
        ticketDao.updateOneTicket(ticket);
        return true;
    }

    /**
     * 计算三种购票方式的收益
     *
     * @return Map<String       ,       Integer>
     */
    @Override
    public Map<String, Integer> calculateConsume() {
        Map<String, Integer> map = new HashMap<>();
        map.put("选座购买", 0);
        map.put("立即购买", 0);
        map.put("现场缴费", 0);
        List<Ticket> tickets = ticketDao.getAllTicket();
        for (Ticket ticket : tickets) {
            Order order = ticket.getOrder();
            if (order == null) {
                map.put("现场缴费", map.get("现场缴费") + 1);
            }

            if (order != null && order.isAutoTicket()) {
                map.put("立即购买", map.get("立即购买") + 1);
            }

            map.put("选座购买", map.get("选座购买") + 1);
        }
        return map;
    }

    /**
     * 在添加时调用，根据位置和已经初始化的票价规则计算票价
     *
     * @param raw       行
     * @param col       列
     * @param basePrice 底价
     * @return double
     */
    private double calculatePrice(int raw, int col, double basePrice) {
        double price = basePrice;
        for (Integer[] key : raw_price_rule.keySet()) {
            if (key[0] <= raw && key[1] >= raw) {
                price = price + raw_price_rule.get(key);
            }
        }
        for (Integer[] key : col_price_rule.keySet()) {
            if (key[0] <= col && key[1] >= col) {
                price = price + col_price_rule.get(key);
            }
        }
        return price;
    }
}
