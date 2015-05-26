/*
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
 */

package edu.self.startux.lastLog;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DumpInfoExecutor implements CommandExecutor {
        private LastLogPlugin plugin;

        public DumpInfoExecutor(LastLogPlugin plugin) {
                this.plugin = plugin;
        }
      
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        	boolean lastlog_flag = false;
        	
        	if (args.length > 0) {
        		lastlog_flag = true;
        	}

        	Iterator<PlayerList.Entry> pli;
        	pli = plugin.getPlayerList(lastlog_flag).iterator();
        	
        	while (pli.hasNext()) {
        		PlayerList.Entry entry = pli.next();
        		
        		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		String ss = s.format(entry.time);
        		String message = "[" + entry.uuid + "]: '" + entry.name + "' " + LastLogColors.DATE + ss;
            	sender.sendMessage(message); 
        	}
        	
        	
        	return true;
        }
}
