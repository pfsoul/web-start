package top.soulblack.quick.manager.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.soulblack.quick.client.so.base.BaseSo;
import top.soulblack.quick.common.enity.base.BaseModel;
import top.soulblack.quick.common.so.page.PageSo;
import top.soulblack.quick.common.util.ColumnUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public interface BaseManager<T extends BaseModel> extends IService<T> {

    /**
     * 根据So获取一个相关对象
     *
     * @param so       SearchObject
     * @return 返回一个entity对象
     */
    T getOne(BaseSo so);

    /**
     * 根据So获取一个相关对象
     *
     * @param so       SearchObject
     * @return 返回一个entity对象
     */
    T getOne(BaseSo so, boolean throwEx);

    /**
     * 根据So获取一个相关列表
     *
     * @param so       SearchObject
     * @return 返回一个List<entity>类
     */
    List<T> list(BaseSo so);

    /**
     * 分页查询
     *
     * @param pageSo     封装支持分页So
     * @return 返回分页结果Ipage
     */
    IPage<T> page(PageSo<T> pageSo);

    /**
     * 分页查询
     *
     * @param page     Ipage类，用于接收参数
     * @param so       SearchObject
     * @return 返回分页结果Ipage
     */
    IPage<T> page(IPage<T> page, BaseSo so);

    /**
     * 将So装换为Wrapper
     * @param so       SearchObject
     * @return queryWrapper
     */
    QueryWrapper<T> newQueryWrapper(BaseSo so);

    /**
     * 获取数量
     *
     * @param so
     * @return
     */
    int count(BaseSo so);

    /**
     * 获取最小值
     * 日期默认响应TimeStamp
     * @param column 当前行
     * @param so 查询条件
     * @return 当前行最小值
     */
    <R> R min(ColumnUtil.Fun<T, R> column, BaseSo so);

    /**
     * 获取最大值
     * 日期默认响应TimeStamp
     * @param column 当前行
     * @param so 查询条件
     * @return 当前行最大值
     */
    <R> R max(ColumnUtil.Fun<T, R> column, BaseSo so);

    /**
     * 求和
     *
     * @param column
     * @param so
     * @return
     */
    <R> Double sum(ColumnUtil.Fun<T, R> column, BaseSo so);

    /**
     * 获取新的查询条件构造器
     * @return
     */
    QueryWrapper<T> newQueryWrapper();

    /**
     * 插入并返回结果
     *
     * @param entity
     * @return
     */
    T saveAndReturn(T entity);

    /**
     * 根据id更新并返回结果
     *
     * @param entity
     * @return
     */
    T updateByIdAndReturn(T entity);

    /**
     * 更新并返回结果
     *
     * @param entity
     * @param updateWrapper
     * @return
     */
    T updateAndReturn(T entity, QueryWrapper<T> updateWrapper);

    /**
     * 根据id查询并行锁
     *
     * @param id
     * @return
     */
    T getByIdForUpdate(Serializable id);

    /**
     * 获取最小值
     * 日期默认响应TimeStamp
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    <R> R min(ColumnUtil.Fun<T, R> column, QueryWrapper<T> queryWrapper);

    /**
     * 获取最大值
     * 日期默认响应TimeStamp
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    <R> R max(ColumnUtil.Fun<T, R> column, QueryWrapper<T> queryWrapper);

    /**
     * 求和
     *
     * @param column
     * @param queryWrapper
     * @return
     */
    <R> Double sum(ColumnUtil.Fun<T, R> column, QueryWrapper<T> queryWrapper);
}
