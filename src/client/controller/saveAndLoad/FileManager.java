package client.controller.saveAndLoad;

import client.controller.updater.Controller;
import client.model.bulletModel.NonRigidBulletModel;
import client.model.bulletModel.RigidBulletModel;
import client.model.charactersModel.*;
import client.model.collectibleModel.Collectible;
import client.model.movement.Direction;
import client.model.panelModel.Isometric;
import client.model.panelModel.PanelModel;
import client.model.panelModel.Rigid;
import client.model.panelModel.WallSideIndicator;
import client.view.container.GlassFrame;

import java.awt.geom.Point2D;
import java.io.*;

import static client.controller.constant.GameValues.*;

public class FileManager {
    public static boolean canLoad(boolean checkPoint) {
        String path = "C:\\Users\\benya\\Documents\\GitHub\\WindowKillProj\\save";
        String toAdd = (checkPoint ? "CheckPoint.txt" : ".txt");

        File valueFile = new File(path + "/values" + toAdd);
        File epsilonFile = new File(path + "/Epsilon" + toAdd);
        File rigidFile = new File(path + "/Rigid" + toAdd);
        File nonRigidFile = new File(path + "/NonRigid" + toAdd);
        File archmireFile = new File(path + "/Archmire" + toAdd);
        File barricadosFile = new File(path + "/Barricados" + toAdd);
        File blackOrbFile = new File(path + "/BlackOrb" + toAdd);
        File necropickFile = new File(path + "/Necropick" + toAdd);
        File omenoctFile = new File(path + "/Omenoct" + toAdd);
        File squarePath = new File(path + "/Square" + toAdd);
        File trianglePath = new File(path + "/Triangle" + toAdd);
        File wyrmFile = new File(path + "/Wyrm" + toAdd);
        File collectible = new File(path + "/Collectible" + toAdd);

        return valueFile.exists() && epsilonFile.exists() && rigidFile.exists() && nonRigidFile.exists() && archmireFile.exists() &&
                barricadosFile.exists() && blackOrbFile.exists() && necropickFile.exists() && omenoctFile.exists() && squarePath.exists() &&
                trianglePath.exists() && wyrmFile.exists() && collectible.exists();
    }
    public static void saveGame(boolean checkPoint) {
        String path = "C:\\Users\\benya\\Documents\\GitHub\\WindowKillProj\\save";
        String toAdd = (checkPoint ? "CheckPoint.txt" : ".txt");

        saveValues(path + "/values" + toAdd);
        saveEpsilonAndPanel(path + "/Epsilon" + toAdd);
        saveRigidBullet(path + "/Rigid" + toAdd);
        saveNonRigidBullet(path + "/NonRigid" + toAdd);
        saveArchmire(path + "/Archmire" + toAdd);
        saveBarricados(path + "/Barricados" + toAdd);
        saveBlackOrb(path + "/BlackOrb" + toAdd);
        saveNecropick(path + "/Necropick" + toAdd);
        saveOmenoct(path + "/Omenoct" + toAdd);
        saveSquare(path + "/Square" + toAdd);
        saveTriangle(path + "/Triangle" + toAdd);
        saveWyrm(path + "/Wyrm" + toAdd);
        saveCollectible(path + "/Collectible" + toAdd);
    }
    private static void saveValues(String path) {
        File valueFile = new File(path);
        try (FileWriter writer = new FileWriter(valueFile, false)) {
            writer.write(waveNumber + "#" + bulletFired + "#" + successfulBullet + "#" + enemyKilled + "#" +
                    firstRoundFinish + "#" + temporaryEnemyKilledNumber + "#" + waveStartTime + "#" + waveLengthTime + "#"
                    + secondRoundFinish + "#" + bossFightStart + "#" + GlassFrame.getINSTANCE().getTimer().getSeconds()
                    + "#" + GlassFrame.getINSTANCE().getTimer().getMiliSecond() + "#" + totalProgressTime + "#" + lastAttackUpdate
                    + "#" + lastSaveTime + "#" + canSpawn + "#" + lastCheckPointMade);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveEpsilonAndPanel(String path) {
        File epsilonFile = new File(path);
        EpsilonModel epsilonModel = EpsilonModel.epsilonModelsList.get(0);
        PanelModel panel = PanelModel.panelModelList.get(0);

        try (FileWriter writer = new FileWriter(epsilonFile, false)) {
            writer.write(epsilonModel.getCenter().getX() + "#" + epsilonModel.getCenter().getY() + "#" + epsilonModel.getXp()
                        + "#" + epsilonModel.getHp() + "#" + epsilonModel.getVerticesNumber() + "#" + epsilonModel.getId() + "\n");
            writer.write(panel.getX() + "#" + panel.getY() + "#" + panel.getWidth() + "#" + panel.getHeight());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveRigidBullet(String path) {
        File rigidFile = new File(path);

        try (FileWriter writer = new FileWriter(rigidFile, false)) {
            for (RigidBulletModel ptr : RigidBulletModel.rigidBulletModelList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getDirection().getDirectionVector().getX()
                + "#" + ptr.getDirection().getDirectionVector().getY() + "#" + ptr.getReduceHp() + "#" + ptr.getShooterEntity()
                + "#" + ptr.getSpeed() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveNonRigidBullet(String path) {
        File nonRigidFile = new File(path);

        try (FileWriter writer = new FileWriter(nonRigidFile, false)) {
            for (NonRigidBulletModel ptr : NonRigidBulletModel.nonRigidBulletModelsList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getDirection().getDirectionVector().getX()
                        + "#" + ptr.getDirection().getDirectionVector().getY() + "#" + ptr.getReduceHp() + "#" + ptr.getShooterEnemy()
                        + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveArchmire(String path) {
        File archmireFile = new File(path);

        try (FileWriter writer = new FileWriter(archmireFile, false)) {
            for (ArchmireEnemy ptr : ArchmireEnemy.archmireEnemiesList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "#" + ptr.getSpeed() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveBarricados(String path) {
        File barricados = new File(path);

        try (FileWriter writer = new FileWriter(barricados, false)) {
            for (BarricadosEnemy ptr : BarricadosEnemy.barricadosEnemiesList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "#" + ptr.isPanelRigid()
                + "#" + ptr.getTimeMade() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveBlackOrb(String path) {
        File blackOrbFile = new File(path);

        try (FileWriter writer = new FileWriter(blackOrbFile, false)) {
            for (BlackOrbMiniBoss ptr : BlackOrbMiniBoss.blackOrbMiniBossesList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveNecropick(String path) {
        File necropickFile = new File(path);

        try (FileWriter writer = new FileWriter(necropickFile, false)) {
            for (NecropickEnemy ptr : NecropickEnemy.necropickEnemiesList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "#" + ptr.getSpeed() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveOmenoct(String path) {
        File omenoctFile = new File(path);

        try (FileWriter writer = new FileWriter(omenoctFile, false)) {
            for (OmenoctEnemy ptr : OmenoctEnemy.omenoctEnemyList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "#" + ptr.getSpeed() +
                        "#" + ptr.getWallSideIndicator() + "#" + ptr.isImpact() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveSquare(String path) {
        File squareFile = new File(path);

        try (FileWriter writer = new FileWriter(squareFile, false)) {
            for (SquareEnemy ptr : SquareEnemy.squareEnemyList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "#" + ptr.getSpeed() + "#" + ptr.isImpact() + "#" + ptr.isDash() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveTriangle(String path) {
        File triangleFile = new File(path);

        try (FileWriter writer = new FileWriter(triangleFile, false)) {
            for (TriangleEnemy ptr : TriangleEnemy.triangleEnemyList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() + "#" + ptr.getSpeed() + "#" + ptr.isImpact() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveWyrm(String path) {
        File wyrmFile = new File(path);

        try (FileWriter writer = new FileWriter(wyrmFile, false)) {
            for (WyrmEnemy ptr : WyrmEnemy.wyrmEnemiesList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getSize() +
                        "#" + ptr.getSpeed() + "#" + ptr.isClockWise() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveCollectible(String path) {
        File collectibleFile = new File(path);

        try (FileWriter writer = new FileWriter(collectibleFile, false)) {
            for (Collectible ptr : Collectible.collectibleList) {
                writer.write(ptr.getCenter().getX() + "#" + ptr.getCenter().getY() + "#" + ptr.getAddedXp() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void loadGame(boolean checkPoint) {
        String path = "C:\\Users\\benya\\Documents\\GitHub\\WindowKillProj\\save";
        String toAdd = (checkPoint ? "CheckPoint.txt" : ".txt");

        loadValues(path + "/values" + toAdd);
        loadEpsilonAndPanel(path + "/Epsilon" + toAdd, checkPoint);
        loadRigid(path + "/Rigid" + toAdd);
        loadNonRigid(path + "/NonRigid" + toAdd);
        loadArchmire(path + "/Archmire" + toAdd);
        loadBarricados(path + "/Barricados" + toAdd);
        loadBlackOrb(path + "/BlackOrb" + toAdd);
        loadNecropick(path + "/Necropick" + toAdd);
        loadOmenoct(path + "/Omenoct" + toAdd);
        loadSquare(path + "/Square" + toAdd);
        loadTriangle(path + "/Triangle" + toAdd);
        loadWyrm(path + "/Wyrm" + toAdd);
        loadCollectible(path + "/Collectible" + toAdd);

        if (checkPoint) {
            File valueFile = new File(path + "/values" + toAdd);
            valueFile.delete();

            File epsilonFile = new File(path + "/Epsilon" + toAdd);
            epsilonFile.delete();

            File rigidFile = new File(path + "/Rigid" + toAdd);
            rigidFile.delete();

            File nonRigidFile = new File(path + "/NonRigid" + toAdd);
            nonRigidFile.delete();

            File archmireFile = new File(path + "/Archmire" + toAdd);
            archmireFile.delete();

            File barricadosFile = new File(path + "/Barricados" + toAdd);
            barricadosFile.delete();

            File blackOrbFile = new File(path + "/BlackOrb" + toAdd);
            blackOrbFile.delete();

            File necropickFile = new File(path + "/Necropick" + toAdd);
            necropickFile.delete();

            File omenoctFile = new File(path + "/Omenoct" + toAdd);
            omenoctFile.delete();

            File squarePath = new File(path + "/Square" + toAdd);
            squarePath.delete();

            File trianglePath = new File(path + "/Triangle" + toAdd);
            trianglePath.delete();

            File wyrmFile = new File(path + "/Wyrm" + toAdd);
            wyrmFile.delete();

            File collectible = new File(path + "/Collectible" + toAdd);
            collectible.delete();
        }
    }
    private static void loadValues(String path) {
        File valueFile = new File(path);
        try {
            FileReader fileReader = new FileReader(valueFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line =bufferedReader.readLine();
            String[] strings = line.split("#");

            waveNumber = Integer.parseInt(strings[0]);
            bulletFired = Integer.parseInt(strings[1]);
            successfulBullet = Integer.parseInt(strings[2]);
            enemyKilled = Integer.parseInt(strings[3]);
            firstRoundFinish = Boolean.parseBoolean(strings[4]);
            temporaryEnemyKilledNumber = Integer.parseInt(strings[5]);
            waveStartTime = Integer.parseInt(strings[6]);
            waveLengthTime = Integer.parseInt(strings[7]);
            secondRoundFinish = Boolean.parseBoolean(strings[8]);
            bossFightStart = Boolean.parseBoolean(strings[9]);
            GlassFrame.getINSTANCE().getTimer().setSeconds(Integer.parseInt(strings[10]));
            GlassFrame.getINSTANCE().getTimer().setMiliSeconds(Integer.parseInt(strings[11]));
            totalProgressTime = Integer.parseInt(strings[12]);
            lastAttackUpdate = Integer.parseInt(strings[13]);
            lastSaveTime = Integer.parseInt(strings[14]);
            canSpawn = Boolean.parseBoolean(strings[15]);
            lastCheckPointMade = Integer.parseInt(strings[16]);

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadEpsilonAndPanel(String path, boolean checkPoint) {
        File epsilonFile = new File(path);
        try {
            FileReader fileReader = new FileReader(epsilonFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            String[] strings = line.split("#");

            Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
            EpsilonModel epsilonModel = new EpsilonModel(center, strings[5]);
            epsilonModel.setXp(Integer.parseInt(strings[2]));
            if (!checkPoint) epsilonModel.setHp(Integer.parseInt(strings[3]));
            else {
                epsilonModel.setHp(10);
            }
            epsilonModel.setVerticesNumber(Integer.parseInt(strings[4]));

            if (Controller.getINSTANCE().logic != null) Controller.getINSTANCE().logic.setEpsilon(epsilonModel);

            line = bufferedReader.readLine();
            String[] strings1 = line.split("#");

            new PanelModel(Double.parseDouble(strings1[0]), Double.parseDouble(strings1[1])
                    , Double.parseDouble(strings1[2]), Double.parseDouble(strings1[3]), Isometric.NO, Rigid.NO);

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadRigid(String path) {
        File rigidFile = new File(path);
        try {
            FileReader fileReader = new FileReader(rigidFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                Direction direction = new Direction(new Point2D.Double(Double.parseDouble(strings[2]), Double.parseDouble(strings[3])));
                new RigidBulletModel(center, direction, Integer.parseInt(strings[4]), strings[5], Double.parseDouble(strings[6]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadNonRigid(String path) {
        File nonRigidFile = new File(path);
        try {
            FileReader fileReader = new FileReader(nonRigidFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                Direction direction = new Direction(new Point2D.Double(Double.parseDouble(strings[2]), Double.parseDouble(strings[3])));
                new NonRigidBulletModel(center, direction, Integer.parseInt(strings[4]), strings[5]);
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadArchmire(String path) {
        File archmireFile = new File(path);
        try {
            FileReader fileReader = new FileReader(archmireFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                new ArchmireEnemy(center, Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadBarricados(String path) {
        File barricadosFile = new File(path);

        try {
            FileReader fileReader = new FileReader(barricadosFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                new BarricadosEnemy(center, Double.parseDouble(strings[2]), Boolean.parseBoolean(strings[3]), Integer.parseInt(strings[4]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadBlackOrb(String path) {
        File blackOrbFile = new File(path);

        try {
            FileReader fileReader = new FileReader(blackOrbFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                new BlackOrbMiniBoss(center, Double.parseDouble(strings[2]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadNecropick(String path) {
        File necropickFile = new File(path);
        try {
            FileReader fileReader = new FileReader(necropickFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                new NecropickEnemy(center, Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadOmenoct(String path) {
        File omenoctFile = new File(path);
        try {
            FileReader fileReader = new FileReader(omenoctFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                WallSideIndicator wallSide = null;
                if (strings[4].equals("LEFT")) wallSide = WallSideIndicator.LEFT;
                if (strings[4].equals("RIGHT")) wallSide = WallSideIndicator.RIGHT;
                if (strings[4].equals("UP")) wallSide = WallSideIndicator.UP;
                if (strings[4].equals("DOWN")) wallSide = WallSideIndicator.DOWN;
                OmenoctEnemy tmp = new OmenoctEnemy(center, Double.parseDouble(strings[2]), Double.parseDouble(strings[3]), wallSide);
                tmp.setImpact(Boolean.parseBoolean(strings[5]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadSquare(String path) {
        File squarePath = new File(path);

        try {
            FileReader fileReader = new FileReader(squarePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                SquareEnemy tmp = new SquareEnemy(center, Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                tmp.setImpact(Boolean.parseBoolean(strings[4]));
                tmp.setDash(Boolean.parseBoolean(strings[5]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadTriangle(String path) {
        File trianglePath = new File(path);

        try {
            FileReader fileReader = new FileReader(trianglePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                TriangleEnemy tmp = new TriangleEnemy(center, Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                tmp.setImpact(Boolean.parseBoolean(strings[4]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadWyrm(String path) {
        File wyrmFile = new File(path);

        try {
            FileReader fileReader = new FileReader(wyrmFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                new WyrmEnemy(center, Double.parseDouble(strings[2]), Double.parseDouble(strings[3]), Boolean.parseBoolean(strings[4]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadCollectible(String path) {
        File collectible = new File(path);

        try {
            FileReader fileReader = new FileReader(collectible);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split("#");
                Point2D center = new Point2D.Double(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
                new Collectible(center, Integer.parseInt(strings[2]));
            }

            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
