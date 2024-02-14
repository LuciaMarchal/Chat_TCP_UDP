# Práctica 3.- Sockets TCP y UDP

# Version TCP:

### Funcionalidades:

- La aplicación de chat multihilo permite a múltiples clientes conectarse simultáneamente a un servidor y comunicarse entre sí.
- Cada cliente puede enviar mensajes al servidor, que luego son distribuidos a todos los otros clientes conectados.
- Los clientes pueden ingresar un nombre o apodo para identificarse en el chat.
- El servidor gestiona las conexiones entrantes y salientes, así como la distribución de mensajes entre los clientes.

### Estructura del Código:

- El código consta de tres clases principales: **`ComunHilos`**, **`HiloChat`**, y **`ClienteChat`**.
- La clase **`ComunHilos`** es una clase compartida entre el servidor y los hilos de chat de los clientes. Almacena información sobre las conexiones actuales, los nombres de usuario, y los mensajes enviados en el chat.
- La clase **`HiloChat`** es un hilo que maneja la comunicación con un cliente específico. Recibe mensajes del cliente y los distribuye al resto de los clientes conectados.
- La clase **`ClienteChat`** representa la interfaz de usuario del cliente. Permite enviar mensajes al servidor y recibir mensajes del mismo.
- La clase **`Servidor`** es la entrada principal del servidor. Escucha las conexiones entrantes de los clientes y crea hilos de chat para manejar la comunicación con cada cliente.

### Algoritmos Principales:

1. **Manejo de Conexiones en el Servidor**:
    - El servidor utiliza un bucle while para aceptar conexiones entrantes de clientes.
    - Cuando un cliente se conecta, se crea un nuevo hilo de chat para manejar la comunicación con ese cliente.
    - El servidor verifica si el nombre de usuario proporcionado por el cliente ya está en uso antes de aceptar la conexión.
2. **Hilo de Chat del Cliente**:
    - El hilo de chat del cliente recibe mensajes del cliente y los distribuye a todos los demás clientes conectados.
    - Utiliza un bucle while para escuchar continuamente los mensajes del cliente.
    - Cuando un cliente se desconecta, el hilo de chat actualiza el estado del servidor y cierra la conexión.
3. **Cliente Chat**:
    - El cliente chat permite al usuario enviar mensajes al servidor y recibir mensajes de otros clientes.
    - Utiliza flujos de entrada y salida de datos para comunicarse con el servidor.
    - Proporciona una interfaz gráfica simple para enviar y recibir mensajes.

### Pruebas:

- Se probaron casos donde múltiples clientes se conectan y envían mensajes simultáneamente.
- Se verificó que el servidor maneje correctamente los nombres de usuario duplicados y la distribución de mensajes entre los clientes.

# Version UDP (Multicast):

### Funcionalidades:

1. **Chat Multicast**: Permite a los usuarios comunicarse entre sí en un entorno de red utilizando el protocolo multicast, lo que significa que un mensaje enviado por un usuario se distribuye a múltiples receptores simultáneamente.
2. **Interfaz gráfica de usuario (GUI)**: Proporciona una interfaz gráfica de usuario donde los usuarios pueden escribir mensajes, enviarlos y ver los mensajes recibidos.
3. **Control de nombres de usuario**: Evita que se utilicen nombres de usuario duplicados en el chat, garantizando que cada nombre de usuario sea único.
4. **Salir del chat**: Permite a los usuarios salir del chat de manera controlada y cerrar la aplicación.

### Estructura del código:

- El código está estructurado en una clase Java llamada **`ChatMulticast`**, que extiende **`JFrame`** para crear la interfaz gráfica de usuario.
- Se utiliza el paquete **`UDP.util`** para organizar las clases relacionadas con el chat multicast.
- Se importan las clases necesarias de **`javax.swing.*`** y **`java.awt.event.*`** para la creación de la GUI y el manejo de eventos.
- Se define una clase principal que contiene el método **`main()`** para iniciar la aplicación.

### Algoritmos principales:

1. **Envío de mensajes**:
    - Cuando un usuario presiona el botón "Enviar", se crea un paquete **`DatagramPacket`** que contiene el mensaje del usuario.
    - Este paquete se envía al grupo multicast utilizando el socket multicast.
2. **Recepción de mensajes**:
    - El hilo de ejecución principal del programa está constantemente esperando la recepción de mensajes.
    - Cuando llega un mensaje, se recibe a través del socket multicast y se muestra en el área de texto de la GUI.
3. **Control de nombres de usuario**:
    - Antes de permitir que un usuario ingrese al chat, se verifica si el nombre de usuario no está vacío y no está duplicado.
    - Se utiliza un conjunto (**`Set`**) para almacenar los nombres de usuario ya utilizados y garantizar que sean únicos.
4. **Salir del chat**:
    - Cuando un usuario presiona el botón "Salir", se envía un mensaje indicando que el usuario abandona el chat al grupo multicast.
    - Luego, se cierra el socket y se finaliza la ejecución del programa.
