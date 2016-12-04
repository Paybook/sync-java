
##QUICKSTART SAT

### Requerimientos

1. Manejo de shell/bash
2. Algunas credenciales de acceso al SAT (RFC y CIEC)
3. Eclipse ó Netbeans

### Introducción

A lo largo de este tutorial te enseñaremos como consumir el API Rest de Paybook por medio de la librería de Paybook. Al terminar este tutorial habrás podido crear nuevos usuarios en Paybook, sincronizar algunas instituciones de estos usuarios y visualizar las transacciones sincronizadas.

La documentación completa de la librería la puedes consultar [aquí](https://github.com/Paybook/sync-php/blob/master/README.md) 

## Ejecución:

####1. Instalamos la librería de Paybook y dependencias:

Para consumir el API de Paybook lo primero que tenemos que hacer es instalar la libreria de Paybook, puede descargar el .jar e incluirlo a sus dependencias.

####2. Ejecutamos el Script:
Este tutorial está basado en el script [quickstart_sat.java](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_sat.java) de paquete com.quickstarts, por lo que puedes clonar el proyecto de la carpeta editar el main en Quickstart.java, configurar los valores YOUR_API_KEY, SAT_RFC y SAT_CIEC, ejecutarlo:

```java
public class Quickstart {	
	public static void main(String[] args) {
		quickstart_sat qs = new quickstart_sat();
		qs.start();    
		//quickstart_normal qn = new quickstart_normal();
		//qn.start();
		//quickstart_token qt = new quickstart_token();
		//qt.start();
	}//End of main
}
```

A continuación explicaremos detalladamente la lógica del script que acabas de ejecutar.

####3. Importamos paybook
El primer paso es importar la librería y algunas dependencias:

```java
import com.paybook.sync.*;
import com.paybook.sync.Error;
```

####4. Configuramos la librería
Una vez importada la librería tenemos que configurarla, para esto únicamente se necesita tu API KEY de Paybook.

```java
Paybook.init(YOUR_API_KEY);
```

####5. Creamos un usuario:
Una vez configurada la librería, el primer paso será crear un usuario, este usuario será, por ejemplo, aquel del cual queremos obtener sus facturas del SAT.

**Importante**: todo usuario estará ligado al API KEY con el que configuraste la librería (paso 4)

```java
user = new User(USERNAME);
```

####6. Consultamos los usuarios ligados a nuestra API KEY:
Para verificar que el usuario creado en el paso 5 se haya creado corréctamente podemos consultar la lista de usuarios ligados a nuestra API KEY.

```java
for(int i=0;i<users.size();i++){
	if(users.get(i).name.equals(USERNAME)){
		System.out.println("User " + USERNAME + " already exists");
		user = users.get(i);
		break;
	}//End of IF
}//End of For
```

####7. Creamos una nueva sesión:
Para sincronizar las facturas del SAT primero tenemos que crear una sesión, la sesión estará ligada al usuario y tiene un proceso de expiración de 5 minutos después de que ésta ha estado inactiva. Para crear una sesión:

```java
session = new Session(user);
System.out.println("Token: " + session.token);
```

####8. Podemos validar la sesión creada:
De manera opcional podemos validar la sesión, es decir, checar que no haya expirado.

```java
boolean sessionVerified = session.verify();
System.out.println("Sesion verified:" + sessionVerified);
```

####9. Consultamos el catálogo de instituciones que podemos sincronizar y extraemos el SAT:
Paybook tiene un catálogo de instituciones que podemos sincronizar por usuario:

![Instituciones](https://github.com/Paybook/sync-py/blob/master/sites.png "Instituciones")

A continuación consultaremos este catálogo y seleccionaremos el sitio del SAT para sincronizar las facturas del usuario que hemos creado en el paso 5:

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

####10. Configuramos nuestras credenciales del SAT:
Una vez que hemos obtenido el sitio del SAT del catálogo de institiciones, configuraremos las credenciales de nuestro usuario (estas credenciales son las que el usuario utiliza para acceder al portal del SAT).

```java
HashMap<String,Object> credentials_data = new HashMap<String,Object>();
credentials_data.put("rfc", BANK_USERNAME);
credentials_data.put("password", BANK_PASSWORD);
credentials = new Credentials(session,bank_site.id_site,credentials_data); 
System.out.println("Credentials username: " + credentials.username);
```

####11. Checamos el estatus de sincronización de las credenciales creadas y esperamos a que la sincronización finalice:
Cada vez que registamos unas credenciales Paybook inicia un Job (proceso) que se encargará de validar esas credenciales y posteriormente sincronizar las transacciones. Este Job se puede representar como una maquina de estados:

![Job Estatus](https://github.com/Paybook/sync-py/blob/master/normal.png "Job Estatus")

Una vez registradas las credenciales se obtiene el primer estado (Código 100), posteriormente una vez que el Job ha empezado se obtiene el segundo estado (Código 101). Después de aquí, en caso de que las credenciales sean válidas, prosiguen los estados 202, 201 o 200. Estos indican que la sincronización está en proceso (código 201), que no se encontraron transacciones (código 202), o bien, la sincronización ha terminado (código 200). La librería proporciona un método para consultar el estado actual del Job. Este método se puede ejecutar constantemente hasta que se obtenga el estado requerido por el usuario, para este ejemplo especifico consultamos el estado hasta que se obtenga un código 200, es decir, que la sincronización haya terminado:

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

####12. Consultamos las facturas sincronizadas:
Una vez que ya hemos checado el estado de la sincronización y hemos verificado que ha terminado (código 200) podemos consultar las facturas sincronizadas:

```java
HashMap<String,Object> options = new HashMap<String,Object>();
options.put("id_credential", credentials.id_credential);
options.put("limit", REQUEST_LIMIT);			
transactions = Transaction.get(session, options);
System.out.println("Bank transactions: " + transactions.size());
```

####13. Consultamos la información de archivos adjuntos:
Podemos también consultar los archivos adjuntos a estas facturas, recordemos que por cada factura el SAT tiene una archivo XML y un archivo PDF:
```java
attachments = Attachment.get(session, options);
System.out.println("Bank attachments: " + attachments.size());
```

####14. Obtenemos el XML y PDF de alguna factura:
Podemos descargar estos archivos:

```java	
if(attachments.size() > 0){
	String id_attachment = attachments.get(0).id_attachment;
	attachment = Attachment.get(session, id_attachment).get(0);
	System.out.println(attachment.content);
}//End of IF
```

¡Felicidades! has terminado con este tutorial. 

### Siguientes Pasos

- Revisar el tutorial de como sincronizar una institución bancaria con credenciales simples (usuario y contraseña) [aquí](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_normal_bank.md)

- Revisar el tutorial de como sincronizar una institución bancaria con token [aquí](https://github.com/Paybook/sync-java/blob/master/src/main/java/com/quickstarts/quickstart_token_bank.md)

- Puedes consultar y analizar la documentación completa de la librería [aquí](https://github.com/Paybook/sync-java/blob/master/README.md)

- Puedes consultar y analizar la documentación del API REST [aquí](https://www.paybook.com/sync/docs)

- Acceder a nuestro proyecto en Github y checar todos los recursos que Paybook tiene para ti [aquí](https://github.com/Paybook)


























