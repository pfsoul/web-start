package top.soulblack.quick.cache.base;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public interface L2Cache<K, V> {
    V get(K k);

    void remove(K k);

    void put(K k, V v);

    void putIfAbsent(K k, V v);
}