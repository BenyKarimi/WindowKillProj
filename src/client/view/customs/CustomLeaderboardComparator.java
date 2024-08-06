package client.view.customs;

import java.util.Comparator;

public class CustomLeaderboardComparator implements Comparator<String> {
    private boolean sortByXp;

    public CustomLeaderboardComparator(boolean sortByXp) {
        this.sortByXp = sortByXp;
    }

    @Override
    public int compare(String o1, String o2) {
        String[] parts1 = o1.split("█");
        String[] parts2 = o2.split("█");

        int xp1 = Integer.parseInt(parts1[1]);
        int xp2 = Integer.parseInt(parts2[1]);

        String time1 = parts1[2];
        String time2 = parts2[2];

        if (sortByXp) {
            return Integer.compare(xp1, xp2);
        }
        else {
            return time1.compareTo(time2);
        }
    }

    public void setSortByXp(boolean sortByXp) {
        this.sortByXp = sortByXp;
    }
}
