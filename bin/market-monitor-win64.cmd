SET SPREADS=C:\Users\localstorm\Dropbox\Covert\Finance\Options\SpreadsMonitoring\*.txt
SET CLASSPATH="../lib/*;../lib/win64/*;../sounds;../out/production/market-monitor"
SET JAVA="C:\Program Files\Java\jdk1.7.0_15\bin\java"
%JAVA% %1 -Xmx48m -cp %CLASSPATH% co.kuznetsov.market.Main %SPREADS%