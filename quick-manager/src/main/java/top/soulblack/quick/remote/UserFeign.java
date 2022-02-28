package top.soulblack.quick.remote;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author : soulblack
 * @since : 2022/1/14
 */
@FeignClient(contextId = "user-server", url = "${remote.user.domain}")
public interface UserFeign {
}
