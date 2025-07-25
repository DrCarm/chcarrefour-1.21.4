package net.carm.chcarrefour.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class LoreCommand {

    // Compteur de ticks
    public static int ticksUntilAnalyze = -1;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                ClientCommandManager.literal("scanshop")
                        .executes(ctx -> {
                            MinecraftClient client = MinecraftClient.getInstance();

                            if (client.player == null) return 0;

                            // Envoie la commande "/shop"
                            client.player.networkHandler.sendChatCommand("shop");

                            // Lance le compteur de ticks (attente avant analyse)
                            ticksUntilAnalyze = 20;

                            return 1;
                        })
        );

        // Enregistre un listener pour détecter chaque tick client
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ticksUntilAnalyze > 0) {
                ticksUntilAnalyze--;
            } else if (ticksUntilAnalyze == 0) {
                ticksUntilAnalyze = -1;
                analyzeScreen();
            }
        });
    }

    private static void analyzeScreen() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!(client.currentScreen instanceof HandledScreen<?> screen)) {
            client.player.sendMessage(Text.literal("[LoreAnalyzer] Aucun GUI ouvert après /shop.").formatted(Formatting.RED), false);
            return;
        }

        ScreenHandler handler = screen.getScreenHandler();
        StringBuilder output = new StringBuilder();

        for (Slot slot : handler.slots) {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                String itemName = stack.getName().getString();
//                ComponentMap tag = stack.getComponents();
//                tag.get()
                List<Text> loreList = stack.getOrDefault(DataComponentTypes.LORE, LoreComponent.DEFAULT).styledLines();


                output.append("Item: ").append(itemName).append("\n");

                for (int i = 0; i < loreList.size(); i++) {
                    Text text = loreList.get(i);
                    output.append("  → ").append(text.getString()).append("\n");
                }

                output.append("\n");
            }
        }

        if (output.isEmpty()) {
            client.player.sendMessage(Text.literal("[LoreAnalyzer] Aucun item avec lore trouvé.").formatted(Formatting.GRAY), false);
        } else {
            writeToFile(output.toString());
            client.player.sendMessage(Text.literal("[LoreAnalyzer] Lore extrait dans lore_output.txt").formatted(Formatting.GREEN), false);
        }
    }

    private static void writeToFile(String data) {
        try {
            Path path = MinecraftClient.getInstance().runDirectory.toPath().resolve("lore_output.txt");
            Files.writeString(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}