package net.dblsaiko.winwonders.mixin;

import net.minecraft.client.util.Window;

import net.dblsaiko.winwonders.Config;
import net.dblsaiko.winwonders.WindowExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

@Mixin(Window.class)
public abstract class WindowMixin implements WindowExt {

    @Shadow private int windowedX;

    @Shadow private int windowedY;

    @Shadow private int windowedWidth;

    @Shadow private int windowedHeight;

    @Shadow
    public abstract boolean isFullscreen();

    @Redirect(
        method = "<init>(Lnet/minecraft/client/WindowEventHandler;Lnet/minecraft/client/util/MonitorTracker;Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)V",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwMakeContextCurrent(J)V")
    )
    private void setWindowPosition(long window) {
        Config config = Config.getInstance();
        if (config.restorePosition && config.winX >= 0 && config.winY >= 0) {
            windowedX = config.winX;
            windowedY = config.winY;
        }
        glfwMakeContextCurrent(window);
    }

    @Override
    public int getWindowedX() {
        return windowedX;
    }

    @Override
    public int getWindowedY() {
        return windowedY;
    }

    @Override
    public int getWindowedWidth() {
        return windowedWidth;
    }

    @Override
    public int getWindowedHeight() {
        return windowedHeight;
    }

}
