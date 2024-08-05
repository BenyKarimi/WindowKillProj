package client.view.container;

import java.awt.event.MouseAdapter;

public class CustomMouseAdaptor extends MouseAdapter {
    private final int pointer;

    public CustomMouseAdaptor(int pointer) {
        this.pointer = pointer;
    }

    public int getPointer() {
        return pointer;
    }
}
