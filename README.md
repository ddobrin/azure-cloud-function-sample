You can run this Azure function locally, similar to other Spring Cloud Function samples, however this time by using the Azure Maven plugin, as the Microsoft Azure functions execution context must be available.

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

To run locally on top of Azure Functions, and to deploy to your live Azure environment, you will need the Azure Functions Core Tools installed along with the Azure CLI (see https://docs.microsoft.com/en-us/azure/azure-functions/functions-create-first-java-maven for more details).

To deploy the function to your live Azure environment, including an automatic provisioning of an HTTPTrigger for the function:
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

Please ensure that you use the right URL for the function above. 

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

# observe the HTTP response content
{
  "greeting": "HELLO",
  "name": "YOUR NAME"
}
```

Please note that the Dashboard provides by default information on Function Execution Count, Memory Consumption and Execution Time.
