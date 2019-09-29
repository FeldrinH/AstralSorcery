/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.constellation.*;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ConstellationItemRecipe
 * Created by HellFirePvP
 * Date: 28.09.2019 / 06:48
 */
public class ConstellationItemRecipe extends SimpleAltarRecipe {

    private static final String KEY_CONSTELLATION_ATTUNE = "attuned_constellation";
    private static final String KEY_CONSTELLATION_TRAIT = "trait_constellation";

    private IWeakConstellation attunedConstellation = null;
    private IMinorConstellation traitConstellation = null;

    public ConstellationItemRecipe(ResourceLocation recipeId, AltarType altarType, int duration, int starlightRequirement, AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, recipeGrid);
    }

    public static ConstellationItemRecipe convertToThis(SimpleAltarRecipe other) {
        return new ConstellationItemRecipe(other.getId(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getInputs());
    }

    @Override
    public void deserializeAdditionalJson(JsonObject recipeObject) throws JsonSyntaxException {
        super.deserializeAdditionalJson(recipeObject);

        if (JSONUtils.hasField(recipeObject, KEY_CONSTELLATION_ATTUNE)) {
            ResourceLocation cstName = new ResourceLocation(JSONUtils.getString(recipeObject, KEY_CONSTELLATION_ATTUNE));
            IConstellation cst = RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstName);
            if (cst instanceof IWeakConstellation) {
                this.attunedConstellation = (IWeakConstellation) cst;
            }
        }
        if (JSONUtils.hasField(recipeObject, KEY_CONSTELLATION_TRAIT)) {
            ResourceLocation cstName = new ResourceLocation(JSONUtils.getString(recipeObject, KEY_CONSTELLATION_TRAIT));
            IConstellation cst = RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstName);
            if (cst instanceof IMinorConstellation) {
                this.traitConstellation = (IMinorConstellation) cst;
            }
        }
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack getOutputForRender(TileAltar altar) {
        ItemStack out = super.getOutputForRender(altar);
        setConstellations(out);
        return out;
    }

    @Override
    @Nonnull
    public List<ItemStack> getOutputs(TileAltar altar) {
        List<ItemStack> out = super.getOutputs(altar);
        out.forEach(this::setConstellations);
        return out;
    }

    private void setConstellations(ItemStack out) {
        if (out.getItem() instanceof ConstellationItem) {
            if (this.attunedConstellation != null) {
                ((ConstellationItem) out.getItem()).setAttunedConstellation(out, this.attunedConstellation);
            }
            if (this.traitConstellation != null) {
                ((ConstellationItem) out.getItem()).setTraitConstellation(out, this.traitConstellation);
            }
        }
    }

    @Override
    public void writeRecipeSync(PacketBuffer buf) {
        super.writeRecipeSync(buf);

        ByteBufUtils.writeOptional(buf, this.attunedConstellation, ByteBufUtils::writeRegistryEntry);
        ByteBufUtils.writeOptional(buf, this.traitConstellation, ByteBufUtils::writeRegistryEntry);
    }

    @Override
    public void readRecipeSync(PacketBuffer buf) {
        super.readRecipeSync(buf);

        this.attunedConstellation = ByteBufUtils.readOptional(buf, ByteBufUtils::readRegistryEntry);
        this.traitConstellation = ByteBufUtils.readOptional(buf, ByteBufUtils::readRegistryEntry);
    }
}