# Cache Sample Project
Cache Sample Project is cache the json response data from external url.

### Technologies
* Java 17
* Maven
* Spring boot version 3.3.3
* Redis Cache
* lombok

### External url
`https://api.mfapi.in/mf/100077`

### Purpose
To get and respond to the json response data from external url(`https://api.mfapi.in/mf/100077`) based on filter for the first request.
further request get and respond to the json response data from cache based on the filter.
###### Filter:
* 1W -> Last 1 Week Data
* 1M -> Last 1 Month Data
* 1Y -> Last 1 Year Data
* 5Y -> Last 5 Years data