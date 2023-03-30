package org.adg.ibatis.core;

/**
 * 普通的Java类：pojo,封装的一个SQL标签
 * 一个MapperStatement对象对应一个SQL标签
 * 一个SQL标签中的所有信息封装到MapperStatement对象当中
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class MapperStatement {
    /**sql语句*/
    private String sql;
    /**要封装的结果集类型，有的时候resultType的结果为null
     * 比如：insert delete update语句时resultType的结果为null
     * 只有当sql语句为select时resultType才有值*/
    private String resultType;

    public MapperStatement() {
    }

    public MapperStatement(String sql, String resultType) {
        this.sql = sql;
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public String toString() {
        return "MapperStatement{" +
                "sql='" + sql + '\'' +
                ", resultType='" + resultType + '\'' +
                '}';
    }
}
