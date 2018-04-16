package booker.service;

import booker.model.Program;
import booker.model.Ticket;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.VenueState;
import booker.util.exception.ProgramInvalidException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProgramService {

    /**
     * 新增节目
     *
     * @param program 节目
     * @return boolean
     */
    boolean addOneProgram(Program program);

    /**
     * 修改节目信息节目
     *
     * @param program 节目
     * @return boolean
     */
    boolean modifyProgram(Program program);

    /**
     * 获取一个节目信息
     *
     * @param programID 节目ID
     * @return boolean
     */
    Program getOneProgram(ProgramID programID);

    /**
     * 获取一个节目信息
     *
     * @param localDateTime 时间
     * @param address       地址
     * @param type          节目类型
     * @return boolean
     */
    Program getOneProgram(LocalDateTime localDateTime, String address, String type);

    /**
     * 获取指定节目类型的最大页数
     *
     * @param type     节目类型
     * @param city     城市
     * @param start    开始时间
     * @param end      结束时间
     * @param pageSize 页大小
     * @return int
     */
    int getMaxPage(String type, String city, LocalDateTime start, LocalDateTime end, int pageSize);

    /**
     * 分页获取节目信息
     *
     * @param type      节目类型
     * @param city      城市
     * @param start     开始时间
     * @param end       结束时间
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    List<Program> getPrograms(String type, String city, LocalDateTime start, LocalDateTime end, int pageNow, int page_size);


    /**
     * 获取节目的最新时间
     *
     * @return LocalDate
     */
    LocalDate getLastTime(String programType);

    /**
     * 根据状态获取计划
     *
     * @param programState 场馆状态
     * @return List<Program>
     */
    List<Program> getPlanByState(ProgramState programState);

    /**
     * 根据状态获取计划
     *
     * @param venueID      场馆ID
     * @param programState 场馆状态
     * @return List<Program>
     */
    List<Program> getPlanByState(int venueID, ProgramState programState);

    /**
     * 根据时间获取计划
     *
     * @param venueID 场馆ID
     * @param start   计划开始时间
     * @param end     计划结束时间
     * @return List<Plan>
     */
    List<Program> getPlanByTime(int venueID, LocalDateTime start, LocalDateTime end);

    /**
     * 根据时间获取计划
     *
     * @param venueID 场馆ID
     * @return List<Plan>
     */
    List<Program> getPlan(String venueID);

    /**
     * 获取指定计划的最大页数
     *
     * @param address  节目类型
     * @param pageSize 页大小
     * @return int
     */
    int getPlanMaxPage(String address, int pageSize);

    /**
     * 分页获取节目信息
     *
     * @param address   地址
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    List<Program> getPlans(String address, int pageNow, int page_size);

    /**
     * 根据计划ID修改场馆状态
     *
     * @param programID    节目ID
     * @param programState 场馆状态
     * @return boolean
     */
    boolean updatePlanState(ProgramID programID, ProgramState programState);

    /**
     * 用于判断节目信息是否有效
     *
     * @param ticketID 节目票务ID
     * @return boolean
     */
    boolean isInRangeProgram(TicketID ticketID) throws ProgramInvalidException;

    /**
     * 获取计划的最大页数
     *
     * @param key      关键字
     * @param start    开始时间
     * @param end      结束时间
     * @param pageSize 页大小
     * @return int
     */
    int getSearchPlanMaxPage(String key, LocalDateTime start, LocalDateTime end, int pageSize);

    /**
     * 分页获取节目信息
     *
     * @param key      关键字
     * @param start    开始时间
     * @param end      结束时间
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    List<Program> getPlans(String key, LocalDateTime start, LocalDateTime end, int pageNow, int page_size);

    /**
     * 获取数据库中计划的最开始时间和结束时间
     *
     * @return List<LocalDateTime> 只有两个元素
     */
    List<LocalDateTime> findPlanMaxAndMinTime();

    /**
     * 根据赞助商的ID获取该用户的场馆租用情况
     *
     * @param carter 赞助商ID
     * @return List<ProgramID>
     */
    List<Program> getVenueBook(String carter);

    /**
     * 现场缴费获取节目信息
     *
     * @param key      关键字
     * @param time    开始时间
     * @return List<Program>
     */
    List<Program> searchPrograms(String key, LocalDateTime time);
}
