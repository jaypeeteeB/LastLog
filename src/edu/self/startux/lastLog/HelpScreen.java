/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright 2012 StarTux.
 * Copyright 2015 Jaypeetee.
 * Fixed for Bukkit 1.8.4
 *
 * This file is part of LastLog.
 *
 * LastLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LastLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LastLog.  If not, see <http://www.gnu.org/licenses/>.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package edu.self.startux.lastLog;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class HelpScreen {
        // private LastLogPlugin plugin;
        private String[] lines;

        public HelpScreen(LastLogPlugin plugin) {
                // this.plugin = plugin;
                File help = new File("help.yml");
                // plugin.getResource("help.yml")
                if (!help.exists()) {
                	System.out.println("LastLog: help.yml not found");
                	return;
                }
                ConfigurationSection section = YamlConfiguration.loadConfiguration(help);
                String root =  section.getName();
                if (root == null) {
                	System.out.println("LastLog: root == null");
                } else {
                	System.out.println("LastLog: root = " + root);
                }
                
                String message = section.getString("helpmessage");
                if (message == null) {
                	System.out.println("LastLog: Failed to load helpmessage");
                	return;
                }
                Pattern pattern = Pattern.compile("`([0-9a-f])");
                Matcher matcher = pattern.matcher(message);
                StringBuffer buf = new StringBuffer();
                while (matcher.find()) {
                        matcher.appendReplacement(buf, ChatColor.getByChar(matcher.group(1).charAt(0)).toString());
                }
                matcher.appendTail(buf);
                lines = buf.toString().split("\n");
        }

        public void send(CommandSender sender) {
                sender.sendMessage(LastLogColors.HEADER + "[LastLog] Help");
                for (String line : lines) {
                        sender.sendMessage(line);
                }
        }
}