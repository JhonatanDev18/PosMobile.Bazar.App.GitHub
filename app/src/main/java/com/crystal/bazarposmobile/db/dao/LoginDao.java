package com.crystal.bazarposmobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.crystal.bazarposmobile.db.entity.LoginEntity;

@Dao
public interface LoginDao {
    @Insert
    void insert(LoginEntity login);

    @Query("SELECT * FROM BPM_LOGIN WHERE usuario = :usuario")
    LoginEntity getUser(String usuario);

    @Query("SELECT * FROM BPM_LOGIN WHERE usuario = :usuario and contrasena = :contrasena")
    LoginEntity getLogin(String usuario, String contrasena);

    @Update
    void update(LoginEntity login);
}
