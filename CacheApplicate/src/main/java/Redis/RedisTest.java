package Redis;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Xi on 2018/8/3.
 */
public class RedisTest {
    private Jedis jedis;

    @Before
    public void connectToServer() {
        //连接redis服务器(在这里是连接本地的)
        jedis = new Jedis("localhost", 6379);
        //权限认证
        //jedis.auth("chenhaoxiang");
        System.out.println("连接服务成功");
    }

    /**
     * jedis操作字符串
     */
    @Test
    public void testString() {
        //添加数据
        jedis.set("name", "xht");
        System.out.println("拼接前:" + jedis.get("name"));

        //向key为name的值后面加上数据 ---拼接
        jedis.append("name", " is my name;");
        System.out.println("拼接后:" + jedis.get("name"));

        //删除某个键值对
        jedis.del("name");
        System.out.println("删除后:" + jedis.get("name"));

        //设置多个键值对
        jedis.mset("name", "xihaotian", "age", "22", "email", "123456@qq.com");
        jedis.incr("age");//用于将键的整数值递增1。如果键不存在，则在执行操作之前将其设置为0。 如果键包含错误类型的值或包含无法表示为整数的字符串，则会返回错误。此操作限于64位有符号整数。
        System.out.println(jedis.get("name") + " " + jedis.get("age") + " " + jedis.get("email"));
    }

    /**
     * jedis操作map
     */
    @Test
    public void testMap() {
        //添加数据
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "chx");
        map.put("age", "22");
        map.put("email", "***@outlook.com");
        jedis.hmset("user", map);
        //取出user中的name，结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key是可变参数
        List<String> list = jedis.hmget("user", "name", "age", "email");
        System.out.println(list);

        //删除map中的某个键值
        jedis.hdel("user", "age");
        System.out.println("age:" + jedis.hmget("user", "age")); //因为删除了，所以返回的是null
        System.out.println("user的键中存放的值的个数:" + jedis.hlen("user")); //返回key为user的键中存放的值的个数2
        System.out.println("是否存在key为user的记录:" + jedis.exists("user"));//是否存在key为user的记录 返回true
        System.out.println("user对象中的所有key:" + jedis.hkeys("user"));//返回user对象中的所有key
        System.out.println("user对象中的所有value:" + jedis.hvals("user"));//返回map对象中的所有value

        //拿到key，再通过迭代器得到值
        Iterator<String> iterator = jedis.hkeys("user").iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }
        /*jedis.del("user");
        System.out.println("删除后是否存在key为user的记录:" + jedis.exists("user"));*/
    }

    /**
     * jedis操作List
     */
    @Test
    public void testList() {
        //移除javaFramwork所所有内容
        jedis.del("javaFramework");
        //存放数据
        jedis.lpush("javaFramework", "spring");
        jedis.lpush("javaFramework", "springMVC");
        jedis.lpush("javaFramework", "mybatis");
        //jedis.llen获取长度
        System.out.println("长度:" + jedis.llen("javaFramework"));
        //取出所有数据,jedis.lrange是按范围取出
        //第一个是key，第二个是起始位置，第三个是结束位置,-1表示取得所有
        System.out.println("javaFramework:" + jedis.lrange("javaFramework", 0, -1));

        /*jedis.del("javaFramework");
        System.out.println("删除后长度:"+jedis.llen("javaFramework"));
        System.out.println(jedis.lrange("javaFramework",0,-1));*/
    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {
        //添加
        jedis.sadd("users", "AM");
        jedis.sadd("users", "BH");
        jedis.sadd("users", "CK");
        jedis.sadd("users", "DP");
        jedis.sadd("users", "FV");
        jedis.sadd("users", "GA");
        //移除users集合中的元素are
        jedis.srem("users", "GA");
        System.out.println("users中的value:" + jedis.smembers("users"));//获取所有加入users的value
        System.out.println("AM是否是user中的元素:" + jedis.sismember("users", "AM"));//判断AM是否是user集合中的元素
        System.out.println("集合中的一个随机元素:" + jedis.srandmember("users"));//返回集合中的一个随机元素
        System.out.println("users中元素的个数:" + jedis.scard("users"));
    }

    /**
     * 排序
     */
    @Test
    public void testSort() {
        jedis.del("number");//先删除数据，再进行测试
        jedis.rpush("number", "4");//将一个或多个值插入到列表的尾部(最右边)
        jedis.rpush("number", "5");
        jedis.rpush("number", "3");

        jedis.lpush("number", "9");//将一个或多个值插入到列表头部
        jedis.lpush("number", "1");
        jedis.lpush("number", "2");
        System.out.println(jedis.lrange("number", 0, jedis.llen("number")));
        System.out.println("排序:" + jedis.sort("number"));
        System.out.println(jedis.lrange("number", 0, -1));//sort方法不改变原来的排序
    }
}
