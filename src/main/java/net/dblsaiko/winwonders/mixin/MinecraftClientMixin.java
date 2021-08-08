package net.dblsaiko.winwonders.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.WindowProvider;

import net.dblsaiko.winwonders.Config;
import net.dblsaiko.winwonders.WindowExt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(
        method = "<init>(Lnet/minecraft/client/RunArgs;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/WindowProvider;createWindow(Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/util/Window;")
    )
    private Window wwCreateWindow(WindowProvider windowProvider, WindowSettings windowSettings, String string, String string2) {
        Config config = Config.getInstance();

        // Only override if overrideWidth/overrideHeight is not set, those should still have precedence
        if (config.restoreDimensions && config.winHeight > 0 && config.winWidth > 0 && !(this.options.overrideHeight > 0 && this.options.overrideWidth > 0)) {
            windowSettings = new WindowSettings(
                config.winWidth,
                config.winHeight,
                windowSettings.fullscreenWidth,
                windowSettings.fullscreenHeight,
                windowSettings.fullscreen
            );
        }

        return windowProvider.createWindow(windowSettings, string, string2);
    }

    @Shadow @Final private Window window;

    @Shadow @Final public GameOptions options;

    @Inject(method = "close()V", at = @At("HEAD"))
    private void wwClose(CallbackInfo ci) {
        Config config = Config.getInstance();
        WindowExt wext = WindowExt.from(this.window);

        if (this.window.isFullscreen()) {
            if (config.savePosition) {
                config.winX = wext.getWindowedX();
                config.winY = wext.getWindowedY();
            }

            if (config.saveDimensions) {
                config.winWidth = wext.getWindowedWidth();
                config.winHeight = wext.getWindowedHeight();
                config.maximized = wext.isMaximized();
            }
        } else {
            // The game only sets the windowed* variables when entering
            // fullscreen mode, so these are the accurate values when not in
            // fullscreen mode.
            if (config.savePosition) {
                config.winX = this.window.getX();
                config.winY = this.window.getY();
            }

            if (config.saveDimensions) {
                config.winWidth = this.window.getWidth();
                config.winHeight = this.window.getHeight();
                config.maximized = wext.isMaximized();
            }
        }

        config.write(Config.DEFAULT_PATH);
    }
}
