# flume-slj4j-sink
flume에서 전달받은 event를 slj4j를 통해 남겨주는 sink
- 기존 `type = logger` 다른 점: EventDump를 통하여 남기는게 아니라 순수 String 으로 남긴다.

## build
```bash
maven clean package
```

## install
```bash
cp ./target/flume-slj4j-sink.jar $FLUME_HOME/lib/
```

## use
### flume-agent1.conf config example
```bash
agent1.sources = r1
agent1.channels = c1
agent1.sinks = k1

agent1.sources.r1.type = avro
agent1.sources.r1.bind = 0.0.0.0
agent1.sources.r1.port = 4545
agent1.sources.r1.channels = c1

agent1.channels.c1.type = memory
agent1.channels.c1.capacity = 10000
agent1.channels.c1.transactionCapacity = 1000

agent1.sinks.k1.type = kimpaper.flume.sink.Slj4jSink
agent1.sinks.k1.logLevel = info
agent1.sinks.k1.channel = c1
```

### sinks.{name}.logLevel
- logger에 남길때 level을 지정   
  (trace, debug, info, warn, error)

### 원래 있는 log4j.properties를 아래로 수정
```bash
...
log4j.appender.LOGFILE=org.apache.log4j.RollingFileAppender
log4j.appender.LOGFILE.MaxFileSize=100MB
log4j.appender.LOGFILE.MaxBackupIndex=10
log4j.appender.LOGFILE.File=${flume.log.dir}/${flume.log.file}
log4j.appender.LOGFILE.layout=org.apache.log4j.PatternLayout
#log4j.appender.LOGFILE.layout.ConversionPattern=%d{dd MMM yyyy HH:mm:ss,SSS} %-5p [%t] (%C.%M:%L) %x - %m%n
log4j.appender.LOGFILE.layout.ConversionPattern=%m%n
...
```


