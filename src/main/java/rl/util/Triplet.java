package rl.util;

import java.util.Objects;

public class Triplet<F,S,T> {
    F f;
    S s;
    T t;

    public Triplet(F f, S s, T t) {
        this.f = f;
        this.s = s;
        this.t = t;
    }

    public static <F,S,T> Triplet<F,S,T> with(F f, S s, T t)
    {
        return new Triplet<>(f,s,t);
    }

    public F first()
    {
        return f;
    }
    public S second()
    {
        return s;
    }

    public T third()
    {
        return t;
    }

    @Override
    public String toString() {
        return "<"+ f + ","+ s+","+ t + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(f, triplet.f) && Objects.equals(s, triplet.s) && Objects.equals(t, triplet.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(f, s, t);
    }
}
