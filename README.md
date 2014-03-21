market-monitor
==============

Tool to monitor S&amp;P 500, Russel 2000, Nasdaq Composite, VIX and other indices

It sits in system notification area (Windows/MacOS are supported, Linux is doable too) and 
monitors if any index is about to breach the spread configured in text file (I put this text file into Dropbox) 
so it is synchronized between my Windows laptop at office and MacOS laptop at home.

The whole thing is currently built for one sole purpose: to automonitor Iron Condor option strategy trading.

To do this I source data from Yahoo Finance and the icon in system notification area changes its color from 
green to amber and from amber gradually to red.
