package kz.edu.kmf.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "firstName") var firstName: String,
    @ColumnInfo(name = "lastName") var lastName: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "userStatus") var userStatus: Int
):Parcelable {
    override fun toString(): String {
        return "User(id=$id, username='$username', firstName='$firstName', lastName='$lastName', email='$email', password='$password', phone='$phone', userStatus=$userStatus)"
    }
}
