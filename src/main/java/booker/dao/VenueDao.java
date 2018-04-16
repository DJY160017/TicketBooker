package booker.dao;

import booker.model.Venue;
import booker.util.enums.state.VenueState;

import java.util.List;
import java.util.Map;

public interface VenueDao {

    /**
     * 添加场馆实体
     *
     * @param venue 场馆实体
     * @return int
     */
    int addVenue(Venue venue);

    /**
     * 获取场馆实体
     *
     * @param venueID 场馆ID
     * @return Venue
     */
    Venue getVenue(int venueID);

    /**
     * 获取场馆信息
     *
     * @param address 场馆ID
     * @return Venue
     */
    Venue getOneVenue(String address);

    /**
     * 修改场馆信息
     *
     * @param venue 场馆
     * @return boolean
     */
    boolean modifyVenue(Venue venue);

    /**
     * 根据地址查找场馆，判别场馆是否重复
     *
     * @param address 地址
     * @return boolean
     */
    boolean findVenueByAddress(String address);

    /**
     * 根据状态获取场馆
     *
     * @param venueState 状态
     * @return List<Venue>
     */
    List<Venue> getVenueByState(VenueState venueState);

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param venueID 场馆ID
     * @param venueState 状态
     * @return boolean
     */
    boolean updateVenueState(int venueID, VenueState venueState);

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param alreadyPassedID 已通过场馆ID
     * @return boolean
     */
    boolean updateVenuesState(List<Integer> alreadyPassedID);

    /**
     * 获取指定场馆的最大页数
     *
     * @param area 区域
     * @param key 关键词
     * @return int
     */
    int getMaxRecords(String area, String key);

    /**
     * 分页获取节目信息
     *
     * @param area 区域
     * @param key 关键词
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Program>
     */
    List<Venue> getVenues(String area, String key, int pageNow, int page_size);

    /**
     * 统计场馆各个状态的数量
     *
     * @param venueState 场馆状态
     * @return int
     */
    int countVenueState(VenueState venueState);
}
