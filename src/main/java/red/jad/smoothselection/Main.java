package red.jad.smoothselection;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import red.jad.smoothselection.config.AutoConfigIntegration;
import red.jad.smoothselection.config.DefaultConfig;

public class Main implements ClientModInitializer {

    public static final String MOD_ID = "smoothselection";
    public static DefaultConfig config;

    @Override
    public void onInitializeClient() {
        if(FabricLoader.getInstance().isModLoaded("autoconfig1u")){
            AutoConfig.register(AutoConfigIntegration.class, GsonConfigSerializer::new);
            config = AutoConfig.getConfigHolder(AutoConfigIntegration.class).getConfig();
        }else{
            config = new DefaultConfig();
        }
    }

    private static float now = 0;
    private static float selectedX = 0;
    private static float speed = 0;
    private static float acceleration = 0;

    @Environment(EnvType.CLIENT)
    public static float getHorizontalOffset(PlayerEntity player){

        int target = player.inventory.selectedSlot*20;

        if(Main.config.isEnabled()){
            if(!MinecraftClient.getInstance().isPaused()){
                // correct speed regardless of framerate
                float before = now;
                now = (float)player.world.getTime() + MinecraftClient.getInstance().getTickDelta();
                float delta = now - before;

                // acceleration
                if(speed < Main.config.getMaxSpeed()){
                    acceleration += delta;
                    speed += (Main.config.getSpeed()*acceleration)*delta;
                }

                speed = Math.min(Math.max(speed, 0), Main.config.getMaxSpeed());

                // movement
                float inc = speed * delta;
                if(selectedX+inc < target) {
                    selectedX += (speed*delta);
                }else if(selectedX-inc > target){
                    selectedX -= (speed*delta);
                }else{
                    // Restarting values
                    selectedX = target;
                    speed = 0;
                    acceleration = 1;
                }

                selectedX = Math.min(Math.max(selectedX, 0), PlayerInventory.getHotbarSize()*20);

                // TODO: REMOVE DEBUG!!!!
                // TODO: REMOVE DEBUG!!!!
                // TODO: REMOVE DEBUG!!!!
                player.sendMessage(new LiteralText("x: "+selectedX), false);
                player.sendMessage(new LiteralText("target: "+target), false);
                player.sendMessage(new LiteralText("speed: "+speed), false);
                player.sendMessage(new LiteralText("accel: "+acceleration), false);
                // TODO: REMOVE DEBUG!!!!
                // TODO: REMOVE DEBUG!!!!
                // TODO: REMOVE DEBUG!!!!
            }
            return selectedX;
        }

        return target;
    }
}
