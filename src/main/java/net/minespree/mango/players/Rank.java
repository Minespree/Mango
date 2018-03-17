package net.minespree.mango.players;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.minespree.mango.util.StringUtils;

/**
 * @since 09/02/2018
 */
@Getter
public enum Rank {
    MEMBER("Member", (String) null, ChatColor.GRAY),
    IRON("Iron", ChatColor.WHITE),
    GOLD("Gold", ChatColor.GOLD),
    DIAMOND("Diamond", ChatColor.AQUA),
    VIP("VIP", ChatColor.GOLD, ChatColor.WHITE),
    YOTUBE("Youtuber", "YT", ChatColor.RED, ChatColor.WHITE),
    HELPER("Helper", ChatColor.LIGHT_PURPLE),
    MODERATOR("Moderator", "Mod", ChatColor.RED),
    ADMIN("Administrator", "Admin", ChatColor.DARK_RED);

    private final String name;
    private final String tag;
    private final ChatColor color;
    private final ChatColor nameColor;
    private final char scoreboardChar;

    Rank(String name, String tag, ChatColor color, ChatColor nameColor) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(color);

        this.name = name;
        this.tag = tag;
        this.color = color;
        this.nameColor = nameColor;

        // Get color char
        this.scoreboardChar = color.toString().charAt(1);
    }

    Rank(String name, ChatColor color, ChatColor nameColor) {
        this(name, name, color, nameColor);
    }

    Rank(String name, String tag, ChatColor color) {
        this(name, tag, color, color);
    }

    Rank(String name, ChatColor color) {
        this(name, name, color, color);
    }

    public String getTag() {
        return name;
    }

    public String getColoredTag() {
        String tag = getTag();

        if (StringUtils.isBlank(tag)) {
            return nameColor.toString();
        }

        return getColor() + ChatColor.BOLD.toString() + tag + " " + nameColor;
    }

    public boolean hasTag() {
        return StringUtils.isBlank(getTag());
    }

    public boolean has(Rank rank) {
        return compareTo(rank) >= 0;
    }
}
