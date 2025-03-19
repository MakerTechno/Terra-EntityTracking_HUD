package nowebsite.makertechno.terra_ethud.config;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import nowebsite.makertechno.terra_ethud.TerraETHUD;
import nowebsite.makertechno.terra_ethud.algorithm.ProjectAlgorithmLib;
import nowebsite.makertechno.terra_ethud.define.MappingStyle;
import nowebsite.makertechno.terra_ethud.define.TCursor;
import nowebsite.makertechno.terra_ethud.tool4c.ConfigProcessor;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = TerraETHUD.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class TConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.BooleanValue AVAILABLE = BUILDER
        .comment("Enable tracking pointer")
        .define("Available", true);
    private static final ModConfigSpec.EnumValue<MappingStyle> MAPPING_STYLE = BUILDER
        .comment("The style of the pointer")
        .defineEnum("mappingStyle", MappingStyle.NORMAL);
    public static final ModConfigSpec.EnumValue<ProjectAlgorithmLib.Type> PROJECT_ALGORITHM = BUILDER
        .comment("The algorithm of the projection")
        .defineEnum("projectAlgorithm", ProjectAlgorithmLib.Type.AITOFF);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITY_TYPES = BUILDER
        .comment("List of entity types with cursors, separated with \"|\"")
        .defineList(
            "entityTypes|Pointer|EntityIcon",
            List.of(
                "minecraft:ender_dragon|normal_red|ender_dragon_head",
                "minecraft:wither|normal_white|wither_head"
            ),
            () -> "minecraft:villager|normal_green|none",
            ConfigProcessor::isValidEntityBindCursor
        );

    public static final ModConfigSpec SPEC = BUILDER.build();
    public static boolean available;
    public static MappingStyle mappingStyle;
    public static ProjectAlgorithmLib.Type projectAlgorithm;
    public static Set<Pair<EntityType<?>, TCursor>> pointerWithEntities;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        available = AVAILABLE.get();
        mappingStyle = MAPPING_STYLE.get();
        projectAlgorithm = PROJECT_ALGORITHM.get();
        pointerWithEntities =  ConfigProcessor.collectEntityBindCursor(ENTITY_TYPES.get());
    }
}