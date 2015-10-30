package kimpaper.flume.sink;

/**
 * Created by paper on 2015. 10. 30..
 */
public interface AppendHandler {
    void append(String event);
    boolean isEnabled();
}
