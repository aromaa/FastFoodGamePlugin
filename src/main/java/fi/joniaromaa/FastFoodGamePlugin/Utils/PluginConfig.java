package fi.joniaromaa.FastFoodGamePlugin.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import lombok.Getter;

public class PluginConfig
{
	@Getter private HashMap<String, String> config;
	
	public PluginConfig(Path file) throws IOException
	{
		this.config = new HashMap<String, String>();
		
		for (String line : Files.readAllLines(file))
		{
			if (!line.startsWith("##"))
			{
				int index = line.indexOf('=');
				if (index != -1)
				{
					this.config.put(line.substring(0, index).trim(), line.substring(index + 1).trim());
				}
			}
		}
	}
	
	public String getString(String key)
	{
		return this.config.getOrDefault(key, null);
	}
	
	public int getInt(String key)
	{
		return new Integer(this.getString(key));
	}
	
	public boolean getBoolean(String key)
	{
		return this.getString(key).equals("1");
	}
}
