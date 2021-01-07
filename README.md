# graduation-TopJava
An actual version of graduation. Application use H2 DataBase (InMemory released) and start with some data in DB for demonstrate work.
The "soapUi_project" directory contains the SoapUI project file for demonstrating the operation of the REST controller.
___  
Update: Removed Spring-Data-JPA repository and add Spring-Cache. Now [Restaurants with today menu](#Get-Restaurant-list-with-today-menu) and request user which uses Spring security are cached.

# The structure of the document:  
* [Restaurants with today menu](#Get-Restaurant-list-with-today-menu)
* [Restaurants with history menu](#Get-Restaurant-list-with-history-menu)
* [Single restaurant with today menu](#Get-single-Restaurant-with-today-menu)
* [Single restaurant with history menu](#Get-single-Restaurant-with-history-menu)
* [Create Restaurant](#Create-Restaurant)
* [Delete Restaurant](#Delete-Restaurant)
* [Add Dishes](#Add-Dishes)
* [Vote for a restaurant](#Vote-for-the-restaurant)
* [Update Restaurant](#Update-Restaurant)
* [Create regular User](#Create-User)
* [Create admin User](#Create-Admin)
* [Cache](#Spring-Cache)
* [Known issues](#Known-issues)
* [Note](#Note)
___  

# Structure of rest controller:  
```
\  
|-restaurants\  
|            |-create
|            |-history\
|            |        |-{id}    
|            |-{id}\
|                  |-update
|                  |-make_vote
|                  |-add_dishes
|    
|-user\
      |-create
      |-admin
    
```
# Get Restaurant list with today menu
## Resource: 
[/restaurants](http://localhost:8080/restaurants)
## Type
>GET
#### Description:
Return Array restaurants in JSON format from DB that have flag `"Enable":true` and dishes that are added today.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant today.

# Get Restaurant list with history menu
## Resource: 
[/restaurants/history](http://localhost:8080/restaurants/history)
## Type
>GET
#### Description:
Return Array restaurants in JSON format from DB that have any flag `"Enable"` and dishes of all date, and also Restaurants which do not have dish.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant of all date.

# Get single Restaurant with today menu
## Resource: 
[/restaurants/{restaurantId}](http://localhost:8080/restaurants/{restaurantId})
___
For demonstration, you can use for example {restaurantId} = 100002
## Type
>GET
#### Description:
Return single restaurant in JSON format from DB that have flag `"Enable":true` and dishes that are added today.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant today.

# Get single Restaurant with history menu
## Resource: 
[/restaurants/history/{restaurantId}](http://localhost:8080/restaurants/history/{restaurantId})
___
For demonstration, you can use for example {restaurantId} = 100003. It has a dish with date 2020.10.20
## Type
>GET
#### Description:
Return single restaurant in JSON format from DB that have any flag `"Enable"` and dishes of all date, or empty dish array if restaurant haven't dish.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant of all date.

# Create Restaurant
## Resource: 
[/restaurants/create](http://localhost:8080/restaurants/create)
## Type:
>POST
### Format JSON:
```
{
   "name": "String restaurant name",
   "menu":    [
            {
         "cost": Double dish cost,
         "name": "String dish name"
      }
  ],
   "enable": boolean
}
```
### Example:
```JSON
{
   "name": "New restaurant",
   "menu":    [
            {
         "cost": 1.01,
         "name": "first dish"
      },
            {
         "cost": 2.1,
         "name": "second dish"
      }
   ],
   "enable": true
}
```
###Example 2:
```JSON
{
   "name": "New Restaurant"
}
```
#### Description:
Creat object can only user with role "[ADMIN](#Note)".
Restaurant can be creat without "menu" and flag "enable".
Flag "enable" will be installed to "true" automatically.
If the "id" or date in menu fields defined in the JSON body, it will be ignored and the new object "Restaurant" will be assigned an ID from the DB, date dish in a menu was install to current date.
The field "name" are required.

# Add Dishes
## Resource:
[/restaurants/{restaurantId}/add_dish](http://localhost:8080/restaurants/{restaurantId}/add_dish)
## Type:
>POST
### Format JSON:
```
 [
                  {
            "cost": Double value,
            "name": "String Dish name"
         }
      ]
```
#### Description:
Resource allows to add one or more dishes to restaurants. Resource available only users with [ADMIN](#Note) role.
Added the dish will be had current date.

# Vote for the restaurant
## Resource:
[/restaurants/{restaurantId}/make_vote](http://localhost:8080/restaurants/{restaurantId}/make_vote)
___
For demonstration, you can use for example {restaurantId} = 100003
## Type:
>GET
#### Description:
The resource allows to vote a single user for a restaurant. Voting can be made during the day. User can vote only for one restaurant.
A second vote will update the vote for the other restaurant. Re-voting is only possible until 11am o'clock.
For change vote time limiter you need change class `ru.topjava.service.VoteService.class` at string number `26`.

# Update Restaurant
## Resource: 
[/restaurants/{restaurantId}/update](http://localhost:8080/restaurants/{restaurantId}/update)
## Type:
>POST
### Format JSON:
```
{
   "id": int restaurant id,
   "name": "String restaurant name",
   "menu": [   {
      "cost": Double dish cost,
      "name": "String dish name",
      "date":       [
         int year,
         int month,
         int number of day
      ]
   }],
   "enable": boolean
}
```
### Example:
```JSON
{
   "id": 100003,
   "name": "Changed name",
   "menu": [   {
      "cost": 2.19,
      "name": "Changed dish name",
      "date":       [
         2021,
         1,
         3
      ]
   }],
   "enable": true
}
```
#### Description:
Resource available only users with [ADMIN](#Note) role. Resource allows to modified entity "Restaurant" and add or update Dish related to the Restaurant.
Field "id" is required, another rule same as [Create Restaurant](#Create-Restaurant) resource.

# Delete Restaurant
#### Description:
Technical specification do not suggest deleting the restaurant, but you can set the flag `"Enable":false` using resource [Update restaurant](#Update-Restaurant). 
This will prevent display the restaurant when is called the resource [Restaurant List with today menu](#Get-Restaurant-list-with-today-menu).


# Create Regular User
## Resource: 
[/user/create](http://localhost:8080/user/create)
## Type:
>POST
### Format JSON:
```JSON
{
   "name": "String value",
   "email": "String value",
   "password": "String value"
}
```
### Example:
```JSON
{
   "name": "John Connor",
   "email": "test@examlpe.edu",
   "password": "AnyPassword123"
}
```
#### Description: 
Resource available not authorized users. Resource creat users with role "USER". For create with role "Admin" use [another resource](#Create-Admin).
If the "id" field is defined in the JSON body, it will be ignored and the new object "User" will be assigned an ID from the DB.
The field "email" is unique.
The fields "name", "email" and "password" are required.

# Create Admin
## Resource: 
[/user/create/admin](http://localhost:8080/user/create/admin)
## Type:
>POST
#### Description:
Resource create users with role "USER" and "ADMIN". Important: only user with role Admin can create new Admin.
Remaining description does not differ from [Create User](#Create-Regular-User).

#Spring Cache
In application use two CacheManager with different parametrs:  
>The user is stored in the cache 5 minutes after last access.

>The restaurant is stored in the cache 30 minutes after write or until the cache reaches 300 records.

# Known issues
- Unhandled exception:
An error that occurs when saving two users with the same email address was not handled.
- Not yet implemented test methods for REST controller and Spring Security. But their work is demonstrated in the SoapUi project. 
In the future, it is planned to release JUnit tests.

# Note
Spring Security is connected to the project. With start application two users are automatically created.  
`USER:`
> user@yandex.ru | password

`ADMIN:`
> admin@gmail.com | admin

Thank you for your time.