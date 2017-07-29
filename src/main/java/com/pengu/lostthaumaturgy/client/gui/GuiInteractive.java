package com.pengu.lostthaumaturgy.client.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.common.utils.IOUtils;
import com.mrdimka.hammercore.gui.GuiCentered;
import com.mrdimka.hammercore.math.MathHelper;
import com.pengu.lostthaumaturgy.LTInfo;
import com.pengu.lostthaumaturgy.LostThaumaturgy;

public class GuiInteractive extends GuiCentered
{
	public final GuiScreen parent;
	public int connectStatus = 0;
	private int selected = -1, scroll;
	private File[] crashFolder;
	public float avg;
	public int votes;
	public boolean canVote;
	public int lastUpload = -1;
	public GuiTextField lastUploaded;
	public String[] log;
	
	public final ResourceLocation gui = new ResourceLocation(LTInfo.MOD_ID, "textures/gui/gui_interactive.png");
	
	public GuiInteractive(GuiScreen parent)
	{
		this.parent = parent;
		xSize = 202;
		ySize = 175;
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		
		try
		{
			File f = new File("config", LTInfo.MOD_ID + ".dat");
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
			dos.writeInt(lastUpload);
			dos.writeInt(log != null ? log.length : 0);
			if(log != null)
				for(String l : log)
					dos.writeUTF(l);
			dos.close();
		} catch(Throwable err)
		{
		}
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		File c = new File("crash-reports");
		if(c.isDirectory())
			crashFolder = c.listFiles();
		else
			crashFolder = null;
		addButton(new GuiButton(0, (int) guiLeft + 5, (int) guiTop + 103, 49, 14, "Send"));
		addButton(new GuiButton(1, (int) guiLeft + 59, (int) guiTop + 142, 40, 20, "Check"));
		addButton(new GuiButton(2, (int) guiLeft + 55, (int) guiTop + 103, 60, 14, "Clear Log"));
		lastUploaded = new GuiTextField(0, fontRenderer, (int) guiLeft + 6, (int) guiTop + 142, 40, 20);
		new ConnectionEstablishTread().start();
		try
		{
			File f = new File("config", LTInfo.MOD_ID + ".dat");
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			lastUpload = dis.readInt();
			log = new String[dis.readInt()];
			for(int i = 0; i < log.length; ++i)
				log[i] = dis.readUTF();
			dis.close();
		} catch(Throwable err)
		{
		}
		selected = -1;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		drawDefaultBackground();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		
		mc.getTextureManager().bindTexture(gui);
		RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft + 190, guiTop + 106, 0);
		GL11.glScaled(.5, .5, 1);
		RenderUtil.drawTexturedModalRect(0, 0, 220, connectStatus * 16, 16, 16);
		GL11.glPopMatrix();
		
		if(mouseX >= guiLeft + 190 && mouseX < guiLeft + 198 && mouseY >= guiTop + 25 && mouseY < guiTop + 33)
			RenderUtil.drawTexturedModalRect(guiLeft + 190, guiTop + 25, 236, 0, 8, 8);
		
		if(mouseX >= guiLeft + 190 && mouseX < guiLeft + 198 && mouseY >= guiTop + 65 && mouseY < guiTop + 73)
			RenderUtil.drawTexturedModalRect(guiLeft + 190, guiTop + 65, 236, 8, 8, 8);
		
		float avg = this.avg;
		
		GL11.glPushMatrix();
		GL11.glTranslated(guiLeft + (xSize - 16 * 5) / 2F, guiTop - 16, 0);
		boolean mouseOver = mouseX >= guiLeft + (xSize - 16 * 5) / 2F && mouseY >= guiTop - 16 && mouseX < guiLeft + (xSize + 16 * 5) / 2F && mouseY < guiTop && canVote;
		
		if(mouseOver)
		{
			avg = (float) (mouseX - (guiLeft + (xSize - 16 * 5) / 2F)) / 16F;
			if(avg > 4.5F)
				avg = 5;
		}
		
		for(int i = 0; i < 5; ++i)
		{
			RenderUtil.drawTexturedModalRect(16 * i, 0, 228, 48, 16, 16);
			if(avg > i)
				RenderUtil.drawTexturedModalRect(16 * i, 0, 212, 48, MathHelper.clip(avg - i, 0, 1) * 16, 16);
		}
		GL11.glPopMatrix();
		
		String t = (mouseOver ? "Your" : "Average") + " Mark: (" + LostThaumaturgy.standartDecimalFormat.format(avg) + "/5). " + votes + " voted.";
		fontRenderer.drawString(t, (float) (guiLeft + (xSize - fontRenderer.getStringWidth(t)) / 2F), 6, 0xFFFFFF, false);
		
		String text = "Issue Heartbeat";
		fontRenderer.drawString(text, (int) (guiLeft + (xSize - fontRenderer.getStringWidth(text)) / 2F), (int) guiTop + 127, 0, false);
		
		if(crashFolder != null)
		{
			int i = 0;
			for(int j = scroll; j < crashFolder.length; ++j)
			{
				if(i >= 7 || j >= crashFolder.length)
					break;
				
				String name = crashFolder[j].getName();
				if(name.startsWith("crash-") && (name.endsWith("-client.txt") || name.endsWith("-server.txt")))
				{
					GL11.glPushMatrix();
					GL11.glTranslated(guiLeft, guiTop, 0);
					drawString(fontRenderer, name.substring(6, name.length() - 4), 9, 12 + i * 12, mouseX >= guiLeft + 9 && mouseX < guiLeft + 183 && mouseY >= guiTop + 12 + i * 12 && mouseY < guiTop + 24 + i * 12 ? 0xFFFFAA : selected == j ? 0xFFFF00 : 0xFFFFFF);
					GL11.glPopMatrix();
				}
				
				++i;
			}
		}
		
		if(log != null)
			for(int i = 0; i < log.length; ++i)
			{
				int j = log.length - i - 1;
				if(j < 0 || j >= log.length)
					break;
				fontRenderer.drawString(log[j], 0, 4 + i * 12, 0xFFFFFF, false);
			}
		
		buttonList.get(0).enabled = selected > -1;
		
		if(lastUploaded != null)
			lastUploaded.drawTextBox();
		
		String s = "" + (StatusTread.status == 0 ? TextFormatting.YELLOW + "?" : StatusTread.status == 1 ? TextFormatting.DARK_RED + "Unsolved" : TextFormatting.DARK_GREEN + "Solved");
		fontRenderer.drawString("Status: " + s, (float) guiLeft + 102, (float) guiTop + 148, 0, false);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(lastUploaded == null || !lastUploaded.textboxKeyTyped(typedChar, keyCode))
			super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		boolean so = false;
		
		if(lastUploaded != null)
			lastUploaded.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(crashFolder != null)
		{
			int i = 0;
			for(int j = scroll; j < crashFolder.length; ++j)
			{
				if(i >= 7 || j >= crashFolder.length)
					break;
				if(mouseX >= guiLeft + 9 && mouseX < guiLeft + 183 && mouseY >= guiTop + 12 + i * 12 && mouseY < guiTop + 24 + i * 12)
				{
					so = selected != j;
					selected = j;
				}
				++i;
			}
		}
		
		if(mouseX >= guiLeft + (xSize - 16 * 5) / 2F && mouseY >= guiTop - 16 && mouseX < guiLeft + (xSize + 16 * 5) / 2F && mouseY < guiTop && canVote)
		{
			float avg = (float) (mouseX - (guiLeft + (xSize - 16 * 5) / 2F)) / 16F;
			if(avg > 4.5F)
				avg = 5;
			
			VoteTread th = new VoteTread();
			th.setVote = avg;
			th.start();
			so = true;
		}
		
		if(mouseX >= guiLeft + 190 && mouseX < guiLeft + 198 && mouseY >= guiTop + 25 && mouseY < guiTop + 33 && scroll > 0)
		{
			scroll--;
			so = true;
		}
		
		if(crashFolder != null && mouseX >= guiLeft + 190 && mouseX < guiLeft + 198 && mouseY >= guiTop + 65 && mouseY < guiTop + 73 && scroll < crashFolder.length - 6)
		{
			scroll++;
			so = true;
		}
		
		if(buttonList.get(0).isMouseOver() && buttonList.get(0).enabled)
		{
			UploadThread th = new UploadThread();
			th.selected = this.selected;
			th.start();
			selected = -1;
		}
		
		if(buttonList.get(1).isMouseOver())
		{
			StatusTread th = new StatusTread(this);
			th.start();
		}
		
		if(buttonList.get(2).isMouseOver())
			log = new String[0];
		
		if(so)
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
	}
	
	public class ConnectionEstablishTread extends Thread
	{
		@Override
		public void run()
		{
			setName("LostThaumaturgyPing" + getName());
			
			String remote = "apengu.servegame.com";
			int port = 5466;
			
			try
			{
				URL paste = new URL("https://pastebin.com/raw/RrAThrsn");
				byte[] data = IOUtils.pipeOut(paste.openStream());
				String[] str = new String(data).replaceAll("\r", "").split("\n");
				remote = str[0];
				port = Integer.parseInt(str[1]);
			} catch(Throwable err)
			{
				connectStatus = 2;
				return;
			}
			
			try
			{
				Socket s = new Socket(remote, port);
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				out.write(0);
				out.flush();
				byte[] b = "        ".getBytes();
				in.read(b);
				DataInputStream dis = new DataInputStream(in);
				avg = dis.readFloat();
				votes = dis.readInt();
				canVote = dis.readBoolean();
				s.close();
				connectStatus = new String(b).equals("LT Hello") ? 1 : 2;
			} catch(Throwable err)
			{
				connectStatus = 2;
				return;
			}
		}
	}
	
	public class VoteTread extends Thread
	{
		public float setVote;
		
		@Override
		public void run()
		{
			setName("LostThaumaturgyVote" + getName());
			
			String remote = "apengu.servegame.com";
			int port = 5466;
			
			connectStatus = 0;
			
			try
			{
				URL paste = new URL("https://pastebin.com/raw/RrAThrsn");
				byte[] data = IOUtils.pipeOut(paste.openStream());
				String[] str = new String(data).replaceAll("\r", "").split("\n");
				remote = str[0];
				port = Integer.parseInt(str[1]);
			} catch(Throwable err)
			{
				connectStatus = 2;
				return;
			}
			
			try
			{
				Socket s = new Socket(remote, port);
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				out.write(3);
				DataOutputStream dos = new DataOutputStream(out);
				
				dos.writeFloat(setVote);
				out.flush();
				DataInputStream dis = new DataInputStream(in);
				avg = dis.readFloat();
				votes = dis.readInt();
				canVote = dis.readBoolean();
				s.close();
				
				connectStatus = 1;
			} catch(Throwable err)
			{
				connectStatus = 2;
				return;
			}
		}
	}
	
	public class UploadThread extends Thread
	{
		public int selected;
		
		@Override
		public void run()
		{
			setName("LostThaumaturgyUpload" + getName());
			
			String remote = "apengu.servegame.com";
			int port = 5466;
			
			connectStatus = 0;
			
			try
			{
				URL paste = new URL("https://pastebin.com/raw/RrAThrsn");
				byte[] data = IOUtils.pipeOut(paste.openStream());
				String[] str = new String(data).replaceAll("\r", "").split("\n");
				remote = str[0];
				port = Integer.parseInt(str[1]);
			} catch(Throwable err)
			{
				connectStatus = 2;
				return;
			}
			
			try
			{
				Socket s = new Socket(remote, port);
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				out.write(2);
				DataOutputStream dos = new DataOutputStream(out);
				dos.writeInt((int) Files.size(crashFolder[selected].toPath()));
				FileInputStream fis = new FileInputStream(crashFolder[selected]);
				dos.write(IOUtils.deflaterCompress(IOUtils.pipeOut(fis)));
				fis.close();
				out.flush();
				DataInputStream dis = new DataInputStream(in);
				byte[] bytes = new byte[dis.read()];
				dis.read(bytes);
				String name = new String(bytes);
				lastUpload = Integer.parseInt(name);
				s.close();
				crashFolder[selected].delete();
				log = ArrayUtils.add(log, "Uploaded. ID: " + lastUpload);
				connectStatus = 1;
			} catch(Throwable err)
			{
				connectStatus = 2;
				return;
			}
			
			initGui();
		}
	}
	
	public static class StatusTread extends Thread
	{
		public static int status;
		public GuiInteractive gui;
		
		public StatusTread(GuiInteractive g)
		{
			gui = g;
		}
		
		@Override
		public void run()
		{
			setName("LostThaumaturgyStatus" + getName());
			
			String remote = "apengu.servegame.com";
			int port = 5466;
			
			try
			{
				URL paste = new URL("https://pastebin.com/raw/RrAThrsn");
				byte[] data = IOUtils.pipeOut(paste.openStream());
				String[] str = new String(data).replaceAll("\r", "").split("\n");
				remote = str[0];
				port = Integer.parseInt(str[1]);
			} catch(Throwable err)
			{
				gui.connectStatus = 2;
				return;
			}
			
			try
			{
				Socket s = new Socket(remote, port);
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				out.write(1);
				byte[] name = gui.lastUploaded.getText().getBytes();
				out.write(name.length);
				out.write(name);
				out.flush();
				status = in.read();
				s.close();
			} catch(Throwable err)
			{
				gui.connectStatus = 2;
				return;
			}
		}
	}
}