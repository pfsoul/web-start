package top.soulblack.quick.common.so.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import top.soulblack.quick.common.so.sort.SortSo;

import javax.validation.constraints.NotNull;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
@Data
public class PageSo<E> extends SortSo {

    @NotNull
    @Range(message = "每页显示区间为{min}至{max}", min = 0, max = 500)
    @ApiModelProperty(value = "每页显示的数量")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页数")
    private Integer current = 1;

    public Page<E> newPage(){
        return new Page<>(this.current, this.size);
    }
}
