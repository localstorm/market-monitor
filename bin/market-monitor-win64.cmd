SET SPREADS="C:\Documents and Settings\localstorm\Dropbox\Covert\Finance\Options\SpreadsMonitoring\spreads.txt"
SET CLASSPATH="../lib/*;../lib/win64/*;../out/production/market-monitor"
SET JAVA="C:\Program Files\Java\jdk1.7.0_15\bin\java"
%JAVA% -cp %CLASSPATH% co.kuznetsov.market.Main %SPREADS% 