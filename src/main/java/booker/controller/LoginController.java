package booker.controller;

import booker.model.Member;
import booker.service.MemberService;
import booker.util.exception.*;
import booker.util.infoCarrier.ManagerAccount;
import booker.task.CodeTask;
import booker.task.MD5Task;
import booker.task.MailTask;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    /**
     * service对象
     */
    private final MemberService memberService;

    @Autowired
    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/userInfoManage")
    public String userInfo() {
        return "user/userInfo";
    }

    /**
     * 【请求】用户注册
     */
    @PostMapping(value = "/req_register", produces = "text/json;charset=UTF-8")
    public @ResponseBody
    String reqRegister(@RequestParam("userName") String username, @RequestParam("password") String password,
                       @RequestParam("email") String email, @RequestParam("idCode") String idCode,
                       HttpServletRequest request, HttpServletResponse response) {
        boolean result = false;
        Member member = null;
        JSONObject jsonObject = new JSONObject();
        try {
            HttpSession session = request.getSession(false);
            if (session.getAttribute("idCode") == null) {
                jsonObject.put("result", "-1;验证码失效");
                return jsonObject.toString();
            }
            String idCode_compare = String.valueOf(session.getAttribute("idCode"));
            if (idCode_compare != null && idCode_compare.equals(idCode)) {
                member = new Member();
                member.setMail(email);
                member.setPassword(MD5Task.encodeMD5(password));
                member.setCurrentMarks(0);
                member.setAccumulateMarks(0);
                member.setUsername(username);
                member.setCancel(false);
                result = memberService.signUp(member);
            }
            session.setAttribute("user_mail", member != null ? member.getMail() : null);
            session.setAttribute("user_type", "user");
        } catch (UserExistedException | EmailInputException e) {
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }

        if (result) {
            jsonObject.put("result", "1;注册成功");
        } else {
            jsonObject.put("result", "-1;服务器开了一个小差。。请稍后重试");
        }
        return jsonObject.toString();
    }


    /**
     * 【请求】用户登录
     * produces少一点都不行
     */
    @PostMapping(value = "/req_log_in", produces = "text/json;charset=UTF-8;application/json")
    public @ResponseBody
    String reqLogIn(@RequestParam("email") String email, @RequestParam("password") String password,
                    HttpServletRequest request, HttpServletResponse response) {

        boolean result = false;
        JSONObject jsonObject = new JSONObject();
        try {
            result = memberService.login(email, password);
            HttpSession session = request.getSession(false);
            session.setAttribute("user_mail", email);
            if (email.equals(ManagerAccount.getSystemMail())) {
                session.setAttribute("user_type", "admin");
                jsonObject.put("user_type", "admin");
            } else {
                session.setAttribute("user_type", "user");
                jsonObject.put("user_type", "user");
            }
        } catch (UserNotExistException | MemberCancelException | PasswordInvalidException e) {
            jsonObject.put("result", "-1;" + e.getMessage());
            return jsonObject.toString();
        }

        if (result) {
            jsonObject.put("result", "1;登录成功");
        } else {
            jsonObject.put("result", "-1;服务器开了一个小差。。请稍后重试");
        }
        return jsonObject.toString();
    }


    /**
     * 【请求】用户登出
     */
    @PostMapping(value = "/req_log_out", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqLogOut(HttpServletRequest request, HttpServletResponse response) {
        // 限制进入
        HttpSession session = request.getSession(false);
        session.setAttribute("user_mail", null);
        session.setAttribute("user_type", null);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1");
        return jsonObject.toString();
    }

    /**
     * 【请求】发送验证码
     */
    @PostMapping(value = "/req_sendIDCode", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqSendIDCode(@RequestParam("email") String email, HttpServletRequest request, HttpServletResponse response) {
        CodeTask codeTask = new CodeTask();
        MailTask mailTask = new MailTask();
        try {
            String code = codeTask.createCode();
            mailTask.sendMail(email, code);
            HttpSession session = request.getSession(false);
            session.setAttribute("idCode", code);
            //TODO 此处应该开启验证码失效线程
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("result", "1;发送成功");
        return json.toString();
    }

    /**
     * 【请求】获取用户信息
     */
    @PostMapping(value = "/req_getUserInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqGetUserInfo(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        Member member = memberService.getUserInfo(mail);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_name", member.getUsername());
        return jsonObject.toString();
    }

    /**
     * 【请求】修改用户信息
     */
    @PostMapping(value = "/req_ModifyUserInfo", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqModifyUserInfo(@RequestParam("user_name") String user_name, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        Member member = memberService.getUserInfo(mail);
        member.setUsername(user_name);
        memberService.modifyInfo(member);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1;成功");
        return jsonObject.toString();
    }

    /**
     * 【请求】修改用户密码
     */
    @PostMapping(value = "/req_ModifyPassword", produces = "text/json;charset=UTF-8;")
    public @ResponseBody
    String reqModifyPassword(@RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String mail = String.valueOf(session.getAttribute("user_mail"));
        Member member = memberService.getUserInfo(mail);
        member.setPassword(MD5Task.encodeMD5(password));
        memberService.modifyInfo(member);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "1;成功");
        return jsonObject.toString();
    }
}
