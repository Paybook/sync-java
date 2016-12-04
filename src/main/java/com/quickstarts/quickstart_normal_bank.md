
##QUICKSTART BANCO NORMAL

A lo largo de este tutorial te enseñaremos como sincronizar una institución bancaria normal, es decir, aquella que solo requiere una autenticación (usuario y contraseña), ejemplos de estas instituciones pueden ser Banamex o Santander. En el tutorial asumiremos que ya hemos creado usuarios y por tanto tenemos usuarios ligados a nuestra API KEY, también asumiremos que hemos instalado la librería y hecho las configuraciones pertinentes. Si tienes dudas acerca de esto te recomendamos que antes de tomar este tutorial consultes el [Quickstart para sincronizar al SAT](https://github.com/Paybook/sync-php/blob/master/doc/quickstart_sat.md) ya que aquí se abordan los temas de creación de usuarios y sesiones.  

### Requerimientos

Para consumir el API de Paybook lo primero que tenemos que hacer es instalar la libreria de Paybook, puede descargar el .jar e incluirlo a sus dependencias:

##Ejecución:

Este tutorial está basado en el script [quickstart_sat.java](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_normal.java) de paquete com.quickstarts, por lo que puedes clonar el proyecto de la carpeta editar el main en Quickstart.java, configurar los valores YOUR_API_KEY, YOUR_RFC y YOUR_CIEC, ejecutarlo:

```java
public class Quickstart {	
	public static void main(String[] args) {
		//quickstart_sat qs = new quickstart_sat();
		//qs.start();    
		quickstart_normal qn = new quickstart_normal();
		qn.start();
		//quickstart_token qt = new quickstart_token();
		//qt.start();
	}//End of main
}
```

Una vez que has ejecutado el archivo podemos continuar analizando el código.

####1. Obtenemos un usuario e iniciamos sesión:
El primer paso para realizar la mayoría de las acciones en Paybook es tener un usuario e iniciar una sesión, por lo tanto haremos una consulta de nuestra lista de usuarios y seleccionaremos el usuario con el que deseamos trabajar. Una vez que tenemos al usuario iniciamos sesión con éste.

```java
List<User> users;
users = User.get();
user = null;
for(int i=0;i<users.size();i++){
	if(users.get(i).name.equals(USERNAME)){
		System.out.println("User " + USERNAME + " already exists");
		user = users.get(i);
		break;
	}//End of IF
}//End of For
if(user == null){
	System.out.println("Creating user: " + USERNAME);
	user = new User(USERNAME);
}//End of IF
session = new Session(user);
System.out.println("Token: " + session.token);
```

####2. Consultamos el catálogo de las instituciones de Paybook:
Recordemos que Paybook tiene un catálogo de instituciones que podemos seleccionar para sincronizar nuestros usuarios. A continuación consultaremos este catálogo:

```java
sites = Catalogues.get_sites(session,test_environment);
System.out.println("Sites list:");
for(int i=0;i<sites.size();i++){
	System.out.println(sites.get(i).name);
	if(sites.get(i).name.equals(BANK_NAME)){
		bank_site = sites.get(i); 
	}//End of IF
}//End of for
```

El catálogo muestra las siguienes instituciones:

1. AfirmeNet
2. Personal
3. BancaNet Personal
4. eBanRegio
5. Banorte Personal
6. CIEC
7. Banorte en su empresa
8. BancaNet Empresarial
9. Banca Personal
10. Corporativo
11. Banco Azteca
12. American Express México
13. SuperNET Particulares
14. ScotiaWeb
15. Empresas
16. InbuRed

Usted puede seleccionar cualquier institución y guardarla en la variable $bank_site.

####3. Registramos las credenciales:

A continuación registraremos las credenciales de nuestro banco, es decir, el usuario y contraseña que nos proporcionó el banco para acceder a sus servicios en línea:

```java
HashMap<String,Object> credentials_data = new HashMap<String,Object>();
credentials_data.put("username", BANK_USERNAME);
credentials_data.put("password", BANK_PASSWORD);
credentials = new Credentials(session,bank_site.id_site,credentials_data); 
System.out.println("Credentials username: " + credentials.username);
```
####4. Checamos el estatus

Una vez que has registrado las credenciales de una institución bancaria para un usuario en Paybook el siguiente paso consiste en checar el estatus de las credenciales, el estatus será una lista con los diferentes estados por los que las credenciales han pasado, el último será el estado actual. A continuación se describen los diferentes estados de las credenciales:

| Código         | Descripción                                |                                
| -------------- | ---------------------------------------- | ------------------------------------ |
| 100 | Credenciales registradas   | 
| 101 | Validando credenciales  | 
| 401      | Credenciales inválidas    |
| 102      | La institución se está sincronizando    |
| 200      | La institución ha sido sincronizada    | 

Checamos el estatus de las credenciales:

```java
List<HashMap<String,Object>> status = credentials.get_status(session);
```
####5. Analizamos el estatus:

El estatus se muestra a continuación:

```
[{code=100}, {code=101}, {code=102}, {code=200}]
```

Esto quiere decir que las credenciales han sido registradas y se están validando. Puesto que la institución bancaria a sincronizar, no requiere autenticación de dos pasos e.g. token o captcha podemos únicamente checar el estatus buscando que las credenciales hayan sido validadads y se esté sincronizando (código 2XX) o bien hayan sido inválidas (código 401)

```java
System.out.println("Wait for validation");
boolean bank_sync_completed = false;
while(!bank_sync_completed ){
	Thread.sleep(1000);
	List<HashMap<String,Object>> status = credentials.get_status(session);
	System.out.println(status);
	int last_index = status.size()-1;
	int code = (Integer) status.get(last_index).get("code");
	if(code >= 200 && code < 400){
		bank_sync_completed = true;
		break;
	}else if((code >= 400 && code<410) || (code >= 500 && code<=504)){
		return false;
	}//End of if
}//End of while
```

####6. Consultamos las transacciones de la institución bancaria:

Una vez que la sincronización ha terminado podemos consultar las transacciones:

```java
HashMap<String,Object> options = new HashMap<String,Object>();
options.put("id_credential", credentials.id_credential);
options.put("limit", REQUEST_LIMIT);
transactions = Transaction.get(session, options);
System.out.println("Bank transactions: " + transactions.size());
```

¡Felicidades! has terminado con este tutorial.

###Siguientes Pasos

- Revisar el tutorial de como sincronizar una institución bancaria con token [aquí](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_token_bank.md)

- Puedes consultar y analizar la documentación completa de la librearía [aquí](https://github.com/Paybook/sync-java/blob/master/README.md)

- Puedes consultar y analizar la documentación del API REST [aquí](https://www.paybook.com/sync/docs)

- Acceder a nuestro proyecto en Github y checar todos los recursos que Paybook tiene para ti [aquí](https://github.com/Paybook)














