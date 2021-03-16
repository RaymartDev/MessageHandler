package net.messagehandler.listeners.inventory;

import net.messagehandler.MessageHandler;
import net.messagehandler.listeners.inventory.email.Mail;
import net.messagehandler.listeners.inventory.groups.GroupChat;
import net.messagehandler.listeners.inventory.players.Customization;
import net.messagehandler.listeners.inventory.players.Online;
import net.messagehandler.listeners.inventory.players.Preference;
import net.messagehandler.listeners.inventory.players.Staffs;
import net.messagehandler.listeners.inventory.ticket.TicketMenu;
import net.messagehandler.listeners.inventory.words.BannedWords;
import net.messagehandler.utility.User;
import net.messagehandler.utility.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuClick implements Listener {
    private final MessageHandler plugin;
    public static Online online;
    public static Staffs staff;
    public static GroupChat groupChat;
    public static BannedWords bannedWords;
    public MenuClick(MessageHandler plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickMenu(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(Utility.colorize("&6&lMain Menu"))) {
            return;
        }
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(getName(e.getCurrentItem()) == null) return;
        ItemStack itemStack = e.getCurrentItem();
        String name = getName(itemStack);
        Player player = (Player) e.getWhoClicked();
        User user = new User(player);

        switch (name) {
            case "Ticket":
                e.setCancelled(true);
                TicketMenu menu = new TicketMenu(user);
                if(e.getClick() == ClickType.SHIFT_LEFT && user.hasPermission("messagehandler.gui.ticketadmin")) {
                    menu.setup("admin");
                    menu.openInventory();
                    return;
                }
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.ticket")) {
                    break;
                }
                menu.setup("player");
                menu.openInventory();
                break;
            case "eMail":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.email")) {
                    break;
                }
                Mail mail = new Mail(user);
                mail.setup();
                mail.openEmail();
                break;
            case "GroupChat":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.groupchat")) {
                    break;
                }
                groupChat = new GroupChat(user);
                groupChat.setup();
                groupChat.open();
                break;
            case "Online":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.online")) {
                    break;
                }
                online = new Online(user);
                online.setup(1);
                online.open();
                break;
            case "Staffs":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.staffs")) {
                    break;
                }
                staff = new Staffs(user);
                staff.setup(1);
                staff.open();
                break;
            case "Close":
                e.setCancelled(true);
                player.closeInventory();
                break;
            case "Banned Words":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.bannedwords")) {
                    break;
                }
                bannedWords = new BannedWords(user);
                bannedWords.setup(1);
                bannedWords.open();
                break;
            case "MessageHandler":
                e.setCancelled(true);
                user.getPlayer().closeInventory();
                user.sendMessage("&6MessageHandler: &fv" + MessageHandler.getInstance().getDescription().getVersion());
                user.sendMessage("&6Author: &f" + MessageHandler.getInstance().getDescription().getAuthors().get(0));
                user.sendMessage("&6/messagehandler reload: &fto reload configurations");
                break;
            case "Customization":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.customization")) {
                    break;
                }
                Customization customization = new Customization(user);
                customization.setup();
                customization.open();
                break;
            case "Your Preference":
                e.setCancelled(true);
                if(!e.getWhoClicked().hasPermission("messagehandler.gui.chat")) {
                    break;
                }
                Preference preference = new Preference(user);
                preference.setup();
                preference.openInventory();
                break;
            default:
                e.setCancelled(true);
                break;
        }
    }

    public String getName(ItemStack itemStack) {
        return Utility.stripColor(itemStack.getItemMeta().getDisplayName());
    }
}
