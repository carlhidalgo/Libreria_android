# Librería Android

Descripción breve

- Aplicación Android nativa (Kotlin) que permite explorar una lista de libros, ver los detalles de cada libro y agregar/eliminar libros en un carrito de compras local.

Cómo ejecutar el proyecto

1. Clona el repositorio:

```cmd
cd %USERPROFILE%\source\repos
git clone https://github.com/carlhidalgo/Libreria_android.git
cd Libreria_android
```

2. Abrir y compilar en Android Studio:

- Abrir Android Studio → "Open" → seleccionar la carpeta del proyecto.
- Esperar a que Gradle sincronice y descargue dependencias.
- Ejecutar la app en un emulador o dispositivo (Run ▶️).

3. Alternativa por terminal (Windows cmd):

```cmd
cd C:\Users\carlos\AndroidStudioProjects\libreria
.\gradlew clean
.\gradlew assembleDebug
.\gradlew installDebug
```

Decisiones tomadas durante el desarrollo

- Interfaz nativa en Kotlin usando componentes de Material (MaterialComponents) para coherencia visual.
- Lista de libros implementada con `RecyclerView` para rendimiento en scroll y reutilización de vistas.
- Navegación simple entre pantallas usando `Intent` (MainActivity → BookDetailActivity) pasando título y descripción.
- Carrito implementado en memoria (simple `List`/`Map`) para la versión inicial; incluye botones para agregar y eliminar por ítem, y pantalla de carrito con botón para cerrar.
- Recursos y accesibilidad: textos extraídos a `strings.xml`, `contentDescription` en imágenes y compatibilidad RTL (uso de `paddingStart`/`paddingEnd`).
- Icono redondo añadido como recurso para evitar errores aapt y facilitar el despliegue.
- Pequeñas optimizaciones de UX/performance: uso de vistas recicladas, imágenes escaladas y layouts planos para minimizar trabajo del renderer.

Notas


- Para subir estos cambios al repositorio remoto, usa los comandos Git locales (ver abajo) y verifica que tu clave SSH/HTTPS esté configurada.

Comandos útiles para subir cambios

```cmd
cd C:\Users\carlos\AndroidStudioProjects\libreria
git add README.md
git commit -m "Agregar README con instrucciones de ejecución y decisiones" 
git push origin main
```

Si tu rama principal tiene otro nombre (por ejemplo `master`), ajusta el comando `git push` en consecuencia.