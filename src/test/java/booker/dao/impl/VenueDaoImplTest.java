package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.VenueDao;
import booker.model.Venue;
import booker.util.enums.state.VenueState;
import booker.task.MD5Task;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class VenueDaoImplTest {

    private VenueDao venueDao;

    private String[] province = {"深圳市", "北京市", "吉林省长春市", "江苏省南京市", "辽宁省大连市", "广东省广州市", "浙江省杭州市", "湖南省长沙市",
            "河北省石家庄市", "新疆维吾尔自治区乌鲁木齐市", "山东省青岛市", "河南省郑州市", "山西省太原市", "江西省南昌市", "安徽省合肥市", "湖北省武汉市", "内蒙古自治区呼和浩特市",
            "海南省海口市", "四川省成都市", "重庆市", "陕西省西安市", "广西省桂林市", "福建省福州市", "天津市", "云南省昆明市", "贵州省贵阳市", "甘肃省兰州市",
            "黑龙江省哈尔滨市", "宁夏省银川市", "青海省西宁市", "上海市", "西藏自治区拉萨市"};

    private String[] road = {"北京路", "南京路", "上海路", "九乡街", "五环", "街心花园", "体育路", "仙林大道", "珠江路", "中山路", "王府井", "玉泉路",
            "广州路", "山西路", "汉中路"};

    private String[] name = {"国立馆", "工人体育馆", "市一宫体育馆", "市大剧场", "市羽毛球馆", "奥体中心"};

    @Before
    public void setUp() throws Exception {
        venueDao = DaoManager.venueDao;
    }

    @Test
    public void addVenue() throws Exception {
        for (int i = 0; i < 100; i++) {
            Venue venue = new Venue();
            venue.setPrice(createPrice());
            venue.setPassword(MD5Task.encodeMD5("qwertyuiop"));
            venue.setName(name[createRandomKey(name.length - 1)]);
            String address = province[createRandomKey(province.length - 1)] + road[createRandomKey(road.length - 1)];
            venue.setAddress(address + String.valueOf(createRandomNum(i, i)) + "号");
            venue.setRaw_num(8);
            venue.setCol_num(7);
            venue.setVenueState(VenueState.Unapproved);
            int venueID = venueDao.addVenue(venue);
            System.out.println(venueID);
        }
    }

    @Test
    public void getVenueByState() throws Exception {
        List<Venue> venues = venueDao.getVenueByState(VenueState.Unapproved);
        for (Venue venue : venues) {
            venue.setPrice(formatPrice(venue.getPrice()));
            venueDao.modifyVenue(venue);
        }
    }

    @Test
    public void updateVenueState() throws Exception {

        venueDao.updateVenueState(100, VenueState.AlreadyPassed);
    }

    @Test
    public void getVenue() {
    }

    @Test
    public void modifyVenue() {
    }

    @Test
    public void findVenueByAddress() {
    }

    @Test
    public void updateVenuesState() {
    }

    @Test
    public void getMaxRecords() {
    }

    @Test
    public void getVenues() {
        List<Venue> venues = venueDao.getVenues("", "", 1, 10);
        System.out.println("--------------------");
    }

    private double formatPrice(double price) {
        String str_price = String.valueOf(price);
        str_price = str_price.substring(0, 2);
        str_price = str_price + "000";
        return Double.parseDouble(str_price);
    }

    private Integer createRandomKey(Integer maxVal) {
        Integer v = new Random().nextInt(maxVal);
        if (v <= (Integer) 0) {
            v = 0;
        }
        return v;
    }

    private Integer createRandomNum(Integer maxVal, int backup) {
        Integer v = new Random().nextInt(maxVal + 100);
        if (v <= (Integer) 0) {
            v = 100 + backup;
        }
        return v;
    }

    public int createPrice() {
        int result = 0;
        while (result < 10000) {
            result = (int) (Math.random() * 100000);
        }
        return result;
    }
}