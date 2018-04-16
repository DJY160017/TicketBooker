package booker.controller;

import booker.model.Program;
import booker.service.ProgramService;
import booker.util.infoCarrier.ProgramShowInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/program")
public class ProgramController {

    private final ProgramService programService;

    @Autowired
    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/show")
    public String program() {
        return "ticket/program";
    }

    /**
     * 【请求】获取节目信息
     */
    @PostMapping(value = "/req_programInitInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetProgramInitInfo(@RequestParam("programType") String programType, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDate localDate = programService.getLastTime(programType);
        LocalDateTime start = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.of(23, 59, 59));
        int maxPage = programService.getMaxPage(programType, "全部", start, end, 8);
        session.setAttribute("maxPage", maxPage);
        session.setAttribute("pageNow", 1);
        session.setAttribute("programType", programType);
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPrograms(programType, "全部", start, end, 1, 8));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", localDate.toString());
        jsonObject.put("programs", programs);
        jsonObject.put("programs_size", programs.size());
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目信息
     */
    @PostMapping(value = "/req_programInfoByPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetProgramByPage(@RequestParam("city") String city, @RequestParam("pageNow") String pageNow,
                               @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                               HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime start = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.of(23, 59, 59));
        String programType = String.valueOf(session.getAttribute("programType"));
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPrograms(programType, city, start, end, Integer.parseInt(pageNow), 8));
        session.setAttribute("pageNow", Integer.parseInt(pageNow));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("programs_size", programs.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目信息,根据条件
     */
    @PostMapping(value = "/req_search", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetProgramByConditions(@RequestParam("city") String city, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                                     HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime start = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.of(23, 59, 59));
        String programType = String.valueOf(session.getAttribute("programType"));
        int maxPage = programService.getMaxPage(programType, city, start, end, 8);
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPrograms(programType, city, start, end, 1, 8));
        session.setAttribute("pageNow", 1);
        session.setAttribute("maxPage", maxPage);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("programs_size", programs.size());
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取单个节目详情
     */
    @PostMapping(value = "/req_getOneProgram", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetOneProgram(@RequestParam("address") String address, @RequestParam("time") String time, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime localDateTime = LocalDateTime.parse(time.replace(' ', 'T'));
        String programType = String.valueOf(session.getAttribute("programType"));
        Program program = programService.getOneProgram(localDateTime, address, programType);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("program", program);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目的最大页数
     */
    @PostMapping(value = "/req_getMaxPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetMaxPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String maxPage = String.valueOf(session.getAttribute("maxPage"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目的当前页数
     */
    @PostMapping(value = "/req_getPageNow", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetPageNow(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String pageNow = String.valueOf(session.getAttribute("pageNow"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNow", pageNow);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取节目类型
     */
    @PostMapping(value = "/req_getProgramType", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetProgramType(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String programType = String.valueOf(session.getAttribute("programType"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programType", programType);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆计划获取
     */
    @PostMapping(value = "/req_getMyVenuePlan", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetMyVenuePlan(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String venueID = String.valueOf(session.getAttribute("venueID"));
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPlan(venueID));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("length", programs.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆计划获取
     */
    @PostMapping(value = "/req_getVenuePlanInit", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetVenuePlanInit(@RequestParam("address") String address, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        int maxPage = programService.getPlanMaxPage(address, 5);
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPlans(address, 1, 5));
        session.setAttribute("plan_max_page", maxPage);
        session.setAttribute("plan_pageNow", 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("program_size", programs.size());
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆分页计划获取
     */
    @PostMapping(value = "/req_getVenuePlanByPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetVenuePlanByPage(@RequestParam("address") String address, @RequestParam("pageNow") String pageNow, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPlans(address, Integer.parseInt(pageNow), 5));
        session.setAttribute("plan_pageNow", Integer.parseInt(pageNow));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("program_size", programs.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】获取计划的最大页数
     */
    @PostMapping(value = "/req_getPlanMaxPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetPlanMaxPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String maxPage = String.valueOf(session.getAttribute("plan_max_page"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取计划的当前页数
     */
    @PostMapping(value = "/req_getPlanPageNow", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetPlanPageNow(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String pageNow = String.valueOf(session.getAttribute("plan_pageNow"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNow", pageNow);
        return jsonObject.toString();
    }

    /**
     * 【请求】场馆计划获取
     */
    @PostMapping(value = "/req_getSearchPlanInit", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetSearchPlanInit(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        List<LocalDateTime> localDateTimes = programService.findPlanMaxAndMinTime();
        int maxPage = programService.getSearchPlanMaxPage("", localDateTimes.get(1), localDateTimes.get(0), 10);
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPlans("", localDateTimes.get(1), localDateTimes.get(0), 1, 10));
        session.setAttribute("search_plan_max_page", maxPage);
        session.setAttribute("search_plan_pageNow", 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("program_size", programs.size());
        jsonObject.put("maxPage", maxPage);
        jsonObject.put("startTime", localDateTimes.get(1).toLocalDate());
        jsonObject.put("endTime", localDateTimes.get(0).toLocalDate());
        return jsonObject.toString();
    }

    /**
     * 【请求】根据条件获取计划
     */
    @PostMapping(value = "/req_searchPlan", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSearchPlan(@RequestParam("key") String key, @RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime start = LocalDateTime.of(LocalDate.parse(startTime), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(endTime), LocalTime.of(0, 0, 0));
        int maxPage = programService.getSearchPlanMaxPage(key, start, end, 10);
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPlans(key, start, end, 1, 10));
        session.setAttribute("search_plan_max_page", maxPage);
        session.setAttribute("search_plan_pageNow", 1);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("program_size", programs.size());
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】分页获取计划
     */
    @PostMapping(value = "/req_getSearchPlaByPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetSearchPlanByPage(@RequestParam("key") String key, @RequestParam("startTime") String startTime,
                                  @RequestParam("endTime") String endTime, @RequestParam("pageNow") String pageNow,
                                  HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        LocalDateTime start = LocalDateTime.of(LocalDate.parse(startTime), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(LocalDate.parse(endTime), LocalTime.of(0, 0, 0));
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getPlans(key, start, end, Integer.parseInt(pageNow), 10));
        session.setAttribute("search_plan_pageNow", Integer.parseInt(pageNow));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取计划的最大页数
     */
    @PostMapping(value = "/req_getSearchPlanMaxPage", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetSearchPlanMaxPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String maxPage = String.valueOf(session.getAttribute("search_plan_max_page"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("maxPage", maxPage);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取计划的当前页数
     */
    @PostMapping(value = "/req_getSearchPlanPageNow", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetSearchPlanPageNow(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String pageNow = String.valueOf(session.getAttribute("search_plan_pageNow"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNow", pageNow);
        return jsonObject.toString();
    }

    /**
     * 【请求】获取赞助商的场馆预定计划
     */
    @PostMapping(value = "/req_getCarterVenueBook", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetCarterVenueBook(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userID = String.valueOf(session.getAttribute("user_mail"));
        List<ProgramShowInfo> programs = getProgramsShowInfo(programService.getVenueBook(userID));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("programs", programs);
        jsonObject.put("programs_size", programs.size());
        return jsonObject.toString();
    }

    /**
     * 过滤不需要的场馆信息
     *
     * @param programs 未设置为null的循环
     * @return 已为null的结果
     */
    private List<ProgramShowInfo> getProgramsShowInfo(List<Program> programs) {
        List<ProgramShowInfo> result = new ArrayList<>();
        for (Program program : programs) {
            result.add(new ProgramShowInfo(program));
        }
        return result;
    }
}
