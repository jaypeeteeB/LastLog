/*
 * Copyright 2012 Thomas Loy.
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

package edu.self.lastLog;

import java.lang.Iterable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;

public class PlayerList implements Iterable<PlayerList.Entry> {
        final private static int PAGE_LENGTH = 10; // how many lines per page?

        public class Entry {
                public String name;
                public long time;
                public Entry(String name, long time) {
                        this.name = name;
                        this.time = time;
                }
        }

        public class PlayerListIterator implements Iterator<Entry> {
                private int i = -1;
                PlayerList list;

                public PlayerListIterator(PlayerList list) {
                        this.list = list;
                }
                
                @Override
                public boolean hasNext() {
                        if (i + 1 >= list.getLength()) return false;
                        return true;
                }

                @Override
                public Entry next() {
                        return list.getEntry(++i);
                }

                @Override
                public void remove() {
                        throw new UnsupportedOperationException();
                }
        }

        private Entry[] playerList;
        private int length;
        static private Comparator<Entry> comparator;

        static {
                comparator = new Comparator<Entry>() {
                        public int compare(Entry a, Entry b) {
                                if (a.time > b.time) return -1;
                                if (a.time < b.time) return 1;
                                return 0;
                        }
                };
        }

        public PlayerList(OfflinePlayer[] playerList, boolean lastlog) {
                int tmplen = 512;
                length = playerList.length;
                while (tmplen < length) tmplen *= 2;
                this.playerList = new Entry[tmplen];
                int i = 0;
                for (OfflinePlayer player : playerList) {
                        long time = (lastlog ? player.getLastPlayed() : player.getFirstPlayed());
                        this.playerList[i++] = new Entry(player.getName(), time);
                }
        }

        public void sort() {
                Arrays.sort(playerList, 0, length, comparator);
        }

        public Iterator<Entry> iterator() {
                return new PlayerListIterator(this);
        }

        public int getLength() {
                return length;
        }

        public Entry getEntry(int index) {
                if (index >= length) {
                        throw new IndexOutOfBoundsException("PlayerList.playerList");
                }
                return playerList[index];
        }

        public Entry getEntry(String name) {
                for (int i = 0; i < length; ++i) {
                        Entry entry = playerList[i];
                        if (entry.name.equalsIgnoreCase(name)) return entry;
                }
                return null;
        }

        Entry addEntry(String name) {
                length += 1;
                if (length > playerList.length) {
                        playerList = Arrays.copyOf(playerList, playerList.length * 2);
                }
                Entry entry = new Entry(name, 0);
                playerList[length - 1] = entry;
                return entry;
        }

        public void set(String name, long time) {
                Entry entry = getEntry(name);
                if (entry == null) {
                        entry = addEntry(name);
                }
                entry.time = time;
        }

        public int getPageCount() {
                return ((getLength() - 1) / PAGE_LENGTH) + 1;
        }
        
        public void displayPage(int pageNumber, boolean lastlog, CommandSender sender) {
                if (pageNumber >= getPageCount()) {
                        sender.sendMessage(LastLogColors.ERROR
                                           + "[LastLog] Page "
                                           + (pageNumber + 1)
                                           + " selected, but only "
                                           + getPageCount()
                                           + " available!");
                        return;
                }
                sender.sendMessage(LastLogColors.HEADER
                                   + (lastlog ? "Last login" : "First login")
                                   + " - " + getLength() + " Players - Page ["
                                   + (pageNumber + 1)
                                   + "/" + getPageCount() + "]");
                for (int i = 0; i < PAGE_LENGTH; ++i) {
                        int index = pageNumber * PAGE_LENGTH + i;
                        if (index >= getLength() || index < 0) break;
                        String name = getEntry(index).name;
                        long date = getEntry(index).time;
                        String nameColor = Bukkit.getServer().getOfflinePlayer(name).isOnline() ? LastLogColors.ONLINE : LastLogColors.RESET;
                        sender.sendMessage(LastLogColors.DATE
                                           + new LastLogDate(date)
                                           + " " + nameColor + name);
                }
        }
}
