# ExecApi

All URIs are relative to *http://localhost/v1.47*

| Method                                        | HTTP request                   | Description              |
|-----------------------------------------------|--------------------------------|--------------------------|
| [**containerExec**](ExecApi.md#containerExec) | **POST** /containers/{id}/exec | Create an exec instance  |
| [**execInspect**](ExecApi.md#execInspect)     | **GET** /exec/{id}/json        | Inspect an exec instance |
| [**execResize**](ExecApi.md#execResize)       | **POST** /exec/{id}/resize     | Resize an exec instance  |
| [**execStart**](ExecApi.md#execStart)         | **POST** /exec/{id}/start      | Start an exec instance   |

<a id="containerExec"></a>

# **containerExec**

> IdResponse containerExec(id, execConfig)

Create an exec instance

Run a command inside a running container.

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = ExecApi()
val id : kotlin.String = id_example // kotlin.String | ID or name of container
val execConfig : ExecConfig =  // ExecConfig | Exec configuration
try {
    val result : IdResponse = apiInstance.containerExec(id, execConfig)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ExecApi#containerExec")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ExecApi#containerExec")
    e.printStackTrace()
}
```

### Parameters

| **id** | **kotlin.String**| ID or name of container | |
| Name | Type | Description | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **execConfig** | [**ExecConfig**](ExecConfig.md)| Exec configuration | |

### Return type

[**IdResponse**](IdResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a id="execInspect"></a>

# **execInspect**

> ExecInspectResponse execInspect(id)

Inspect an exec instance

Return low-level information about an exec instance.

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = ExecApi()
val id : kotlin.String = id_example // kotlin.String | Exec instance ID
try {
    val result : ExecInspectResponse = apiInstance.execInspect(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ExecApi#execInspect")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ExecApi#execInspect")
    e.printStackTrace()
}
```

### Parameters

| Name   | Type              | Description      | Notes |
|--------|-------------------|------------------|-------|
| **id** | **kotlin.String** | Exec instance ID |       |

### Return type

[**ExecInspectResponse**](ExecInspectResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a id="execResize"></a>

# **execResize**

> execResize(id, h, w)

Resize an exec instance

Resize the TTY session used by an exec instance. This endpoint only works if &#x60;tty&#x60; was specified as part of
creating and starting the exec instance.

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = ExecApi()
val id : kotlin.String = id_example // kotlin.String | Exec instance ID
val h : kotlin.Int = 56 // kotlin.Int | Height of the TTY session in characters
val w : kotlin.Int = 56 // kotlin.Int | Width of the TTY session in characters
try {
    apiInstance.execResize(id, h, w)
} catch (e: ClientException) {
    println("4xx response calling ExecApi#execResize")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ExecApi#execResize")
    e.printStackTrace()
}
```

### Parameters

| **id** | **kotlin.String**| Exec instance ID | |
| **h** | **kotlin.Int**| Height of the TTY session in characters | |
| Name | Type | Description | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **w** | **kotlin.Int**| Width of the TTY session in characters | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, text/plain

<a id="execStart"></a>

# **execStart**

> execStart(id, execStartConfig)

Start an exec instance

Starts a previously set up exec instance. If detach is true, this endpoint returns immediately after starting the
command. Otherwise, it sets up an interactive session with the command.

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = ExecApi()
val id : kotlin.String = id_example // kotlin.String | Exec instance ID
val execStartConfig : ExecStartConfig =  // ExecStartConfig | 
try {
    apiInstance.execStart(id, execStartConfig)
} catch (e: ClientException) {
    println("4xx response calling ExecApi#execStart")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ExecApi#execStart")
    e.printStackTrace()
}
```

### Parameters

| **id** | **kotlin.String**| Exec instance ID | |
| Name | Type | Description | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **execStartConfig** | [**ExecStartConfig**](ExecStartConfig.md)| | [optional] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/vnd.docker.raw-stream, application/vnd.docker.multiplexed-stream

