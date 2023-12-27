package com.example.lesson3.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lesson3.data.Manager
import com.example.lesson3.data.Client
import com.example.lesson3.data.Invoice
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ListDAO {
        @Query("SELECT * FROM restaurants order by name")
        fun getFaculty(): Flow<List<Manager>>

        @Insert(entity = Manager::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertFaculty(manager: Manager)

        @Update(entity = Manager::class)
        suspend fun updateFaculty(manager: Manager)

        @Insert(entity = Manager::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAllFaculty(managerList: List<Manager>)

        @Delete(entity = Manager::class)
        suspend fun deleteFaculty(manager: Manager)

        @Query("DELETE FROM restaurants")
        suspend fun deleteAllFaculty()

        @Query("SELECT * FROM courses")
        fun getAllGroups(): Flow<List<Client>>

        @Query("SELECT * FROM courses where restaurant_id=:restaurantId")
        fun getFacultyGroups(restaurantId : UUID): Flow<List<Client>>

        @Insert(entity = Client::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertGroup(client: Client)

        @Delete(entity = Client::class)
        suspend fun deleteGroup(client: Client)

        @Query("DELETE FROM courses")
        suspend fun deleteAllGroups()

        @Query("SELECT * FROM foods")
        fun getAllStudents(): Flow<List<Invoice>>

        @Query("SELECT * FROM foods where course_id=:courseId")
        fun getGroupStudents(courseId : UUID): Flow<List<Invoice>>

        @Insert(entity = Invoice::class, onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertStudent(invoice: Invoice)

        @Delete(entity = Invoice::class)
        suspend fun deleteStudent(invoice: Invoice)

        @Query("DELETE FROM foods")
        suspend fun deleteAllStudents()
}
