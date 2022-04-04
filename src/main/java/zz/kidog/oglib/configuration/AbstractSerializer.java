package zz.kidog.oglib.configuration;

public abstract class AbstractSerializer<T> {

    public abstract String toString(T var1);
    public abstract T fromString(String var1);

}

