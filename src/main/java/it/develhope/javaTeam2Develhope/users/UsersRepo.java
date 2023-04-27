package it.develhope.javaTeam2Develhope.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;

@Repository
public interface UsersRepo extends JpaRepository<Users,Long> {

}
