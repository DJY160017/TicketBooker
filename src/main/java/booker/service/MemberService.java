package booker.service;

import booker.model.Member;
import booker.util.exception.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MemberService {

    /**
     * 会员登录
     *
     * @param mail     邮箱
     * @param password 密码
     * @return boolean 是否成功登录
     */
    boolean login(String mail, String password) throws UserNotExistException, MemberCancelException, PasswordInvalidException;

    /**
     * 会员注册
     *
     * @param member 会员信息实体
     * @return boolean 是否成功注册
     */
    boolean signUp(Member member) throws UserExistedException, EmailInputException;

    /**
     * 修改会员信息（会员名，积分信息等）
     *
     * @param member 会员信息实体
     * @return boolean  是否成功修改信息
     */
    boolean modifyInfo(Member member);

    /**
     * 获取会员信息（会员名，积分信息等）
     *
     * @param mail 邮箱
     * @return Member 用户信息实体
     */
    Member getUserInfo(String mail);

    /**
     * 取消指定用户的会员资格
     *
     * @param mail 邮箱
     * @return boolean
     */
    boolean cancelMemberQualification(String mail);

    /**
     * 获取会员等级
     *
     * @param mail 邮箱
     * @return 会员等级
     */
    String getMemberLevel(String mail);

    /**
     * 获取会员等级折扣
     *
     * @param mail 邮箱
     * @return double 折扣
     */
    double getDiscount(String mail);

    /**
     * 获取当前会员的积分信息
     *
     * @param mail 邮箱
     * @return int 当前等级积分
     */
    int getCurrentMarks(String mail);

    /**
     * 获取当前会员的积分信息
     *
     * @param mail 邮箱
     * @return int 当前等级积分
     */
    int getAccumulateMarks(String mail);

    /**
     * 获取下一等级会员的需要的积分
     *
     * @param mail 邮箱
     * @return int 需要的积分
     */
    int getNeedNextLevelMarks(String mail);

    /**
     * 增加用户积分
     *
     * @param mail  邮箱
     * @param marks 积分
     * @return boolean 是否成功增加积分
     */
    boolean addMarks(String mail, int marks);

    /**
     * 积分兑换优惠卷(每100兑换1元)
     *
     * @param mail  邮箱
     * @param marks 需要兑换的积分
     * @return 优惠卷卷额
     * @throws IntegralInsufficientException 积分不足
     */
    int exchange(String mail, int marks) throws IntegralInsufficientException;

    /**
     * 统计获取系统总注册人数
     *
     * @return 总人数
     */
    int getSignUpUserNum();

    /**
     * 统计获取每个等级的会员人数
     *
     * @return Map<String,Integer>
     */
    Map<String, Integer> getLevelMembers();
}
