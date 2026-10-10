---
title: Measuring the Application
permalink: en/learning-framework/measuring-the-application
abstract: >- # this means to ignore newlines until "baseurl:"
  The Workforce Managment Data Management Framework (WFM-DMF) fundamentally transforms how organizations handle and interact with data by allowing direct access to business and reference information at its source, bypassing the need for traditional ETL (Extract, Transform, Load) pipelines. 
---

`/actuator/metrics/http.server.requests` provides **aggregated statistics about HTTP requests handled by WFM-DMF**—how many requests completed, how much time they took, and which routes and response statuses were involved. Spring Boot collects this through Micrometer. [docs.spring.io](https://docs.spring.io/spring-boot/reference/actuator/metrics.html?utm_source=chatgpt.com)

An illustrative response looks like:

```json
{
  "name": "http.server.requests",
  "baseUnit": "seconds",
  "measurements": [
    { "statistic": "COUNT", "value": 1200 },
    { "statistic": "TOTAL_TIME", "value": 360.0 },
    { "statistic": "MAX", "value": 4.8 }
  ],
  "availableTags": [
    { "tag": "method", "values": ["GET", "POST"] },
    { "tag": "status", "values": ["200", "400", "500"] },
    { "tag": "uri", "values": ["/api/v1/dmf/authorizations/{userid}"] },
    { "tag": "outcome", "values": ["SUCCESS", "CLIENT_ERROR", "SERVER_ERROR"] }
  ]
}
```

The main measurements mean:

| Measurement | Meaning | Interpretation of example |
|---|---|---|
| `COUNT` | Number of recorded, completed requests | 1,200 requests |
| `TOTAL_TIME` | Sum of their processing durations | 360 seconds combined |
| `MAX` | Maximum recorded duration within a configurable time window | 4.8 seconds |

For this example, the **average response time** is `TOTAL_TIME / COUNT = 0.3 seconds`, or **300 ms**. Combined processing time can exceed elapsed clock time because requests run concurrently. Counts and totals depend on the registry; with the usual in-memory cumulative registry, they accumulate during the current application run. `MAX` is a windowed value and can return to zero after inactivity—it is not necessarily the slowest request since startup. [docs.micrometer.io](https://docs.micrometer.io/micrometer/reference/1.16/concepts/timers.html?utm_source=chatgpt.com)

**You can filter the statistics using tags.**

| Tag | What you can select |
|---|---|
| `uri` | A route, usually its template, such as `/authorizations/{userid}` |
| `method` | `GET`, `POST`, `PUT`, `DELETE`, etc. |
| `status` | An HTTP response code, such as `200` or `500` |
| `outcome` | A status category, such as `SUCCESS` or `SERVER_ERROR` |
| `exception` | Recorded exception type, when available |

The actual tag names and values appear in `availableTags`. Route templates group requests for different IDs together; query parameters ordinarily do not create separate route metrics. [docs.spring.io](https://docs.spring.io/spring-boot/reference/actuator/metrics.html?utm_source=chatgpt.com)

For example, to inspect **all POST requests**:

```text
http://localhost:9090/actuator/metrics/http.server.requests?tag=method:POST
```

To inspect **server errors**:

```text
http://localhost:9090/actuator/metrics/http.server.requests?tag=outcome:SERVER_ERROR
```

To inspect **successful GET requests for a particular WFM-DMF route**, copy its exact `uri` value from `availableTags`:

```bash
curl --get \
  'http://localhost:9090/actuator/metrics/http.server.requests' \
  --data-urlencode 'tag=uri:/api/v1/dyn/sql/wfm-bridge/sdss-pending-requests' \
  --data-urlencode 'tag=method:GET' \
  --data-urlencode 'tag=status:200'
```

Multiple `tag` parameters narrow the selection together. Without filters, the endpoint aggregates matching meters across routes, methods, and statuses. [docs.spring.io](https://docs.spring.io/spring-boot/api/rest/actuator/metrics.html?utm_source=chatgpt.com)

For WFM-DMF, this would help answer: **How frequently is an API called? How long does it take? How many calls fail?** Its HTTP count measures API calls, so one bulk upload processing 1,000 rows ordinarily contributes one request.

It does not provide individual request timestamps, users, payloads, SQL details, or a historical activity log. With multiple Cirrus instances, the response represents the instance serving that request; a monitoring backend is needed to combine instances and retain trends. Also, request throughput requires comparing counts over time—for example, an increase of 600 requests in 60 seconds means **10 requests/second**.