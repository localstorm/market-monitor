market-monitor
==============

Tool to monitor S&amp;P 500, Russel 2000, Nasdaq Composite, VIX and other indices

It sits in system notification area (Windows/MacOS are officially supported, Linux is doable too due to a cross-platform nature of SWT library) and monitors if any index is about to breach the spread configured in text file (I put this text file into Dropbox) so it is synchronized between my Windows laptop at office and MacOS laptop at home.

The whole thing is currently built for one sole purpose: to automate option spreads positions monitoring.

To do this I source data from Yahoo Finance and the icon in system notification area changes its color from 
green to amber and from amber gradually to red:

![Market Closed](/docs/images/market-closed.png "Market Closed")

It also writes current index values to console: 

![Console](/docs/images/console.png "Console")

This tool greatly simplifies monitoring multiple non-directional trades and identify moments where manual intervention is necessary.  
