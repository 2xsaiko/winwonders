package net.dblsaiko.winwonders.mixin;

import net.minecraft.client.util.Window;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.dblsaiko.winwonders.Config;
import net.dblsaiko.winwonders.WindowExt;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMaximizeCallback;

@Mixin(Window.class)
public abstract class WindowMixin implements WindowExt {

    @Shadow private int windowedX;

    @Shadow private int windowedY;

    @Shadow private int windowedWidth;

    @Shadow private int windowedHeight;

    private boolean maximized;

    @Shadow
    public abstract boolean isFullscreen();

    @Shadow private boolean currentFullscreen;

    @Shadow private boolean fullscreen;

    @Redirect(
        method = "<init>(Lnet/minecraft/client/WindowEventHandler;Lnet/minecraft/client/util/MonitorTracker;Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)V",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwMakeContextCurrent(J)V")
    )
    private void wwInitWindow(long window) {
        Config config = Config.getInstance();

        if (config.restorePosition && config.winX >= 0 && config.winY >= 0) {
            this.windowedX = config.winX;
            this.windowedY = config.winY;
        }

        glfwMakeContextCurrent(window);
        glfwSetWindowMaximizeCallback(window, this::wwOnWindowMaximizedChanged);

        if (config.maximized) {
            glfwMaximizeWindow(window);
            this.maximized = true;
        }
    }

    @Override
    public int getWindowedX() {
        return this.windowedX;
    }

    @Override
    public int getWindowedY() {
        return this.windowedY;
    }

    @Override
    public int getWindowedWidth() {
        return this.windowedWidth;
    }

    @Override
    public int getWindowedHeight() {
        return this.windowedHeight;
    }

    @Override
    public boolean isMaximized() {
        return this.maximized;
    }

    private void wwOnWindowMaximizedChanged(long window, boolean maximized) {
        this.maximized = maximized;
    }

}
