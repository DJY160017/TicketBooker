package booker.service.impl;

import booker.dao.ProgramDao;
import booker.model.Program;
import booker.model.id.ProgramID;
import booker.model.id.TicketID;
import booker.service.ProgramService;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.VenueState;
import booker.util.exception.ProgramInvalidException;
import booker.util.formatter.VenueIDFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service("ProgramService")
public class ProgramServiceImpl implements ProgramService {

    /**
     * dao对象
     */
    private final ProgramDao programDao;

    @Autowired
    public ProgramServiceImpl(ProgramDao programDao) {
        this.programDao = programDao;
    }

    /**
     * 新增节目
     *
     * @param program 节目
     * @return boolean
     */
    @Override
    public boolean addOneProgram(Program program) {
        return programDao.addOneProgram(program);
    }

    /**
     * 修改节目信息节目
     *
     * @param program 节目
     * @return boolean
     */
    @Override
    public boolean modifyProgram(Program program) {
        return programDao.modifyProgram(program);
    }

    /**
     * 获取一个节目信息
     *
     * @param programID 节目ID
     * @return boolean
     */
    @Override
    public Program getOneProgram(ProgramID programID) {
        return programDao.getOneProgram(programID);
    }

    /**
     * 获取一个节目信息
     *
     * @param localDateTime 时间
     * @param address       地址
     * @param type          节目类型
     * @return boolean
     */
    @Override
    public Program getOneProgram(LocalDateTime localDateTime, String address, String type) {
        return programDao.getOneProgram(localDateTime, address, type);
    }


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
    @Override
    public int getMaxPage(String type, String city, LocalDateTime start, LocalDateTime end, int pageSize) {
        String real_city = city;
        if (city.equals("全部")) {
            real_city = "";
        }
        int result = programDao.getMaxRecords(type, real_city, start, end);
        return calculateMaxPage(result, pageSize);
    }

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
    @Override
    public List<Program> getPrograms(String type, String city, LocalDateTime start, LocalDateTime end, int pageNow, int page_size) {
        String real_city = city;
        if (city.equals("全部")) {
            real_city = "";
        }
        return programDao.getPrograms(type, real_city, start, end, pageNow, page_size);
    }

    /**
     * 获取节目的最新时间
     *
     * @return LocalDate
     */
    @Override
    public LocalDate getLastTime(String programType) {
        return programDao.getLastTime(programType);
    }

    /**
     * 根据状态获取计划
     *
     * @param programState 场馆状态
     * @return List<Program>
     */
    @Override
    public List<Program> getPlanByState(ProgramState programState) {
        return programDao.getPlanByState(programState);
    }

    /**
     * 根据状态获取计划
     *
     * @param venueID      场馆ID
     * @param programState 场馆状态
     * @return List<Program>
     */
    @Override
    public List<Program> getPlanByState(int venueID, ProgramState programState) {
        return programDao.getPlanByState(venueID, programState);
    }

    /**
     * 根据时间获取计划
     *
     * @param venueID 场馆ID
     * @param start   计划开始时间
     * @param end     计划结束时间
     * @return List<Plan>
     */
    @Override
    public List<Program> getPlanByTime(int venueID, LocalDateTime start, LocalDateTime end) {
        return programDao.getPlanByTime(venueID, start, end);
    }

    /**
     * 根据时间获取计划
     *
     * @param venueID 场馆ID
     * @return List<Plan>
     */
    @Override
    public List<Program> getPlan(String venueID) {
        int vid = VenueIDFormatter.deFormate(venueID);
        return programDao.getPlan(vid);
    }

    /**
     * 获取指定计划的最大页数
     *
     * @param address  节目类型
     * @param pageSize 页大小
     * @return int
     */
    @Override
    public int getPlanMaxPage(String address, int pageSize) {
        int result = programDao.getPlanMaxRecords(address);
        return calculateMaxPage(result, pageSize);
    }

    /**
     * 分页获取节目信息
     *
     * @param address   地址
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    @Override
    public List<Program> getPlans(String address, int pageNow, int page_size) {
        return programDao.getPlans(address, pageNow, page_size);
    }

    /**
     * 根据计划ID修改场馆状态
     *
     * @param programID    节目ID
     * @param programState 场馆状态
     * @return boolean
     */
    @Override
    public boolean updatePlanState(ProgramID programID, ProgramState programState) {
        return programDao.updatePlanState(programID, programState);
    }

    /**
     * 用于判断节目信息是否有效
     *
     * @param ticketID 节目票务ID
     * @return boolean
     */
    @Override
    public boolean isInRangeProgram(TicketID ticketID) throws ProgramInvalidException {
        Program program = programDao.getOneProgram(ticketID.getProgramID());
        if (program.getProgramState() == ProgramState.Invalid) {
            throw new ProgramInvalidException();
        }
        return true;
    }

    /**
     * 获取计划的最大页数
     *
     * @param key      关键字
     * @param start    开始时间
     * @param end      结束时间
     * @param pageSize 页大小
     * @return int
     */
    @Override
    public int getSearchPlanMaxPage(String key, LocalDateTime start, LocalDateTime end, int pageSize) {
        int result = programDao.getSearchPlanMaxRecords(key, start, end);
        return calculateMaxPage(result, pageSize);
    }

    /**
     * 分页获取节目信息
     *
     * @param key       关键字
     * @param start     开始时间
     * @param end       结束时间
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    @Override
    public List<Program> getPlans(String key, LocalDateTime start, LocalDateTime end, int pageNow, int page_size) {
        return programDao.getPlans(key, start, end, pageNow, page_size);
    }

    /**
     * 获取数据库中计划的最开始时间和结束时间
     *
     * @return List<LocalDateTime> 只有两个元素
     */
    @Override
    public List<LocalDateTime> findPlanMaxAndMinTime() {
        return programDao.findPlanMaxAndMinTime();
    }

    /**
     * 根据赞助商的ID获取该用户的场馆租用情况
     *
     * @param carter 赞助商ID
     * @return List<ProgramID>
     */
    @Override
    public List<Program> getVenueBook(String carter) {
        return programDao.getVenueBook(carter);
    }

    /**
     * 现场缴费获取节目信息
     *
     * @param key  关键字
     * @param time 开始时间
     * @return List<Program>
     */
    @Override
    public List<Program> searchPrograms(String key, LocalDateTime time) {
        return programDao.searchPrograms(key, time);
    }

    /**
     * 根据页大小和数据库的最大记录数，计算页数
     *
     * @param maxRecords 最大记录数
     * @param pageSize   页大小
     * @return 页数
     */
    private int calculateMaxPage(int maxRecords, int pageSize) {
        if (maxRecords == 0) {
            return maxRecords;
        }
        return ((maxRecords - 1) / pageSize) + 1;
    }
}
