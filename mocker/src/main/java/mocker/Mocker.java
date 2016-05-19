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
    Class<T> clazz;
    T that;
    Func1<T, ?> when;
    Func1<T, ?> thenReturn;

    public interface Func0<R> {
        public R call();
    }

    public interface Func1<V, R> {
        public R call(V v);
    }

    public interface Func2<V, V2, R> {
        public R call(V v, V2 v2);
    }

    public interface Action1<V> {
        public void call(V v);
    }

    public Mocker(Class<T> clazz) {
        this.clazz = clazz;
        this.that = mock(clazz);
    }

    public <R> Mocker<T> when(Func1<T, R> when) {
        this.when = when;
        return this;
    }

    public <R> Mocker<T> thenReturn(Func1<T, R> thenReturn) {
        if (when == null) throw new NullPointerException("Missin .when()");
        Mockito.when(when.call(that)).thenReturn(thenReturn.call(that));
        when = null;
        return this;
    }

    //public static <V> Mocker<V> of(Class<?> clazz) {
        //return new Mocker<>
    //}

    public static <V> Mocker<V> of(Class<V> clazz) {
        return new Mocker<>(clazz);
    }

    public T mock() {
        return that;
    }

    /**
     * Support infer instead of Mockito.mock()
     */
    @SuppressWarnings("unchecked")
    public static <V> V mock(Class<V> clazz) {
        return (V) Mockito.mock(clazz);
    }

    /**
     * For import
     */
    public static <V> Mocker<V> mocker(Class<V> clazz) {
        return of(clazz);
    }

    public <V> Mocker<T> then(Action1<T> then) {
        then.call(that);
        return this;
    }
}
