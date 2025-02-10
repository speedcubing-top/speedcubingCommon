package top.speedcubing.common.player;

import java.util.UUID;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.rank.Rank;
import top.speedcubing.common.rank.RankFormat;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLRow;

public class JoinedPlayer extends MinecraftPlayer {
    public final int id;
    public final String ip;
    public String realRank;
    public String displayRank;
    public final int lang;
    public String timeZone;
    public final boolean isStaff;

    public enum SearchType {
        ALL, //displayname realname match
        REAL, //realname match
        FAKE //displayname match
    }

    // offline
    public JoinedPlayer(UUID uuid, SQLRow playersdata) {
        this(uuid, playersdata.getString("name"), playersdata);
    }

    // offline
    public JoinedPlayer(UUID uuid, String realName, SQLRow playersdata) {
        this(uuid, realName, playersdata.getInt("id"), playersdata.getString("ip"), playersdata.getString("priority"), playersdata.getString("nickpriority"), playersdata);
    }

    // online
    public JoinedPlayer(UUID uuid, String realName, int id, String ip, String realRank, String displayRank, SQLRow playersdata) {
        super(uuid, realName);
        this.id = id;
        this.ip = ip;
        this.realRank = realRank;
        this.displayRank = displayRank;
        this.lang = playersdata.getInt("lang");
        this.timeZone = playersdata.getString("timezone");
        this.isStaff = Rank.isStaff(realRank);
    }

    public RankFormat getFormat() {
        return Rank.getFormat(realRank, id);
    }

    public SQLRow dbSelect(String field) {
        try (SQLConnection connection = Database.getCubing()) {
            return this.dbSelect(connection, field);
        }
    }

    public SQLRow dbSelect(SQLConnection connection, String field) {
        return connection.select(field).from("playersdata").where("id=" + this.id).executeResult().get(0);
    }

    public void dbUpdate(String field) {
        try (SQLConnection connection = Database.getCubing()) {
            this.dbUpdate(connection, field);
        }

    }

    public void dbUpdate(SQLConnection connection, String field) {
        connection.update("playersdata", field, "id=" + this.id);
    }


    public String getColorName() {
        return getFormat().getNameColor() + realName;
    }

    public String getPrefixName() {
        return getFormat().getPrefix() + realName;
    }
}
