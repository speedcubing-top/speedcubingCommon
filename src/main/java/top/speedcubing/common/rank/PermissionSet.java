package top.speedcubing.common.rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import top.speedcubing.common.database.Database;
import top.speedcubing.lib.utils.SQL.SQLConnection;
import top.speedcubing.lib.utils.SQL.SQLResult;
import top.speedcubing.lib.utils.SQL.SQLRow;

public class PermissionSet {

    public static Pattern regex = Pattern.compile("^group\\.[^|*.]+$");
    public static HashMap<String, PermissionSet> sets = new HashMap<>();

    private final String name;
    private final Set<String> permissions = new HashSet<>();

    private PermissionSet(String name) {
        this.name = name;
    }


    private PermissionSet(String name, String perms) {
        this.name = name;
        this.permissions.addAll(Arrays.asList(perms.split("\\|")));
    }

    public Set<String> getPerms() {
        return permissions;
    }

    public void addPerms(Set<String> perms) {
        permissions.addAll(perms);
        update();
    }

    public void removePerms(Set<String> perms) {
        permissions.removeAll(perms);
        update();
    }

    public void reset() {
        permissions.clear();
        update();
    }

    public void update() {
        try (SQLConnection connection = Database.getConfig()) {
            connection.update("mc_permsets", "perms=\"" + getPermString() + "\"", "name=\"" + name + "\"");
        }
    }

    private String getPermString() {
        if (permissions.isEmpty()) {
            return "";
        }
        StringBuilder reason = new StringBuilder();
        List<String> perms = new ArrayList<>(permissions);
        Collections.sort(perms);
        for (String string : perms) {
            reason.append("|").append(string);
        }
        return reason.substring(1);
    }

    //utils

    public static void reload() {
        try (SQLConnection connection = Database.getConfig()) {
            SQLResult result = connection.select("name,perms").from("mc_permsets").executeResult();
            for (SQLRow r : result) {
                PermissionSet.sets.put(r.getString("name"), new PermissionSet(r.getString("name"), r.getString("perms")));
            }
        }
    }

    public static PermissionSet get(String name) {
        return sets.get(name);
    }

    public static boolean create(String name) {
        if (sets.containsKey(name) || !regex.matcher(name).matches()) {
            return false;
        }
        sets.put(name, new PermissionSet(name));
        try (SQLConnection connection = Database.getConfig()) {
            connection.insert("mc_permsets", "name", name);
        }
        return true;
    }

    public static boolean delete(String name) {
        if (!sets.containsKey(name) || !regex.matcher(name).matches()) {
            return false;
        }
        sets.remove(name);
        try (SQLConnection connection = Database.getConfig()) {
            connection.delete("mc_permsets", "name=\"" + name + "\"");
        }
        return true;
    }

    public static void findGroups(Set<String> perms) {
        perms.remove("");
        Set<String> toAdd = new HashSet<>();
        Iterator<String> iterator = perms.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (!regex.matcher(s).matches()) {
                continue;
            }
            PermissionSet set = sets.get(s.substring(6));
            if (set != null) {
                iterator.remove();
                toAdd.addAll(set.getPerms());
            }
        }
        perms.addAll(toAdd);
    }
}
