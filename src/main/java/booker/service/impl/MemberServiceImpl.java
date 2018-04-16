package booker.service.impl;

import booker.dao.MemberDao;
import booker.model.Coupon;
import booker.model.Member;
import booker.model.id.CouponID;
import booker.service.CouponService;
import booker.service.MemberService;
import booker.task.MD5Task;
import booker.task.ValidateTask;
import booker.util.exception.*;
import booker.util.helper.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("MemberService")
public class MemberServiceImpl implements MemberService {

    /**
     * dao对象
     */
    private final MemberDao memberDao;

    private final CouponService couponService;

    /**
     * 会员等级和折扣的映射
     */
    private Map<String, Double> levelToDiscount;

    /**
     * 会员积分和等级的映射
     */
    private Map<Integer, String> marksToLevel;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, CouponService couponService) {
        this.memberDao = memberDao;
        init();
        this.couponService = couponService;
    }

    /**
     * 会员登录
     *
     * @param mail     邮箱
     * @param password 密码
     * @return boolean 是否成功登录
     */
    @Override
    public boolean login(String mail, String password) throws UserNotExistException, MemberCancelException, PasswordInvalidException {
        Member member = memberDao.get(mail);
        if (member == null) {
            throw new UserNotExistException(mail);
        }

        if (member.isCancel()) {
            throw new MemberCancelException(mail);
        }

        String encodePassword = MD5Task.encodeMD5(password);
        if (!encodePassword.equals(member.getPassword())) {
            throw new PasswordInvalidException();
        }
        return true;
    }

    /**
     * 会员注册
     *
     * @param member 会员信息实体
     * @return boolean 是否成功注册
     */
    @Override
    public boolean signUp(Member member) throws UserExistedException, EmailInputException {
        Member member_temp = memberDao.get(member.getMail());
        if (member_temp != null) {
            throw new UserExistedException(member.getMail());
        }
        ValidateTask validateTask = new ValidateTask();
        validateTask.emailDetector(member.getMail());
        member.setPassword(MD5Task.encodeMD5(member.getPassword()));
        return memberDao.add(member);
    }

    /**
     * 修改会员信息（会员名，积分信息等）
     *
     * @param member 会员信息实体
     * @return boolean  是否成功修改信息
     */
    @Override
    public boolean modifyInfo(Member member) {
        return memberDao.update(member);
    }

    /**
     * 获取会员信息（会员名，积分信息等）
     *
     * @param mail 邮箱
     * @return Member 用户信息实体
     */
    @Override
    public Member getUserInfo(String mail) {
        return memberDao.get(mail);
    }

    /**
     * 取消指定用户的会员资格
     *
     * @param mail 邮箱
     * @return boolean
     */
    @Override
    public boolean cancelMemberQualification(String mail) {
        return memberDao.cancelMember(mail);
    }

    /**
     * 获取会员等级
     *
     * @param mail 邮箱
     * @return 会员等级
     */
    @Override
    public String getMemberLevel(String mail) {
        Member member = memberDao.get(mail);
        return calculateLevel(member.getAccumulateMarks());
    }

    /**
     * 获取会员等级折扣
     *
     * @param mail 邮箱
     * @return double 折扣
     */
    @Override
    public double getDiscount(String mail) {
        String level = getMemberLevel(mail);
        return levelToDiscount.get(level);
    }

    /**
     * 获取当前会员的积分信息
     *
     * @param mail 邮箱
     * @return int 当前等级积分
     */
    @Override
    public int getCurrentMarks(String mail) {
        Member member = memberDao.get(mail);
        return member.getCurrentMarks();
    }

    /**
     * 获取当前会员的积分信息
     *
     * @param mail 邮箱
     * @return int 累计等级积分
     */
    @Override
    public int getAccumulateMarks(String mail) {
        Member member = memberDao.get(mail);
        return member.getAccumulateMarks();
    }

    /**
     * 获取下一等级会员的需要的积分
     *
     * @param mail 邮箱
     * @return int 需要的积分
     */
    @Override
    public int getNeedNextLevelMarks(String mail) {
        Member member = memberDao.get(mail);
        String level = getMemberLevel(mail);
        String next_level = String.valueOf(Integer.parseInt(level) + 1);
        for (Integer integer : marksToLevel.keySet()) {
            if (marksToLevel.get(integer).equals(next_level)) {
                return integer - member.getAccumulateMarks();
            }
        }
        return 0;
    }

    /**
     * 增加用户积分
     *
     * @param mail  邮箱
     * @param marks 积分
     * @return boolean 是否成功增加积分
     */
    @Override
    public boolean addMarks(String mail, int marks) {
        Member member = memberDao.get(mail);
        member.setCurrentMarks(member.getCurrentMarks() + marks);
        member.setAccumulateMarks(member.getAccumulateMarks() + marks);
        memberDao.update(member);
        return true;
    }

    /**
     * 积分兑换优惠卷(每100兑换1元)
     *
     * @param mail  邮箱
     * @param marks 需要兑换的积分
     * @return 优惠卷卷额
     * @throws IntegralInsufficientException 积分不足
     */
    @Override
    public int exchange(String mail, int marks) throws IntegralInsufficientException {
        Member member = memberDao.get(mail);
        if (marks > member.getCurrentMarks()) {
            throw new IntegralInsufficientException();
        }
        int result = member.getCurrentMarks() - marks;
        member.setCurrentMarks(result);
        memberDao.update(member);
        Coupon coupon = new Coupon();
        CouponID couponID = new CouponID();
        couponID.setUserID(mail);
        couponID.setTime(LocalDateTime.now());
        coupon.setCouponID(couponID);
        coupon.setPrice(marks / 100.0);
        couponService.addCoupon(coupon);
        return result;
    }

    /**
     * 统计获取系统总注册人数
     *
     * @return 总人数
     */
    @Override
    public int getSignUpUserNum() {
        return memberDao.getSignUpUserNum();
    }

    /**
     * 统计获取每个等级的会员人数
     *
     * @return Map<String,Integer>
     */
    @Override
    public Map<String, Integer> getLevelMembers() {
        List<Member> members = memberDao.getLevelMembers();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put("Lv." + String.valueOf(i), 0);
        }
        for (Member member : members) {
            String level = "Lv." + calculateLevel(member.getAccumulateMarks());
            map.put(level, map.get(level) + 1);
        }
        return map;
    }

    /**
     * 用于初始化会员等级制度的信息
     */
    private void init() {
        FileHelper fileHelper = new FileHelper();
        levelToDiscount = fileHelper.readLevelToDiscount();
        marksToLevel = fileHelper.readLevelToMarks();
    }

    /**
     * 根据会员累计积分获取等级
     *
     * @param accumulateMarks 当前累计积分
     * @return 等级
     */
    private String calculateLevel(int accumulateMarks) {
        Set<Integer> set = marksToLevel.keySet();
        int key_last = 0;
        for (Integer key : set) {
            if (accumulateMarks == key) {
                return marksToLevel.get(key);
            }

            if (accumulateMarks > key) {
                key_last = key;
                continue;
            }

            return marksToLevel.get(key_last);
        }
        return null;
    }
}
