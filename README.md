# Run The Bank

[Link da collection](https://documenter.getpostman.com/view/28919033/2s9YsNeWJn#intro)
[Link API Docs](http://localhost:8082/api-docs)

### API

#### Customer

| ENDPOINT               | HTTP METHOD | HTTP CODE RESPONSE | HEADER AUTHORIZATION |
|------------------------|------------|--------------------|----------------------|
| /customer              | POST       | 201                | NO                   |
| /customer/{id}         | PUT        | 200                | NO                   |
| /customer/{id}         | PATCH      | 200                | NO                   |         
| /customer/{id}| PUT        | 200                | NO                   |
| /customer/{id}| GET        | 200                | NO                   |
| /paged                 | GET        | 200                | NO                   |
| /deleted/{id} | DELETE     | 204                | NO                   |

#### Account

| ENDPOINT                      | HTTP METHOD | HTTP CODE RESPONSE | HEADER AUTHORIZATION |
|-------------------------------|-------------|--------------------|----------------------|
| /account                      | POST        | 201                | NO                   |
| /account/{id}        | GET         | 200                | NO                   |
| /account/{id}/inactive | PATCH       | 200                | YES                  |
| /paged                        | GET         | 200                | NO                   |

#### Payment

| ENDPOINT                      | HTTP METHOD | HTTP CODE RESPONSE | HEADER AUTHORIZATION |
|-------------------------------|-------------|--------------------|----------------------|
| /payment                      | POST        | 201                | NO                   |
| /payment/{id}/reverse  | PATCH       | 200                | NO                   |
| /payment/{id}/annulled | PATCH       | 200                | NO                   |
