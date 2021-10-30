package top.soulblack.quick.common.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author : soulblack
 * @since : 2021/10/30
 */
public class OrikaBeanUtil {
    private static MapperFacade mapperFacade;

    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    static {
        mapperFacade = mapperFactory.getMapperFacade();
    }

    private OrikaBeanUtil() {
    }

    public static <V, P> P convert(V base, Class<P> target) {
        if (base == null) {
            return null;
        }
        return mapperFacade.map(base, target);
    }

    public static <V, P> P convert(V base, P target) {
        if (base == null) {
            return null;
        }
        mapperFacade.map(base, target);
        return target;
    }

    public static <V, P> List<P> convertList(List<V> baseList, Class<P> target) {
        if (baseList == null) {
            return Collections.emptyList();
        }
        return mapperFacade.mapAsList(baseList, target);
    }

    public static <P> P clone(P base) {
        if (base == null) {
            return null;
        }
        return (P) mapperFacade.map(base, base.getClass());
    }
}
