package kimpaper.flume.sink;

import com.google.common.base.Strings;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paper on 2015. 10. 30..
 */
public class Slj4jSink extends AbstractSink implements Configurable {
    private static final Logger logger = LoggerFactory.getLogger(Slj4jSink.class);
    public static final String CONFIG_KEY = "logLevel";
    
    private AppendHandler _appendHandler;
    
    @Override
    public void configure(Context context) {
        String logLevel = context.getString(CONFIG_KEY, "info").trim();
        if (!Strings.isNullOrEmpty(logLevel)) {
            if(logLevel.equals("trace") || logLevel.equals("debug") || logLevel.equals("error") || logLevel.equals("warn")) {
                // nothing...
            }
            else {
                logLevel = "info";
            }
        }
        
        if(logLevel.equals("trace")) {
            _appendHandler = new AppendHandler() {
                @Override
                public void append(String event) {
                    logger.trace(event);
                }

                @Override
                public boolean isEnabled() {
                    return logger.isTraceEnabled();
                }
            };
        }
        else if(logLevel.equals("debug")) {
            _appendHandler = new AppendHandler() {
                @Override
                public void append(String event) {
                    logger.debug(event);
                }

                @Override
                public boolean isEnabled() {
                    return logger.isDebugEnabled();
                }
            };
        }
        else if(logLevel.equals("info")) {
            _appendHandler = new AppendHandler() {
                @Override
                public void append(String event) {
                    logger.info(event);
                }

                @Override
                public boolean isEnabled() {
                    return logger.isInfoEnabled();
                }
            };
        }
        else if(logLevel.equals("warn")) {
            _appendHandler = new AppendHandler() {
                @Override
                public void append(String event) {
                    logger.warn(event);
                }

                @Override
                public boolean isEnabled() {
                    return logger.isWarnEnabled();
                }
            };
        }
        else if(logLevel.equals("error")) {
            _appendHandler = new AppendHandler() {
                @Override
                public void append(String event) {
                    logger.error(event);
                }

                @Override
                public boolean isEnabled() {
                    return logger.isErrorEnabled();
                }
            };
        }
    }
    
    
    

    @Override
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event = null;

        try {
            transaction.begin();
            event = channel.take();
            
            if (event != null) {
                String eventString = new String(event.getBody());
                if(_appendHandler.isEnabled()) {
                    _appendHandler.append(eventString);
                }
            } else {
                result = Status.BACKOFF;
            }
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw new EventDeliveryException("Failed to log event: " + event, ex);
        } finally {
            transaction.close();
        }

        return result;
    }
    
}
