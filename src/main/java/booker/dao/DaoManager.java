package booker.dao;

import booker.statistics.dao.ManagerStatisticsDao;
import booker.statistics.dao.PublisherStatisticsDao;
import booker.util.helper.ApplicationContextHelper;
import org.springframework.context.ApplicationContext;

/**
 * Created by Byron Dong on 2017/5/10.
 * 方便测试使用
 */
public class DaoManager {

    public final static ExternalBalanceDao externalBalanceDao;

    public final static MemberDao memberDao;

    public final static OrderDao orderDao;

    public final static ProgramDao programDao;

    public final static SettlementDao settlementDao;

    public final static TicketDao ticketDao;

    public final static VenueDao venueDao;

    public final static CouponDao couponDao;

    public final static PublisherStatisticsDao publisherStatisticsDao;

    public final static ManagerStatisticsDao managerStatisticsDao;

    static {
        ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
        externalBalanceDao = applicationContext.getBean(ExternalBalanceDao.class);
        memberDao = applicationContext.getBean(MemberDao.class);
        orderDao = applicationContext.getBean(OrderDao.class);
        programDao = applicationContext.getBean(ProgramDao.class);
        settlementDao = applicationContext.getBean(SettlementDao.class);
        ticketDao = applicationContext.getBean(TicketDao.class);
        venueDao = applicationContext.getBean(VenueDao.class);
        couponDao = applicationContext.getBean(CouponDao.class);
        publisherStatisticsDao = applicationContext.getBean(PublisherStatisticsDao.class);
        managerStatisticsDao = applicationContext.getBean(ManagerStatisticsDao.class);
    }

}
