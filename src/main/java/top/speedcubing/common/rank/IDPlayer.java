package top.speedcubing.common.rank;

import java.util.UUID;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLRow;

public abstract class IDPlayer {
    public String realName;
    public UUID uuid;
    public Integer id;

    public IDPlayer(String realName, UUID uuid, Integer id) {
        this.realName = realName;
        this.uuid = uuid;
        this.id = id;
    }

    public SQLRow dbSelect(String field) {
        try (SQLConnection connection = Database.getCubing()) {
            return dbSelect(connection, field);
        }
    }

    public SQLRow dbSelect(SQLConnection connection, String field) {
        return connection.select(field).from("playersdata").where("id=" + id).executeResult().get(0);

    }

    public void dbUpdate(String field) {
        try (SQLConnection connection = Database.getCubing()) {
            dbUpdate(connection, field);
        }
    }

    public void dbUpdate(SQLConnection connection, String field) {
        connection.update("playersdata", field, "id=" + id);
    }
}
