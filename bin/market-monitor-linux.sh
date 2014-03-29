#! /bin/sh
export SPREADS=~/Dropbox/Covert/Finance/Options/SpreadsMonitoring/*.txt
export CLASSPATH=../lib/*:../sounds:../lib/linux64/*:../out/production/market-monitor
java $1 -Xmx48m -cp $CLASSPATH co.kuznetsov.market.Main $SPREADS