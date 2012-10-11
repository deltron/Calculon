create table eod_temp as 
select  p.id,
        symbol,
        exchange,
        dt,
        open,
        high,
        low,
        close,
        volume,
        drift,
        drift_pct,
        adv_10d,
        adv_30d,
        adv_60d,
        volatility_10d,
        volatility_30d,
        volatility_60d,
        sma_20d,
        sma_50d,
        sma_200d,
        beta_spx_60d,
        beta_spx_200d
from eod_prices p, eod_derived d 
where p.id = d.id;

\copy eod_temp to 'eod.csv' with CSV

drop table eod_temp
