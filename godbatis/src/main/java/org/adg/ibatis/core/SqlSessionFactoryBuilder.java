package org.adg.ibatis.core;

import org.adg.ibatis.utils.Resources;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sqlSessionFactory构建器对象
 * 通过sqlSessionFactoryBuilder的build方法来解析godbatis-config.xml文件，
 * 然后创建sqlSessionFactory对象
 * @author Brain
 * @since 1.0
 * @version 1.0
 */
public class SqlSessionFactoryBuilder {
    /**
     * 无参数构造方法
     * */
    public SqlSessionFactoryBuilder() {
    }
    /**
     * 解析godbatis-config.xml文件，来构建sqlSessionFactory对象
     * @param inputStream 指向godbatis-config.xml文件的一个输入流
     * @return sqlSessionFactory对象
     * */
    public SqlSessionFactory build(InputStream inputStream){
        SqlSessionFactory sqlSessionFactory = null;
        try {
            //解析godbatis-config.xml文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element environments= (Element) document.selectSingleNode("/configuration/environments");
            String defaultId = environments.attributeValue("default");
            Element environment = (Element) document.selectSingleNode("/configuration/environments/environment[@id = '"+defaultId+"']");
            Element transactionElt = environment.element("transactionManager");
            Element dataSourceElt = environment.element("dataSource");
            //获取数据源
            DataSource dataSource = getDataSource(dataSourceElt);
            //获取事务管理器
            Transaction transaction = getTransaction(transactionElt,dataSource);
            //获取mapperStatements
            List<String> sqlMapperXMLPathList = new ArrayList<>();
            List<Node> nodes = document.selectNodes("//mapper");
            nodes.forEach(node -> {
                Element mapper = (Element) node;
                String resource = mapper.attributeValue("resource");
                sqlMapperXMLPathList.add(resource);
            });
            Map<String,MapperStatement> mapperStatements = getMapperStatements(sqlMapperXMLPathList);
            //解析完成之后，构建sqlSessionFactory对象
            sqlSessionFactory = new SqlSessionFactory(transaction,mapperStatements);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;

    }


    /**
     * @param dataSourceElt 数据源对象
     * @return DataSource
     */
    private DataSource getDataSource(Element dataSourceElt) {
        Map<String,String> map = new HashMap<>();
        //获取所有的property
        List<Element> propertyElts = dataSourceElt.elements("property");
        propertyElts.forEach(propertyElt -> {
            String name = propertyElt.attributeValue("name");
            String value = propertyElt.attributeValue("value");
            map.put(name,value);
        });
        DataSource dataSource = null;
        String type = dataSourceElt.attributeValue("type").trim().toUpperCase();
        switch (type){
            case Const.UN_POOLED_DATASOURCE:
                dataSource = new UnPooledDataSource(map.get("driver"),map.get("url"),map.get("username"),map.get("password"));
                break;
            case  Const.POOLED_DATASOURCE:
                dataSource = new PooledDataSource();
                break;
            case Const.JNDI_DATASOURCE:
                dataSource = new JNDIDataSource();
                break;
            default:
        }
        return dataSource;
    }

    /**
     * @param transactionElt 事务管理器对象
     * @param dataSource 数据源对象
     * @return Transaction
     */
    private Transaction getTransaction(Element transactionElt, DataSource dataSource) {
        Transaction transaction = null;
        String type = transactionElt.attributeValue("type").trim().toUpperCase();
        switch (type){
            case Const.JDBC_TRANSACTION:
                transaction = new JdbcTransaction(dataSource,false);//默认是开启事务的，将来需要手动提交
                break;
            case  Const.MANAGED_TRANSACTION:
                transaction = new ManagedTransaction();
                break;

            default:
        }
        return transaction;
    }

    /**
     * 解析所有的SqlMapper.xml文件，构建Map集合
     * @param sqlMapperXMLPathList SqlMapper.xml文件路径集合
     * @return Map<String, MapperStatement>
     */
    private Map<String, MapperStatement> getMapperStatements(List<String> sqlMapperXMLPathList) {
        Map<String,MapperStatement> mapperStatements = new HashMap<>();
        sqlMapperXMLPathList.forEach(sqlMapperXMLPath ->{
            try {
                SAXReader saxReader = new SAXReader();
                //一个document代表一个SqlMapper.xml文档对象
                Document document = saxReader.read(Resources.getResourceAsStream(sqlMapperXMLPath));
                Element mapper = (Element) document.selectSingleNode("mapper");
                String namespace = mapper.attributeValue("namespace");
                List<Element> elements = mapper.elements();
                elements.forEach(element -> {
                    String id = element.attributeValue("id");
                    //对namespace和id进行拼接，生成最终的sqlID
                    String sqlId = namespace+"."+id;
                    String resultType = element.attributeValue("resultType");
                    String sql = element.getTextTrim();
                    MapperStatement mapperStatement = new MapperStatement(sql, resultType);
                    mapperStatements.put(sqlId,mapperStatement);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return mapperStatements;
    }

}
