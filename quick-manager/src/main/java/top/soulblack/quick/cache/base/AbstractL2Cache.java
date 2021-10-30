package top.soulblack.quick.cache.base;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import top.soulblack.quick.common.provider.ApplicationContextProvider;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Slf4j
public abstract class AbstractL2Cache<K, V> implements L2Cache<K, V> {
    private static final String NULLABLE = "NULL";

    private final long l1TTLSeconds = l1TTLSeconds();

    private final int l2TTLSeconds = l2TTLSeconds();

    private final int l1MaxSize = l1MaxSize();

    private boolean isEnableL2 = isEnableL2();

    private boolean isCacheNull = isCacheNull();

    private final Cache<K, Optional<V>> caffeineCache = initCaffeine();

    private final RedisCache redisCache = initRedisCache();

    /**
     * 存储redis当中的键
     * @param k key
     * @return String类型
     */
    protected abstract String l2Key(K k);

    /**
     * caffeine缓存写后最大过期时间
     * @return long过期时间
     */
    protected abstract long l1TTLSeconds();

    /**
     * L2(Redis)缓存写后最大过期时间
     * @return long 过期时间
     */
    protected abstract int l2TTLSeconds();

    /**
     * 缓存的最大数量限制
     * @return 最大数量
     */
    protected abstract int l1MaxSize();

    /**
     * 是否开启L2(Redis)缓存
     * @return true开启，false关闭
     */
    protected abstract boolean isEnableL2();

    /**
     * 是否开启防缓存穿透、击穿
     * @return true开启，false关闭
     */
    protected abstract boolean isCacheNull();

    /**
     * 初始化Caffeine
     * @return caffeine
     */
    private Cache<K, Optional<V>> initCaffeine() {
        if (l1TTLSeconds <= 0L || l1MaxSize <= 0) {
            throw new RuntimeException("初始化L1(Caffeine)参数不可小于0");
        }
        return Caffeine.newBuilder()
                .expireAfterWrite(l1TTLSeconds, TimeUnit.SECONDS)
                .maximumSize(l1MaxSize)
                .build();
    }

    /**
     * 初始化RedisCache
     * @return RedisCache
     */
    private RedisCache initRedisCache() {
        // 未启用不处理
        if (!isEnableL2) {
            return null;
        }

        // RedisCache未注册bean不处理
        RedisCache redisCache = ApplicationContextProvider.getBean(RedisCache.class);
        if (redisCache == null) {
            isEnableL2 = false;
            log.warn("isEnableL2 is true, but redisCache not exist.");
            return null;
        }

        // 初始化校验
        if (l2TTLSeconds <= 0) {
            throw new RuntimeException("初始化L2(RedisCache)参数不可小于0");
        }
        return redisCache;
    }

    /**
     * 从数据源最终获取Value
     * @param k key
     * @return value
     */
    protected abstract V finalGet(K k);

    private Optional<V> l1Get(K k) {
        return caffeineCache.getIfPresent(k);
    }

    private void l1Put(K k, Optional<V> v) {
        if (v.isPresent() || isCacheNull) {
            caffeineCache.put(k, v);
        }
    }

    private void l1PutIfAbsent(K k, Optional<V> v) {
        if (l1Get(k) == null) {
            l1Put(k, v);
        }
    }

    private void l1Remove(K k) {
        caffeineCache.invalidate(k);
    }

    private Optional<V> l2Get(K k) {
        if (!isEnableL2) {
            return null;
        }
        V v = redisCache.get(l2Key(k));
        if (NULLABLE.equals(v)) {
            return Optional.empty();
        }
        return v == null ? null : Optional.of(v);
    }

    private void l2Put(K k, Optional<V> v) {
        if (!isEnableL2 || redisCache == null) {
            return;
        }
        if (v.isPresent()) {
            redisCache.set(l2Key(k), v.get(), l2TTLSeconds, TimeUnit.SECONDS);
        } else if (isCacheNull) {
            redisCache.set(l2Key(k), NULLABLE, l2TTLSeconds, TimeUnit.SECONDS);
        }
    }

    private void l2PutIfAbsent(K k, Optional<V> v) {
        if (l2Get(k) == null) {
            l2Put(k, v);
        }
    }

    private void l2Remove(K k) {
        if (!isEnableL2) {
            return;
        }
        redisCache.remove(l2Key(k));
    }

    /**
     * 取缓存流程获取
     * @param k key
     * @return value
     */
    @Override
    public final V get(K k) {
        // 获取一级缓存
        Optional<V> v = this.l1Get(k);
        if(v != null){
            return v.orElse(null);
        }

        // 获取二级缓存
        v = this.l2Get(k);
        if(v != null){
            this.l1Put(k, v);
            return v.orElse(null);
        }

        // 获取源数据
        v = Optional.ofNullable(this.finalGet(k));
        this.l1Put(k, v);
        this.l2Put(k, v);
        return v.orElse(null);
    }

    /**
     * 删除caffeine、redis中的缓存
     * @param k key
     */
    @Override
    public final void remove(K k) {
        this.l2Remove(k);
        this.l1Remove(k);
    }

    /**
     * 写入caffeine、redis中的缓存
     * @param k key
     */
    @Override
    public final void put(K k, V v) {
        Optional<V> val = Optional.ofNullable(v);
        this.l1Put(k, val);
        this.l2Put(k, val);
    }

    /**
     * 写入caffeine、redis中的缓存
     * @param k key
     */
    @Override
    public final void putIfAbsent(K k, V v) {
        if (v != null || isCacheNull) {
            Optional<V> val = Optional.ofNullable(v);
            l1PutIfAbsent(k, val);
            l2PutIfAbsent(k, val);
        }
    }
}
