//package net.carm.chcarrefour.commands;
//
//import com.mojang.brigadier.Command;
//import com.mojang.brigadier.CommandDispatcher;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.screen.ingame.HandledScreen;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.server.command.CommandManager;
//import net.minecraft.server.command.ServerCommandSource;
//
//public class ReadShopCommand{
//    //public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated){
//    //    dispatcher.register(CommandManager.literal("readshop").executes(analyseShop()));
//    //}
//
//
//    public static int analyseScreen(){
//        MinecraftClient client = MinecraftClient.getInstance();
//
//        if (!(client.currentScreen instanceof HandledScreen<?> screen)) {
//            client.player.networkHandler.sendChatCommand("shop");
//        }
//
//        Screen screen = client.currentScreen;
//        ScreenHandler sh = client.player.openHandledScreen();
////        ScreenHandler handler = screen
//
//
//        return 1;
//
//    }
//}
