package com.beeclient.render;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;

public class TextureLoader {

    private static final Map<String, Integer> textureCache = new HashMap<>();

    public static int loadTexture(String resourcePath) {
        if (textureCache.containsKey(resourcePath)) {
            return textureCache.get(resourcePath);
        }

        try {
            InputStream is = TextureLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("[Bee Client] Texture not found: " + resourcePath);
                return -1;
            }

            BufferedImage image = ImageIO.read(is);
            is.close();

            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());
            for (int y = height - 1; y >= 0; y--) {
                for (int x = 0; x < width; x++) {
                    int argb = pixels[y * width + x];
                    buffer.put((byte) ((argb >> 16) & 0xFF));
                    buffer.put((byte) ((argb >> 8) & 0xFF));
                    buffer.put((byte) (argb & 0xFF));
                    buffer.put((byte) ((argb >> 24) & 0xFF));
                }
            }
            buffer.flip();

            int textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            textureCache.put(resourcePath, textureId);
            return textureId;
        } catch (Exception e) {
            System.err.println("[Bee Client] Failed to load texture: " + resourcePath + " - " + e.getMessage());
            return -1;
        }
    }

    public static void bind(int textureId) {
        if (textureId > 0) {
            GlStateManager.bindTexture(textureId);
        }
    }
}
