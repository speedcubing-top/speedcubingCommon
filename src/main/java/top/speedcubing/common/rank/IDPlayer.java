package top.speedcubing.common.rank;

import java.util.UUID;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLConnection;

public abstract class IDPlayer {
    public String realName;
    public UUID uuid;
    public Integer id;

    public IDPlayer(String realName, UUID uuid, Integer id) {
        this.realName = realName;
        this.uuid = uuid;
        this.id = id;
    }

    public void dbUpdate(String field) {
        Database.getCubing().update("playersdata", field, "id=" + id);
    }

    public SQLConnection.SQLPrepare dbSelect(String field) {
        return Database.getCubing().select(field).from("playersdata").where("id=" + id);
    }
}
