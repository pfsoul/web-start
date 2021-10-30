package top.soulblack.quick.manager.base;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import top.soulblack.quick.client.so.base.BaseSo;
import top.soulblack.quick.common.enity.base.BaseModel;
import top.soulblack.quick.common.so.page.PageSo;
import top.soulblack.quick.common.util.ColumnUtil;
import top.soulblack.quick.common.util.OrikaBeanUtil;
import top.soulblack.quick.common.util.WrapperUtil;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public class BaseManagerImpl<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> implements BaseManager<T> {

    // 子类会通过spring管理
    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

    @Override
    public T getOne(BaseSo so){
        return this.getOne(so, true);
    }

    @Override
    public T getOne(BaseSo so, boolean throwEx){
        return this.getOne(this.newQueryWrapper(so), throwEx);
    }

    @Override
    public List<T> list(BaseSo so) {
        return this.list(this.newQueryWrapper(so));
    }

    @Override
    public IPage<T> page(PageSo<T> pageSo) {
        return this.page(pageSo.newPage(), pageSo);
    }

    @Override
    public IPage<T> page(IPage<T> page, BaseSo so) {
        return this.page(page, this.newQueryWrapper(so));
    }

    @Override
    public QueryWrapper<T> newQueryWrapper(BaseSo so) {
        Assert.notNull(so, "So为空");
        Class<T> modelClz = getActualTypeArgument();
        T queryModel = OrikaBeanUtil.convert(so, modelClz);
        QueryWrapper<T> queryWrapper = WrapperUtil.injectEqConditionByField(queryModel);
        WrapperUtil.convertByQueryCondition(queryWrapper, so);
        return queryWrapper;
    }

    @Override
    public int count(BaseSo so) {
        return this.count(this.newQueryWrapper(so));
    }

    @Override
    public <R> R min(ColumnUtil.Fun<T, R> column, BaseSo so) {
        return min(column, this.newQueryWrapper(so));
    }

    @Override
    public <R> R max(ColumnUtil.Fun<T, R> column, BaseSo so) {
        return max(column, this.newQueryWrapper(so));
    }

    @Override
    public <R> Double sum(ColumnUtil.Fun<T, R> column, BaseSo so) {
        return sum(column, this.newQueryWrapper(so));
    }

    @Override
    public QueryWrapper<T> newQueryWrapper() {
        return new QueryWrapper<>();
    }

    /**
     * 插入并返回结果
     * @param entity
     * @return
     */
    @Override
    public T saveAndReturn(T entity) {
        this.save(entity);
        return this.getById(entity.getId());
    }

    /**
     * 根据id更新并返回结果
     * @param entity
     * @return
     */
    @Override
    public T updateByIdAndReturn(T entity) {
        this.updateById(entity);
        return this.getById(entity.getId());
    }

    /**
     * 更新并返回结果
     * @param entity
     * @param updateWrapper
     * @return
     */
    @Override
    public T updateAndReturn(T entity, QueryWrapper<T> updateWrapper) {
        this.update(entity, updateWrapper);
        return this.getById(entity.getId());
    }

    /**
     * 根据id查询并行锁
     * @param id
     * @return
     */
    @Override
    public T getByIdForUpdate(Serializable id) {
        QueryWrapper<T> queryWrapper = newQueryWrapper();
        queryWrapper.eq(BaseModel.PRIMARY_KEY, id).last(" for update");
        return this.getOne(queryWrapper);
    }

    /**
     * 获取最小值
     * 日期默认响应TimeStamp
     * @param column
     * @param queryWrapper
     * @return
     */
    @Override
    public <R> R min(ColumnUtil.Fun<T, R> column, QueryWrapper<T> queryWrapper) {
        queryWrapper.select("MIN(" + ColumnUtil.resolveColumn(column) + ") as min");
        Map<String, Object> map = this.getMap(queryWrapper);
        Object sqlResult = map == null ? null : map.get("min");
        return ColumnUtil.convertSqlResultToPatternReturnType(column, sqlResult);
    }

    /**
     * 获取最大值
     * 日期默认响应TimeStamp
     * @param column
     * @param queryWrapper
     * @return
     */
    @Override
    public <R> R max(ColumnUtil.Fun<T, R> column, QueryWrapper<T> queryWrapper) {
        queryWrapper.select("MAX(" + ColumnUtil.resolveColumn(column) + ") as max");
        Map<String, Object> map = this.getMap(queryWrapper);
        Object sqlResult = map == null ? null : map.get("max");
        return ColumnUtil.convertSqlResultToPatternReturnType(column, sqlResult);
    }

    /**
     * 求和
     * @param column
     * @param queryWrapper
     * @return
     */
    @Override
    public <R> Double sum(ColumnUtil.Fun<T, R> column, QueryWrapper<T> queryWrapper) {
        // 查询并获取结果
        queryWrapper.select("SUM(" + ColumnUtil.resolveColumn(column) + ") as sum");
        Map<String, Object> map = this.getMap(queryWrapper);
        if (map == null) {
            return null;
        }

        // 结果转化
        Object sqlResultObj = map.get("sum");
        if (sqlResultObj instanceof BigDecimal) {
            BigDecimal sqlResult = (BigDecimal) sqlResultObj;
            return sqlResult == null ? null : sqlResult.doubleValue();
        } else if (sqlResultObj instanceof Long) {
            Long sqlResult = (Long) sqlResultObj;
            return sqlResult == null ? null : sqlResult.doubleValue();
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 获取泛型T的类
     * @return T泛型的Class
     */
    private Class<T> getActualTypeArgument() {
        Type type = getClass().getGenericSuperclass();
        Class<T> result = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            result = (Class<T>) pType.getActualTypeArguments()[1];
        }
        if (result == null) {
            throw new RuntimeException("获取不到泛型类导致异常发生，请检查是否使用了代理等例如（SpyBean）或者遇到了泛型擦除等问题");
        }
        return result;
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        List<BatchResult> batchResults;
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
                if (anEntityList.getVersion() == null) {
                    throw new RuntimeException("批量更新中部分数据未携带版本号");
                }
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchResults = batchSqlSession.flushStatements();
                    checkBatchResults(batchResults);
                }
                i++;
            }
            batchResults = batchSqlSession.flushStatements();
            checkBatchResults(batchResults);
        }
        return true;
    }

    private void checkBatchResults(List<BatchResult> batchResultList) {
        if (CollectionUtils.isEmpty(batchResultList)) {
            return;
        }
        batchResultList.forEach(
                bl -> {
                    if (Arrays.stream(bl.getUpdateCounts()).anyMatch(v -> v == 0)) {
                        throw new RuntimeException("批量更新中部分数据更新失败");
                    }
                }
        );
    }
}
