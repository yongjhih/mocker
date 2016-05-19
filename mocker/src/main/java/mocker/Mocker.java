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
    T that;
    Func1<T, ?> when;
    Func1<T, ?> thenReturn;

    interface Func0<R> {
        R call();
    }

    interface Func1<V, R> {
        R call(V v);
    }

    interface Func2<V, V2, R> {
        R call(V v, V2 v2);
    }

    private Func0<Class<T>> clazz;

    public Mocker(Func0<Class<T>> clazz) {
        this.clazz = clazz;
        this.that = Mockito.mock(clazz.call());
    }

    public <R> Mocker<T> when(Func1<T, R> when) {
        this.when = when;
        return this;
    }

    public <R> Mocker<T> thenReturn(Func1<T, R> thenReturn) {
        Mockito.when(when.call(that)).thenReturn(thenReturn.call(that));
        return this;
    }

    //public static <V> Mocker<V> of(Class<?> clazz) {
        //return new Mocker<>
    //}

    public static <V> Mocker<V> of(Func0<Class<V>> clazz) {
        return new Mocker<>(clazz);
    }

    public T mock() {
        return that;
    }
}
