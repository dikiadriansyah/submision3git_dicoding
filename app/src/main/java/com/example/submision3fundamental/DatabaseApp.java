package com.example.submision3fundamental;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.submision3fundamental.db.UserDAO;
import com.example.submision3fundamental.models.User;
//    sama di folder models

@Database(entities = {User.class}, version = 1)
public abstract class DatabaseApp extends RoomDatabase{
    public abstract UserDAO userDAO();
//    sama di userDAO
}
