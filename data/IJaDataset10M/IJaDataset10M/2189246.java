package drivers;

import static arcadeflex.libc.*;
import static mame.commonH.*;
import static mame.cpuintrf.*;
import static mame.driverH.*;
import static mame.mame.*;
import static mame.osdependH.*;
import static vidhrdw.generic.*;
import static sndhrdw.generic.*;
import static machine.superpac.*;
import static vidhrdw.superpac.*;
import static sndhrdw.mappy.*;

/**
 *
 * @author shadow
 */
public class superpac {

    static MemoryReadAddress readmem_cpu1[] = { new MemoryReadAddress(0x0000, 0x1fff, MRA_RAM), new MemoryReadAddress(0x4040, 0x43ff, superpac_sharedram_r, superpac_sharedram), new MemoryReadAddress(0x4800, 0x480f, superpac_customio_r_1, superpac_customio_1), new MemoryReadAddress(0x4810, 0x481f, superpac_customio_r_2, superpac_customio_2), new MemoryReadAddress(0xa000, 0xffff, MRA_ROM), new MemoryReadAddress(-1) };

    static MemoryWriteAddress writemem_cpu1[] = { new MemoryWriteAddress(0x0000, 0x03ff, videoram_w, videoram, videoram_size), new MemoryWriteAddress(0x0400, 0x07ff, colorram_w, colorram), new MemoryWriteAddress(0x0800, 0x0f7f, MWA_RAM), new MemoryWriteAddress(0x0f80, 0x0fff, MWA_RAM, spriteram, spriteram_size), new MemoryWriteAddress(0x1000, 0x177f, MWA_RAM), new MemoryWriteAddress(0x1780, 0x17ff, MWA_RAM, spriteram_2), new MemoryWriteAddress(0x1800, 0x1f7f, MWA_RAM), new MemoryWriteAddress(0x1f80, 0x1fff, MWA_RAM, spriteram_3), new MemoryWriteAddress(0x2000, 0x2000, MWA_NOP), new MemoryWriteAddress(0x4040, 0x43ff, MWA_RAM), new MemoryWriteAddress(0x4800, 0x480f, superpac_customio_w_1), new MemoryWriteAddress(0x4810, 0x481f, superpac_customio_w_2), new MemoryWriteAddress(0x5002, 0x5003, superpac_interrupt_enable_1_w), new MemoryWriteAddress(0x5008, 0x5009, mappy_sound_enable_w), new MemoryWriteAddress(0x500a, 0x500b, superpac_cpu_enable_w), new MemoryWriteAddress(0x8000, 0x8000, MWA_NOP), new MemoryWriteAddress(0xa000, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static MemoryReadAddress superpac_readmem_cpu2[] = { new MemoryReadAddress(0xf000, 0xffff, MRA_ROM), new MemoryReadAddress(0x0040, 0x03ff, superpac_sharedram_r2), new MemoryReadAddress(-1) };

    static MemoryWriteAddress superpac_writemem_cpu2[] = { new MemoryWriteAddress(0x0040, 0x03ff, superpac_sharedram_w), new MemoryWriteAddress(0x0000, 0x003f, mappy_sound_w, mappy_soundregs), new MemoryWriteAddress(0xf000, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static MemoryReadAddress pacnpal_readmem_cpu2[] = { new MemoryReadAddress(0xf000, 0xffff, MRA_ROM), new MemoryReadAddress(0x0040, 0x03ff, pacnpal_sharedram_r2), new MemoryReadAddress(-1) };

    static MemoryWriteAddress pacnpal_writemem_cpu2[] = { new MemoryWriteAddress(0x0040, 0x03ff, pacnpal_sharedram_w2), new MemoryWriteAddress(0x0000, 0x003f, mappy_sound_w, mappy_soundregs), new MemoryWriteAddress(0x2000, 0x2001, pacnpal_interrupt_enable_2_w), new MemoryWriteAddress(0x2006, 0x2007, mappy_sound_enable_w), new MemoryWriteAddress(0xf000, 0xffff, MWA_ROM), new MemoryWriteAddress(-1) };

    static InputPort superpac_input_ports[] = { new InputPort(0x00, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0x00, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0x00, new int[] { OSD_KEY_UP, OSD_KEY_RIGHT, OSD_KEY_DOWN, OSD_KEY_LEFT, 0, OSD_KEY_CONTROL, 0, 0 }), new InputPort(0x00, new int[] { OSD_KEY_3, 0, 0, 0, OSD_KEY_1, OSD_KEY_2, 0, 0 }), new InputPort(-1) };

    static InputPort pacnpal_input_ports[] = { new InputPort(0x00, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0x98, new int[] { 0, 0, 0, 0, 0, 0, 0, 0 }), new InputPort(0x00, new int[] { OSD_KEY_UP, OSD_KEY_RIGHT, OSD_KEY_DOWN, OSD_KEY_LEFT, OSD_KEY_CONTROL, 0, 0, 0 }), new InputPort(0x00, new int[] { OSD_KEY_3, 0, 0, 0, OSD_KEY_1, OSD_KEY_2, 0, 0 }), new InputPort(-1) };

    static TrakPort trak_ports[] = { new TrakPort(-1) };

    static KEYSet keys[] = { new KEYSet(2, 0, "MOVE UP"), new KEYSet(2, 3, "MOVE LEFT"), new KEYSet(2, 1, "MOVE RIGHT"), new KEYSet(2, 2, "MOVE DOWN"), new KEYSet(2, 5, "FIRE"), new KEYSet(-1) };

    static DSW superpac_dsw[] = { new DSW(0, 0x0F, "DIFFICULTY", new String[] { "R0 NORMAL", "R1 EASIEST", "R2", "R3", "R4", "R5", "R6", "R7", "R8 DEFAULT", "R9", "RA", "RB HARDEST", "RC EASIEST AUTO", "RD", "RE", "RF HARDEST AUTO" }), new DSW(0, 0x40, "DEMO SOUND", new String[] { "ON", "OFF" }, 1), new DSW(1, 0x07, "COINS", new String[] { "1 COIN 1 CREDIT", "1 COIN 2 CREDIT", "1 COIN 3 CREDIT", "1 COIN 6 CREDIT", "1 COIN 7 CREDIT", "2 COIN 1 CREDIT", "2 COIN 3 CREDIT", "3 COIN 1 CREDIT" }), new DSW(1, 0x38, "BONUS AT", new String[] { "30000 100000", "30000 80000", "30000 120000", "30000 80000@@@", "30000 100000@@@", "30000 120000@@@", "80000", "NONE" }), new DSW(1, 0xc0, "LIVES", new String[] { "3", "1", "2", "5" }), new DSW(-1) };

    static DSW pacnpal_dsw[] = { new DSW(0, 0x0c, "RANK", new String[] { "A", "B", "C", "D" }), new DSW(1, 0x07, "COINS", new String[] { "1 COIN 1 CREDIT", "1 COIN 2 CREDIT", "1 COIN 3 CREDIT", "1 COIN 6 CREDIT", "1 COIN 7 CREDIT", "2 COIN 1 CREDIT", "2 COIN 3 CREDIT", "3 COIN 1 CREDIT" }), new DSW(1, 0x38, "BONUS AT", new String[] { "NONE", "20K 70K 70K", "30K 30K 80K", "20K 70K", "30K 70K", "30K 80K", "30K 100K", "30K" }), new DSW(1, 0xc0, "LIVES", new String[] { "1", "2", "3", "5" }), new DSW(-1) };

    static GfxLayout charlayout = new GfxLayout(8, 8, 256, 2, new int[] { 0, 4 }, new int[] { 7 * 8, 6 * 8, 5 * 8, 4 * 8, 3 * 8, 2 * 8, 1 * 8, 0 * 8 }, new int[] { 8 * 8 + 0, 8 * 8 + 1, 8 * 8 + 2, 8 * 8 + 3, 0, 1, 2, 3 }, 16 * 8);

    static GfxLayout spritelayout = new GfxLayout(16, 16, 128, 2, new int[] { 0, 4 }, new int[] { 39 * 8, 38 * 8, 37 * 8, 36 * 8, 35 * 8, 34 * 8, 33 * 8, 32 * 8, 7 * 8, 6 * 8, 5 * 8, 4 * 8, 3 * 8, 2 * 8, 1 * 8, 0 * 8 }, new int[] { 0, 1, 2, 3, 8 * 8, 8 * 8 + 1, 8 * 8 + 2, 8 * 8 + 3, 16 * 8 + 0, 16 * 8 + 1, 16 * 8 + 2, 16 * 8 + 3, 24 * 8 + 0, 24 * 8 + 1, 24 * 8 + 2, 24 * 8 + 3 }, 64 * 8);

    static GfxDecodeInfo gfxdecodeinfo[] = { new GfxDecodeInfo(1, 0x0000, charlayout, 0, 64), new GfxDecodeInfo(1, 0x1000, spritelayout, 64 * 4, 64), new GfxDecodeInfo(-1) };

    static char superpac_color_prom[] = { 0xf6, 0xc9, 0x3f, 0x07, 0xef, 0xf8, 0x2f, 0xaf, 0x3c, 0x5d, 0x38, 0xe7, 0x29, 0x66, 0x54, 0x00, 0x00, 0xd8, 0x66, 0x29, 0xe7, 0x38, 0x5d, 0xd5, 0xaf, 0x2f, 0xb8, 0xef, 0x07, 0x3f, 0xf6, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x02, 0x00, 0x00, 0x03, 0x01, 0x00, 0x00, 0x0b, 0x03, 0x0e, 0x00, 0x0b, 0x01, 0x0e, 0x00, 0x06, 0x01, 0x0b, 0x00, 0x0b, 0x01, 0x08, 0x00, 0x04, 0x01, 0x07, 0x00, 0x04, 0x01, 0x05, 0x00, 0x02, 0x06, 0x04, 0x00, 0x02, 0x06, 0x03, 0x00, 0x01, 0x01, 0x01, 0x00, 0x03, 0x01, 0x0d, 0x00, 0x02, 0x06, 0x0d, 0x00, 0x0d, 0x09, 0x01, 0x00, 0x09, 0x07, 0x0a, 0x00, 0x01, 0x06, 0x09, 0x00, 0x02, 0x09, 0x0a, 0x00, 0x03, 0x0b, 0x0a, 0x00, 0x01, 0x07, 0x03, 0x00, 0x04, 0x0b, 0x0a, 0x00, 0x0a, 0x01, 0x0c, 0x00, 0x01, 0x04, 0x03, 0x00, 0x06, 0x09, 0x01, 0x00, 0x02, 0x06, 0x01, 0x00, 0x0a, 0x01, 0x0c, 0x00, 0x03, 0x02, 0x0e, 0x00, 0x01, 0x0b, 0x03, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0f, 0x0f, 0x0f, 0x0f, 0x02, 0x02, 0x02, 0x0f, 0x00, 0x00, 0x00, 0x0f, 0x00, 0x04, 0x05, 0x0f, 0x03, 0x00, 0x01, 0x0f, 0x04, 0x00, 0x01, 0x0f, 0x05, 0x00, 0x01, 0x0f, 0x06, 0x00, 0x01, 0x0f, 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0f, 0x00, 0x01, 0x0f, 0x07, 0x00, 0x00, 0x0f, 0x0f, 0x0f, 0x0f, 0x0f, 0x08, 0x0f, 0x0f, 0x0f, 0x08, 0x08, 0x0f, 0x0f, 0x08, 0x08, 0x08, 0x0f, 0x03, 0x00, 0x0d, 0x0f, 0x02, 0x06, 0x0d, 0x0f, 0x0d, 0x0e, 0x00, 0x0f, 0x09, 0x07, 0x0a, 0x0f, 0x00, 0x06, 0x09, 0x0f, 0x02, 0x09, 0x0a, 0x0f, 0x03, 0x0b, 0x08, 0x0f, 0x00, 0x07, 0x03, 0x0f, 0x04, 0x0b, 0x0a, 0x0f, 0x0a, 0x0c, 0x00, 0x0f, 0x00, 0x04, 0x03, 0x0f, 0x06, 0x09, 0x00, 0x0f, 0x02, 0x00, 0x06, 0x0f, 0x0a, 0x00, 0x0c, 0x0f, 0x03, 0x02, 0x01, 0x0f, 0x00, 0x0b, 0x03, 0x0f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x00, 0x00, 0x00, 0x0f, 0x00, 0x00, 0x00, 0x0f, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x02, 0x0f, 0x0f, 0x0f, 0x02, 0x02, 0x0f, 0x0f, 0x02, 0x02, 0x02, 0x0f, 0x02, 0x02, 0x0f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x00, 0x04, 0x00, 0x0f, 0x00, 0x05, 0x00, 0x0f, 0x00, 0x06, 0x00, 0x0f, 0x00, 0x08, 0x00 };

    static char pacnpal_color_prom[] = { 0xf6, 0xc0, 0x3f, 0x3c, 0x8f, 0x07, 0xe7, 0xf0, 0x2f, 0x28, 0xc9, 0xed, 0x5d, 0xa4, 0xf6, 0x00, 0x00, 0xf6, 0x5d, 0xc9, 0xe0, 0xf0, 0xba, 0x38, 0x3c, 0x3f, 0x27, 0xb7, 0xef, 0x8f, 0x07, 0x00, 0x00, 0x06, 0x05, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x0e, 0x00, 0x00, 0x07, 0x0e, 0x02, 0x00, 0x01, 0x0e, 0x0d, 0x00, 0x06, 0x0e, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04, 0x05, 0x03, 0x00, 0x04, 0x00, 0x0b, 0x00, 0x09, 0x0c, 0x0b, 0x00, 0x09, 0x00, 0x02, 0x00, 0x03, 0x00, 0x0b, 0x00, 0x0d, 0x00, 0x0d, 0x00, 0x0e, 0x00, 0x09, 0x00, 0x0c, 0x00, 0x00, 0x0e, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x05, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x0d, 0x00, 0x0f, 0x0f, 0x0f, 0x0f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0f, 0x0f, 0x0f, 0x0f, 0x02, 0x0f, 0x0f, 0x0f, 0x03, 0x0e, 0x04, 0x0f, 0x05, 0x00, 0x01, 0x0f, 0x06, 0x00, 0x01, 0x0f, 0x07, 0x00, 0x01, 0x0f, 0x08, 0x00, 0x01, 0x0f, 0x01, 0x07, 0x0e, 0x0f, 0x0d, 0x00, 0x0b, 0x0f, 0x04, 0x00, 0x0d, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0d, 0x00, 0x02, 0x0f, 0x00, 0x00, 0x00, 0x0f, 0x07, 0x07, 0x00, 0x0f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x05, 0x02, 0x0d, 0x0f, 0x05, 0x0b, 0x0e, 0x0f, 0x02, 0x08, 0x0e, 0x0f, 0x0e, 0x05, 0x08, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0e, 0x05, 0x05, 0x0f, 0x05, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x05, 0x0e, 0x0c, 0x0f, 0x05, 0x0e, 0x03, 0x0f, 0x08, 0x03, 0x09, 0x0f, 0x03, 0x0e, 0x09, 0x0f, 0x02, 0x0e, 0x09, 0x0f, 0x08, 0x0c, 0x03, 0x0f, 0x07, 0x0d, 0x0e, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0e, 0x05, 0x0d, 0x0f, 0x0b, 0x05, 0x0c, 0x0f, 0x0b, 0x05, 0x09, 0x0f, 0x0b, 0x08, 0x03, 0x0f, 0x0b, 0x09, 0x03, 0x0f, 0x0b, 0x02, 0x03, 0x0f, 0x0b, 0x08, 0x03, 0x0f, 0x0b, 0x0d, 0x0d, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x07, 0x0b, 0x0e, 0x0f, 0x0e, 0x0d, 0x0b, 0x0f, 0x0e, 0x0b, 0x07, 0x0f, 0x02, 0x0f, 0x0e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0f, 0x0f, 0x0f, 0x0f, 0x0f, 0x0f, 0x00, 0x0f, 0x0f, 0x0f, 0x00, 0x00, 0x0f, 0x00, 0x0f, 0x0f, 0x0f, 0x00, 0x00, 0x0f, 0x0f, 0x0f, 0x0f, 0x00, 0x0f, 0x00, 0x00, 0x00 };

    static char superpac_samples[] = { 0xf8, 0x18, 0x28, 0x38, 0x48, 0x58, 0x58, 0x68, 0x68, 0x68, 0x58, 0x58, 0x48, 0x38, 0x28, 0x18, 0xf8, 0xd8, 0xc8, 0xb8, 0xa8, 0x98, 0x98, 0x88, 0x88, 0x88, 0x98, 0x98, 0xa8, 0xb8, 0xc8, 0xd8, 0x88, 0x98, 0xa8, 0xb8, 0xc8, 0xd8, 0xe8, 0xf8, 0x08, 0x18, 0x28, 0x38, 0x48, 0x58, 0x68, 0x78, 0x78, 0x68, 0x58, 0x48, 0x38, 0x28, 0x18, 0x08, 0xf8, 0xe8, 0xd8, 0xc8, 0xb8, 0xa8, 0x98, 0x88, 0xf8, 0x08, 0xe8, 0x18, 0xd8, 0x28, 0xc8, 0x38, 0xb8, 0x48, 0xa8, 0x58, 0x98, 0x68, 0x88, 0x78, 0x88, 0x78, 0x98, 0x68, 0xa8, 0x58, 0xb8, 0x48, 0xc8, 0x38, 0xd8, 0x28, 0xe8, 0x18, 0xf8, 0x08, 0xf8, 0x28, 0x48, 0x58, 0x68, 0x58, 0x48, 0x28, 0xf8, 0xc8, 0xa8, 0x98, 0x88, 0x98, 0xa8, 0xc8, 0xf8, 0x38, 0x58, 0x68, 0x58, 0x38, 0xf8, 0xb8, 0x98, 0x88, 0x98, 0xb8, 0xf8, 0x68, 0xf8, 0x88, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x88, 0x68, 0x48, 0xf8, 0x28, 0x48, 0xf8, 0xd8, 0x48, 0x68, 0x58, 0x08, 0x28, 0x38, 0xe8, 0xa8, 0xf8, 0x48, 0x08, 0xb8, 0xc8, 0xe8, 0x98, 0x88, 0xb8, 0x18, 0xf8, 0xa8, 0xc8, 0xf8, 0xa8, 0x88, 0xf8, 0xf8, 0x48, 0x68, 0x68, 0x58, 0x38, 0x18, 0x28, 0x38, 0x38, 0x28, 0x18, 0xe8, 0xc8, 0xb8, 0xd8, 0xf8, 0x18, 0x38, 0x28, 0x08, 0xd8, 0xc8, 0xb8, 0xb8, 0xc8, 0xd8, 0xb8, 0x98, 0x88, 0x88, 0xa8, 0x48, 0x68, 0x68, 0x68, 0x68, 0x48, 0x38, 0x18, 0xf8, 0xe8, 0xd8, 0xd8, 0xd8, 0xe8, 0x08, 0x28, 0x38, 0x38, 0x18, 0xe8, 0xc8, 0xa8, 0xa8, 0xa8, 0xb8, 0xd8, 0xc8, 0xb8, 0xa8, 0x98, 0xb8, 0xf8 };

    static char pacnpal_samples[] = { 0xf8, 0x58, 0x68, 0x78, 0x48, 0x58, 0x58, 0x68, 0x68, 0x68, 0x58, 0x58, 0x48, 0x78, 0x68, 0x58, 0xf8, 0xd8, 0xc8, 0xf8, 0xe8, 0xd8, 0xd8, 0xc8, 0xc8, 0xc8, 0xd8, 0xd8, 0xe8, 0xf8, 0xc8, 0xd8, 0x48, 0x68, 0x68, 0x68, 0x68, 0x58, 0x58, 0x68, 0x68, 0x58, 0x68, 0x58, 0x48, 0xe8, 0xe8, 0xf8, 0x48, 0x48, 0xe8, 0xd8, 0xc8, 0xd8, 0xc8, 0xc8, 0xd8, 0xd8, 0xc8, 0xc8, 0xc8, 0xc8, 0xe8, 0xf8, 0x48, 0x68, 0x68, 0x68, 0x68, 0x58, 0x78, 0x48, 0x68, 0x68, 0x48, 0x68, 0x48, 0x68, 0xe8, 0xd8, 0x48, 0x48, 0xe8, 0xc8, 0xe8, 0xd8, 0xc8, 0xc8, 0xf8, 0xd8, 0xc8, 0xc8, 0xc8, 0xc8, 0xe8, 0xd8, 0xf8, 0x68, 0x48, 0x58, 0x68, 0x58, 0x48, 0x68, 0xf8, 0xc8, 0xe8, 0xd8, 0xc8, 0xd8, 0xe8, 0xc8, 0xf8, 0x78, 0x58, 0x68, 0x58, 0x78, 0xf8, 0xf8, 0xd8, 0xc8, 0xd8, 0xf8, 0xf8, 0x68, 0xf8, 0xc8, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0xc8, 0x68, 0x48, 0xf8, 0x68, 0x48, 0xf8, 0xd8, 0x48, 0x68, 0x58, 0x48, 0x68, 0x78, 0xe8, 0xe8, 0xf8, 0x48, 0x48, 0xf8, 0xc8, 0xe8, 0xd8, 0xc8, 0xf8, 0x58, 0xf8, 0xe8, 0xc8, 0xf8, 0xe8, 0xc8, 0xf8, 0x68, 0x68, 0x68, 0x68, 0x68, 0x68, 0x48, 0x58, 0x48, 0x68, 0xf8, 0x48, 0xd8, 0xf8, 0xd8, 0xd8, 0x48, 0x78, 0x68, 0x68, 0x48, 0xc8, 0xe8, 0xd8, 0xd8, 0xc8, 0xd8, 0xf8, 0xd8, 0xc8, 0xd8, 0xf8, 0x48, 0x68, 0x68, 0x68, 0x68, 0x48, 0x78, 0x58, 0xf8, 0xe8, 0xd8, 0xd8, 0xd8, 0xe8, 0x48, 0x68, 0x78, 0x78, 0x58, 0xe8, 0xc8, 0xe8, 0xe8, 0xe8, 0xf8, 0xd8, 0xc8, 0xb8, 0xa8, 0x98, 0xb8, 0xf8 };

    static MachineDriver superpac_machine_driver = new MachineDriver(new MachineCPU[] { new MachineCPU(CPU_M6809, 1100000, 0, readmem_cpu1, writemem_cpu1, null, null, interrupt, 1), new MachineCPU(CPU_M6809, 1100000, 2, superpac_readmem_cpu2, superpac_writemem_cpu2, null, null, ignore_interrupt, 1) }, 60, superpac_init_machine, 28 * 8, 36 * 8, new rectangle(0 * 8, 28 * 8 - 1, 0 * 8, 36 * 8 - 1), gfxdecodeinfo, 32, 4 * (64 + 64), superpac_vh_convert_color_prom, VIDEO_TYPE_RASTER | VIDEO_SUPPORTS_DIRTY, null, generic_vh_start, generic_vh_stop, superpac_vh_screenrefresh, superpac_samples, null, null, null, mappy_sh_update);

    static MachineDriver pacnpal_machine_driver = new MachineDriver(new MachineCPU[] { new MachineCPU(CPU_M6809, 1100000, 0, readmem_cpu1, writemem_cpu1, null, null, interrupt, 1), new MachineCPU(CPU_M6809, 1100000, 2, pacnpal_readmem_cpu2, pacnpal_writemem_cpu2, null, null, pacnpal_interrupt_2, 1) }, 60, superpac_init_machine, 28 * 8, 36 * 8, new rectangle(0 * 8, 28 * 8 - 1, 0 * 8, 36 * 8 - 1), gfxdecodeinfo, 32, 4 * (64 + 64), superpac_vh_convert_color_prom, VIDEO_TYPE_RASTER | VIDEO_SUPPORTS_DIRTY, null, generic_vh_start, generic_vh_stop, superpac_vh_screenrefresh, pacnpal_samples, null, null, null, mappy_sh_update);

    static RomLoadPtr superpac_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x10000);
            ROM_LOAD("sp1.2", 0xc000, 0x2000, 0x4bb33d9c);
            ROM_LOAD("sp1.1", 0xe000, 0x2000, 0x846fbb4a);
            ROM_REGION(0x3000);
            ROM_LOAD("sp1.6", 0x0000, 0x1000, 0x91c5935c);
            ROM_LOAD("spv-2.3f", 0x1000, 0x2000, 0x670a42f2);
            ROM_REGION(0x10000);
            ROM_LOAD("spc-3.1k", 0xf000, 0x1000, 0x04445ddb);
            ROM_END();
        }
    };

    static RomLoadPtr pacnpal_rom = new RomLoadPtr() {

        public void handler() {
            ROM_REGION(0x10000);
            ROM_LOAD("pap13b.cpu", 0xa000, 0x2000, 0xed64a565);
            ROM_LOAD("pap12b.cpu", 0xc000, 0x2000, 0x15308bcf);
            ROM_LOAD("pap11b.cpu", 0xe000, 0x2000, 0x3cac401c);
            ROM_REGION(0x3000);
            ROM_LOAD("pap16.cpu", 0x0000, 0x1000, 0xa36b96cb);
            ROM_LOAD("pap15.vid", 0x1000, 0x2000, 0xfb6f56e3);
            ROM_REGION(0x10000);
            ROM_LOAD("pap14.cpu", 0xf000, 0x1000, 0x330e20de);
            ROM_END();
        }
    };

    static HiscoreLoadPtr superpac_hiload = new HiscoreLoadPtr() {

        public int handler(String name) {
            int writing = 0;
            char[] RAM = Machine.memory_region[0];
            if ((memcmp(RAM, 0x113c, "N@N", 3) == 0) && (RAM[0x1087] == 0) && (RAM[0x1089] == 0) && (RAM[0x1088] != 0) && (memcmp(RAM, 0x3ee, "000", 3) == 0)) {
                FILE localFILE;
                if ((localFILE = fopen(name, "rb")) != null) {
                    fread(RAM, 0x1138, 1, 40, localFILE);
                    fclose(localFILE);
                    RAM[0x1087] = RAM[0x1138];
                    RAM[0x1088] = RAM[0x1139];
                    RAM[0x1089] = RAM[0x113a];
                }
                if (writing == 0) writing = RAM[0x1087] >> 4;
                if (writing != 0) videoram_w.handler(0x3f4, (RAM[0x1087] >> 4) + '0');
                if (writing == 0) writing = RAM[0x1087] & 0x0f;
                if (writing != 0) videoram_w.handler(0x3f3, (RAM[0x1087] & 0x0f) + '0');
                if (writing == 0) writing = RAM[0x1088] >> 4;
                if (writing != 0) videoram_w.handler(0x3f2, (RAM[0x1088] >> 4) + '0');
                if (writing == 0) writing = RAM[0x1088] & 0x0f;
                if (writing != 0) videoram_w.handler(0x3f1, (RAM[0x1088] & 0x0f) + '0');
                if (writing == 0) writing = RAM[0x1089] >> 4;
                if (writing != 0) videoram_w.handler(0x3f0, (RAM[0x1089] >> 4) + '0');
                if (writing == 0) writing = (RAM[0x1089] & 0x0f);
                if (writing != 0) videoram_w.handler(0x3ef, (RAM[0x1089] & 0x0f) + '0');
                videoram_w.handler(0x3ee, 0 + '0');
                return 1;
            }
            return 0;
        }
    };

    static HiscoreSavePtr superpac_hisave = new HiscoreSavePtr() {

        public void handler(String name) {
            char[] RAM = Machine.memory_region[0];
            FILE localFILE;
            if ((localFILE = fopen(name, "wb")) != null) {
                fwrite(RAM, 0x1138, 1, 40, localFILE);
                fclose(localFILE);
            }
        }
    };

    static HiscoreLoadPtr pacnpal_hiload = new HiscoreLoadPtr() {

        public int handler(String name) {
            int writing = 0;
            char[] RAM = Machine.memory_region[0];
            if ((memcmp(RAM, 0x1051, new char[] { 0x1a, 0x25, 0x1a }, 3) == 0) && (RAM[0x116d] == 0) && (RAM[0x116f] == 0) && (RAM[0x116e] != 0) && (memcmp(RAM, 0x3ed, new char[] { 0x00, 0x00, 0x00 }, 3) == 0)) {
                FILE localFILE;
                if ((localFILE = fopen(name, "rb")) != null) {
                    fread(RAM, 0x104c, 1, 40, localFILE);
                    fclose(localFILE);
                    RAM[0x116d] = RAM[0x104d];
                    RAM[0x116e] = RAM[0x104e];
                    RAM[0x116f] = RAM[0x104f];
                }
                if (writing == 0) writing = RAM[0x116d] >> 4;
                if (writing != 0) videoram_w.handler(0x3f3, (RAM[0x116d] >> 4)); else videoram_w.handler(0x3f3, 0x24);
                if (writing == 0) writing = RAM[0x116d] & 0x0f;
                if (writing != 0) videoram_w.handler(0x3f2, (RAM[0x116d] & 0x0f)); else videoram_w.handler(0x3f2, 0x24);
                if (writing == 0) writing = RAM[0x116e] >> 4;
                if (writing != 0) videoram_w.handler(0x3f1, (RAM[0x116e] >> 4)); else videoram_w.handler(0x3f1, 0x24);
                if (writing == 0) writing = RAM[0x116e] >> 4;
                if (writing != 0) videoram_w.handler(0x3f0, (RAM[0x116e] & 0x0f)); else videoram_w.handler(0x3f0, 0x24);
                if (writing == 0) writing = RAM[0x116f] >> 4;
                if (writing != 0) videoram_w.handler(0x3ef, (RAM[0x116f] >> 4)); else videoram_w.handler(0x3ef, 0x24);
                if (writing == 0) writing = RAM[0x116f] & 0x0f;
                if (writing != 0) videoram_w.handler(0x3ee, (RAM[0x116f] & 0x0f)); else videoram_w.handler(0x3ee, 0x24);
                videoram_w.handler(0x3ed, 0);
                return 1;
            }
            return 0;
        }
    };

    static HiscoreSavePtr pacnpal_hisave = new HiscoreSavePtr() {

        public void handler(String name) {
            char[] RAM = Machine.memory_region[0];
            FILE localFILE;
            if ((localFILE = fopen(name, "wb")) != null) {
                fwrite(RAM, 0x104c, 1, 40, localFILE);
                fclose(localFILE);
            }
        }
    };

    public static GameDriver superpac_driver = new GameDriver("Super Pac-Man", "superpac", "AARON GILES\nKEVIN BRISLEY\nALAN J MCCORMICK", superpac_machine_driver, superpac_rom, null, null, null, superpac_input_ports, null, trak_ports, superpac_dsw, keys, superpac_color_prom, null, null, ORIENTATION_DEFAULT, superpac_hiload, superpac_hisave);

    public static GameDriver pacnpal_driver = new GameDriver("Pac & Pal", "pacnpal", "AARON GILES\nKEVIN BRISLEY\nLAWNMOWER MAN", pacnpal_machine_driver, pacnpal_rom, null, null, null, pacnpal_input_ports, null, trak_ports, pacnpal_dsw, keys, pacnpal_color_prom, null, null, ORIENTATION_DEFAULT, pacnpal_hiload, pacnpal_hisave);
}
