🧾 Gestión de Gastos – Spring Boot
<img width="1904" height="939" alt="controldegastos" src="https://github.com/user-attachments/assets/1fcb9835-57b2-42fd-9241-04252a5818ec" />

Este proyecto es una aplicación web de control de gastos personales, desarrollada con Spring Boot, Thymeleaf, Bootstrap y MySQL. Permite registrar gastos diarios y mensuales, filtrar por fechas, y visualizar un resumen del presupuesto disponible.

🔹 Características principales

Registro de gastos diarios y mensuales.

Visualización de los totales diarios, mensuales y saldo disponible.

Filtro de gastos por rango de fechas y tipo.

Interfaz responsiva con Bootstrap 5.

Integración con Thymeleaf para plantillas HTML dinámicas.

Manejo de presupuestos mensuales y cálculo automático del saldo restante.

Botón de “Hoy” para filtrar automáticamente los gastos del día actual.

Sistema sencillo, fácil de mantener y expandir.

🛠 Tecnologías utilizadas

Backend: Java 17, Spring Boot 3

Frontend: Thymeleaf, Bootstrap 5

Base de datos: MySQL

Dependencias principales:

Spring Data JPA

Spring Web

Spring Boot DevTools

MySQL Connector

📂 Estructura del proyecto
gastos/
├─ src/main/java/com/example/gastos/
│  ├─ controller/       -> Controladores (GastoController)
│  ├─ model/            -> Entidades (Gasto, Presupuesto)
│  └─ repository/       -> Repositorios JPA
├─ src/main/resources/
│  ├─ templates/        -> HTML con Thymeleaf (index.html, form.html, gastos-diarios.html, gastos-mensuales.html)
│  └─ application.properties -> Configuración de Spring Boot y MySQL
└─ pom.xml              -> Dependencias Maven

⚡ Funcionalidad

Página principal (index):
Muestra el presupuesto del mes, los totales de gastos diarios y mensuales, y el saldo disponible.

Agregar gasto:

Tipo de gasto: Diario o Mensual

Categoría y monto

Fecha (por defecto el día actual)

Filtrar gastos:

Por rango de fechas

Por tipo (Diario o Mensual)

Botón “Hoy” para filtrar automáticamente el día actual

Resumen de gastos mensuales:

Tabla con gastos mensuales

Totales y saldo restante

⚙ Configuración del proyecto

Clonar el repositorio:

[git clone https://github.com/usuario/gastos.git](https://github.com/jeffer8811/Gastos.diarios.mensuales)
cd gastos


Configurar la base de datos en application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/gastos_db
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update


Ejecutar la aplicación:

mvn spring-boot:run


Acceder desde el navegador:

http://localhost:8080/

💡 Mejoras futuras

Autenticación de usuarios (login/registro).

Reportes en PDF o Excel.

Gráficos de gastos con Chart.js.

Gestión de categorías personalizadas.

📌 Autor

Jeffer Villegas – Proyecto personal de control de gastos.
