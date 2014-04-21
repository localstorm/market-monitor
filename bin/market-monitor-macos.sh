#! /bin/sh
export SPREADS1='/Users/localstorm/Dropbox/Covert/Finance/Options/SpreadsMonitoring/LongTerm(CS).txt'
export SPREADS2='/Users/localstorm/Dropbox/Covert/Finance/Options/SpreadsMonitoring/ShortTerm(CS).txt'
export CLASSPATH=../lib/*:../sounds:../lib/macos64/*:../out/production/market-monitor

java $1 -Xmx48m -XstartOnFirstThread -cp $CLASSPATH co.kuznetsov.market.Main "$SPREADS1" "$SPREADS2"
