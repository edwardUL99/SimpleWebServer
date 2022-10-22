package io.github.edwardUL99.simple.web.injection;

import io.github.edwardUL99.simple.web.configuration.annotations.Resource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Default implementation of our resource injector
 */
public class ResourceInjectorImpl implements ResourceInjector {
    private void inject(Resource resource, Field field, Object obj) {
        Object resourceInstance = Injection.getInjectable(resource.value());
        Class<?> resourceCls = resourceInstance.getClass();
        Class<?> fieldType = field.getType();

        if (fieldType.isAssignableFrom(resourceCls)) {
            try {
                boolean accessible = field.canAccess(obj);
                field.setAccessible(true);
                field.set(obj, resourceInstance);
                field.setAccessible(accessible);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Failed to set @Resource field", ex);
            }
        } else {
            throw new IllegalStateException(String.format("@Resource of type %s not assignable to %s", resourceCls,
                    fieldType));
        }
    }

    private void doInjection(Field[] fields, Object obj) {
        for (Field field : fields) {
            Resource resource = field.getAnnotation(Resource.class);

            if (resource != null) {
                if (!Modifier.isFinal(field.getModifiers())) {
                    inject(resource, field, obj);
                } else {
                    throw new IllegalStateException("Fields annotated with @Resource must not be final");
                }
            }
        }
    }

    @Override
    public void inject(Object obj) {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        doInjection(fields, obj);
    }
}
