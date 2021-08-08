package net.dblsaiko.winwonders;

import net.minecraft.client.util.Window;

public interface WindowExt {

    int getWindowedX();

    int getWindowedY();

    int getWindowedWidth();

    int getWindowedHeight();

    boolean isMaximized();

    @SuppressWarnings("ConstantConditions")
    static WindowExt from(Window window) {
        return (WindowExt) (Object) window;
    }

}
