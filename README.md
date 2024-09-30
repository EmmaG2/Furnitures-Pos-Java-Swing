# Mueblería
El dueño de la mueblería, fabrica y compra muebles, el negocio creció
y tiene muchos muebles, ahorita tiene todo guardado de manera física

Cuando un cliente necesita hacer una venta, tiene que contar los muebles
Tiene el problema de que cuando vende, el no siempre está ahí, y vende alguien más

El cliente necesita conocer sus ganancias, también necesita conocer los registros


# Solución

El programa debe tener la capacidad de poder crear productos
Debe poder llevar un registro de las ventas

## Capas del proyecto

Las capas del proyecto serán las siguientes

### Modelos
Los modelos representan como se guardará la información en el sistema, es solo una
representación sobre como será almacenada la información

### Repositorios
Esta parte, únicamente será la responsable de guardar, eliminar, o modificar la información, esta
capa no tiene ninguna lógica 

### Servicios
En los servicios, se almacena la lógica de negocio, en base a ciertas lógica, el servicio
será el encargado de comunicarse con los repositorios, para poder modificar la información
een base a las necesidades del negocio, el servicio se encarga de modificar la base de datos
con la lógica

### Controladores
Los controladores interactúan con los servicios, y reciben las órdenes desde la vista, el controlador
llama al servicio, recibe las órdenes de la vista

### Vista
La vista, es la parte con la que interactuará el usuario del programa


El programa, únicamente estará disponible en una computadora, en caso de 
que el negocio crezca, y se necesite el programa en más de una sucursal
Esté podrá ser extendable, para poder almacenar toda la información en una base de datos
El programa tendrá una arquitectura limpia, actualmente, la base será en archivos json
Pero las capas del programa, permitirán solamente modificar la capa de persistencia
Para realizar la migración, sin tener que afectar a lo demás

El producto debe tener los siguientes campos
+ Tipo de mueble
+ Precio
+ Stock
+ imagen
+ nombre


