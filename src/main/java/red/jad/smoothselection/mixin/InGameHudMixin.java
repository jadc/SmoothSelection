package red.jad.smoothselection.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.smoothselection.Main;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 1
            )
    )
    private void preInject(float f, MatrixStack matrixStack, CallbackInfo ci){
        matrixStack.push();
        matrixStack.translate(Main.getHorizontalOffset(getCameraPlayer()), Main.config.getVerticalOffset(), 0);
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void postInject(float f, MatrixStack matrixStack, CallbackInfo ci){
        matrixStack.pop();
    }

    @ModifyArg(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
                    ordinal = 1
            ),
            index = 1
    )
    private int relyOnMatrices(int x){
        return x - (this.getCameraPlayer().inventory.selectedSlot * 20);
    }
}
