package com.ssakura49.sakuratinker_tools.compat.extrabotany.library.tool.stat;

import com.ssakura49.sakuratinker.SakuraTinker;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;

public class EBToolStats {
    private static final List<IToolStat<?>> STATS = new ArrayList<>();
    private static FloatToolStat registerFloatStat(FloatToolStat stat){
        STATS.add(stat);
        return stat;
    }
    public static final FloatToolStat PHANTOM_AMOUNT = registerFloatStat(new FloatToolStat(name("phantom_amount"), -3135232, 1.0F, 1.0F, 32.0F));

    private static ToolStatId name(String name) {
        return new ToolStatId(SakuraTinker.getResource(name));
    }
    public static void initStats(){
        STATS.forEach(ToolStats::register);
    }
}
