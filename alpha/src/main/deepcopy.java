package main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class deepcopy {
        public static <T> T deepCopy(T t) {
        if (t == null) return t;
        if (t instanceof Map) return (T) deepCopyMap((LinkedHashMap) t);
        if (t instanceof List) return (T) deepCopyList((ArrayList) t);
        if (t instanceof Enum) return t;
        if (!t.getClass().toString().startsWith("class java.")) return deepCopyBean(t);
        return t;
    }

    private static <K> List<K> deepCopyList(List<K> m) {
        ArrayList<K> t = new ArrayList<>();
        for (K k : m) {
            try {
                t.add(deepCopy(k));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    private static <K, V> Map<K, V> deepCopyMap(Map<K, V> m) {
        Map<K, V> t = new LinkedHashMap<>();
        for (Map.Entry entry : m.entrySet()) {
            try {
                t.put(deepCopy((K) entry.getKey()), deepCopy((V) entry.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    private static <T> T deepCopyBean(T s) {

        T t = null;
        try {
            t = (T) Class.forName(s.getClass().getName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Field field : s.getClass().getDeclaredFields()) {
            char[] cs = field.getName().toCharArray();
            //first alphabet UPcase
            cs[0] -= 32;
            Method mg = null;
            try {
                mg = s.getClass().getMethod("get" + new String(cs));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Method ms = null;
            try {
                ms = t.getClass().getDeclaredMethod("set" + new String(cs), field.getType());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                ms.invoke(t, deepCopy(mg.invoke(s)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }


}
