package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.MemberDao;
import booker.dao.ProgramDao;
import booker.dao.VenueDao;
import booker.model.Member;
import booker.model.Program;
import booker.model.Venue;
import booker.model.id.ProgramID;
import booker.util.enums.state.ProgramState;
import booker.util.enums.state.VenueState;
import booker.util.helper.TimeHelper;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class ProgramDaoImplTest {

    private ProgramDao programDao;

    private VenueDao venueDao;

    private MemberDao memberDao;

    private String[] type = {"动漫", "话剧", "体育比赛", "音乐会"};

    private String[] comic = {"阿童木", "火影忍者", "海贼王", "死神", "妖精的尾巴", "一拳超人", "家庭教师", "名侦探柯南", "星游记"};
    private String[] drama = {"大餐", "女学究", "我是最美的公主", "有爱才有家", "黑猫警长之城市猎人", "追忆张国荣", "三只小猪", "三生三世十里桃花", "捕鼠器"};
    private String[] music = {"黄致列《致列爱》", "林俊杰圣所世界巡回演唱会", "CoCo李玟世界巡回演唱会", "Gary曹格城市唱遊格友會",
            "杨宗纬VOCAL巡唱PLUS", "金玟岐全国巡回演唱会", "费玉清演唱会", "张信哲歌时代2音乐会", "周杰伦世界巡回演唱会"};
    private String[] match = {"中超联赛", "女子半程马拉松", "中国10公里锦标赛", "世界职业角斗士娱乐风云争霸赛", "亚足联冠军联赛",
            "俄罗斯世界杯", "WESG全球总决赛", "万箭齐发", "FINA世界跳水系列赛"};

    @Before
    public void setUp() throws Exception {
        programDao = DaoManager.programDao;
        venueDao = DaoManager.venueDao;
        memberDao = DaoManager.memberDao;
    }

    @Test
    public void addOneProgram() throws Exception {
        LocalDate localDate = LocalDate.of(2018, 3, 12);
        for (int i = 0; i < 4; i++) {
            Venue venue = venueDao.getVenue(3);
            localDate = localDate.plusDays(4);
            LocalDateTime start = LocalDateTime.of(localDate.minusDays(3), LocalTime.of(0, 0, 0));
            LocalDateTime end = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
            String programType = "音乐会";
            ProgramID programID = new ProgramID();
            programID.setVenueID(venue.getVenueID());
            programID.setReserve_time(LocalDateTime.of(localDate.minusDays(1), LocalTime.of(19, 0, 0)));
            Program program = new Program();
            program.setProgramID(programID);
            program.setStart_time(start);
            program.setEnd_time(end);
            program.setCaterer("15338595517@163.com");
            program.setIntroduction("十分精彩");
            program.setName(getProgramName(programType));
            program.setProgramType(programType);
            program.setVenue(venue);
            program.setProgramState(ProgramState.AlreadyPassed);
            programDao.addOneProgram(program);
        }
    }

    @Test
    public void addNewProgram() throws Exception {
        List<Venue> venues = venueDao.getVenueByState(VenueState.AlreadyPassed);
        List<Member> members = memberDao.getLevelMembers();
        List<Program> programs = readProgram();
        for (int i = 0; i < 100; i++) {
            Venue venue = venues.get(createRandomKey(venues.size()));
            Member member = members.get(createRandomKey(members.size()));
            Program program = programs.get(createRandomKey(programs.size()));
            LocalDate startDate = randomDate(2018);
            LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
            LocalDateTime end = LocalDateTime.of(startDate.plusDays(3), LocalTime.of(0, 0, 0));
            LocalDateTime reserveTime = LocalDateTime.of(end.toLocalDate().minusDays(1), LocalTime.of(19, 0, 0));
            ProgramID programID = new ProgramID();
            programID.setVenueID(venue.getVenueID());
            programID.setReserve_time(reserveTime);
            Program testProgram = programDao.getOneProgram(programID);
            while (testProgram != null) {
                venue = venues.get(createRandomKey(venues.size()));
                programID.setVenueID(venue.getVenueID());
                testProgram = programDao.getOneProgram(programID);
            }
            program.setProgramID(programID);
            program.setStart_time(start);
            program.setEnd_time(end);
            program.setProgramState(ProgramState.AlreadyPassed);
            program.setCaterer(member.getMail());
            venue.getPrograms().add(program);
            program.setVenue(venue);
            programDao.addOneProgram(program);
        }
    }

    @Test
    public void modifyProgram() throws Exception {
        List<Program> programs = programDao.getPlanByState(ProgramState.AlreadyPassed);
        for (Program program : programs) {
            String name = program.getName();
            program.setName(name.substring(1));
            programDao.modifyProgram(program);
        }
    }

    @Test
    public void getAllProgram() throws Exception {
//        List<Program> programs = programDao.getAllProgram("音乐会");
    }

    @Test
    public void getPrograms() throws Exception {
        LocalDate localDate = programDao.getLastTime("音乐会");
        LocalDateTime start = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
        int maxPage = programDao.getMaxRecords("音乐会", "", start, end);
//        List<Program> programs = programDao.getPrograms("音乐会", start, end);
        System.out.println("-------------------");
    }

    @Test
    public void getPlanByState() throws Exception {
    }

    @Test
    public void getPlanByState1() throws Exception {
    }

    @Test
    public void getPlanByTime() throws Exception {
    }

    @Test
    public void updatePlanState() throws Exception {
    }

    @Test
    public void getOneProgram() {
        LocalDateTime end = LocalDateTime.of(2018, 2, 13, 13, 17, 15);
        ProgramID programID = new ProgramID();
        programID.setVenueID(201);
        programID.setReserve_time(end);
        Program program = programDao.getOneProgram(programID);
        System.out.println("---------------");
    }

    @Test
    public void getLastTime() {
        LocalDate localDate = programDao.getLastTime("音乐会");
        System.out.println("--------------");
    }

    @Test
    public void getMaxRecords() {
        LocalDate localDate = LocalDate.of(2018, 2, 22);
        LocalDateTime start = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
        programDao.getMaxRecords("音乐会", "", start, end);
        System.out.println("-------------------");
    }

    @Test
    public void getPlan() {
    }

    @Test
    public void getPlanMaxRecords() {
    }

    @Test
    public void getPlans() {
    }

    @Test
    public void getSearchPlanMaxRecords() {
    }

    @Test
    public void getPlans1() {
        LocalDateTime start = LocalDateTime.of(2018, 2, 11, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 2, 24, 0, 0, 0);

        List<Program> programs = programDao.getPlans("场馆", start, end, 1, 10);
        System.out.println("----------------'");
    }

    @Test
    public void findPlanMaxAndMinTime() {
        List<LocalDateTime> localDateTimes = programDao.findPlanMaxAndMinTime();
        System.out.println("-------------'");
    }

    private String getProgramName(String type) {
        String result = null;
        switch (type) {
            case "动漫":
                result = comic[createRandomKey(comic.length - 1)];
                break;
            case "话剧":
                result = drama[createRandomKey(drama.length - 1)];
                break;
            case "音乐会":
                result = music[createRandomKey(music.length - 1)];
                break;
            default:
                result = match[createRandomKey(match.length - 1)];
                break;
        }
        return result;
    }

    private Integer createRandomKey(Integer maxVal) {
        Integer v = new Random().nextInt(maxVal);
        if (v <= (Integer) 0) {
            v = 0;
        }
        return v;
    }

    private LocalDate randomDate(int year) {
        Random random = new Random();
        Integer month = random.nextInt(6);
        if (month <= 0) {
            month = 1;
        }
        int monthLength = TimeHelper.getMonthLength(year, month);
        Integer day = random.nextInt(monthLength + 1);
        if (day <= 0) {
            day = 1;
        }
        return LocalDate.of(year, month, day);
    }

    private String formate(int num) {
        String str_num = String.valueOf(num);
        for (int i = str_num.length(); i < 3; i++) {
            str_num = "0" + str_num;
        }
        return str_num;
    }

    private List<Program> readProgram() {
            String type[] = new String[]{"演唱会", "音乐会", "话剧歌剧", "体育赛事", "舞蹈芭蕾", "儿童亲子", "曲艺杂谈", "展览休闲"};
        File file = new File("C:\\Users\\Byron Dong\\Desktop\\other\\program.txt");
        List<Program> result = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.endsWith("|")) {
                    line = line + "无";
                }

                String info[] = line.split("\\|");
                Program program = new Program();
                program.setName(info[0]);
                program.setProgramType(type[Integer.parseInt(info[1])]);
                program.setIntroduction(info[2]);
                result.add(program);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}