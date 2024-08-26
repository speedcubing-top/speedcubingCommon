package top.speedcubing.common.rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.database.DatabaseData;
import top.speedcubing.lib.utils.SQL.SQLBuilder;
import top.speedcubing.lib.utils.collection.Sets;

public class Rank {

    public static Map<String, Rank> rankByName = new HashMap<>();

    private final String rank;
    private final int weight;
    private final RankFormat format;
    private final long discord;
    private final Set<String> perms;
    public int orderCode;

    public Rank(String rank, int weight, String prefix, String chatColor, long discord, Set<String> perms) {
        this.rank = rank;
        this.weight = weight;
        this.format = new RankFormat(prefix, chatColor);
        this.discord = discord;
        this.perms = perms;
    }

    public String getRank() {
        return rank;
    }


    public int getWeight() {
        return weight;
    }

    public RankFormat getFormat() {
        return format;
    }

    public long getDiscord() {
        return discord;
    }

    public Set<String> getPerms() {
        return perms;
    }

    //utils

    public static String getRank(String rank, int id) {
        return rank.equals("default") && DatabaseData.champs.contains(id) ? "champ" : rank;
    }

    public static String getRankByDiscordID(long discordID) {
        return Database.systemConnection.select("priority").from("speedcubing", "playersdata").where("uuid=(" + new SQLBuilder().select("verifieduuid").from("speedcubingsystem", "memberdata").where("id=" + discordID).toSQL() + ")").getString();
    }

    public static int getCode(String rank) {
        return 10 + rankByName.get(rank).orderCode;
    }

    public static RankFormat getFormat(String rank, int id) {
        return rankByName.get(getRank(rank, id)).getFormat();
    }

    public static boolean isStaff(String realRank) {
        return realRank.equals("builder") || realRank.equals("helper") || realRank.equals("admin") || realRank.equals("owner") || realRank.equals("mod") || realRank.equals("developer");
    }
}
