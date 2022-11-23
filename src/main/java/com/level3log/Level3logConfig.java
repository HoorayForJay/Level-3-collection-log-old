package com.level3log;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Level3log")
public interface Level3logConfig extends Config
{
	@ConfigItem(
		keyName = "greeting",
		name = "Description",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hides log slots Level 3's cannot obtain";
	}
}
