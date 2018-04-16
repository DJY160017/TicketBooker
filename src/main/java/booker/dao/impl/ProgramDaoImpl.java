package booker.dao.impl;

import booker.dao.ProgramDao;
import booker.model.Program;
import booker.model.id.ProgramID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.VenueState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProgramDaoImpl implements ProgramDao {

    /**
     * session工厂对象
     */
    private final SessionFactory sessionFactory;

    @Autowired
    public ProgramDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 获取节目
     *
     * @param programID 节目ID
     * @return boolean
     */
    @Override
    public Program getOneProgram(ProgramID programID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Program program = session.get(Program.class, programID);
        transaction.commit();
        session.close();
        return program;
    }

    /**
     * 获取一个节目信息
     *
     * @param localDateTime 时间
     * @param address       地址
     * @param type          节目类型
     * @return Program
     */
    @Override
    public Program getOneProgram(LocalDateTime localDateTime, String address, String type) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programID.reserve_time=:time and p.venue.address=:address and p.programType=:programType and p.programState=:state";
        Query query = session.createQuery(hql);
        query.setParameter("time", localDateTime);
        query.setParameter("state", ProgramState.AlreadyPassed);
        query.setParameter("address", address);
        query.setParameter("programType", type);
        Program program = (Program) query.list().get(0);
        transaction.commit();
        session.close();
        return program;
    }

    /**
     * 新增节目
     *
     * @param program 节目
     * @return boolean
     */
    @Override
    public boolean addOneProgram(Program program) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(program);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 修改节目信息节目
     *
     * @param program 节目
     * @return boolean
     */
    @Override
    public boolean modifyProgram(Program program) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(program);
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 获取指定节目类型的最大页数
     *
     * @param type 节目类型
     * @return int
     */
    @Override
    public int getMaxRecords(String type, String city, LocalDateTime start, LocalDateTime end) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(p) from Program p where p.programType=:programType and p.programState=:programState and p.programID.reserve_time<=:endTime " +
                "and p.programID.reserve_time>=:startTime and p.venue.address like :city";
        Query query = session.createQuery(hql);
        query.setParameter("programType", type);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("endTime", end);
        query.setParameter("startTime", start);
        query.setParameter("city", "%" + city + "%");
        Long result = (Long) query.list().get(0);
        transaction.commit();
        session.close();
        return result.intValue();
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programType=:programType and p.programState=:programState and p.programID.reserve_time<=:endTime " +
                "and p.programID.reserve_time>=:startTime and p.venue.address like :city";
        Query query = session.createQuery(hql);
        query.setParameter("programType", type);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("endTime", end);
        query.setParameter("startTime", start);
        query.setParameter("city", "%" + city + "%");
        query.setFirstResult((pageNow - 1) * page_size);
        query.setMaxResults(page_size);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取节目的最新时间
     *
     * @return LocalDate
     */
    @Override
    public LocalDate getLastTime(String programType) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select max(p.programID.reserve_time) from Program p where p.programType=:type";
        Query query = session.createQuery(hql);
        query.setParameter("type",programType);
        LocalDateTime localDateTime = (LocalDateTime) query.list().get(0);
        transaction.commit();
        session.close();
        return localDateTime.toLocalDate();
    }

    /**
     * 根据状态获取计划
     *
     * @param programState 场馆状态
     * @return List<Program>
     */
    @Override
    public List<Program> getPlanByState(ProgramState programState) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program where programState=:programState";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program where programID.venueID=:venueID and programState=:programState";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据时间获取计划
     *
     * @param venueID 场馆ID
     * @param start   计划开始时间
     * @param end     计划结束时间
     * @return List<Program>
     */
    @Override
    public List<Program> getPlanByTime(int venueID, LocalDateTime start, LocalDateTime end) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programID.venueID=:venueID and p.programState=:programState and p.programID.reserve_time<=:endTime " +
                "and p.programID.reserve_time>=:startTime";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("endTime", end);
        query.setParameter("startTime", start);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 根据时间获取计划
     *
     * @param venueID 场馆ID
     * @return List<Plan>
     */
    @Override
    public List<Program> getPlan(int venueID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programID.venueID=:venueID and p.programState=:programState";
        Query query = session.createQuery(hql);
        query.setParameter("venueID", venueID);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取指定计划的最大页数
     *
     * @param address 节目类型
     * @return int
     */
    @Override
    public int getPlanMaxRecords(String address) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(p) from Program p where p.programState=:programState and p.venue.address =:address";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("address", address);
        Long result = (Long) query.list().get(0);
        transaction.commit();
        session.close();
        return result.intValue();
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programState=:programState and p.venue.address =:address order by p.start_time asc";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("address", address);
        query.setFirstResult((pageNow - 1) * page_size);
        query.setMaxResults(page_size);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取指定计划的最大页数
     *
     * @param key   关键字
     * @param start 开始时间
     * @param end   结束时间
     * @return int
     */
    @Override
    public int getSearchPlanMaxRecords(String key, LocalDateTime start, LocalDateTime end) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select count(p) from Program p where p.programState=:programState " +
                "and p.start_time>=:startTime and p.end_time<=:endTime and p.programID in " +
                "(select p1.programID from Program p1 where p1.name like:programName or p1.venue.name like:name or p1.venue.address like:address)";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("startTime", start);
        query.setParameter("endTime", end);
        query.setParameter("programName", "%" + key + "%");
        query.setParameter("name", "%" + key + "%");
        query.setParameter("address", "%" + key + "%");
        Long result = (Long) query.list().get(0);
        transaction.commit();
        session.close();
        return result.intValue();
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programState=:programState and p.start_time>=:startTime and p.end_time<=:endTime and p.programID in " +
                "(select p1.programID from Program p1 where p1.name like:programName or p1.venue.name like:name or p1.venue.address like:address)";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("startTime", start);
        query.setParameter("endTime", end);
        query.setParameter("programName", "%" + key + "%");
        query.setParameter("name", "%" + key + "%");
        query.setParameter("address", "%" + key + "%");
        query.setFirstResult((pageNow - 1) * page_size);
        query.setMaxResults(page_size);
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }

    /**
     * 获取数据库中计划的最开始时间和结束时间
     *
     * @return List<LocalDateTime> 只有两个元素
     */
    @Override
    public List<LocalDateTime> findPlanMaxAndMinTime() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "select max(p.end_time), min(p.start_time) from Program p where p.programState=:programState";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        List list = query.getResultList();
        Object[] objects = (Object[]) list.get(0);
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        localDateTimes.add((LocalDateTime) objects[0]);
        localDateTimes.add((LocalDateTime) objects[1]);
        transaction.commit();
        session.close();
        return localDateTimes;
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "update Program p set p.programState=:programState where p.programID=:programID";
        Query query = session.createQuery(hql);
        query.setParameter("programState", programState);
        query.setParameter("programID", programID);
        query.executeUpdate();
        transaction.commit();
        session.close();
        return true;
    }

    /**
     * 根据赞助商的ID获取该用户的场馆租用情况
     *
     * @param carter 赞助商ID
     * @return List<ProgramID>
     */
    @Override
    public List<Program> getVenueBook(String carter) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.caterer=:carter";
        Query query = session.createQuery(hql);
        query.setParameter("carter", carter);
        List<Program> programs = query.list();
        transaction.commit();
        session.close();
        return programs;
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String hql = "from Program p where p.programState=:programState and p.programID.reserve_time=:time and p.programID in " +
                "(select p1.programID from Program p1 where p1.name like:programName or p1.venue.address like:address)";
        Query query = session.createQuery(hql);
        query.setParameter("programState", ProgramState.AlreadyPassed);
        query.setParameter("time", time);
        query.setParameter("programName", "%" + key + "%");
        query.setParameter("address", "%" + key + "%");
        List<Program> result = query.list();
        transaction.commit();
        session.close();
        return result;
    }
}
