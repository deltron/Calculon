********************
* INFO             *
********************

This is the source code for a trading system I co-wrote in 2008. It can
also backtest strategies and optimize trading variables. Many of the 
metrics and principles are based on the Turtle Trading System which
aims to detect and follow trends in commodity markets.

The architecture is Java based with a functional slant from what was
at the time called Google Collections (now called Guava).

A few interesting details are the use of MonetDB as a high-speed column 
database for storing stock prices and the integration of OAT (Optimization 
Algorithm Toolkit) for plugging in every imaginable kind of optimizer.
To borrow some horsepower on the cheap I would upload and run everything
on Amazon's EC2 cloud platform.

One of the examples I left in the code is a mean-reversion strategy I 
called "Gapper" -- the idea is to discover the optimal gap size and 
recovery time as a trading strategy. For example, if Stock A opens down 5% 
will it recover in 3 days? Or is it better to buy stocks that open down 8% 
and wait 5 days? Run the optimizer to find out!

I'm putting this up in case anyone might find something useful here,
and also as a little trip down memory lane for myself. I'm releasing
the code into the public domain as per the license below. In short,
you can do whatever you want with it, just don't sue me!


********************
* LICENSE          *
********************

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>

