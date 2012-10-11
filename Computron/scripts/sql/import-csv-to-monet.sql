CREATE TABLE "eod" (
        "id" bigint,
        "symbol" varchar(10),
        "exchange" varchar(4),
        "dt" date,
        "open" decimal(11,4),
        "high" decimal(11,4),
        "low" decimal(11,4),
        "close" decimal(11,4),
        "volume" decimal(11),
        "drift" decimal(11,4),
        "drift_pct" decimal(8,6),
        "adv_10d" decimal(11,1),
        "adv_30d" decimal(11,1),
        "adv_60d" decimal(11,1),
        "volatility_10d" decimal(8,6),
        "volatility_30d" decimal(8,6),
        "volatility_60d" decimal(8,6),
        "sma_20d" decimal(11,4),
        "sma_50d" decimal(11,4),
        "sma_200d" decimal(11,4),
        "beta_spx_60d" decimal(8,6),
        "beta_spx_200d" decimal(8,6)
);

copy 3000000 records into eod from '/root/eod.csv' using delimiters ',','\n';
