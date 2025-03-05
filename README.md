# Mpesa Java SDK


## Introduction

The M-Pesa SDK provides a unified interface for interacting with M-Pesa's various payment services, including B2C (Business-to-Customer), C2B (Customer-to-Business), STK Push, and transaction management. It abstracts away complexities like authentication, retries, and API response parsing, providing a simplified interface for Java developers to integrate M-Pesa payment functionality into their applications.

The SDK is composed of two main modules:
- **sdk-core**: Contains the core functionality, independent of any frameworks.
- **sdk-spring**: Spring DI Container support for projects using the Spring framework.
### Features

- **API Abstraction**: Provides core functionalities for interacting with M-Pesa’s API (Authentication, C2B, B2C, etc.).
- **Serialization and Deserialization**: Proper and type-safe handling of request and response objects.
- **Spring Dependency Injection**: Spring-based integration for easier configuration and use with Spring Boot applications.
- **Callback DTO Schemas**: Classes that simplify callback endpoint deserialization and help developers process callback data in their endpoints.
- **Retry Mechanisms**: Automatic retry during network issues, or certain response codes.

## Installation

You can install the SDK by adding the following dependency to your Maven `pom.xml`:
```xml
<dependency>
    <groupId>dev.mpesa</groupId>
    <artifactId>sdk-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Or if you're using Spring:
```xml
<dependency>
    <groupId>dev.mpesa</groupId>
    <artifactId>sdk-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

You can clone the repository and build it yourself:
```bash
git clone https://github.com/Zeph16/mpesa-sdk-java.git
cd mpesa-sdk
mvn clean install
```

This will prepare both jar files and make them available for your system.

## Usage
### Core SDK
#### 1. SDK Instantiation

The SDK requires no extra configuration to interact with the M-Pesa API. You simply have to provide your credentials to the MpesaSdk object:
```java
MpesaSdk mpesaSdk = new MpesaSdk(consumerKey, consumerSecret);

// Or with custom configuration
MpesaConfig config = new MpesaConfig.Builder().build();
MpesaSdk mpesaSdk = new MpesaSdk(consumerKey, consumerSecret, config);
```

#### 2. SDK Configuration 

The `MpesaConfig` class is used for setting up any optional global configurations for the internal workings of the sdk.
- API URLs for C2B, B2C, STK Push, and more
- Timeout values and retry configurations

The config object can then be passed to the MpesaSdk object's constructor for the changes to take effect only during instantiation. Here's the current exhaustive list of parameters you can configure, all of which are optional:
```java  
MpesaConfig config = new MpesaConfig.Builder()  
        .environment(environment)  
        .authUrl(authUrl)  
        .c2bRegisterUrl(c2bRegisterUrl)  
        .c2bPaymentUrl(c2bPaymentUrl)  
        .c2bSimulatePaymentUrl(c2bSimulatePaymentUrl)  
        .stkPushUrl(stkPushUrl)  
        .b2cPaymentUrl(b2cPaymentUrl)  
        .transactionStatusUrl(transactionStatusUrl)  
        .transactionReversalUrl(transactionReversalUrl)  
        .accountBalanceUrl(accountBalanceUrl)  
        .connectTimeout(connectTimeout)  
        .readTimeout(readTimeout)  
        .writeTimeout(writeTimeout)  
        .retryBackoffTime(retryBackoffTime)  
        .maxRetries(maxRetries)  
        .build();

MpesaSdk sdk = new MpesaSdk(consumerKey, consumerSecret, config);
```

**Note**: The above URL parameters are only paths (`authUrl` is `/v1/token/generate?grant_type=client_credentials` by default). Base URLs are fixed according to the environment (`https://api.safaricom.et` or `https://apisandbox.safaricom.et`). 

### Spring SDK
The `sdk-spring` module provides seamless integration with Spring by automatically configuring the SDK as a bean. Here's how to use it:

#### 1. **Configuration via `application.properties`**

You can bootstrap the SDK by adding your credentials `application.properties` or `application.yml`:
```
mpesa.consumer-key=myConsumerKey
mpesa.consumer-secret=myConsumerSecret
```

Your configuration file can be your interface to providing a custom `MpesaConfig` object, as you can specify all attributes of that class via configuration parameters
```
mpesa.environment=PRODUCTION
mpesa.max-retries=3
mpesa.retry-backoff-time=500
mpesa.auth-url=/mpesa/auth/path
// ... and so on
```

#### 2. **Injecting the SDK**

Once configured, you can inject `MpesaSdk` into any Spring-managed component:
```java
@Service
public class PaymentService {
    private final MpesaSdk mpesaSdk;

    @Autowired
    public PaymentService(MpesaSdk mpesaSdk) {
        this.mpesaSdk = mpesaSdk;
    }

    public void initiatePayment() {
        // Use the SDK to call M-Pesa APIs
    }
}
```

#### 3. **Explicitly Importing the Configuration**

If the SDK beans are not being detected in your application, explicitly import the configuration by adding:
```java
@Import(MpesaSdkDefaultConfiguration.class)
@Configuration
public class MyConfig {
}
```

**Note**: Since both the MpesaSdk and MpesaConfig classes are public, feel free to create your own custom configured beans if you don't want to use environment variables or configuration files.

### SDK Methods 
Once you have the sdk object, a suite of methods which directly reflect the api endpoints are available. **Request Objects** are available contextually for each endpoint (each method), that can only be created using the **builder method**. Extensive validation is carried out when each parameter is assigned, as well as during the `build()`call so your compiler can guide you if you're missing any parameters.

The provided examples will NOT compile since the request objects are not fully created. Refer to the `dev.mpesa.sdk.dto.request` package for an inside look on each object, or follow your compiler errors to correct any issues. Full listing of request objects is omitted in the documentation for brevity.
#### Test Authentication
```java
mpesaSdk.testAuth();
```
#### Check Account Balance
```java
AccountBalanceRequest request = AccountBalanceRequest.builder().build();
mpesaSdk.checkAccountBalance(request);
```
#### Initiate B2C Payment
```java
B2CPaymentRequest request = B2CPaymentRequest.builder().build();
mpesaSdk.initiateB2CPayment(request);
```
#### Register C2B
```java
C2BRegisterRequest request = C2BRegisterRequest.builder().build();
mpesaSdk.registerC2B(request, "apiKey");
```
#### Initiate C2B Payment
```java
C2BPaymentRequest request = C2BPaymentRequest.builder().build();
mpesaSdk.initiatePayment(request);
```
#### Simulate C2B Payment
```java
C2BSimulatePaymentRequest request = C2BSimulatePaymentRequest.builder().build();
mpesaSdk.simulateC2BPayment(request);
```
#### Request STK Push (USSD Push)
```java
StkPushRequest request = StkPushRequest.builder().build();
mpesaSdk.requestStkPush(request);
```
#### Check Transaction Status
```java
TransactionStatusRequest request = TransactionStatusRequest.builder().build();
mpesaSdk.checkTransactionStatus(request);
```
#### Reverse Transaction
```java
TransactionReversalRequest request = TransactionReversalRequest.builder().build();
mpesaSdk.reverseTransaction(request);
```

## Design Philosophy

- **Strict Validation:** The M-Pesa API docs are followed to the letter when validating requests. Every input is checked thoroughly during request object creation via the `ValidationUtils` class, even if it means being more susceptible to api change breaks.

- **Builder Pattern:** All request objects can only be built via the builder method. This ensures immutability and guarantees all required fields are set and validated upfront, making things clearer and safer to use.

- **Exceptions Everywhere:** The SDK leans heavily on exceptions to handle errors—authentication issues, network problems, or unexpected responses. While it might seem heavy-handed, it keeps error handling centralized and consistent.

- **Raw Response in Exceptions:** Even when the response can't be parsed, it’s still available in the exception attribute. This is great for edge cases or debugging if you need to see exactly what M-Pesa returned.
    
- **SLF4J Logging:** SLF4J is used for logging, which gives flexibility for you to use your preferred logging framework while keeping logs detailed and helpful for troubleshooting.


## Exception Handling

The SDK employs custom exceptions to manage various error scenarios, ensuring robust error tracking and troubleshooting. Here’s an overview of how exceptions are used:

#### `MpesaException`
The base class for all SDK exceptions. It extends `RuntimeException` for unchecked exceptions, enabling more flexible error handling in your code. Every exception in this SDK derives from it.
    
#### `MpesaAuthenticationException`
It is thrown when authentication fails. This could happen due to invalid credentials or expired tokens. It includes the response body from M-Pesa for debugging.
    
#### `MpesaNetworkException`
This exception handles network failures, such as timeouts or connectivity issues. It’s designed to notify when the SDK cannot reach M-Pesa’s API.

#### `MpesaUnexpectedResponseException`
`MpesaUnexpectedResponseException` is the most widely applicable exception. It is used when M-Pesa’s response is invalid or doesn’t match the expected format. All HTTP bad responses (non 2xx) are also encapsulated by this exception. It includes an `MpesaErrorCode` and tries to parse the response body into a `MpesaErrorResponse` object for easier debugging. If parsing fails, the raw response body is still available.

- **Error Codes:** The SDK includes an `MpesaErrorCode` enum as part of `MpesaUnexpectedResponseException`, which categorizes common API errors like `INVALID_REQUEST` or `UNKNOWN_ERROR`. This can be useful for more granular error handling but in the current state of the M-Pesa API, it has been difficult to properly record business errors. For specific business level errors, you are advised to inspect the response body string and check errors manually.

### Callback DTOs
While the SDK does not directly handle callbacks (as this is typically the responsibility of the client system), the SDK makes it easier for developers to work with M-Pesa’s callback responses by providing ready-to-use DTOs. These DTOs are designed according to M-Pesa’s schema, and allows developers to easily deserialize callback data into Java objects.

The following callback DTOs are provided:
- **Validation and Confirmation Callbacks:** `ValidationConfirmationRequest`
- `StkPushCallbackResponse`
- **Service Result Callbacks:** `ServiceResultResponse` (for requests like Account Balance, Transaction Status).
- `ValidationConfirmationResponse`

## Contributing

Contributions to improve the SDK are more than welcome. If you would like to contribute, please follow these guidelines:

1. **Fork the repository**.
2. **Create a new branch** for your changes.
3. Make your changes and write tests.
4. **Ensure tests pass** by running `mvn test`.
5. Submit a pull request with a clear explanation of your changes.
### Guidelines:

- Follow existing code style and naming conventions.
- Ensure that all public methods and classes are documented.
- Write unit tests for any new functionality or bug fixes.
- **Maven** is used for dependency management and building the project. Make sure to use it for your contributions.

---
## License

This SDK is licensed under the MIT License.

---
