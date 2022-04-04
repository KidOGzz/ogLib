package zz.kidog.oglib.command;

@FunctionalInterface
public interface Processor<T, R> {

    R process(T var1);

}

