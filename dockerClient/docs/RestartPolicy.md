# RestartPolicy

## Properties

| Name                  | Type                | Description                                                                                                                                                                                                                                                                                              | Notes      |
|-----------------------|---------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------|
| **name**              | [**inline**](#Name) | - Empty string means not to restart - &#x60;no&#x60; Do not automatically restart - &#x60;always&#x60; Always restart - &#x60;unless-stopped&#x60; Restart always except when the user has manually stopped the container - &#x60;on-failure&#x60; Restart only when the container exit code is non-zero | [optional] |
| **maximumRetryCount** | **kotlin.Int**      | If &#x60;on-failure&#x60; is used, the number of times to retry before giving up.                                                                                                                                                                                                                        | [optional] |

<a id="Name"></a>

## Enum: Name

| Name | Value                                    |
|------|------------------------------------------|
| name | , no, always, unless-stopped, on-failure |



