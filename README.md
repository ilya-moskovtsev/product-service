#### This is an implementation of the following coding task

The product service is required to maintain following information about the products:
```text
* unique product code
* product price
* product marketplace
* product status
```
Above information is received as a JSON message, for example:
```json
{
  "productCode":"123-ABC",
  "price": 24.56,
  "marketplace":"NL",
  "status":"active"
}
```
Implement a REST API that does the following:
```text
1. Accepts and stores new product
2. Retrieves product information by provided product code
3. Updates product price
4. Retrieves all products based on provided status
```
In addition, the following needs to be taken into account:
```text
1. The system should keep collection of unique products
2. Received product message should be validated.
```
If a message doesn't meet below conditions, the service should respond with the relevant error message and not store the product.
```text
a) the product must be present and cannot be empty
b) received product should be rejected if already exists
c) the price should exists and contain valid characters
d) the marketplace can only be of following values: NL, GB, FR
e) the status can only be of following values: active, inactive, pending
```
Validation points should be covered with relevant unit tests.
