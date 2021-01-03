# graduation-TopJava
Actual version of graduation

Format JSON for create Restaurant:
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

Restaurant can be creat without "menu" and flag "enable".
Flag "enable" will be installed to "enable" automatically.
If the "id" field is defined in the JSON body, it will be ignored and the new object "Restaurant" will be assigned an ID from the DB.
