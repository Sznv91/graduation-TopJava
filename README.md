# graduation-TopJava
Actual version of graduation
Create Restaurant:
Resource: /restaurants/create
Format JSON:
{
"name":"String value", //Restaurant name
"menu":[
			{	"cost":Double value,
				"name":"String value",
					[int year,
					int month,
					int num of day
					]
			},
			{	"cost":Double value,
				"name":"String value",
					[	int year,
					int month,
					int num of day
					]
			}
		],
"enable":true/false
}

Example:
{
"name": "New restaurant",
"menu":[
         {"cost": 1.01,
         "name": "First dish",
         "date":[
                2021,
                1,
                3
                 ]
      	 },
      	 {"cost": 2.19,
          "name": "Second dish",
          "date":[
          2021,
          1,
          3
                ]
      	}
      ],
"enable":false      
}
Example 2:
{
"name":"New Restaurant"
}
Description:
Creat object can only user with role "ADMIN".
Restaurant can be creat without "menu" and flag "enable".
Flag "enable" will be installed to "true" automatically.
If the "id" field is defined in the JSON body, it will be ignored and the new object "Restaurant" will be assigned an ID from the DB.
The field "name" are required.

####################

Create User:
Resource: /user/create
Format JSON:
{
   "name": "String value",
   "email": "String value",
   "password": "String value"
}

Example:

{
   "name": "John Connor",
   "email": "test@examlpe.edu",
   "password": "AnyPassword123"
}

Description: Resource creat users with role "USER". For create with role "Admin" use another resource.
If the "id" field is defined in the JSON body, it will be ignored and the new object "User" will be assigned an ID from the DB.
The fields "name", "email" and "password" are required.