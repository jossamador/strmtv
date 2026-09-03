Integrantes: Josué Amador Ynfante, Jorge Estrada Estrada, Orlando Andrade Hernandez

Este repositorio es dónde se encuentra todo el proyecto de Android Studio llamado Strmtv, una app que muestra informacion de películas, series y cast. 


# STRMTV

Aplicación de streaming y exploración de contenido audiovisual desarrollada para Android. 
La aplicación permite consultar películas, series y personas, utilizando tanto una fuente 
de datos local como la API de The Movie Database (TMDB).

---

## 1. Tecnologías usadas

### Front-end / Android

- **Kotlin** — Lenguaje principal del proyecto.
- **Jetpack Compose** — Desarrollo de la interfaz de usuario.
- **Material 3** — Componentes y estilos de UI.
- **Navigation Compose** — Navegación entre las diferentes pantallas.
- **Coil** — Carga y visualización de imágenes desde URLs.
- **ViewModel** — Gestión del estado y lógica de presentación.
- **StateFlow / MutableStateFlow** — Manejo reactivo del estado de la aplicación.
- **Coroutines** — Ejecución de operaciones asíncronas.
- **Hilt / Dagger Hilt** — Inyección de dependencias.
- **Android SDK** — Plataforma de desarrollo de la aplicación.

### Backend / Capa de datos

La aplicación utiliza una arquitectura de acceso a datos mediante repositorios y servicios 
de red.

- **Retrofit** — Comunicación HTTP con APIs externas.
- **Gson** — Serialización y deserialización de datos JSON.
- **TMDB API** — Fuente externa para obtener información de películas, series y personas.
- **API local** — Fuente de datos local utilizada por la aplicación.
- **JSON** — Formato utilizado para el intercambio de información.
- **YouTube** — Los trailers obtenidos desde TMDB se reproducen mediante URLs de YouTube.

### APIs utilizadas

#### The Movie Database (TMDB)

Se utiliza la API de TMDB para:

- Buscar películas.
- Buscar series.
- Buscar personas.
- Obtener información detallada.
- Obtener créditos.
- Obtener trailers.
- Obtener recomendaciones.
- Obtener información de producción y duración.

La búsqueda utiliza principalmente el endpoint:

`/search/multi`

También se utilizan endpoints específicos para películas y series, como:

- `/movie/{movie_id}`
- `/tv/{tv_id}`
- `/movie/{movie_id}/videos`
- `/tv/{tv_id}/videos`
- `/{media_type}/{media_id}/credits`
- `/{media_type}/{media_id}/recommendations`

---

## 2. Arquitectura

STRMTV utiliza una arquitectura basada en **MVVM (Model-View-ViewModel)**, separando 
la interfaz de usuario, la lógica de presentación y el acceso a los datos.

### Estructura general

```text
app/
└── src/
    └── main/
        └── java/
            └── com.example.strmtv/
                │
                ├── data/
                │   └── model/
                │       ├── Item.kt
                │       ├── remote/
                │       │   ├── TMDBApiService.kt
                │       │   ├── TMDBItem.kt
                │       │   └── ...
                │       └── repository/
                │           └── ItemRepositoryImpl.kt
                │
                ├── domain/
                │   └── repository/
                │       └── ItemRepository.kt
                │
                ├── presentation/
                │   └── home/
                │       ├── HomeScreen.kt
                │       ├── SharedViewModel.kt
                │       │
                │       ├── detail/
                │       │   └── DetailScreen.kt
                │       │
                │       ├── search/
                │       │   ├── SearchScreen.kt
                │       │   ├── SearchUiState.kt
                │       │   └── SearchViewModel.kt
                │       │
                │       └── navigation/
                │           └── Routes.kt
                │
                └── MainActivity.kt

