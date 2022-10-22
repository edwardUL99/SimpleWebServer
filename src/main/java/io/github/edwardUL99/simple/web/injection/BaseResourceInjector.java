package io.github.edwardUL99.simple.web.injection;

import io.github.edwardUL99.simple.web.configuration.annotations.Resource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * A base implementation of the resource injector. Provides common injection functionality, with the differing
 * functionality being how the fields are looked up
 */
public abstract class BaseResourceInjector implements ResourceInjector {
    private void inject(Resource resource, Field field, Object obj) {
        Object resourceInstance;
        String value = resource.value();

        if (value.equals("")) {
            resourceInstance = Injection.getInjectableByType(field.getType());
        } else {
            resourceInstance = Injection.getInjectable(resource.value());
        }

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

    private void doInjection(List<Field> fields, Object obj) {
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
    public final void inject(Object obj) {
        Class<?> cls = obj.getClass();
        doInjection(getFields(cls), obj);
    }

    /**
     * Get the list of fields to search through for resource annotated fields. This should simply return all fields in
     * the class. This class will find the Resource annotated fields and inject them
     * @param cls the class of the object being injected with values
     * @return the list of possible fields
     */
    protected abstract List<Field> getFields(Class<?> cls);
}
