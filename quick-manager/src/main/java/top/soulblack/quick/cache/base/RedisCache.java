package top.soulblack.quick.cache.base;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Service
@ConditionalOnProperty(
        name = {"redisson.enable"},
        havingValue = "true"
)
public class RedisCache {

    @Autowired
    private RedissonClient redissonClient;

    public RedisCache() {

    }

    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        bucket.set(value, timeout, timeUnit);
    }

    public void set(String key, Object value) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        bucket.set(value);
    }

    public <T> T get(String key) {
        RBucket<T> bucket = this.redissonClient.getBucket(key);
        return bucket.get();
    }

    public boolean delete(String key) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        return bucket.delete();
    }

    public void setAsync(String key, Object value, long timeout, TimeUnit timeUnit) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        bucket.setAsync(value, timeout, timeUnit);
    }

    public boolean trySet(String key, Object value, long timeout, TimeUnit timeUnit) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        return bucket.trySet(value, timeout, timeUnit);
    }

    public long incrementAtomicLong(String key) {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        return atomicLong.incrementAndGet();
    }

    public boolean expireAtomicLong(String key, long timeout, TimeUnit timeUnit) {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        return atomicLong.expire(timeout, timeUnit);
    }

    public RFuture<Boolean> expireAsyncAtomicLong(String key, long timeout, TimeUnit timeUnit) {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        return atomicLong.expireAsync(timeout, timeUnit);
    }

    public long getAtomicLong(String key) {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong(key);
        return atomicLong.get();
    }

    public RLock lock(String lockKey) {
        RLock lock = this.redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = this.redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = this.redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = this.redissonClient.getLock(lockKey);

        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException var9) {
            return false;
        }
    }

    public void unlock(String lockKey) {
        RLock lock = this.redissonClient.getLock(lockKey);
        lock.unlock();
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

    public Boolean remove(String key) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        return bucket.delete();
    }

}
