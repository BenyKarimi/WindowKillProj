import controller.Controller;
import controller.Updater;
import view.container.GamePanel;
import view.container.GlassFrame;

public class WindowKill implements Runnable{
    @Override
    public void run() {
        GamePanel gamePanel = new GamePanel();
        Controller controller = new Controller();
    }

    public static void main(String[] args) {
        new WindowKill().run();
    }
}
