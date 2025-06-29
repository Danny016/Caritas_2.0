package com.example.caritas20.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Pedido::class,
        Cliente::class,
        ProductoColor::class,
        ProductoBlanca::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun pedidoDao(): PedidoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun colorDao(): ColorDao
    abstract fun blancasDao(): BlancasDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        // Migración de versión 2 a 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear nuevas tablas con la estructura actualizada
                database.execSQL("""
                    CREATE TABLE producto_color_new (
                        numero TEXT PRIMARY KEY NOT NULL,
                        cantidad INTEGER NOT NULL,
                        precio REAL NOT NULL
                    )
                """.trimIndent())
                
                database.execSQL("""
                    CREATE TABLE producto_blanca_new (
                        numero TEXT PRIMARY KEY NOT NULL,
                        cantidad INTEGER NOT NULL,
                        precio REAL NOT NULL
                    )
                """.trimIndent())
                
                // Migrar datos de la tabla Blancas antigua a producto_blanca_new
                database.execSQL("""
                    INSERT INTO producto_blanca_new (numero, cantidad, precio)
                    SELECT CAST(numero AS TEXT) || 'B', cantidad, precio FROM Blancas
                """.trimIndent())
                
                // Migrar datos de la tabla producto_color antigua a producto_color_new
                database.execSQL("""
                    INSERT INTO producto_color_new (numero, cantidad, precio)
                    SELECT CAST(numero AS TEXT) || 'C', cantidad, precio FROM producto_color
                """.trimIndent())
                
                // Eliminar tablas antiguas
                database.execSQL("DROP TABLE IF EXISTS Blancas")
                database.execSQL("DROP TABLE IF EXISTS producto_color")
                
                // Renombrar nuevas tablas
                database.execSQL("ALTER TABLE producto_blanca_new RENAME TO producto_blanca")
                database.execSQL("ALTER TABLE producto_color_new RENAME TO producto_color")
                
                // Actualizar la tabla pedido para usar los nuevos formatos
                database.execSQL("""
                    UPDATE pedido 
                    SET id_producto = CASE 
                        WHEN id_producto IN (SELECT CAST(numero AS TEXT) FROM producto_blanca_new) 
                        THEN id_producto || 'B'
                        ELSE id_producto || 'C'
                    END
                """.trimIndent())
            }
        }
        
        // Migración de versión 3 a 4 - Eliminar foreign keys de productos
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear nueva tabla pedido sin las foreign keys de productos
                database.execSQL("""
                    CREATE TABLE pedido_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        id_producto TEXT NOT NULL,
                        cantidad INTEGER NOT NULL,
                        id_cliente INTEGER NOT NULL,
                        FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente) ON DELETE CASCADE
                    )
                """.trimIndent())
                
                // Copiar datos de la tabla pedido antigua
                database.execSQL("""
                    INSERT INTO pedido_new (id, id_producto, cantidad, id_cliente)
                    SELECT id, id_producto, cantidad, id_cliente FROM pedido
                """.trimIndent())
                
                // Eliminar tabla antigua
                database.execSQL("DROP TABLE pedido")
                
                // Renombrar nueva tabla
                database.execSQL("ALTER TABLE pedido_new RENAME TO pedido")
                
                // Crear índices
                database.execSQL("CREATE INDEX index_pedido_id_producto ON pedido (id_producto)")
                database.execSQL("CREATE INDEX index_pedido_id_cliente ON pedido (id_cliente)")
            }
        }
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "caritas_database"
                )
                .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 