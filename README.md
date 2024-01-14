# Run The Bank

| FERRAMENTA               | LINK |
|------------------------|------------|
| ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)           | [collection](https://documenter.getpostman.com/view/28919033/2s9YsNeWJn#intro)      | 
| H2 Database | [login](http://localhost:8082/h2-console/)

### API

#### Customer

| ENDPOINT               | HTTP METHOD | HTTP CODE RESPONSE | BEARER TOKEN |
|------------------------|------------|--------------------|----------------------|
| /customer              | POST       | 201                | NO                   |
| /customer/{id}         | PUT        | 200                | NO                   |
| /customer/{id}         | PATCH      | 200                | NO                   |         
| /customer/{id}| PUT        | 200                | NO                   |
| /customer/{id}| GET        | 200                | NO                   |
| /paged                 | GET        | 200                | NO                   |
| /deleted/{id} | DELETE     | 204                | NO                   |

#### Account

| ENDPOINT                      | HTTP METHOD | HTTP CODE RESPONSE | BEARER TOKEN |
|-------------------------------|-------------|--------------------|----------------------|
| /account                      | POST        | 201                | NO                   |
| /account/{id}        | GET         | 200                | NO                   |
| /account/{id}/inactive | PATCH       | 200                | YES                  |
| /paged                        | GET         | 200                | NO                   |

#### Payment

| ENDPOINT                      | HTTP METHOD | HTTP CODE RESPONSE | BEARER TOKEN |
|-------------------------------|-------------|--------------------|----------------------|
| /payment                      | POST        | 201                | YES                   |
| /payment/{id}/reverse  | PATCH       | 200                | YES                   |
| /payment/{id}/annulled | PATCH       | 200                | YES                   |
