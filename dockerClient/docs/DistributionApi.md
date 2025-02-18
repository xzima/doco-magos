# DistributionApi

All URIs are relative to *http://localhost/v1.47*

| Method                                                            | HTTP request                      | Description                             |
|-------------------------------------------------------------------|-----------------------------------|-----------------------------------------|
| [**distributionInspect**](DistributionApi.md#distributionInspect) | **GET** /distribution/{name}/json | Get image information from the registry |

<a id="distributionInspect"></a>

# **distributionInspect**

> DistributionInspect distributionInspect(name)

Get image information from the registry

Return image digest and platform information by contacting the registry.

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = DistributionApi()
val name : kotlin.String = name_example // kotlin.String | Image name or id
try {
    val result : DistributionInspect = apiInstance.distributionInspect(name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DistributionApi#distributionInspect")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DistributionApi#distributionInspect")
    e.printStackTrace()
}
```

### Parameters

| Name     | Type              | Description      | Notes |
|----------|-------------------|------------------|-------|
| **name** | **kotlin.String** | Image name or id |       |

### Return type

[**DistributionInspect**](DistributionInspect.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

