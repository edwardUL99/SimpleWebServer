package io.github.edwardUL99.simple.web.injection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * An injector that can inject values up the inheritance hierarchy.
 * A specialisation of the single level injector
 */
public class MultiLevelInjector extends SingleLevelInjector {
    // recursive method to find superclass fields and add them to the provided list
    private void findFields(Class<?> cls, List<Field> fields) {
        if (cls != null) {
            // add all fields on this level of the hierarchy using the single level injector
            fields.addAll(super.getFields(cls));
            findFields(cls.getSuperclass(), fields);
        }
    }

    @Override
    protected List<Field> getFields(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        findFields(cls, fields);

        return fields;
    }
}
