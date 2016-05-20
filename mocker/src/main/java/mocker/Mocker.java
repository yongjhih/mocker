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
    Func1<T, ?> when;
    Func1<T, ?> thenReturn;
    Action1<T> then;
    Mocker<T> mocker;
    T that;

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
    }

    public Mocker(Mocker<T> mocker) {
        this.clazz = mocker.clazz;
        this.mocker = mocker;
    }

    public <R> Mocker<T> when(Func1<T, R> when) {
        if (this.when != null) {
            Mocker<T> mocker = new Mocker<>(this);
            mocker.when(when);
            return mocker;
        }
        this.when = when;
        return this;
    }

    public <R> Mocker<T> thenReturn(Func1<T, R> thenReturn) {
        if (when == null) throw new NullPointerException("Missing .when()");
        this.thenReturn = thenReturn;
        return this;
    }

    //public static <V> Mocker<V> of(Class<?> clazz) {
        //return new Mocker<>
    //}

    public static <V> Mocker<V> of(Class<V> clazz) {
        return new Mocker<>(clazz);
    }

    public T mock() {
        if (that == null) that = mock(clazz);

        if (mocker != null) {
            mocker.that = that;
            mocker.mock();
        }

        if (when != null && thenReturn != null) {
            Mockito.when(when.call(that)).thenReturn(thenReturn.call(that));
        }
        if (then != null) {
            then.call(that);
        }

        // clear that after return, to mack sure next mock() will regenerate mock(clazz) that
        T that = this.that;
        this.that = null;

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
        if (this.then != null) {
            Mocker<T> mocker = new Mocker<>(this);
            mocker.then(then);
            return mocker;
        }
        this.then = then;
        return this;
    }

    /**
     * Warning: lift().lift() NPE risk
     */
    public <V> Mocker<T> lift() {
        return mocker;
    }

    public <V> Mocker<T> safeLift() {
        if (mocker == null) return this;
        return mocker;
    }

    // Alias switchMap()
    public Mocker<T> lift(Mocker<T> mocker) {
        this.mocker = mocker;
        return new Mocker<>(this);
    }
}
