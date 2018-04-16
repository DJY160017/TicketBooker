package booker.service;

import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.TicketState;
import booker.util.exception.TicketCheckedException;
import booker.util.exception.TicketNotExistException;
import booker.util.infoCarrier.TicketInitInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TicketService {

    /**
     * 批量添加ticket
     *
     * @param ticketInitInfo 票添加的初始信息
     * @return boolean
     */
    boolean addTickets(TicketInitInfo ticketInitInfo);

    /**
     * 根据ticketID获取一张票
     *
     * @param ticketID 票ID
     * @return Ticket
     */
    Ticket getOneTicket(TicketID ticketID);

    /**
     * 根据节目的最低价票
     *
     * @param programID 节目ID
     * @return double
     */
    double getProgramLowPrice(ProgramID programID);

    /**
     * 根据节目的时间，地址，状态获取ticket list
     *
     * @param programID 节目ID
     * @param ticketState 票的状态
     * @return List<Ticket>
     */
    List<Ticket> getProgramTicket(ProgramID programID, TicketState ticketState);

    /**
     * 根据节目ID，状态获取ticket list
     *
     * @param programID 节目ID
     * @return List<Ticket>
     */
    List<Ticket> getProgramTicket(ProgramID programID);

    /**
     * 根据ticketID修改状态
     *
     * @param ticketID 票ID
     * @param ticketState 状态
     * @return boolean
     */
    boolean updateTicketState(TicketID ticketID, TicketState ticketState);

    /**
     * 根据ticketID批量修改状态
     *
     * @param ticketIDs id列表
     * @param ticketState 状态
     * @return boolean
     */
    boolean updateTicketsState(List<TicketID> ticketIDs, TicketState ticketState);

    /**
     * 检票登记
     *
     * @param ticketID 票ID
     * @return 检票结果
     */
    boolean check(TicketID ticketID) throws TicketNotExistException, TicketCheckedException;

    /**
     * 计算三种购票方式的收益
     *
     * @return Map<String, Integer>
     */
    Map<String, Integer> calculateConsume();
}
