package me.webhead1104.towncraft.database;

import io.ebean.DB;
import me.webhead1104.towncraft.exceptions.UnknownUserException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    public String getUser(UUID userUUID) throws UnknownUserException {
        DatabaseUser user = DB.find(DatabaseUser.class, userUUID);
        if (user == null) {
            throw new UnknownUserException(userUUID);
        }

        return user.getData();
    }

    public boolean userExists(UUID userUUID) {
        return DB.createQuery(DatabaseUser.class).setId(userUUID).exists();
    }

    public List<UUID> listUsers() {
        List<DatabaseUser> users = DB.find(DatabaseUser.class).findList();
        return new ArrayList<>(users.stream().map(DatabaseUser::getId).toList());
    }

    public void saveUser(UUID userUUID, String userData) {
        DB.save(new DatabaseUser(userUUID, userData));
    }

    public void deleteUser(UUID userUUID) throws UnknownUserException {
        DB.delete(DatabaseUser.class, userUUID);
    }
}
