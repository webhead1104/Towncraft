package me.webhead1104.towncraft.database;

import io.ebean.Model;
import io.ebean.annotation.DbJson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "towncraft_users")
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseUser extends Model {
    @Id
    @Column(name = "id")
    private UUID id;
    @DbJson
    @Column(name = "data")
    private String data;
}
