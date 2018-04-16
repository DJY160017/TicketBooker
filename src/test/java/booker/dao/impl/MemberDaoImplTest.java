package booker.dao.impl;

import booker.dao.DaoManager;
import booker.dao.MemberDao;
import booker.model.Member;
import booker.task.MD5Task;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemberDaoImplTest {

    private MemberDao memberDao;

    @Before
    public void setUp() throws Exception {
        memberDao = DaoManager.memberDao;
    }

    @Test
    public void add() throws Exception {
        for (int i = 1; i < 100; i++) {
            Member member = new Member();
            member.setMail("151250" + formate(i) + "@smail.nju.edu.cn");
            member.setUsername(String.valueOf(i) + "号会员");
            member.setPassword(MD5Task.encodeMD5("1234567890"));
            member.setCurrentMarks(i * 10);
            member.setAccumulateMarks(i * 100);
            member.setCancel(false);
            memberDao.add(member);
        }

    }

    @Test
    public void get() throws Exception {
        Member member = memberDao.get("151250032@smail.nju.edu.cn");
        assertEquals("1234567890", member.getPassword());
        assertEquals("32号会员", member.getUsername());
        assertEquals(0, member.getCurrentMarks());
        assertEquals(0, member.getAccumulateMarks());
    }

    @Test
    public void update() throws Exception {
        Member member = memberDao.get("151250032@smail.nju.edu.cn");
        member.setCurrentMarks(member.getCurrentMarks() + 100);
        member.setAccumulateMarks(member.getAccumulateMarks() + 100);
        memberDao.update(member);
    }

    @Test
    public void cancelMember() throws Exception {
        memberDao.cancelMember("151250099@smail.nju.edu.cn");
    }

    private String formate(int num) {
        String str_num = String.valueOf(num);
        for (int i = str_num.length(); i < 3; i++) {
            str_num = "0" + str_num;
        }
        return str_num;
    }

}