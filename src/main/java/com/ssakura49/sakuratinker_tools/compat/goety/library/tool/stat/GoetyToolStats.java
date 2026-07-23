package com.ssakura49.sakuratinker_tools.compat.goety.library.tool.stat;

import com.ssakura49.sakuratinker.SakuraTinker;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class GoetyToolStats {
    private static ToolStatId name(String name) {
        return new ToolStatId(SakuraTinker.getResource(name));
    }

    public static final FloatToolStat SOUL_POWER = (FloatToolStat) ToolStats.register(new FloatToolStat(name("soul_power"), -8143881, 0.0F, 0.0F, (float)Integer.MAX_VALUE));
    public static final FloatToolStat SOUL_INCREASE = (FloatToolStat)ToolStats.register(new FloatToolStat(name("soul_increase"), -8143881, 1.0F, 0.0F, (float)Integer.MAX_VALUE));

}
