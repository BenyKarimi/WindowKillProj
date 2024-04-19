import controller.Controller;
import controller.Updater;
import view.container.GamePanel;
import view.container.GlassFrame;

public class WindowKill implements Runnable{
    @Override
    public void run() {
        GamePanel gamePanel = GamePanel.getINSTANCE();
        Controller controller = Controller.getINSTANCE();
    }

    public static void main(String[] args) {
        new WindowKill().run();
    }
}
