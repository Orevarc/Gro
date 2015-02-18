# API


<p><i>***You can test all of these requests to see the responses by use a Chrome app called Postman. Search for it on Google and install into Chrome. From there you can open the app and send POST and GET requests and recieve responses directly through the app.***</i></p>

<b><u>Registering a User:</b></u>

Once the user enters in their user credentials on the registration pageand pushes submit, send the credentials as a HTTP POST request to:
```
http://localhost:1336/users/register
```

The credentials should be packed into key value pairs in the request as follows (Replace the <...> with the actual values):

```
username : <your username>
password : <your password>
email    : <your email>
```

The client will recieve the following back if registration went successfully:
```
STATUS CODE: 200
BODY:
{
	"client_id": <client_id>,
	"url": "http://localhost:1336/users/verify/<your email>?code=Y087VfF3bbHmNrQaRsAfOB8srfNB0gDW"
}
```
The ```client_id``` should be saved to the user's device (maybe in the User class on the phone but I dunno). The ```url``` is just the verification link that gets sent to their email. In order to login, the user must click the link sent to their account.

<p><b><u>Loggin In/Obtaining a token:</b></u></p>

Once the user has registered an account and has verified their email, they can now login. In order to login and obtain a token, the login credentials should be sent as an HTTP POST request to:
```
http://localhost:1336/oauth/token
```
The login credentials should be packaged into the request in key value pairs as such:
```
grant_type : password
client_id  : <received client id>
username   : <your username>
password   : <your password>
```
<p>***The grant_type value is just the string password, not the user's password.</p>

If login was a success, the following will be sent back to the client:
```
STATUS CODE: 200
{
    "access_token":  "<received access token>",
    "refresh_token": "<received refresh token>",
    "expires_in":    10000,
    "token_type":    "Bearer"
}
```
<p>The client should store the access_token on their device as this is the token that should be used to make requests on behalf of the user</p>
<p>If they have no verifieid their email, if they try to login, the server will send back:</p>

```
STATUS CODE: 403
BODY:
{
	"error": "invalid_grant",
	"error_description": "Invalid resource owener credentials"
}
```
<p><b><u>Making a request to the server with the obtained credentials</b></u></p>
Now that you have an access token on the device, the client can begin to make autheticated calls to the server. To test to see if this works right now, you can send an HTTP GET request to:
```
http://localhost:1336/users/current
```
This GET request should have a custum authorization header with the following key:value pair:
```
Authorization : Bearer <received access token>
```
If everything went okay, you should recieve back the following response:
```
STATUS CODE: 200
BODY:
{
    "identity": {
        "username": "<your username>",
        "email":    "<your email>"
        ...
    }
}
```
If something went wrong, you should recieve the following response:
```
STATUS CODE: 401
BODY:
Unauthorized
```

<p>______________________________________________________________________</p>
<b><u>Grabbing a User's Food Items:</b></u>
To gran a list of food items associated with a user, simply send an HTTP GET request to:
```
http://localhost:1336/useritems
```
This GET request should have a custum authorization header with the following key:value pair:
```
Authorization : Bearer <received access token>
```
STATUS CODE: 200
BODY:
[{
    "ownershipID": 1,
    "user_id": 103,
    "foodItemID": [
        1,
        1
    ],
    "dateBought": "2015-02-17T00:00:00.000Z",
    "expiryDate": "2015-03-03T00:00:00.000Z",
    "used": 0,
    "upcCode": "05700000299",
    "itemName": "Heinz Ketchup",
    "foodCategoryID": [
        41,
        41
    ],
    "id": null,
    "factualCategory": "Condiments",
    "generalCategory": "Condiments, Spices & Sauces",
    "expiryTime": null
}, {
    "ownershipID": 2,
    "user_id": 103,
    "foodItemID": [
        52,
        52
    ],
    "dateBought": "2015-02-17T00:00:00.000Z",
    "expiryDate": "2015-03-03T00:00:00.000Z",
    "used": 0,
    "upcCode": "4309",
    "itemName": "LYCHEES",
    "foodCategoryID": [
        39,
        39
    ],
    "id": null,
    "factualCategory": "Fruits",
    "generalCategory": "Fruits & Vegtables",
    "expiryTime": 5
}]
```


