package booker.service;

import booker.model.Venue;
import booker.util.enums.state.VenueState;
import booker.util.exception.*;

import java.util.List;
import java.util.Map;

public interface VenueService {

    /**
     * 场馆登录
     *
     * @param venueID  场馆ID
     * @param password 密码
     * @return boolean
     */
    boolean login(String venueID, String password) throws VenueNotExistException, PasswordInvalidException;

    /**
     * 获取场馆信息
     *
     * @param venueID 场馆ID
     * @return Venue
     */
    Venue getVenue(String venueID);

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
     * 场馆申请
     *
     * @param venue 场馆实体
     * @return boolean
     */
    int apply(Venue venue) throws VenueExistedException, PositiveIntegerException, PriceException;

    /**
     * 获取指定节目类型的最大页数
     *
     * @param area 区域
     * @param key 关键词
     * @param pageSize 页大小
     * @return int
     */
    int getMaxPage(String area, String key,int pageSize);

    /**
     * 分页获取场馆信息
     *
     * @param area 区域
     * @param key 关键词
     * @param pageNow   当前页码
     * @param page_size 页大小
     * @return List<Venue>
     */
    List<Venue> getVenues(String area, String key, int pageNow, int page_size);

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
     * @param alreadyPassedID 已通过场馆ID
     * @return boolean
     */
    boolean verify(List<Integer> alreadyPassedID);

    /**
     * 根据场馆ID，修改指定场馆状态
     *
     * @param venueID 单个场馆ID
     * @param venueState 场馆状态
     * @return boolean
     */
    boolean verify(String venueID, VenueState venueState);

    /**
     * 统计场馆各个状态的数量
     *
     * @return Map<String, Integer>结果
     */
    Map<String, Integer> getVenues();
}
