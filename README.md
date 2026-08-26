"""
# Document récapitulatif sur les choix d'architecture, Patterns et les anomalies

## 1. Architecture Globale (Clean Architecture Multi-Modules)
Le projet adopte une Clean Architecture structurée en multi-modules Gradle, séparant strictement les responsabilités selon les principes SOLID et garantissant l'indépendance de la logique métier

**Découpage des Modules :**
*   **app (`MainApplication.kt`)** : Point d'entrée de l'application, initialisation Hilt et assemblage du graphe de navigation avec Jetpack Navigation (`AppNavHost.kt`).
*   **feature/ (`feature:albums`, `feature:album-detail`)** : Modules d'interface utilisateur encapsulant les écrans Jetpack Compose, ViewModels et contrats MVI.
*   **domain/ (`domain:album`, `domain:favs`)** : Pure logique métier, contient les modèles du domaine (Album, FavoriteItem), les Use Cases (`ObserveAlbumsUseCase`, `AddFavoriteUseCase` etc ..) et les interfaces de répertoires (`AlbumRepository`, `FavoritesRepository`).
*   **data/ (`data:albums`, `data:favorites`)** : Implémentations des répertoires (`AlbumRepositoryImpl.kt`, `FavoritesRepositoryImpl`), gestion des sources de données distantes (API) et locales (Room), ainsi que les mappers.
*   **database/ core:database (`AppDatabase.kt`)** : Abstraction et implémentation de la persistance SQLite via Room.
*   **core/** : Modules utilitaires réutilisables (`core:mvi`, `core:network`, `core:ui`, `core:common`, `core:logger`, `core:analytics`).

## 2. Design Patterns Appliqués
*   **MVI (Model-View-Intent)** : Implémenté via une classe de base générique `MviViewModel<Intent, State>`.
    *   **State** : État unique et immuable (`AlbumsUiState`) exposé sous forme de `StateFlow` .
    *   **Intent** : actions utilisateur immuables (`AlbumsIntent`) .
    *   **Effects** : Effets de bord ponctuels (Navigation, Toast) via `SharedFlow` (`AlbumsEffect`) .
*   **Offline-First & Single Source of Truth (SSOT)** : La base de données locale (Room) sert de source unique de vérité. La couche UI observe en continu les flux de la base de données locale (`observeAlbums()`). Les requêtes réseau mettent uniquement à jour Room, déclenchant automatiquement la mise à jour réactive de l'UI.
*   **Repository Pattern** : Découplage entre l'origine des données et leur consommation par les cas d'utilisation via `AlbumRepositoryImpl`.
*   **Use Case / Interactor Pattern** : Encapsulation fine des règles métier dans des classes d'action dédiées (ex: `ObserveAlbumsUseCase`, `ToggleFavoritesUseCase`).
*   **Data Mapper Pattern** : Mappage strict et explicite à chaque frontière de couche : `AlbumDto` (Réseau) ===> `AlbumEntity` (Base locale) ===> `Album` (Domaine).
*   **Dependency Injection (DI)** : Injection de dépendances globale et modulaire avec Dagger Hilt (modules `@InstallIn(SingletonComponent::class)` et `@HiltViewModel`) .
*   **Type-Safe Navigation** : Routes déclarées sous forme de sealed interface sérialisables avec kotlinx.serialization (`AppRoute.kt`) .

## 3. Librairies & Technologies Utilisées
Toutes les dépendances sont centralisées dans le Version Catalog (`libs.versions.toml`)  :

| Catégorie | Librairie | Justification |
| :--- | :--- | :--- |
| **Interface UI** | Jetpack Compose | UI 100% déclarative avec Material 3 (`androidx.compose.material3`). |
| **Navigation** | Navigation Compose | Navigation moderne et fortement typée via `AppRoute`. |
| **Design System** | Adevinta Spark UI | Intégration du design system Spark (`com.adevinta.spark`). |
| **Chargement d'Images** | Coil 3 | Chargement d'images performant avec `coil-compose` et intercepteur OkHttp sur-mesure (ajout d'un User-Agent). |
| **Réseau** | Retrofit 3 & OkHttp 5 | Client HTTP type-safe avec logging-interceptor pour l'inspection des appels REST. |
| **Sérialisation** | Kotlinx Serialization | Parsing JSON léger, rapide et natif Kotlin (`kotlinx-serialization-json`). |
| **Base de Données** | Room | Persistance locale SQLite avec support natif Kotlin Flow et migrations. |
| **Asynchronisme** | Kotlin Coroutines & Flow | Concurrence et flux de données réactifs hors du thread UI (`Dispatchers.IO`). |
| **Injection (DI)** | Dagger Hilt | Injection de dépendances officielles recommandée par Google. |
| **Débogage** | LeakCanary | Détection automatique des fuites de mémoire en environnement de développement. |
| **Tests** | JUnit 4 & Coroutines Test | Tests unitaires isolés pour les ViewModels, Use Cases, Repositories et Mappers. |

## 4. Les anomalies du projet
*   **Perte totale de données au changement de configuration (rotation d'écran)** : Le ViewModel utilise un SharedFlow sans mémoire tampon (`replay = 0`). Lors d'une rotation d'écran, l'UI se réabonne et ne reçoit plus rien : l'écran devient blanc.
*   **Utilisation d'un GlobalScope & Gestion des erreurs erronée** : L'appel réseau est lancé dans `GlobalScope.launch` au lieu de `viewModelScope`, causant des fuites mémoire et empêchant l'annulation des requêtes. Le bloc `catch (_: Exception)` est vide, ce qui masque silencieusement toutes les erreurs réseau sans informer l'utilisateur.
*   **Inversion de sécurité sur l'intercepteur de logs** : Dans `DataModule.kt`, `HttpLoggingInterceptor` est activé en mode BODY lorsque `!BuildConfig.DEBUG`. Les données réseau sensibles sont affichées dans Logcat en version Release/Production, mais masquées en Debug.
*   **Erreur dans le Manifest (Deux icônes d'application)** : `DetailsActivity` possède un intent-filter avec `ACTION_MAIN` et `CATEGORY_LAUNCHER`. Deux icônes apparaissent dans le launcher du téléphone .
*   **Absence totale de persistance locale & Mode Offline** : Aucune base de données (Room) n'est implémentée dans le module `:data`.
*   **Fuite mémoire d'Activity dans AnalyticsHelper** : Stockage d'un Context d'Activity dans une instance singleton conservée par l'Application.
*   **Absence de couche Domain** : Pas de modèles métier (Album), pas de Use Cases [cite: 1]. Les DTOs réseau (`AlbumDto`) fuient directement jusqu'aux Composables UI .
*   **Anti-pattern Service Locator** : Instanciation manuelle des dépendances via des classes lazy (`AppDependencies`, `DataDependencies`) au lieu d'un vrai framework DI comme Hilt .


## 5. Contact 
*   **Prénom & Nom** : Khouloud MAAMOURI 
*   **Email** : khouloud.maamouri97@gmail.com
*   **Téléphone** : +216 54 240 646 
"""
