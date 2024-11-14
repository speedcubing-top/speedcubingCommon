package top.speedcubing.common.rank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLResult;
import top.speedcubing.lib.utils.SQL.SQLRow;
import top.speedcubing.lib.utils.collection.Sets;

public class RankLoader {


    //utils

    public static void loadRanks() {
        Rank.rankByName.clear();

        List<Rank> rankByOrder = new ArrayList<>();

        //ranks
        SQLResult result = Database.getConfig().select("*").from("mc_ranks").executeResult();
        for (SQLRow r : result) {
            String name = r.getString("name");
            int weight = r.getInt("weight");
            String prefix = r.getString("prefix");
            String chatColor = r.getString("chatcolor");
            long discord = r.getLong("discord");
            Set<String> perms = Sets.hashSet(r.getString("perms").split("\\|"));

            Rank rank = new Rank(name, weight, prefix, chatColor, discord, perms);
            Rank.rankByName.put(name, rank);
            rankByOrder.add(rank);
        }

        //perms
        PermissionSet.reload();


        rankByOrder.sort((o1, o2) -> Integer.compare(o2.getWeight(), o1.getWeight()));

        for (int i = 0; i < rankByOrder.size(); i++) {
            rankByOrder.get(i).orderCode = i;
        }
    }
}
