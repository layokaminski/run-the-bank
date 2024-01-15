# Run The Bank

### Pré-requisitos

As instruções a seguir irão lhe proporcionar uma cópia deste projeto e de como rodar em sua máquina local para propósito
de desenvolvimento e testes.

Dependências necessárias para se instalar o software e como instalá-las.

1. É necessário que você tenha Java 17 instalado na sua máquina. Para verificar, rode o seguinte comando:

```bash
$ java -version
```

2. Necessário ter o maven também. Verifique através do comando:

```bash
$ mvn -version
```

### Instalação

Para rodar a aplicação, execute os próximos passos:

1. Faça o clone do projeto:
```bash
$ git clone git@github.com:layokaminski/run-the-bank.git
```

2. Entre na pasta do projeto:

```bash
$ cd run-the-bank
```

3. Execute localmente a aplicação:

```bash
$ mvn exec:java -Dexec.mainClass="com.banco.santander.SantanderApplication"
```

4. Pode-se visualizar a documentação através desse [link](https://documenter.getpostman.com/view/28919033/2s9YsNeWJn#intro)


## Executando os testes

Para rodar os testes automáticos do seu sistema siga os comandos abaixo:

```bash
# rodando todos testes unitários
$ mvn clean test
```

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
