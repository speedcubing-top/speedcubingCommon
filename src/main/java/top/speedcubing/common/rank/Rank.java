package top.speedcubing.common.rank;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import top.speedcubing.common.database.Database;
import top.speedcubing.common.database.DatabaseData;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SystemUtils;

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

    /*
      database priority, player id
    */

    public static String getRank(String dbRank, int id) {
        //default, champ
        if (dbRank.equals("default") && DatabaseData.champs.contains(id)) {
            return "champ";
        }

        return calculatePeriodRank(dbRank, id);
    }

    private static String calculatePeriodRank(String dbRank, int id) {
        try (SQLConnection connection = Database.getCubing()) {
            String[] data = connection.select("priority,at,duration").from("periodrank").where("id=" + id).orderBy("at DESC").limit(0, 1).getStringArray();

            if (data.length == 0) {
                return dbRank;
            }

            //unit. second
            if ((SystemUtils.getCurrentSecond() - Integer.parseInt(data[1])) < Integer.parseInt(data[2])) {
                return data[0];
            }

            return dbRank;
        }
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
