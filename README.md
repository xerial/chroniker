# Chroniker

_Chroniker_ is a framework for simplifying your batch job pipelines in Scala

## Examples

```
import xerial.chroniker._

import sampledb._

// SELECT count(*) FROM nasdaq
def dataCount = nasdaq.size

// SELECT time, close FROM nasdaq WHERE symbol = 'APPL'
def appleStock = nasdaq.filter(_.symbol == "APPL").select(_.time, _.close)

// You can use a raw SQL statement as well:
def appleStockSQL = sql"SELECT time, close FROM nasdaq where symbol = 'APPL'"

// SELECT time, close FROM nasdaq WHERE symbol = 'APPL' LIMIT 10
appleStock.limit(10).print

// time column based filtering
appleStock.between('2015-05-01', '2015-06-01')


```

## Milestones

 - Build SQL + local analysis workflows
 - Submit queries to Presto / Treasure Data
 - Run scheduled queries
 - Retry upon failures
 - Cache intermediate results
 - Resume workflows
 - Partial workflow executions
 - Sampling display

 - Windowing for stream queries

 - Object-oriented workflow

 - Input Source: fluentd/embulk
 - Output Source:

 - Workflow Executor
   - Local-only mode
   - Register SQL part to Treasure Data
   - Run complex analysis on local cache

