# VolumeApi

All URIs are relative to *http://localhost/v1.47*

| Method                                          | HTTP request               | Description                                                         |
|-------------------------------------------------|----------------------------|---------------------------------------------------------------------|
| [**volumeCreate**](VolumeApi.md#volumeCreate)   | **POST** /volumes/create   | Create a volume                                                     |
| [**volumeDelete**](VolumeApi.md#volumeDelete)   | **DELETE** /volumes/{name} | Remove a volume                                                     |
| [**volumeInspect**](VolumeApi.md#volumeInspect) | **GET** /volumes/{name}    | Inspect a volume                                                    |
| [**volumeList**](VolumeApi.md#volumeList)       | **GET** /volumes           | List volumes                                                        |
| [**volumePrune**](VolumeApi.md#volumePrune)     | **POST** /volumes/prune    | Delete unused volumes                                               |
| [**volumeUpdate**](VolumeApi.md#volumeUpdate)   | **PUT** /volumes/{name}    | \&quot;Update a volume. Valid only for Swarm cluster volumes\&quot; |

<a id="volumeCreate"></a>

# **volumeCreate**

> Volume volumeCreate(volumeConfig)

Create a volume

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = VolumeApi()
val volumeConfig : VolumeCreateOptions =  // VolumeCreateOptions | Volume configuration
try {
    val result : Volume = apiInstance.volumeCreate(volumeConfig)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling VolumeApi#volumeCreate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling VolumeApi#volumeCreate")
    e.printStackTrace()
}
```

### Parameters

| Name             | Type                                              | Description          | Notes |
|------------------|---------------------------------------------------|----------------------|-------|
| **volumeConfig** | [**VolumeCreateOptions**](VolumeCreateOptions.md) | Volume configuration |       |

### Return type

[**Volume**](Volume.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a id="volumeDelete"></a>

# **volumeDelete**

> volumeDelete(name, force)

Remove a volume

Instruct the driver to remove the volume.

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = VolumeApi()
val name : kotlin.String = name_example // kotlin.String | Volume name or ID
val force : kotlin.Boolean = true // kotlin.Boolean | Force the removal of the volume
try {
    apiInstance.volumeDelete(name, force)
} catch (e: ClientException) {
    println("4xx response calling VolumeApi#volumeDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling VolumeApi#volumeDelete")
    e.printStackTrace()
}
```

### Parameters

| **name** | **kotlin.String**| Volume name or ID | |
| Name | Type | Description | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **force** | **kotlin.Boolean**| Force the removal of the volume | [optional] [default to false] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json, text/plain

<a id="volumeInspect"></a>

# **volumeInspect**

> Volume volumeInspect(name)

Inspect a volume

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = VolumeApi()
val name : kotlin.String = name_example // kotlin.String | Volume name or ID
try {
    val result : Volume = apiInstance.volumeInspect(name)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling VolumeApi#volumeInspect")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling VolumeApi#volumeInspect")
    e.printStackTrace()
}
```

### Parameters

| Name     | Type              | Description       | Notes |
|----------|-------------------|-------------------|-------|
| **name** | **kotlin.String** | Volume name or ID |       |

### Return type

[**Volume**](Volume.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a id="volumeList"></a>

# **volumeList**

> VolumeListResponse volumeList(filters)

List volumes

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = VolumeApi()
val filters : kotlin.String = filters_example // kotlin.String | JSON encoded value of the filters (a `map[string][]string`) to process on the volumes list. Available filters:  - `dangling=<boolean>` When set to `true` (or `1`), returns all    volumes that are not in use by a container. When set to `false`    (or `0`), only volumes that are in use by one or more    containers are returned. - `driver=<volume-driver-name>` Matches volumes based on their driver. - `label=<key>` or `label=<key>:<value>` Matches volumes based on    the presence of a `label` alone or a `label` and a value. - `name=<volume-name>` Matches all or part of a volume name. 
try {
    val result : VolumeListResponse = apiInstance.volumeList(filters)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling VolumeApi#volumeList")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling VolumeApi#volumeList")
    e.printStackTrace()
}
```

### Parameters

| Name        | Type              | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Notes      |
|-------------|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| **filters** | **kotlin.String** | JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the volumes list. Available filters:  - &#x60;dangling&#x3D;&lt;boolean&gt;&#x60; When set to &#x60;true&#x60; (or &#x60;1&#x60;), returns all    volumes that are not in use by a container. When set to &#x60;false&#x60;    (or &#x60;0&#x60;), only volumes that are in use by one or more    containers are returned. - &#x60;driver&#x3D;&lt;volume-driver-name&gt;&#x60; Matches volumes based on their driver. - &#x60;label&#x3D;&lt;key&gt;&#x60; or &#x60;label&#x3D;&lt;key&gt;:&lt;value&gt;&#x60; Matches volumes based on    the presence of a &#x60;label&#x60; alone or a &#x60;label&#x60; and a value. - &#x60;name&#x3D;&lt;volume-name&gt;&#x60; Matches all or part of a volume name. | [optional] |

### Return type

[**VolumeListResponse**](VolumeListResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a id="volumePrune"></a>

# **volumePrune**

> VolumePruneResponse volumePrune(filters)

Delete unused volumes

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = VolumeApi()
val filters : kotlin.String = filters_example // kotlin.String | Filters to process on the prune list, encoded as JSON (a `map[string][]string`).  Available filters: - `label` (`label=<key>`, `label=<key>=<value>`, `label!=<key>`, or `label!=<key>=<value>`) Prune volumes with (or without, in case `label!=...` is used) the specified labels. - `all` (`all=true`) - Consider all (local) volumes for pruning and not just anonymous volumes. 
try {
    val result : VolumePruneResponse = apiInstance.volumePrune(filters)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling VolumeApi#volumePrune")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling VolumeApi#volumePrune")
    e.printStackTrace()
}
```

### Parameters

| Name        | Type              | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Notes      |
|-------------|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| **filters** | **kotlin.String** | Filters to process on the prune list, encoded as JSON (a &#x60;map[string][]string&#x60;).  Available filters: - &#x60;label&#x60; (&#x60;label&#x3D;&lt;key&gt;&#x60;, &#x60;label&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;, &#x60;label!&#x3D;&lt;key&gt;&#x60;, or &#x60;label!&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;) Prune volumes with (or without, in case &#x60;label!&#x3D;...&#x60; is used) the specified labels. - &#x60;all&#x60; (&#x60;all&#x3D;true&#x60;) - Consider all (local) volumes for pruning and not just anonymous volumes. | [optional] |

### Return type

[**VolumePruneResponse**](VolumePruneResponse.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a id="volumeUpdate"></a>

# **volumeUpdate**

> volumeUpdate(name, version, body)

\&quot;Update a volume. Valid only for Swarm cluster volumes\&quot;

### Example

```kotlin
// Import classes:
//import io.github.xzima.docomagos.docker.infrastructure.*
//import io.github.xzima.docomagos.docker.models.*

val apiInstance = VolumeApi()
val name : kotlin.String = name_example // kotlin.String | The name or ID of the volume
val version : kotlin.Long = 789 // kotlin.Long | The version number of the volume being updated. This is required to avoid conflicting writes. Found in the volume's `ClusterVolume` field. 
val body : VolumeUpdateRequest =  // VolumeUpdateRequest | The spec of the volume to update. Currently, only Availability may change. All other fields must remain unchanged. 
try {
    apiInstance.volumeUpdate(name, version, body)
} catch (e: ClientException) {
    println("4xx response calling VolumeApi#volumeUpdate")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling VolumeApi#volumeUpdate")
    e.printStackTrace()
}
```

### Parameters

| **name** | **kotlin.String**| The name or ID of the volume | |
| **version** | **kotlin.Long**| The version number of the volume being updated. This is required to avoid conflicting
writes. Found in the volume&#39;s &#x60;ClusterVolume&#x60; field. | |
| Name | Type | Description | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | [**VolumeUpdateRequest**](VolumeUpdateRequest.md)| The spec of the volume to update. Currently, only
Availability may change. All other fields must remain unchanged. | [optional] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

