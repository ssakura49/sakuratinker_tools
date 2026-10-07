package com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.modifier;

import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import com.ssakura49.sakuratinker.core.tools.modifiers.base.BaseModifier;
import com.ssakura49.sakuratinker.core.utils.EntityUtil;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.library.tool.stat.EBToolStats;
import com.ssakura49.sakuratinker_tools.compat.extrabotany.entity.PhantomSwordEntity;
import com.ssakura49.sakuratinker_tools.content.entity.part.CelestialBladePart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

import static com.ssakura49.sakuratinker_tools.compat.extrabotany.tool.item.FirstFractal.*;

public class FirstFractalModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        HitResult hitResult = player.pick(80.0, 1.0F, true);
        Entity target = hitResult.getType() == HitResult.Type.ENTITY ?
                ((EntityHitResult)hitResult).getEntity() : null;
        trySpawnPhantomSword(tool, player,target);
        ToolDamageUtil.damageAnimated(tool,1, player);
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {

        trySpawnPhantomSword(tool,player,null);
        ToolDamageUtil.damageAnimated(tool,1, player);
    }

    private void trySpawnPhantomSword(IToolStackView tool, Player player, @Nullable Entity explicitTarget) {
        if (!player.level().isClientSide() && player.getAttackStrengthScale(0) >= 0.9) {
            float phantom_amount = tool.getStats().getInt(EBToolStats.PHANTOM_AMOUNT);
            float range = tool.getStats().getInt(STToolStats.RANGE);
            float moveSpeed = tool.getStats().get(STToolStats.MOVE_SPEED);
            Entity targetEntity = explicitTarget;
            if (targetEntity == null) {
                targetEntity = EntityUtil.getEntityLookedAt(player, 80.0);
            }
            BlockPos targetPos;
            if (targetEntity != null) {
                targetPos = BlockPos.containing(targetEntity.position()).offset(0, 2, 0);
            } else {
                HitResult hitResult = player.pick(80.0, 1.0F, true);
                targetPos = BlockPos.containing(hitResult.getLocation()).offset(0, 2, 0);
            }

            double j = -Math.PI + 2 * Math.PI * Math.random();
            double k;
            double x, y, z;
            ItemStack itemStack = player.getMainHandItem();
            ToolStack toolStack = ToolStack.from(itemStack);
            List<CelestialBladePart> parts = getZenithParts(itemStack);
            CelestialBladePart selectedPart = parts.get(player.getRandom().nextInt(parts.size()));
            ItemStack displayStack = createRandomizedTinkerStack(selectedPart.item().value());
            for (int i = 0; i < phantom_amount; i++) {
                PhantomSwordEntity sword = new PhantomSwordEntity(player.level(), player, targetPos);
                sword.setZenithPart(selectedPart);
                sword.setSpeed(moveSpeed);
                if (CAN_TRIGGER_MODIFIER) {
                    sword.setTool(displayStack);
                } else {
                    sword.setTool(toolStack.createStack());
                }
                sword.setItemStack(displayStack);
                sword.setOwner(player);
                sword.setDelay(5 + 5 * i);

                k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
                x = targetPos.getX() + range * Math.sin(k) * Math.cos(j);
                y = targetPos.getY() + range * Math.cos(k);
                z = targetPos.getZ() + range * Math.sin(k) * Math.sin(j);

                j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;
                sword.setPos(x, y + 1.5, z);
                sword.faceTarget();
                player.level().addFreshEntity(sword);
            }
            PhantomSwordEntity sword2 = new PhantomSwordEntity(player.level(), player, targetPos, itemStack);
            sword2.setZenithPart(selectedPart);
            sword2.setSpeed(moveSpeed);
            if (CAN_TRIGGER_MODIFIER) {
                sword2.setTool(displayStack);
            } else {
                sword2.setTool(toolStack.createStack());
            }
            sword2.setOwner(player);
            sword2.setItemStack(displayStack);
            k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
            x = targetPos.getX() + range * Math.sin(k) * Math.cos(j);
            y = targetPos.getY() + range * Math.cos(k);
            z = targetPos.getZ() + range * Math.sin(k) * Math.sin(j);
            sword2.setPos(x, y + 1.5, z);
            sword2.faceTarget();
            sword2.setVariety(9);
            player.level().addFreshEntity(sword2);
        }
    }
}