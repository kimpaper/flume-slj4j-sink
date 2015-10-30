# flume-slj4j-sink
flume에서 전달받은 event를 순수 slj4j를 통해 남겨주는 sink

# build
maven clean package

# install
cp ./target/flume-slj4j-sink.jar $FLUME_HOME/lib/

## flume-agent1.conf config example
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

#### sinks.{name}.logLevel
- logger에 남길때 level을 지정   
  (trace, debug, info, warn, error)



