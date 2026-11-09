# AutoRent API mejorado

Flujo de cuenta USER:
1. Registro.
2. Código de verificación enviado por correo.
3. Usuario verifica el código.
4. La cuenta queda PENDIENTE.
5. El ADMIN la aprueba.
6. El USER ya puede iniciar sesión.

El ADMIN puede aprobar, bloquear y reactivar cuentas USER. No existe cambio de roles y la cuenta ADMIN principal está protegida.

El catálogo inicial incluye 8 vehículos con fotografías reales remotas. Las imágenes que un administrador suba manualmente no se sobrescriben al reiniciar.

Ejecutar:
mvn clean spring-boot:run
