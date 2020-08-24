package red.jad.smoothselection.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import red.jad.smoothselection.Main;

@Config(name = Main.MOD_ID)
@Config.Gui.Background("minecraft:textures/block/blue_ice.png")
public class AutoConfigIntegration extends DefaultConfig implements ConfigData {
    public boolean enabled = true;
    public float speed = 5.0f;
    public float max_speed = 50.0f;
    public float vertical_offset = 0.0f;

    @Override public boolean isEnabled() { return enabled; }
    @Override public float getSpeed() { return speed; }
    @Override public float getMaxSpeed() { return max_speed; }
    @Override public float getVerticalOffset() { return vertical_offset; }
}
