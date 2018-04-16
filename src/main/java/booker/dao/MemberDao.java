package booker.dao;

import booker.model.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {

    /**
     * 用于增加会员实体
     *
     * @param member 会员实体
     * @return boolean
     */
    boolean add(Member member);

    /**
     * 根据邮箱获取会员实体
     *
     * @param mail 邮箱
     * @return Member
     */
    Member get(String mail);

    /**
     * 更新会员实体
     *
     * @param member 更新会员信息后的实体
     * @return boolean
     */
    boolean update(Member member);

    /**
     * 取消当前邮箱的会员资格
     *
     * @param mail 邮箱
     * @return boolean
     */
    boolean cancelMember(String mail);

    /**
     * 统计获取系统总注册人数
     *
     * @return 总人数
     */
    int getSignUpUserNum();

    /**
     * 统计获取每个等级的会员人数
     *
     * @return List<Member>
     */
    List<Member> getLevelMembers();
}
