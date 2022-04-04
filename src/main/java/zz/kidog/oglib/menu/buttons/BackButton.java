package zz.kidog.oglib.menu.buttons;

import java.util.List;
import zz.kidog.oglib.menu.Button;
import zz.kidog.oglib.menu.Menu;
import zz.kidog.oglib.ogLib;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BackButton extends Button {

    private final Menu menu;

    public BackButton(Menu menu){
        this.menu = menu;
    }


    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Go Back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BED;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        ogLib.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(ogLib.getInstance(), () -> {
            if(menu == null){
                player.closeInventory();
            }else{
                menu.openMenu(player);
            }
        }, 1L);

    }
}
