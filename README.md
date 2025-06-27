# Caritas 2.0 - Arquitectura ViewModel

Este proyecto ha sido refactorizado para seguir la arquitectura ViewModel de Android, implementando las mejores prácticas recomendadas por Google.

## Arquitectura Implementada

### 1. Capa de Datos (Data Layer)
- **Entidades**: `Pedido`, `Cliente`, `Color`, `Blancas`, `PedidoConCliente`
- **DAOs**: Interfaces para acceso a datos con Room
- **Base de Datos**: `AppDatabase` con Room
- **Repositorio**: `Repository` que coordina entre la base de datos y la UI

### 2. Capa de Presentación (Presentation Layer)
- **ViewModels**: Manejan la lógica de negocio y el estado de la UI
  - `HomeViewModel`: Pantalla principal
  - `AddViewModel`: Agregar pedidos
  - `OrderViewModel`: Gestión de pedidos
  - `PriceViewModel`: Tabla de precios
  - `ModifyViewModel`: Modificar pedidos
  - `GaleryViewModel`: Galería de pedidos
  - `SharedViewModel`: Estado compartido entre pantallas

### 3. Capa de UI (UI Layer)
- **Screens**: Composables que observan el estado de los ViewModels
- **Components**: Componentes reutilizables
  - `ErrorComponent`: Mostrar errores
  - `SuccessComponent`: Mostrar mensajes de éxito
  - `LoadingComponent`: Estados de carga

## Características Implementadas

### ✅ ViewModels con StateFlow
- Estado reactivo usando `StateFlow`
- Separación clara entre UI y lógica de negocio
- Manejo de estados de carga, error y éxito

### ✅ Repository Pattern
- Coordinación entre múltiples fuentes de datos
- Operaciones asíncronas con coroutines
- Manejo de errores centralizado

### ✅ Dependency Injection
- `ViewModelFactory` para crear ViewModels
- `CaritasApplication` para inicialización de dependencias
- Inyección de dependencias manual (sin Hilt por simplicidad)

### ✅ Room Database
- Base de datos local con Room
- DAOs con Flow para datos reactivos
- Relaciones entre entidades

### ✅ Componentes Reutilizables
- Componentes comunes para errores, éxito y carga
- Consistencia visual en toda la aplicación

## Estructura del Proyecto

```
app/src/main/java/com/example/caritas20/
├── Data/
│   ├── Entities (Pedido, Cliente, Color, Blancas)
│   ├── DAOs (PedidoDao, ClienteDao, etc.)
│   ├── AppDatabase.kt
│   ├── Repository.kt
│   └── DataInitializer.kt
├── ViewModels/
│   ├── HomeViewModel.kt
│   ├── AddViewModel.kt
│   ├── OrderViewModel.kt
│   ├── PriceViewModel.kt
│   ├── ModifyViewModel.kt
│   ├── GaleryViewModel.kt
│   ├── SharedViewModel.kt
│   └── ViewModelFactory.kt
├── Screens/
│   ├── HomeScreen.kt
│   ├── AddScreen.kt
│   ├── OrderScreen.kt
│   ├── PriceScreen.kt
│   ├── ModifyScreen.kt
│   └── GaleryScreen.kt
├── ui/
│   └── components/
│       ├── ErrorComponent.kt
│       ├── SuccessComponent.kt
│       └── LoadingComponent.kt
├── Functions/
│   └── NavGraph.kt
├── MainActivity.kt
└── CaritasApplication.kt
```

## Beneficios de la Arquitectura

1. **Separación de Responsabilidades**: UI, lógica de negocio y datos están claramente separados
2. **Testabilidad**: ViewModels y Repository son fácilmente testables
3. **Mantenibilidad**: Código organizado y fácil de mantener
4. **Escalabilidad**: Fácil agregar nuevas características
5. **Reactividad**: UI se actualiza automáticamente cuando cambian los datos
6. **Manejo de Estados**: Estados de carga, error y éxito bien definidos

## Uso

1. **Compilar el proyecto**: `./gradlew build`
2. **Ejecutar en dispositivo**: `./gradlew installDebug`
3. **Ejecutar tests**: `./gradlew test`

## Dependencias Principales

- **Room**: Base de datos local
- **ViewModel**: Arquitectura de presentación
- **StateFlow**: Flujos reactivos
- **Coroutines**: Programación asíncrona
- **Compose**: UI declarativa

## Próximos Pasos

- [ ] Implementar tests unitarios para ViewModels
- [ ] Agregar tests de integración para Repository
- [ ] Implementar Hilt para inyección de dependencias
- [ ] Agregar navegación con argumentos
- [ ] Implementar cache offline
- [ ] Agregar logging y analytics 