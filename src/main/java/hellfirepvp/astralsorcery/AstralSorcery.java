/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery;

import hellfirepvp.astralsorcery.client.ClientProxy;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.auxiliary.tick.TickManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: AstralSorcery
 * Created by HellFirePvP
 * Date: 19.04.2019 / 18:14
 */
@Mod(AstralSorcery.MODID)
public class AstralSorcery {

    public static final String MODID = "astralsorcery";
    public static final String NAME = "Astral Sorcery";
    public static final String VERSION = "1.11.0";

    public static Logger log = LogManager.getLogger(NAME);

    private static AstralSorcery instance;
    private static ModContainer modContainer;

    private final CommonProxy proxy;

    public AstralSorcery() {
        instance = this;
        modContainer = ModList.get().getModContainerById(MODID).get();

        this.proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        this.proxy.initialize();
        this.proxy.attachLifecycle(FMLJavaModLoadingContext.get().getModEventBus());
        this.proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
        this.proxy.attachTickListeners(TickManager.INSTANCE::register);

        FluidRegistry.enableUniversalBucket();
    }

    public static AstralSorcery getInstance() {
        return instance;
    }

    public static ModContainer getModContainer() {
        return modContainer;
    }

    public static CommonProxy getProxy() {
        return getInstance().proxy;
    }

}
