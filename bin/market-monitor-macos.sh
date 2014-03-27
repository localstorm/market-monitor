#! /bin/sh
export SPREADS=~/Dropbox/Covert/Finance/Options/SpreadsMonitoring/spreads.txt
export CLASSPATH=../lib/*:../sounds:../lib/macos64/*:../out/production/market-monitor

java $1 -Xmx48m -XstartOnFirstThread -cp $CLASSPATH co.kuznetsov.market.Main $SPREADS
