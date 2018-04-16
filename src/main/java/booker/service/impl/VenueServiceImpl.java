package booker.service.impl;

import booker.dao.VenueDao;
import booker.model.Venue;
import booker.service.VenueService;
import booker.task.MD5Task;
import booker.task.ValidateTask;
import booker.util.enums.state.VenueState;
import booker.util.exception.*;
import booker.util.formatter.VenueIDFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("VenueService")
public class VenueServiceImpl implements VenueService {

    /**
     * dao对象
     */
    private final VenueDao venueDao;

    @Autowired
    public VenueServiceImpl(VenueDao venueDao) {
        this.venueDao = venueDao;
    }

    /**
     * 场馆登录
     *
     * @param venueID  场馆ID
     * @param password 密码
     * @return boolean
     */
    @Override
    public boolean login(String venueID, String password) throws VenueNotExistException, PasswordInvalidException {
        Venue venue = venueDao.getVenue(VenueIDFormatter.deFormate(venueID));
        if (venue == null) {
            throw new VenueNotExistException(venueID);
        }

        if (!venue.getPassword().equals(MD5Task.encodeMD5(password))) {
            throw new PasswordInvalidException();
        }
        return true;
    }

    /**
     * 获取场馆信息
     *
     * @param venueID 场馆ID
     * @return boolean
     */
    @Override
    public Venue getVenue(String venueID) {
        return venueDao.getVenue(VenueIDFormatter.deFormate(venueID));
    }

    /**
     * 获取场馆信息
     *
     * @param address 场馆ID
     * @return Venue
     */
    @Override
    public Venue getOneVenue(String address) {
        return venueDao.getOneVenue(address);
    }

    /**
     * 修改场馆信息
     *
     * @param venue 场馆
     * @return boolean
     */
    @Override
    public boolean modifyVenue(Venue venue) {
        return venueDao.modifyVenue(venue);
    }

    /**
     * 场馆申请
     *
     * @param venue 场馆实体
     * @return boolean
     */
    @Override
    public int apply(Venue venue) throws VenueExistedException, PositiveIntegerException, PriceException {
        boolean isExisted = venueDao.findVenueByAddress(venue.getAddress());
        if (isExisted) {
            throw new VenueExistedException();
        }
        ValidateTask validateTask = new ValidateTask();
        validateTask.positiveIntegerDetector(String.valueOf(venue.getRaw_num()));
        validateTask.positiveIntegerDetector(String.valueOf(venue.getCol_num()));
        validateTask.priceDetector(String.valueOf(venue.getPrice()));
        return venueDao.addVenue(venue);
    }

    /**
     * 获取指定节目类型的最大页数
     *
     * @param area     区域
     * @param key      关键词
     * @param pageSize 页大小
     * @return int
     */
    @Override
    public int getMaxPage(String area, String key, int pageSize) {
        String needArea = area;
        if (area.equals("全部")) {
            needArea = "";
        }
        int result = venueDao.getMaxRecords(needArea, key);
        if (result == 0) {
            return result;
        }
        return ((result - 1) / pageSize) + 1;
    }

    /**
     * 分页获取场馆信息
     *
     * @param area      区域
     * @param key       关键词
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Venue>
     */
    @Override
    public List<Venue> getVenues(String area, String key, int pageNow, int page_size) {
        String needArea = area;
        if (area.equals("全部")) {
            needArea = "";
        }
        return venueDao.getVenues(needArea, key, pageNow, page_size);
    }

    /**
     * 根据状态获取场馆
     *
     * @param venueState 状态
     * @return List<Venue>
     */
    @Override
    public List<Venue> getVenueByState(VenueState venueState) {
        return venueDao.getVenueByState(venueState);
    }

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param alreadyPassedID 已通过场馆ID
     * @return boolean
     */
    @Override
    public boolean verify(List<Integer> alreadyPassedID) {
        return venueDao.updateVenuesState(alreadyPassedID);
    }

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param venueID    单个场馆ID
     * @param venueState 场馆状态
     * @return boolean
     */
    @Override
    public boolean verify(String venueID, VenueState venueState) {
        return venueDao.updateVenueState(VenueIDFormatter.deFormate(venueID), venueState);
    }

    /**
     * 统计场馆各个状态的数量
     *
     * @return Map<String   ,       Integer>结果
     */
    @Override
    public Map<String, Integer> getVenues() {
        Map<String, Integer> map = new HashMap<>();
        map.put("已注册场馆", venueDao.countVenueState(VenueState.AlreadyPassed));
        map.put("待审核场馆", venueDao.countVenueState(VenueState.Unapproved));
        map.put("未通过场馆", venueDao.countVenueState(VenueState.NotThrough));
        return map;
    }
}
