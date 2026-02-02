Este repositorio contiene un microservicio backend desarrollado en Java y Spring Boot, responsable del registro y consulta de la trazabilidad de los pedidos dentro de una arquitectura de microservicios.

El servicio permite almacenar los cambios de estado de los pedidos a lo largo de su ciclo de vida, facilitando auditoría, monitoreo y análisis histórico. La solución está diseñada bajo arquitectura hexagonal (Ports & Adapters), utiliza persistencia NoSQL para el manejo eficiente de eventos y expone APIs REST orientadas a la consulta de información histórica y de seguimiento.
