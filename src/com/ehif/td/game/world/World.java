package com.ehif.td.game.world;

import com.ehif.td.Sketch;
import com.ehif.td.game.world.path.PathField;
import com.ehif.td.game.world.placeable.Placeable;
import com.ehif.td.game.world.placeable.tower.ArcherTower;
import ui.mouse.MouseEvent;
import ui.mouse.MouseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class World {
    private ArrayList<PathField> path;
    private int width, height;
    private ArrayList<Placeable> placeables;

    public World(int width, int height, int widthCell, int heightCell) {

        generatePath(width / widthCell, height / heightCell, widthCell, heightCell);
        this.width = width;
        this.height = height;
        placeables = new ArrayList<Placeable>();

        World w = this;
        Sketch.mouseListeners.add(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                placeables.add(new ArcherTower(w, e.getMouseX(), e.getMouseY()));
                System.out.println("lol");
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
            });
    }

    private void generatePath(int width, int height, int widthCell, int heightCell) {
        width = width / 2;
        height = height / 2;


        Random rand = new Random();
        path = new ArrayList<>();
        boolean[][] field = new boolean[width][height];

        int[] current = new int[]{0, height / 2};
        int[] target = new int[]{width - 1, height / 2};
        field[current[0]][current[1]] = true;

        path.add(new PathField(current[0] * widthCell * 2, current[1] * heightCell * 2, widthCell, heightCell, current[0], current[1]));
        while (!(current[0] == target[0] && current[1] == target[1]) && !(current[0] == target[0] - 1 && current[1] == target[1])) {
            ArrayList<int[]> candidates = new ArrayList<>();
            for (int x = -1; x <= 1; x += 2) {
                if (current[0] + x >= 0 && current[0] + x < width && !field[current[0] + x][current[1]]) {
                    candidates.add(new int[]{current[0] + x, current[1]});
                }
            }
            for (int y = -1; y <= 1; y += 2) {
                if (current[1] + y >= 0 && current[1] + y < height && !field[current[0]][current[1] + y]) {
                    candidates.add(new int[]{current[0], current[1] + y});
                }
            }
            if (candidates.size() > 0) {
                int[] next = candidates.get(rand.nextInt(candidates.size()));

                path.add(new PathField((next[0] + current[0]) * widthCell, (next[1] + current[1]) * heightCell, widthCell, heightCell, next[0], next[1]));
                path.add(new PathField(next[0] * widthCell * 2, next[1] * heightCell * 2, widthCell, heightCell, next[0], next[1]));

                current = next;

                field[current[0]][current[1]] = true;
            } else  {
                //path.remove(path.size() - 1);

                path.remove(path.size() - 1);
                path.remove(path.size() - 1);


                current = new int[]{path.get(path.size() - 1).getxInWorld(), path.get(path.size() - 1).getyInWorld()};
            }
        }
        while (path.get(path.size() - 1).getxInWorld() <= width + 1) {
            path.add(new PathField(path.get(path.size() - 1).getX() + widthCell, path.get(path.size() - 1).getY(), widthCell, heightCell, path.get(path.size() - 1).getxInWorld() + 1, path.get(path.size() - 1).getY()));
        }
    }



    public void display(Sketch s, int x, int y) {
        s.noStroke();
        s.fill(0, 255, 0);
        s.rect(x, y, width, height);
        for (int i = 0; i < path.size(); i++) {
            path.get(i).display(s, x, y);
        }
        for( int i = 0; i < placeables.size(); i++) {
            placeables.get(i).display(s);
        }
    }
}
