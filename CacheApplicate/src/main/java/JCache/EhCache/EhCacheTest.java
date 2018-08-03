package JCache.EhCache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Created by Xi on 2018/8/3.
 */
public class EhCacheTest {
    public static void main(String[] args) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // 1. 创建缓存管理器
        CacheManager cacheManager = CacheManager.create(loader.getResource("ehcache.xml").getPath());
        // 2. 获取缓存对象
        Cache cache = cacheManager.getCache("HelloWorldCache");
        // 3. 创建元素
        Element element = new Element("key1", "value1");

        // 4. 将元素添加到缓存
        cache.put(element);
        // 5. 获取缓存
        Element value = cache.get("key1");
        System.out.println(value);
        System.out.println(value.getObjectValue());

        Dog dog = new Dog("HaShiQi");
        Element element2 = new Element("key2",dog);
        cache.put(element2);
        Element value2 = cache.get("key2");
        System.out.println(value2);
        Dog dog2 = (Dog)value2.getObjectValue();
        System.out.println(dog2.getType());

        // 6. 删除元素
        System.out.println(cache.getSize());
        cache.remove("key1");
        cache.remove("key2");
        System.out.println(cache.getSize());

        // 7. 刷新缓存
        cache.flush();
        // 8. 关闭缓存管理器
        cacheManager.shutdown();
    }
}