This app builds 2 functions, built using different approaches:
* `lowercase` - built as a generic function, can be run in any function service or locally
* `uppercase` - built as a function integrated closely with the Azure CLI, can be run in Microsoft Azure or locally

The 2 approaches wish to illustrate a generic approach vs tight integration with a cloud provider API.

This document looks into:
* building and running the functions to Azure
* deployment to Azure Functions
* debugging in a local environment

You can run these Azure functions locally, similar to other Spring Cloud Function samples, however this time by using the Azure Maven plugin, as the Microsoft Azure functions execution context must be available.

## Build and package 
```shell
# Build and package 
$ mvn clean package 

# Previously, for other examples
$ mvn spring-boot:run

# For Azure Functions
$ mvn clean package azure-functions:run

or 

$ mvn azure-functions:run
```

The `uppercase` function takes `Function<String, String> uppercase()` and it's input is JSON, therefore we need to
provide the appropriate content-type (in this case `application/json`). The function iterates then over each element and returns its `uppercase` mapped value.

Test the function using cURL or HTTPie and notice that the URL is formed by concatenating `<function url>/api/<function name>`
```shell
# testing with cURL
$ curl -H "Content-Type: application/json" localhost:7071/api/uppercase -d '{"greeting": "hello", "name": "your name"}'

# testing with HTTPie
$ http POST localhost:7071/api/uppercase greeting=hello name='your name'

# result
{
  "greeting": "HELLO",
  "name": "YOUR NAME"
}
```

The `lowercase` function follows a similar approach for testing, with different endpoints and test cases:
```shell
# testing with cURL
$ curl -H "Content-Type: application/json" localhost:7071/api/lowercase -d '{"greeting": "HELLO", "name": "Your name"}'

# testing with HTTPie
$ http POST localhost:7071/api/uppercase greeting=HELLO name='Your name'

# result
{
  "greeting": "hello",
  "name": "your name"
}
```
## Deploy to Azure
To run locally on top of Azure Functions, and to deploy to your live Azure environment, you will need the Azure Functions Core Tools installed along with the Azure CLI (see https://docs.microsoft.com/en-us/azure/azure-functions/functions-create-first-java-maven for more details).

To deploy the functions to your live Azure environment, including an automatic provisioning of an HTTPTrigger for the functions:
```shell
# login to Azure from the CLI
$ az login

# deploy the function
$ mvn azure-functions:deploy

[INFO] Authenticate with Azure CLI 2.0
[INFO] The specified function app does not exist. Creating a new function app...
[INFO] Successfully created the function app: function-sample-azure
[INFO] Trying to deploy the function app...
[INFO] Trying to deploy artifact to function-sample-azure...
[INFO] Successfully deployed the artifact to https://function-sample-azure.azurewebsites.net
[INFO] Successfully deployed the function app at https://function-sample-azure.azurewebsites.net
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

# Note: 
# the function URL is: https://function-sample-azure.azurewebsites.net
# the function can be accessed at: https://function-sample-azure.azurewebsites.net/api/uppercase
```

On another terminal try this: 
```shell
# testing
curl https://<azure-function-url-from-the-log>/api/uppercase -d '{"greeting": "hello", "name": "your name"}'

# testing with cURL
$ curl -H "Content-Type: application/json" https://function-sample-azure.azurewebsites.net/api/uppercase -d '{"greeting": "hello", "name": "your name"}'

# testing with HTTPie
$ http POST https://function-sample-azure.azurewebsites.net/api/uppercase greeting=hello name='your name'

# result
{
  "greeting": "HELLO",
  "name": "YOUR NAME"
}
```

for `lowercase`, on another terminal try this: 
```shell
# testing
curl https://<azure-function-url-from-the-log>/api/lowercase -d '{"greeting": "HELLO", "name": "Your name"}'

# testing with cURL
$ curl -H "Content-Type: application/json" https://function-sample-azure.azurewebsites.net/api/lowercase -d '{"greeting": "HELLO", "name": "Your name"}'

# testing with HTTPie
$ http POST https://function-sample-azure.azurewebsites.net/api/lowercase greeting=HELLO name='Your name'

# result
{
  "greeting": "hello",
  "name": "your name"
}
```
Please ensure that you use the right URL for the functions above. 

Alternatively you can test the function in the Azure Dashboard UI:

* click on the Dashboard
* click on the function app `function-sample-azure` 
* click on the left nav `Functions` and click the function name `uppercase`
* click on the left nav `Code and Test` and at the top of the page `Test/Run`
* In the body of the request, on the right-hand side, paste the same example we have used above:
```shell
{
  "greeting": "hello",
  "name": "your name"
}

observe the HTTP response content
{
  "greeting": "HELLO",
  "name": "YOUR NAME"
}
```

For `lowercase` testing:
```shell 
* click on the left nav `Functions` and click the function name `lowercase`
* click on the left nav `Code and Test` and at the top of the page `Test/Run`
* In the body of the request, on the right-hand side, paste the same example we have used above:
{
  "greeting": "HELLO",
  "name": "Your name"
}
```
You can observe the HTTP response content:
```shell
{
  "greeting": "hello",
  "name": "your name"
}
```

Please note that the Dashboard provides by default information on Function Execution Count, Memory Consumption and Execution Time.

## Debug in a local environment
If you would like to debug the functions in your IDE, all you have to do is:
```shell
# start app locally
# enableDebug opens the debug port in the current host at port 5005
$ mvn azure-functions:run -DenableDebug
```

You can start a remote debugging session in any IDE at `localhost` and port `5005`, set breakpoints and send requests.