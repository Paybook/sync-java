
##QUICKSTART BANCO CON TOKEN

A lo largo de este tutorial te enseñaremos como sincronizar una institución bancaria con token, es decir, aquella que además de requerir una autenticación (usuario y contraseña) solicita un token, ejemplos de estas instituciones pueden ser Banorte o BBVA Bancomer. En el tutorial asumiremos que ya hemos creado usuarios y por tanto tenemos usuarios ligados a nuestra API KEY, también asumiremos que hemos instalado la librería y hecho las configuraciones pertinentes. Si tienes dudas acerca de esto te recomendamos que antes de tomar este tutorial consultes el [Quickstart para sincronizar al SAT](https://github.com/Paybook/sync-java/blob/master/doc/quickstart_sat.md) ya que aquí se abordan los temas de creación de usuarios y sesiones.  

### Requerimientos

Para consumir el API de Paybook lo primero que tenemos que hacer es instalar la libreria de Paybook, puede descargar el .jar e incluirlo a sus dependencias:

##Ejecución:

Este tutorial está basado en el script [quickstart_sat.java](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_normal.java) de paquete com.quickstarts, por lo que puedes clonar el proyecto de la carpeta editar el main en Quickstart.java, configurar los valores YOUR_API_KEY, YOUR_RFC y YOUR_CIEC, ejecutarlo:

```java
public class Quickstart {	
	public static void main(String[] args) {
		//quickstart_sat qs = new quickstart_sat();
		//qs.start();    
		//quickstart_normal qn = new quickstart_normal();
		//qn.start();
		quickstart_token qt = new quickstart_token();
		qt.start();
	}//End of main
}
```

Una vez que has ejecutado el archivo podemos continuar analizando el código.

####1. Obtenemos un usuario e iniciamos sesión:
El primer paso para realizar la mayoría de las acciones en Paybook es tener un usuario e iniciar una sesión, por lo tanto haremos una consulta de nuestra lista de usuarios y seleccionaremos el usuario con el que deseamos trabajar. Una vez que tenemos al usuario iniciamos sesión con éste.


```php
$my_users = paybook\User::get();
$user = null;
foreach ($my_users as $index => $my_user) {
    if ($my_user->name == $USERNAME) {
        _print('User '.$USERNAME.' already exists');
        $user = $my_user;
    }//End of if
}//End of foreach
if ($user == null) {
    _print('Creating user '.$USERNAME);
    $user = new paybook\User($USERNAME);
}//End of if
$session = new paybook\Session($user);
_print('Token: '.$session->token);
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


## Maquina de estados exitosos:
El
![Success](https://github.com/Paybook/sync-py/blob/master/token.png "Success")

## Maquina de estados de error:

![Errores](https://github.com/Paybook/sync-py/blob/master/error.png "Errores")

**Importante** El código 401 se puede presentar múltiples veces en caso de que la autenticación con la institución bancaria requiera múltiples pasos e.g. usuario, contraseña (primera autenticación) y además token (segunda autenticación). Entonces al código 401 únicamente le puede preceder un código 100 (después de introducir usuario y password), o bien, un código 401 (después de haber introducido un token). El código 405 indica que el acceso a la cuenta bancaria está bloqueado y, por otro lado, el código 406 indica que hay una sesión iniciada en esa cuenta por lo que Paybook no puede conectarse.

Checamos el estatus de las credenciales:

```java
List<HashMap<String,Object>> status = credentials.get_status(session);
```
####5. Analizamos el estatus:

El estatus se muestra a continuación:

```
[{code=100}, {code=101}, {code=102}, {code=200}]
```

Esto quiere decir que las credenciales han sido registradas y se están validando. La institución bancaria a sincronizar i.e. Banorte, requiere de token por lo que debemos esperar un estatus 410, para esto podemos polear mediante un bucle sobre los estados de las credenciales hasta que se tenga un estatus 410, es decir, que el token sea solicitado:

```java
System.out.println("Wait for token request");
boolean status_410 = false;
while(!status_410 ){
	Thread.sleep(1000);
	List<HashMap<String,Object>> status = credentials.get_status(session);
	System.out.println(status);
	for(int i=0;i<status.size();i++){
		int code = (Integer) status.get(i).get("code");
		if(code == 410){
			status_410 = true;
			break;
		}else if( (code >= 400 && code<410) || (code >= 500 && code<=504)){
			System.out.println("There was an error with your credentials with code:" + code);
			return false;
		}//End of if
	}//End of for
}//End of while
```

**Importante:** En este paso también se debe contemplar que en vez de un código 410 (esperando token) se puede obtener un código 401 (credenciales inválidas) lo que implica que se deben registrar las credenciales correctas, por lo que el bucle se puede módificar para agregar esta lógica.

####6. Enviar token bancario
Ahora hay que ingresar el valor del token, el cual lo podemos solicitar en PHP a través de la interfaz readline:

```java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
String token_value;
token_value = br.readLine();
HashMap<String,Object> twofa_value = new HashMap<String,Object>();
twofa_value.put("token",token_value);
credentials.set_twofa(session, twofa_value);
```

Una vez que el token bancario es enviado, volvemos a polear por medio de un bucle buscando que el estatus sea 102, es decir, que el token haya sido validado y ahora Paybook se encuentre sincronzando a nuestra institución bancaria, o bien, buscando el estatus 401, es decir, que el token no haya sido validado y por tanto lo tengamos que volver a enviar:

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
}//End of while 
```

Es importante checar el código 401 que indica que el token introducido es incorrecto, por lo que se puede programar una rutina para pedir el token nuevamente o terminar el script:

```php
foreach ($status as $index => $each_status) {
    $code = $each_status['code'];
    if ($code == 410) {
        $status_410 = true;
    } elseif ($code >= 400 && $code <= 411) {
        _print('There was an error with your credentials with code: '.strval($code).'.');
        _print('Check the code status in https://www.paybook.com/sync/docs'.PHP_EOL.PHP_EOL);
        exit();
    }//End of if
}//End of foreach
}//End of if	
```

En caso de que el estatus sea 102 se evitará la validación previa y podremos continuar con los siguientes pasos.

####7. Esperamos a que la sincronización termine

Una vez que la sincronización se encuentra en proceso (código 102), podemos construir un bucle para polear y esperar por el estatus de fin de sincronización (código 200).

```php
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

####8. Consultamos las transacciones de la institución bancaria:

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

- Puedes consultar y analizar la documentación completa de la librería [aquí](https://github.com/Paybook/sync-java/blob/master/README.md)

- Puedes consultar y analizar la documentación del API REST [aquí](https://www.paybook.com/sync/docs)

- Acceder a nuestro proyecto en Github y checar todos los recursos que Paybook tiene para ti [aquí](https://github.com/Paybook)














