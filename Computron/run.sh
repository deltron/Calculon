#!/bin/sh

# for remote monitoring
# -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=3333 \
# -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false \

java -Xmx48M -server \
     -XX:CompileThreshold=10 -XX:+UseAdaptiveSizePolicy \
     -XX:+AggressiveOpts -XX:+UseFastAccessorMethods \
     -jar dist/Computron.jar
