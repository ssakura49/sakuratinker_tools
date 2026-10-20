package com.ssakura49.sakuratinker_tools.compat.goety.library.tool.stat;

import com.ssakura49.sakuratinker.SakuraTinker;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

public class GoetyToolStats {
    private static ToolStatId name(String name) {
        return new ToolStatId(SakuraTinker.getResource(name));
    }
    private static final List<IToolStat<?>> STATS = new ArrayList<>();
    private static FloatToolStat registerFloatStat(FloatToolStat stat){
        STATS.add(stat);
        return stat;
    }
    public static final FloatToolStat SOUL_POWER = registerFloatStat(new FloatToolStat(name("soul_power"), -8143881, 0.0F, 0.0F, (float)Integer.MAX_VALUE));
    public static final FloatToolStat SOUL_INCREASE = registerFloatStat(new FloatToolStat(name("soul_increase"), -8143881, 1.0F, 0.0F, (float)Integer.MAX_VALUE));

    public static void initStats(){
        STATS.forEach(ToolStats::register);
    }
}
