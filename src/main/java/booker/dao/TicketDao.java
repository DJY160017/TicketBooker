package booker.dao;

import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.TicketState;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketDao {

    /**
     * 批量添加ticket
     *
     * @param tickets ticket list
     * @return boolean
     */
    boolean addTickets(List<Ticket> tickets);

    /**
     * 根据ticketID获取一张票
     *
     * @param ticketID 票ID
     * @return Ticket
     */
    Ticket getOneTicket(TicketID ticketID);

    /**
     * 更新票的信息
     *
     * @param ticket 票
     * @return Ticket
     */
    boolean updateOneTicket(Ticket ticket);

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
     * 获取所有的票
     *
     * @return List<Ticket>
     */
    List<Ticket> getAllTicket();

    List<ProgramID> getAllProgramID();
}
