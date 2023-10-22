@All
Feature: f0001 verification of get-chart endpoint in version 3

    @Positive
    Scenario Outline: 01.1 Using all required parameters
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | 1mo                   |
            | symbol               | <symbol>              |
            | range                | 5y                    |
            | region               | US                    |
            | includePrePost       | false                 |
            | useYfid              | true                  |
            | includeAdjustedClose | true                  |
            | events               | capitalGain,div,split |
        Then I receive the request response code 200
        And the response structure has the same content as the "schemas/getChartV3Schema.json" schema
        And the response contains the following values:
            | chart.result[0].meta.exchangeTimezoneName | America/New_York |
            | chart.result[0].meta.symbol               | <symbol>         |
        And value in field "chart.error" is null
        Examples:
            | symbol |
            | AMR    |
            | NVDA   |

    @Negative
    Scenario Outline: 01.3 Using endpoint without required parameter - interval
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | <interval>            |
            | symbol               | <symbol>              |
            | range                | <range>               |
            | region               | US                    |
            | includePrePost       | false                 |
            | useYfid              | true                  |
            | includeAdjustedClose | true                  |
            | events               | capitalGain,div,split |
        Then I receive the request response code 200
        And value in field "chart.result" is null
        And the response contains the following values:
            | chart.error.code | Unprocessable Entity |
        Examples:
            | interval | symbol | range |
            |          | NVDA   | 5y    |


    @Negative
    Scenario Outline: 01.4 Using endpoint without required parameter - symbol
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | <interval>            |
            | range                | <range>               |
            | region               | US                    |
            | includePrePost       | false                 |
            | useYfid              | true                  |
            | includeAdjustedClose | true                  |
            | events               | capitalGain,div,split |
        Then I receive the request response code 0
        Examples:
            | interval | range |
            | 1mo      | 5y    |

    @Negative
    Scenario Outline: 01.5 Using endpoint without required parameter - range
        # this should return an error as the required parameter is not provided
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | <interval>            |
            | symbol               | <symbol>              |
            | region               | US                    |
            | includePrePost       | false                 |
            | useYfid              | true                  |
            | includeAdjustedClose | true                  |
            | events               | capitalGain,div,split |
        Then I receive the request response code 200
        And value in field "chart.result" is null
        And the response contains the following values:
            | chart.error.code | Unprocessable Entity |
        Examples:
            | interval | symbol |
            | 1mo      | NVDA   |


    @Positive
    Scenario Outline: 01.6 Using required parameters and optional parameters
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | <interval>             |
            | symbol               | <symbol>               |
            | range                | <range>                |

            | region               | <region>               |
            | period1              | <period1>              |
            | period2              | <period2>              |
            | comparisons          | <comparisons>          |
            | includePrePost       | <includePrePost>       |
            | useYfid              | <useYfid>              |
            | includeAdjustedClose | <includeAdjustedClose> |
            | events               | <events>               |
        Then I receive the request response code 200
        And the response structure has the same content as the "schemas/getChartV3Schema.json" schema
        And the response contains the following values:
            | chart.result[0].meta.exchangeTimezoneName | America/New_York |
            | chart.result[0].meta.symbol               | <symbol>         |
        And value in field "chart.error" is null
        Examples:
            | interval | symbol | range | region | period1    | period2    | comparisons | includePrePost | useYfid | includeAdjustedClose | events |
            | 1m       | NVDA   | max   |        |            |            |             |                |         |                      |        |
            | 1d       | NVDA   |       | DE     | 1556816400 | 1562170150 | ^FCHI       | true           | false   | false                | split  |


    @Negative
    Scenario Outline: 01.7 Using required parameters and optional parameters with incorrect values in period1 and period2
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | <interval>             |
            | symbol               | <symbol>               |
            | range                | <range>                |

            | region               | <region>               |
            | period1              | <period1>              |
            | period2              | <period2>              |
            | comparisons          | <comparisons>          |
            | includePrePost       | <includePrePost>       |
            | useYfid              | <useYfid>              |
            | includeAdjustedClose | <includeAdjustedClose> |
            | events               | <events>               |
        Then I receive the request response code 200
        And the response contains the following values:
            | chart.error.code        | Bad Request                                                                                       |
            | chart.error.description | Invalid input - start date cannot be after end date. startDate = 1562170150, endDate = 1556816400 |
        And value in field "chart.results" is null
        Examples:
            | interval | symbol | range | region | period1    | period2    | comparisons | includePrePost | useYfid | includeAdjustedClose | events |
            | 1d       | NVDA   |       | DE     | 1562170150 | 1556816400 | ^FCHI       | true           | false   | false                | split  |


    @Positive
    Scenario Outline: 01.8 Using fixed values for period1 and period2
        Given I am an "authorized" user
        When I perform a GET request for "stock/v3/get-chart" with parameters:
            | interval             | <interval>             |
            | symbol               | <symbol>               |
            | range                | <range>                |

            | region               | <region>               |
            | period1              | <period1>              |
            | period2              | <period2>              |
            | comparisons          | <comparisons>          |
            | includePrePost       | <includePrePost>       |
            | useYfid              | <useYfid>              |
            | includeAdjustedClose | <includeAdjustedClose> |
            | events               | <events>               |
        Then I receive the request response code 200
        And the response contains the following values:
            | chart.result[0].meta.exchangeTimezoneName | America/New_York |
            | chart.result[0].meta.symbol               | <symbol>         |
        And value in field "chart.error" is null
        Examples:
            | interval | symbol | range | region | period1    | period2    | comparisons | includePrePost | useYfid | includeAdjustedClose | events |
            | 1y       | NVDA   | 1m    | US     | 1609538400 | 1640991600 |             |                |         |                      |        |
