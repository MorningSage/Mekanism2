package mekanism.common.content.blocktype;

import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.util.shape.VoxelShape;

import static mekanism.common.util.VoxelShapeUtils.setShape;
import static net.minecraft.block.Block.createCuboidShape;

public final class BlockShapes {

    private BlockShapes() {
    }

    public static final VoxelShape[] DIGITAL_MINER = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        setShape(VoxelShapeUtils.combine(
            createCuboidShape(16, 18, -13, 30, 28, -11), // monitor1
            createCuboidShape(17, 19, -13.01, 29, 27, -13.01), // monitor1_on
            createCuboidShape(17, 20, -11, 29, 26, -10), // monitor_back1
            createCuboidShape(17, 17.5, -12.005, 18, 18.5, -11.005), // led1
            createCuboidShape(1, 18, -13, 15, 28, -11), // monitor2
            createCuboidShape(2, 19, -13.01, 14, 27, -13.01), // monitor2_on
            createCuboidShape(2, 20, -11, 14, 26, -10), // monitor_back2
            createCuboidShape(2, 17.5, -12.005, 3, 18.5, -11.005), // led2
            createCuboidShape(-14, 18, -13, 0, 28, -11), // monitor3
            createCuboidShape(-13, 19, -13.01, -1, 27, -13.01), // monitor3_on
            createCuboidShape(-13, 20, -11, -1, 26, -10), // monitor_back3
            createCuboidShape(-13, 17.5, -12.005, -12, 18.5, -11.005), // led3
            createCuboidShape(-2, 22, -11.95, 2, 24, -10.95), // monitorBar1
            createCuboidShape(14, 22, -11.95, 18, 24, -10.95), // monitorBar2
            createCuboidShape(4, 22, -11, 6, 24, -9), // monitorMount1
            createCuboidShape(10, 22, -11, 12, 24, -9), // monitorMount2
            createCuboidShape(5, 11, -14, 6, 13, -9), // keyboard_support1
            createCuboidShape(10, 11, -14, 11, 13, -9), // keyboard_support2
            createCuboidShape(4, 11.5, -13.5, 12, 12.5, -10.5), // keyboardBottom
            createCuboidShape(3, 12.5, -14.5, 13, 13.5, -9.5), // keyboard
            createCuboidShape(-8, 3, -9, 24, 32, 3), // frame1
            createCuboidShape(-8, 26.99, 4, 24, 31.99, 19), // frame2a
            createCuboidShape(-8, 21, 4, 24, 26, 19), // frame2b
            createCuboidShape(-8, 15, 4, 24, 20, 19), // frame2c
            createCuboidShape(-8, 9, 4, 24, 14, 19), // frame2d
            createCuboidShape(-8, 3, 4, 24, 8, 19), // frame2e
            createCuboidShape(-8, 3, 19.99, 24, 32, 31.99), // frame3
            createCuboidShape(-7, 15, 3, 23, 31, 20), // core_top
            createCuboidShape(-7, 4, 3, 23, 15, 20), // core_bottom
            createCuboidShape(-9, 24, -6, -8, 29, 0), // bracket_east1
            createCuboidShape(-13, 24, -8, -8, 29, -6), // bracket_north1
            createCuboidShape(-13, 24, 0, -8, 29, 2), // bracket_south1
            createCuboidShape(-9, 24, 23, -8, 29, 29), // bracket_east2
            createCuboidShape(-13, 24, 21, -8, 29, 23), // bracket_north2
            createCuboidShape(-13, 24, 29, -8, 29, 31), // bracket_south2
            createCuboidShape(24, 24, -6, 25, 29, 0), // bracket_west1
            createCuboidShape(24, 24, 0, 29, 29, 2), // bracket_south3
            createCuboidShape(24, 24, -8, 29, 29, -6), // bracket_north3
            createCuboidShape(24, 24, 23, 25, 29, 29), // bracket_west2
            createCuboidShape(24, 24, 29, 29, 29, 31), // bracket_south4
            createCuboidShape(24, 24, 21, 29, 29, 23), // bracket_north4
            createCuboidShape(5, 2, -6, 11, 4, 5), // power_cable1a
            createCuboidShape(5, 1, 5, 11, 3, 11), // power_cable1b
            createCuboidShape(4, 0, 4, 12, 1, 12), // port1
            createCuboidShape(23, 5, 5, 31, 11, 11), // power_cable2
            createCuboidShape(30.99, 4, 4, 31.99, 12, 12), // port2a
            createCuboidShape(24, 4, 4, 25, 12, 12), // port2b
            createCuboidShape(-15, 5, 5, -7, 11, 11), // power_cable3
            createCuboidShape(-15.99, 4, 4, -14.99, 12, 12), // port3a
            createCuboidShape(-9, 4, 4, -8, 12, 12), // port3b
            createCuboidShape(-14, 2, -7, -10, 30, 1), // beam1
            createCuboidShape(-15, 0, -8, -8, 2, 2), // foot1
            createCuboidShape(-14, 2, 22, -10, 30, 30), // beam2
            createCuboidShape(-15, 0, 21, -8, 2, 31), // foot2
            createCuboidShape(26.5, 2, 22, 30.5, 30, 30), // beam3
            createCuboidShape(24.5, 0, 21, 31.5, 2, 31), // foot3
            createCuboidShape(26.5, 2, -7, 30.5, 30, 1), // beam4
            createCuboidShape(24.5, 0, -8, 31.5, 2, 2), // foot4
            createCuboidShape(4, 20, 30.993, 12, 28, 31.993), // port_back
            createCuboidShape(4, 30.993, 4, 12, 31.993, 12) // port_top
        ), DIGITAL_MINER);
    }

}
