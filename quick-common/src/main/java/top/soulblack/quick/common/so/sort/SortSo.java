package top.soulblack.quick.common.so.sort;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import top.soulblack.quick.client.so.base.BaseSo;
import top.soulblack.quick.common.enums.SortTypeEnum;

import java.util.LinkedList;
import java.util.List;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public abstract class SortSo implements BaseSo {

    private List<Sort> sorts = new LinkedList<>();

    public List<Sort> getSorts() {
        return sorts;
    }

    public SortSo sort(String key) {
        return sort(key, SortTypeEnum.ASC);
    }

    public SortSo sort(String key, SortTypeEnum type) {
        Sort sort = new Sort()
                .setKey(key)
                .setType(type);
        sorts.add(sort);
        return this;
    }

    @Data
    @Accessors(chain = true)
    public static class Sort {

        @ApiModelProperty(value = "排序字段")
        private String key;

        @ApiModelProperty(value = "排序类型")
        private SortTypeEnum type;
    }
}
