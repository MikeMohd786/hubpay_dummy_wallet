# hubpay_dummy_wallet

This is a Java-based API that simulates a digital wallet that holds a balance for customers. It allows users to add and withdraw funds from their wallet, and view a paginated list of their transaction history.

Technologies/Frameworks Used
- Java 8
- Spring Boot 2.5.6
- Spring Data JPA
- H2 Database (In-Memory)
- Gradle

# Requirements
- Java 8
- Gradle

# Setup
1. Clone the repository: git clone https://github.com/MikeMohd786/hubpay_dummy_wallet.git
2. Navigate to the project root directory: cd hubpay_dummy_wallet
3. Build the project: mvn clean install
4. Run the application: java -jar target/hubpay_dummy_wallet-0.0.1-SNAPSHOT.jar
5. The application should now be running on http://localhost:8080.

# Endpoints
<h2>Add Funds</h2>
 Endpoint: `POST /api/wallet/{{walletId}}/add-funds`

Request Body:

```JSON
{
"amount":   1200
}
```

Response : ``202 Response status``

<h2>Withdraw Funds</h2>
Endpoint: `POST /api/wallet/{{walletId}}/withdraw-funds`

Request Body:

```JSON
{
"amount":   1200
}
```

Response : ``202 Response status``

<h2>Get Transactions</h2>
Endpoint: `GET /api/wallet/{{walletId}}/transactions`

Response : 
```JSON
{
    "page": 0,
    "size": 10,
    "totalPage": 1,
    "transactions": [
        {
            "id": 6,
            "amount": 1000.00,
            "type": "DEBIT",
            "transactionTime": "2023-04-17T03:25:31.821",
            "createdAt": null,
            "updatedAt": null
        },
        {
            "id": 4,
            "amount": 1200.00,
            "type": "CREDIT",
            "transactionTime": "2023-04-17T03:25:02.252",
            "createdAt": null,
            "updatedAt": null
        }
    ]
}
```

# Limitations
- No authentication or authorization implemented.
- No additional security measures implemented.
- Wallet creation and sign up are not implemented.
