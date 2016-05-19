package mocker;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
import org.mockito.Mockito;

public class Mocker<T> {

    interface Func0<V> {
        V call();
    }

    private Func0<Class<T>> clazz;

    public Mocker(Func0<Class<T>> clazz) {
        this.clazz = clazz;
    }

    //public static <V> Mocker<V> of(Class<?> clazz) {
        //return new Mocker<>
    //}

    public static <V> Mocker<V> of(Func0<Class<V>> clazz) {
        return new Mocker<>(clazz);
    }

    public T mock() {
        return Mockito.mock(clazz.call());
    }
}
