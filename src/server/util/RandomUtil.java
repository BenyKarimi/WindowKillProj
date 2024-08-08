package server.util;

import server.models.Squad;

import java.util.ArrayList;
import java.util.Random;

public class RandomUtil {
    private static Random random = new Random();

    public static ArrayList<Pair<Integer, Integer>> randomSquadInitiation(ArrayList<Squad> squads) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < squads.size(); i++) indices.add(i);

        ArrayList<Pair<Integer, Integer>> out = new ArrayList<>();

        while (indices.size() > 1) {
            int pointer = random.nextInt(0, indices.size());
            int f = indices.get(pointer);
            indices.remove(pointer);
            pointer = random.nextInt(0, indices.size());
            int s = indices.get(pointer);
            indices.remove(pointer);
            out.add(new Pair<>(f, s));
        }
        return out;
    }

    public static int findRandomWinner() {
        return random.nextInt(0, 2);
    }
}
