#! /bin/sh
export SPREADS=~/Dropbox/Covert/Finance/Options/SpreadsMonitoring/spreads.txt
export CLASSPATH=../lib/*:../sounds:../lib/linux64/*:../out/production/market-monitor
java -Xmx48m -cp $CLASSPATH co.kuznetsov.market.Main $SPREADS