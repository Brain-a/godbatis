package org.adg.ibatis.core;

import java.lang.reflect.Method;
import java.sql.*;

/**
 * 专门执行SQL语句的会话对象
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class SqlSession {
    private SqlSessionFactory sqlSessionFactory;

    public SqlSession() {
    }

    public SqlSession(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 执行insert语句，向数据库表中插入记录
     * @param sqlId sql语句的id
     * @param pojo 插入的数据
     * @return 插入结果
     */
    public int insert(String sqlId,Object pojo){
        int count = 0;
        try {
            //JDBC代码，获取连接
            Connection connection = sqlSessionFactory.getTransaction().getConnection();
            //返回sql语句
            String godbatisSql = sqlSessionFactory.getMapperStatements().get(sqlId).getSql();
            //将返回的sql语句的#{}替换成？
            String sql = godbatisSql.replaceAll("#\\{[a-zA-Z0-9_$]*}","?");
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            //前提：#{}里面的内容是String类型
            //不知道有几个？
            //不知道该将pojo对象的哪个属性赋值给哪个？
            //执行sql语句
            int fromIndex = 0;
            int index = 1;
            while (true){
                int jhIndex = godbatisSql.indexOf("#",fromIndex);
                if (jhIndex < 0){
                    break;
                }
                int rightIndex = godbatisSql.indexOf("}",fromIndex);
                String propertyName = godbatisSql.substring(jhIndex + 2, rightIndex).trim();
                fromIndex = rightIndex + 1;
                String getMethodName =  "get"+propertyName.toUpperCase().charAt(0) + propertyName.substring(1);
                Method getMethod = pojo.getClass().getDeclaredMethod(getMethodName);
                Object propertyValue = getMethod.invoke(pojo);
                //给第几个？传什么值
                prepareStatement.setString(index,propertyValue.toString());
                index++;
            }
            count = prepareStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 执行查询语句，返回一个对象。该方法只适合返回一条记录的sql语句
     * @param sqlId sql语句的id
     * @param param 传的值
     * @return 查询结果
     */
    public Object selectOne(String sqlId,Object param){
        Object object = null;
        try {
            Connection connection =sqlSessionFactory.getTransaction().getConnection();
            MapperStatement mapperStatement = sqlSessionFactory.getMapperStatements().get(sqlId);
            String godbatisSql = mapperStatement.getSql();
            String sql = godbatisSql.replaceAll("#\\{[a-zA-Z0-9_$]*}","?");
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //给占位符传值(假设只有一个)
            preparedStatement.setString(1,param.toString());
            //查询返回结果集
            ResultSet resultSet = preparedStatement.executeQuery();
            //要封装的结果类型
            String resultType = mapperStatement.getResultType();
            //从结果集中取数据，封装java对象
            if (resultSet.next()) {
                //获取resultType的Class
                Class<?> resultTypeClass = Class.forName(resultType);
                //调用无参数构造方法创建对象
                object = resultTypeClass.newInstance();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String propertyName = resultSetMetaData.getColumnName(i + 1);
                    // 拼接方法名
                    String setMethodName = "set" + propertyName.toUpperCase().charAt(0) + propertyName.substring(1);
                    // 获取set方法
                    Method setMethod = resultTypeClass.getDeclaredMethod(setMethodName, String.class);
                    // 调用set方法给对象obj属性赋值
                    setMethod.invoke(object, resultSet.getString(propertyName));
                }

            }
            //要封装的结果集对象
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * 提交事务的方法
     */
    public void commit(){
        sqlSessionFactory.getTransaction().commit();
    }

    /**
     * 回滚事务的方法
     */
    public void rollback(){
        sqlSessionFactory.getTransaction().rollback();
    }
    /**
     * 关闭事务的方法
     */
    public void close(){
        sqlSessionFactory.getTransaction().close();
    }

}
