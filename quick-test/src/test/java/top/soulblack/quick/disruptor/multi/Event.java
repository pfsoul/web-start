package top.soulblack.quick.disruptor.multi;

import lombok.Data;

/**
 * @author : soulblack
 * @since : 2022/9/11
 */
@Data
public class Event<T> {

    T data;

}
