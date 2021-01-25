package dev.mattrm.mc.zparkour.util;

import java.util.Objects;

public class TriTuple<T1, T2, T3> {
    public final T1 first;
    public final T2 second;
    public final T3 third;

    public TriTuple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T1 getFirst() {
        return this.first;
    }

    public T2 getSecond() {
        return this.second;
    }

    public T3 getThird() {
        return this.third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TriTuple)) return false;
        TriTuple<?, ?, ?> triTuple = (TriTuple<?, ?, ?>) o;
        return Objects.equals(first, triTuple.first) &&
                Objects.equals(second, triTuple.second) &&
                Objects.equals(third, triTuple.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "TriTuple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
