#! /bin/sh
export SPREADS="~/Dropbox/Covert/Finance/Options/SpreadsMonitoring/spreads.txt"
export CLASSPATH="../lib/*;../lib/macos64/*;../out/production/market-monitor"
java -cp $CLASSPATH co.kuznetsov.market.Main $SPREADS