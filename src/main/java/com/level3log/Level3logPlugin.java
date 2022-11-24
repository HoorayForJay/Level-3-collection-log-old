package com.level3log;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.List;

@Slf4j
@PluginDescriptor(
		name = "Level 3 Collection Log"
)
public class Level3logPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private Level3logConfig config;

	@Inject
	private ClientThread clientThread;

	@Provides
	Level3logConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Level3logConfig.class);
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (scriptPostFired.getScriptId() == 2730)
		{
			clientThread.invokeLater(this::hideUnObtainable);
		}
	}

	private void hideUnObtainable()
	{
		List<String> hiddenPages = ImmutableList.of("Barrows Chests","Commander Zilyana","The Gauntlet",
				"General Graardor","Grotesque Guardians","Hespori","The Inferno","Kree'arra","K'ril Tsutsaroth",
				"Nex","The Nightmare","Vorkath","Zalcano","Zulrah","Theatre of Blood","Tombs of Amascut",
				"Hallowed Sepulchre","Magic Training Arena","Pest Control","Shades of Mort'ton","Soul Wars",
				"Temple Trekking","Trouble Brewing","Chompy Bird Hunting","Cyclopes","Glough's Experiments",
				"Monkey Backpacks","Shayzien Armour");
		// Widget child indexes of each page list, (Bosses, Raids, Clues, Other, Minigames)
		List<Integer> collectionLogListIndex = ImmutableList.of(12, 16, 32, 34, 35);

		for (int listIndex : collectionLogListIndex)
		{
			// Container widget that holds all the page names
			Widget collectionLogList = client.getWidget(WidgetID.COLLECTION_LOG_ID, listIndex);
			if (collectionLogList == null)
			{
				continue;
			}

			// Get a list of page names from the list widget
			Widget[] listPages = collectionLogList.getDynamicChildren();
			for (Widget page : listPages)
			{
				if (hiddenPages.contains(page.getText()))
				{
					page.setHidden(true);
				}
			}
		}

		Widget itemsContainer = client.getWidget(WidgetInfo.COLLECTION_LOG_ENTRY_ITEMS);
		if (itemsContainer == null)
		{
			return;
		}
		List<Integer> badIds = ImmutableList.of(21992,21907,22006,22106,22111,25348,24491,23757,23760,13071,22473,12921,
				21273,12652,12649,21291,21748,12650,12651,6799,21439,7975,7978,4153,11037,11902,20727,8901,4125,4129,
				4131,11840,11235,4099,4101,4107,24777,11942,22100,22103,22918,19707,20433,20436,20439,20442,6571,23943,
				24000,23959,24034,24037,24040,24043,24046,4732,4734,4736,4738,4708,4710,4712,4714,4716,4718,4720,4722,
				4724,4726,4728,4730,4745,4747,4749,4751,4753,4755,4757,4759,4740,11785,11814,11838,13256,11818,11820,
				11822,23956,4207,23859,11810,11812,11816,11832,11834,11836,21730,21736,21739,21742,21726,21746,19701,
				12936,22106,24495,22994,22883,22885,22881,11826,11828,11830,11791,11824,11787,25837,25838,24511,24514,
				24517,24417,24419,24420,24421,19685,21275,23953,23908,12922,12932,25859,21745,21295,24422,12934,12938,
				12927,13200,13201,2978,2979,2980,2981,2982,2983,2984,2985,2986,2987,2988,2989,2990,2991,2992,2993,2994,
				2995,8844,8845,8846,8847,8848,8849,8850,12954,19529,19586,19589,19592,19601,19610,24862,24863,24864,
				24865,24867,24866,13357,13358,13359,13360,13361,13362,13363,13364,13365,13366,13367,13368,13369,13370,
				13371,13372,13373,13374,13375,13376,13377,13378,13379,13380,13381,22486,22324,22481,22326,22327,22328,
				22477,22446,22494,22496,22498,22500,22502,25744,25746,25742,24711,24719,24721,24723,24725,24727,24731,
				24729,24733,24740,24844,24763,24765,24767,24769,24771,6908,6910,6912,6914,6916,6918,6920,6922,6924,6889,
				6926,8839,8840,8841,8842,11663,11664,11665,11666,13072,13073,12851,25630,3470,25442,25445,25448,25451,
				25454,25434,25436,25438,25440,25474,25476,25346,25340,10933,10940,10939,10941,8952,8953,8954,8955,8956,
				8957,8958,8959,8960,8961,8962,8963,8964,8965,8991,8992,8993,8994,8995,8996,8997,8966,8967,8968,8969,
				8970,8971,8988,8940,8941,26376,26378,26380,26235,26372,26231,26370,27277,25985,27226,27229,27232,25975,
				26219,27279,27283,27285,27289,27352,27248,27372,27255,27377,27378,27379,27380,27381,27293,27257,
				27259,27261,27263,27265,26348);

		Widget[] widgetItems = itemsContainer.getDynamicChildren();
		for (Widget widgetItem : widgetItems)
		{
			if (badIds.contains(widgetItem.getItemId()))
			{
				widgetItem.setHidden(true);
			}
		}

	}
}