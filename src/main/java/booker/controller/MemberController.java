package booker.controller;

import booker.model.Coupon;
import booker.service.CouponService;
import booker.service.MemberService;
import booker.util.exception.IntegralInsufficientException;
import booker.util.exception.PositiveIntegerException;
import booker.util.formatter.NumberFormatter;
import booker.task.ValidateTask;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final CouponService couponService;

    @Autowired
    public MemberController(MemberService memberService, CouponService couponService) {
        this.memberService = memberService;
        this.couponService = couponService;
    }

    @GetMapping("/center")
    public String memberCenter() {
        return "user/memberCenter";
    }

    /**
     * 【请求】发送会员数据
     */
    @PostMapping(value = "/req_memberInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqMemberInfo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        JSONObject jsonObject = new JSONObject();
        String current_level = memberService.getMemberLevel(userMail);
        String next_level = String.valueOf(Integer.parseInt(current_level) + 1);
        int current_level_marks = memberService.getCurrentMarks(userMail);
        int accumulate_level_marks = memberService.getAccumulateMarks(userMail);
        int need_next_marks = memberService.getNeedNextLevelMarks(userMail);
        String discount = NumberFormatter.decimaFormat(memberService.getDiscount(userMail) * 10, 1);
        double temp_percent = (double) accumulate_level_marks / (double) (accumulate_level_marks + need_next_marks);
        String percent = NumberFormatter.progressFormat(NumberFormatter.rounding(temp_percent, 2));
        jsonObject.put("current_level", "Lv." + current_level);
        jsonObject.put("next_level", "Lv." + next_level);
        jsonObject.put("current_level_marks", current_level_marks);
        jsonObject.put("accumulate_level_marks", accumulate_level_marks);
        jsonObject.put("need_next_level_marks", need_next_marks);
        jsonObject.put("current_level_discount", discount);
        jsonObject.put("percent", percent);
        return jsonObject.toString();
    }

    /**
     * 【请求】发送会员数据
     */
    @PostMapping(value = "/req_cancelMember", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqCancelMember(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        memberService.cancelMemberQualification(userMail);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1;取消成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】获取优惠卷
     */
    @PostMapping(value = "/req_getCoupon", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetCoupon(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        List<Coupon> couponList = couponService.getCoupons(userMail);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("coupons", couponList);
        jsonObject.put("coupons_size", couponList.size());
        return jsonObject.toString();
    }

    /**
     * 【请求】兑换优惠卷
     */
    @PostMapping(value = "/req_exchange", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqExchange(@RequestParam("mark") String mark, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userMail = String.valueOf(session.getAttribute("user_mail"));
        JSONObject jsonObject = new JSONObject();
        try {
            ValidateTask validateTask = new ValidateTask();
            validateTask.markDetector(mark);
            int new_mark = memberService.exchange(userMail, Integer.parseInt(mark));
            jsonObject.put("result", "1");
            jsonObject.put("price", Integer.parseInt(mark) / 100.0);
            jsonObject.put("new_mark", new_mark);
            return jsonObject.toString();
        } catch (IntegralInsufficientException | PositiveIntegerException e) {
            e.printStackTrace();
            jsonObject.put("result", e.getMessage());
            return jsonObject.toString();
        }
    }
}
