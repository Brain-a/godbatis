package org.adg.ibatis.test;

import org.adg.ibatis.core.SqlSession;
import org.adg.ibatis.core.SqlSessionFactory;
import org.adg.ibatis.core.SqlSessionFactoryBuilder;
import org.adg.ibatis.pojo.User;
import org.adg.ibatis.utils.Resources;
import org.junit.Test;

public class GodBatisTest {
    @Test
    public void testSqlSessionFactory(){
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("godbatis-config.xml"));
        System.out.println(sqlSessionFactory);
    }
    @Test
    public void testInsertUser() {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("godbatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User("111","Brain","24");
        sqlSession.insert("user.insertUser", user);
        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void testSelectOne() {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("godbatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //执行sql语句
        Object object = sqlSession.selectOne("user.selectById", "111");
        System.out.println("object = " + object);
        sqlSession.close();

    }
}
