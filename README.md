
vending-machine

pure REST API based implementation

noticed issue: not possible to change username or password

•	REST API implemented consuming and producing “application/json”
•	product model: {amountAvailable, cost, productName, sellerId}
•	user model: {username, password, deposit, role}
•	/api/user CRUD for users (POST shouldn’t require authentication) // put not working properly
•	/api/product CRUD for a product model (GET can be called by anyone, while POST, PUT and DELETE can be called only by the seller user who created the product)
•	/deposit endpoint so users with a “buyer” role can deposit 5, 10, 20, 50 and 100 cent coins into their vending machine account
•	/buy endpoint (accepts productId, amount of products) so users with a “buyer” role can buy products with the money they’ve deposited. API should return total they’ve spent, products they’ve purchased and their change if there’s any (in 5, 10, 20, 50 and 100 cent coins)
•	/reset endpoint so users with a “buyer” role can reset their deposit. Can be done instead of buy.
•	Take time to think about possible edge cases and access issues that should be solved

usage:
1. add users via POST to localhost:8080/api/user

    {
        "username": "user1",
        "password": "pw",
        "role": "seller"
    }
and
    {
        "username": "user2",
        "password": "pw",
        "role": "buyer"
    }

2. for authorization use basic auth for every call, because it doesn't use sessions

3. login as seller and add product via POST to localhost:8080/api/product

    {
        "productName": "p1",
        "cost": 150,
        "amountAvailable" : 3
    }

3.5. make some changes on product

4. login as buyer and add some coins via POST to localhost:8080/deposit
You can add coin of one certain value at a time

    {
        "value" : 100,
        "amount" : 2
    }

5. buy some product by its id via POST to localhost:8080/buy

    {
        "productId" : 1,
        "amount" : 1
    }

6. test other things around