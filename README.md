# Librería - App Android (Prototipo)

Aplicación de ejemplo para una librería online que consume datos reales de la **Open Library API**.

## Resumen
Proyecto de una app Android que permite explorar libros, ver detalles y agregar libros a un carrito. Está implementado en Kotlin con arquitectura simple (Activity + ViewModel + Repository) y consume la Search API de Open Library.

## Estado actual
- Lista de libros con `RecyclerView` (carga desde Open Library).
- Detalle de libro (nueva Activity mediante `Intent`).
- Carrito en memoria con observación por `LiveData` (`CartManager`).
- Carga de portadas con Glide.
- Fallback de datos si la API falla.

## Estructura relevante
```
app/src/main/java/com/example/libreria/
├── MainActivity.kt
├── BookDetailActivity.kt
├── BookAdapter.kt
├── CartActivity.kt
├── CartAdapter.kt
├── CartManager.kt
├── MainViewModel.kt
├── model/Book.kt
├── api/ (Retrofit + modelos de respuesta)
└── repository/BookRepository.kt

app/src/main/res/
├── layout/ (activity_book_list.xml, item_book.xml, activity_book_detail.xml, etc.)
├── drawable/ (iconos y shapes)
└── values/ (colores, strings, dimensiones)
```

---
## Requisitos y versiones (recomendadas)
- Android Studio: Electric Eel / Flamingo / Giraffe o superior (preferible la más reciente estable disponible).
- Kotlin: versión usada en el proyecto (ver `build.gradle.kts`).
- Gradle Wrapper: la del proyecto (`gradle/wrapper/gradle-wrapper.properties`).

Importante: en algunos entornos puede haber incompatibilidades entre la versión del Android Gradle Plugin (AGP), la versión de Gradle y el `compileSdk` de las librerías (por ejemplo, algunas versiones de `androidx.activity` piden compileSdk 36 y AGP 8.9+). Si obtienes errores al sincronizar o compilar, revisa el apartado "Problemas conocidos" más abajo.

---
## Cómo compilar y ejecutar (pasos)
1. Abre Android Studio y selecciona "Open an existing project" apuntando a la carpeta del proyecto.
2. Espera a que Android Studio sincronice Gradle y descargue dependencias.
3. Si ves errores relacionados con Gradle/AGP/compileSdk, sigue las indicaciones del apartado "Problemas conocidos".
4. Ejecuta en un emulador o dispositivo (API 26+ recomendado si no se cambia `minSdk`).

Comandos Git básicos (desde la raíz del proyecto en Windows cmd):

```cmd
cd C:\Users\carlos\AndroidStudioProjects\libreria
./gradlew clean assembleDebug
```

---
## Dependencias principales (resumen)
- Retrofit 2 + Gson (cliente HTTP y parseo JSON)
- OkHttp (HTTP client)
- Glide (carga y cache de imágenes)
- Coroutines (manejo asíncrono)
- RecyclerView, Material Components

(Ver `build.gradle.kts` para la lista exacta y versiones.)

---
## Problemas conocidos y soluciones rápidas
1. Error AGP / Gradle: "Minimum supported Gradle version is X" o incompatibilidades AGP
   - Solución rápida: editar `gradle/wrapper/gradle-wrapper.properties` para usar la versión de Gradle requerida por la versión de AGP en `build.gradle.kts`. Ejemplo de línea:
     ```text
     distributionUrl=https\://services.gradle.org/distributions/gradle-8.11.1-all.zip
     ```
   - Alternativa: bajar la versión del plugin de Android (en el `plugins` block o `build.gradle.kts`) a una compatible con tu Gradle wrapper.

2. Errores de AAR metadata pidiendo `compileSdk` más alto (p. ej. libs que requieren compileSdk 36):
   - Si no puedes actualizar AGP/Gradle inmediatamente, usa versiones de librerías que sean compatibles con tu `compileSdk` actual (ej., cambia versiones de `androidx.activity` / `core-ktx`).
   - Para proyectos a entregar, recomiendo actualizar `compileSdk` y AGP alineados con las librerías usadas.

3. Fallos al pushear a GitHub (autenticación):
   - Desde 2021 GitHub exige token o SSH para push. Si `git push` falla, configura un PAT (Personal Access Token) y usa HTTPS con credenciales o configura SSH.

---
## Capturas y entregables
Por favor añade las capturas en `docs/screenshots/` con nombres `home.png` y `detail.png`. Estas imágenes son requeridas para la entrega final.

---
## Cómo pushear este proyecto al repositorio remoto
He intentado automatizar el push (si estás en la máquina local te pedirá credenciales si usas HTTPS). Comandos que puedes ejecutar en tu máquina (Windows cmd):

```cmd
cd C:\Users\carlos\AndroidStudioProjects\libreria
# Añadir remote (si no existe)
git remote add origin https://github.com/carlhidalgo/Libreria_android.git
# O actualizar URL si ya existe
# git remote set-url origin https://github.com/carlhidalgo/Libreria_android.git

# Asegúrate de revisar cambios
git status

# Añadir y commitear el README actualizado
git add README.md
git commit -m "Actualizar README con instrucciones de compilación y push"

# Pushear a la rama principal (te pedirá credenciales si corresponde)
git push -u origin HEAD
```

Si el push falla por autenticación con HTTPS, crea un Personal Access Token (PAT) en GitHub y úsalo como contraseña, o configura SSH y añade la clave pública a tu cuenta.

---
## Notas sobre el carrito
- Actualmente el carrito está implementado en memoria (`CartManager` con `LiveData`).
- Persistencia recomendada: SharedPreferences (rápido) o Room (más robusto) si necesitas mantener el carrito entre sesiones.

---
## Mejoras propuestas (prioritarias)
- Resolver compatibilidad AGP/Gradle y `compileSdk` para evitar errores de AAR metadata.
- Implementar persistencia del carrito (SharedPreferences o Room).
- Ajustar colores / contraste del botón "Agregar" en las cards (mejor accesibilidad).
- Añadir Snackbar "Deshacer" tras agregar al carrito.
- Añadir tests básicos (unitarios para `CartManager`).

---
## Contacto / Autor
Proyecto de evaluación. Si quieres que yo realice los cambios y haga el push desde este entorno, puedo intentarlo; puede pedir tus credenciales o fallar por permisos: te indicaré el resultado y, en caso de error, pasos para completarlo desde tu máquina.

---

Gracias: si quieres que intente subir los cambios ahora, dime y lo intento (te mostraré la salida y pasos si hay problemas de autenticación).