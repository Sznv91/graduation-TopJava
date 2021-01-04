# graduation-TopJava
Actual version of graduation. Application use H2 DataBase (InMemory released) and start with some data in DB for demonstrate work.

#Get Restaurant list with today menu:
##Resource: 
[/restaurants](http://localhost:8080/restaurants)
##Type
>GET
####Description:
Return Array restaurants in JSON format from DB that have flag `"Enable":true` and dishes that are added today.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant today.

#Get Restaurant list with history menu:
##Resource: 
[/restaurants/history](http://localhost:8080/restaurants/history)
##Type
>GET
####Description:
Return Array restaurants in JSON format from DB that have any flag `"Enable"` and dishes of all date, and also Restaurants which do not have dish.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant of all date.

#Get single Restaurant with today menu:
##Resource: 
[/restaurants/{restaurantId}](http://localhost:8080/restaurants/{restaurantId})
##Type
>GET
####Description:
Return single restaurant in JSON format from DB that have flag `"Enable":true` and dishes that are added today.
Body of restaurant have field `"voteCount"` that shows quantity of vote which users gave for the restaurant today.



#Create Restaurant:
##Resource: 
[/restaurants/create](http://localhost:8080/restaurants/create)
##Type:
>POST
###Format JSON:
```JSON
{
   "name": "String restaurant name",
   "menu":    [
            {
         "cost": Double value,
         "name": "String dish name",
         "date":          [
            int year,
            int month,
            int num of day
         ]
      }
  ],
   "enable": boolean
}
```
###Example:
```JSON
{
   "name": "New restaurant",
   "menu":    [
            {
         "cost": 1.01,
         "name": "first dish",
         "date":          [
            2021,
            1,
            3
         ]
      },
            {
         "cost": 2.1,
         "name": "second dish",
         "date":          [
            2021,
            1,
            3
         ]
      }
   ],
   "enable": true
}
```
Example 2:
```JSON
{
   "name": "New Restaurant"
}
```
####Description:
Creat object can only user with role "[ADMIN](README.MD#Note)".
Restaurant can be creat without "menu" and flag "enable".
Flag "enable" will be installed to "true" automatically.
If the "id" field is defined in the JSON body, it will be ignored and the new object "Restaurant" will be assigned an ID from the DB.
The field "name" are required.

#Update Restaurant:
##Resource: 
[/restaurants/{restaurantId}/update](http://localhost:8080/restaurants/{restaurantId}/update)
##Type:
>POST
###Format JSON:
```JSON
{
   "id": int value,
   "name": "String value",
   "menu": [   {
      "cost": Double value,
      "name": "String value",
      "date":       [
         int year,
         int month,
         int number of day
      ]
   }],
   "enable": boolean
}
```
###Example:
```JSON
{
   "id": 100007,
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
####Description:
Resource allows to modified entity "Restaurant" and add or update Dish related to the Restaurant.
Field "id" is required, another rule same as [Create Restaurant](README.md#Create Restaurant:) resource.

#Create User:
##Resource: 
[/user/create](http://localhost:8080/user/create)
##Type:
>POST
###Format JSON:
```JSON
{
   "name": "String value",
   "email": "String value",
   "password": "String value"
}
```
###Example:
```JSON
{
   "name": "John Connor",
   "email": "test@examlpe.edu",
   "password": "AnyPassword123"
}
```
####Description: 
Resource creat users with role "USER". For create with role "Admin" use another resource.
If the "id" field is defined in the JSON body, it will be ignored and the new object "User" will be assigned an ID from the DB.
The field "email" is unique.
The fields "name", "email" and "password" are required.

#Knowledge problems:
- Unhandled exception:
An error that occurs when saving two users with the same email address was not handled.

#Note
Spring Security is not yet connected to the app. For change to admin role need go to resource [/user/test_change_user/{userId}](/user/test_change_user/{userId}) where userId is 100001 or admin created with resource [admin resource]()