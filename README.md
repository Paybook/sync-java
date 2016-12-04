

# Paybook Java Library 

Esta es la librería de Paybook para Java. Mediante esta librería usted puede implementar el API REST de Paybook de manera rapida y sencilla a través de sus clases y métodos.


## Quickstart:

Antes de consultar la documentación puedes tomar alguno de nuestros tutoriales:

- [Quickstart para sincronizar al SAT](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_sat.java)

- [Quickstart para sincronizar una cuenta bancaria con credenciales sencillas (usuario y contraseña)](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_normal.java)

- [Quickstart para sincronizar una cuenta bancaria con token (usuario y contraseña)](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_token.java)

## Recordatorios:

- Puedes consultar la documentación del API REST [aquí](https://www.paybook.com/sync/docs#api-Overview)
- Puedes consultar todos los recursos que tenemos para ti [aquí](https://github.com/Paybook)

## Documentación:

Cada método está documentado tomando como base la siguiente estructura:

```java
method_type returned_value_type x = class_or_instance.get(attr1=attr1_type,...,attrn=attrN_type)
```

1. method_type: indica si el método es estático, en caso de no estar indica que el método es de instancia, o bien, es un constructor.
2. returned_value_type: indica el tipo de dato regresado por el método
3. x: es una representación del valor retornado por el método
4. class_or_instance: es la Clase o una instancia de la clase que contiene el método a ejecutar
5. attrX: es el nombre del atributo X
6. attrX_type: es el tipo de dato del atributo X

### Accounts

Estructura de los atributos de la clase:

| Account         |                                  
| -------------- | 
| + String id_account <br> + String id_user <br> + String id_external  <br> + String id_credential <br> + String id_site <br> + String id_site_organization <br> + String name <br> + String number <br> + float balance <br> + Site site <br> + String id_account_type <br> + String account_type <br>  + int is_disable <br> + String account_type <br> + String currency <br> + HashMap<String,Object> extra <br> + String dt_refresh  |
				
Descripción de los métodos de la clase:

| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                  |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Requests accounts of a user | GET https://sync.paybook.com/v1/accounts | ```static List [Account] = Account.get(session=Session,id_user=String)```          |

### Attachments

Estructura de los atributos de la clase:

| Attachments         |                                  
| -------------- | 
| + String id_account <br> + String id_account <br> + String id_user <br>  + String id_external <br> + String id_attachment_type <br> + String id_transaction <br> + String is_valid <br> + String file <br> + String url <br> + HashMap<String,Object> extra <br>  + String content <br>  + String dt_refresh |
		
Descripción de los métodos de la clase:

| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                  |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Requests attachments | GET https://sync.paybook.com/v1/attachments <br> GET https://sync.paybook.com/v1/attachments/:id_attachment <br> GET https://sync.paybook.com/v1/attachments/:id_attachment/extra | ```static list [Attachment] = Attachment.get(session=Session,id_user=String,id_attachment=String,extra=boolean)```          |
| Request the number of attachments | GET https://sync.paybook.com/v1/attachments/counts | ```static int attachments_count = Attachment.get_count(session=Session,id_user=String)```          |

### Catalogues

Estructura de los atributos de las clases:

| Account_type         | Attachment_type | Country |                                 
| -------------- | -------------- | -------------- | 
| + String id_account_type <br> + String name | + String id_attachment_type <br> + String name | + String id_country <br> + String name <br> + String code |

| Site         | Credential_structure | Site_organization |                                 
| -------------- | -------------- | -------------- | 
| + String id_site <br> + String id_site_organization <br> + String id_site_organization_type <br> + String name <br> + List<Credentials> credentials | + String name <br> + String type <br> + String label <br> + boolean required <br> + String username | + String id_site_organization <br> + String id_site_organization_type <br> + String id_country <br> + String name <br> + String avatar <br> + String small_cover <br> + String cover |

Descripción de los métodos de la clase:

| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                  |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Request account types | GET https://sync.paybook.com/v1/catalogues/account_types   | ```static List [Account_type] = Catalogues.get_account_types(session=Session,id_user=String)```          |
| Request attachment types | GET https://sync.paybook.com/v1/catalogues/attachment_types   | ```static List [Attachment_type] = Catalogues.get_attachment_types(session=Session,id_user=String)```          |
| Request available countries | GET https://sync.paybook.com/v1/catalogues/countries   | ```static List [Country] = Catalogues.get_countries(session=Session,id_user=String)```          |
| Request available sites | GET https://sync.paybook.com/v1/catalogues/sites   | ```static List [Site] = Catalogues.get_sites(session=Session,id_user=String)```          |
| Request site organizations | GET https://sync.paybook.com/v1/catalogues/site_organizations   | ```static List [Site_organization] = Catalogues.get_site_organizations(session=Session,id_user=String)```          |

### Credentials

Estructura de los atributos de la clase:

| Credentials         |                                  
| -------------- | 
| + String id_credential <br> + String id_site <br>  + String username <br> + String id_site_organization <br> + String id_site_organization_type <br> + String ws <br> + String status <br> + String twofa <br> + HashMap<String,Object> twofa_config <br> + String dt_refresh <br> |
				
Descripción de los métodos de la clase:

| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                  |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Creates or updates credentials | POST https://sync.paybook.com/v1/credentials | ```Credentials credentials = Credential(session=String,id_user=str,id_site=String,credentials=HashMap<String,Object>)```          |
| Deletes credentials | DELETE https://sync.paybook.com/v1/credentials/:id_credential | ```static boolean deleted Credentials.delete(session=Session,id_user=String,id_credential=String)```          |
| Request status | GET status_url | ```List [HashMap<String,Object>] = credentials.get_status(session=Session,id_user=String)```          |
| Set twofa | POST twofa_url | ```boolean set_twofa = credentials.set_twofa(session=Session,id_user=String,twofa_value=HashMap<String,Object>)```          |
| Request register credentials | GET https://sync.paybook.com/v1/credentials | ```static list [Credentials] = Credentials.get(session=Session,id_user=String)```          |


### Sessions

Estructura de los atributos de la clase:

| Sessions         |                                  
| -------------- | 
| + User user <br> + String token <br> + String iv <br> + String key   | 
				
Descripción de los métodos de la clase:


| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                  |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Creates a session | POST https://sync.paybook.com/v1/sessions   | ```Session session = Session(user=User)```          |
| Verify a session | GET https://sync.paybook.com/v1/sessions/:token/verify | ```boolean verified = session.verify()```                  |
| Deletes a session     | DELETE https://sync.paybook.com/v1/sessions/:token    | ```static boolean deleted = Session.delete(token=String)```|


### Transactions

Estructura de los atributos de la clase:

| Transactions         |                                  
| -------------- | 
| + String id_transaction <br> + String id_user <br> + String id_site <br> + String id_external <br> + String id_site_organization <br> + String id_site_organization_type <br> + String id_account <br> + String id_account_type <br> + String id_currency <br> + String is_disable <br> + String description <br> + float amount <br> + String currency <br> + List<HashMap<String,Object>> attachments <br> + HashMap<String,Object> extra <br> + String reference <br> + String dt_transaction <br> + String dt_refresh <br> + String dt_disable   |
				
Descripción de los métodos de la clase:


| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                  |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Requests number of transactions | GET https://sync.paybook.com/v1/transactions/count | ```static int transactions_count = Transaction.get_count(session=Session,id_user=String)```          |
| Requests transactions | GET https://sync.paybook.com/v1/transactions | ```static list [Transaction] = Transaction.get(session=Session,id_user=String)```          |

### Users

Estructura de los atributos de la clase:

| Users         |                                  
| -------------- | 
| + String id_user <br> + String id_external <br> + String name <br> + String dt_create <br> + String dt_modify   |

Descripción de los métodos de la clase:


| Action         | REST API ENDPOINT                                 | LIBRARY METHOD                                 |
| -------------- | ---------------------------------------- | ------------------------------------ |
| Creates a user | POST https://sync.paybook.com/v1/users   | ```User user = User(name=str,id_user=String)```          |
| Deletes a user | DELETE https://sync.paybook.com/v1/users | ```static boolean deleted = User.delete(id_user=String)```                  |
| Get users      | GET https://sync.paybook.com/v1/users    | ```static list [User] = User.get()```|