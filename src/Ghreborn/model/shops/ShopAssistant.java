package Ghreborn.model.shops;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.players.Client;
import Ghreborn.world.ShopHandler;

public class ShopAssistant {

	private Client c;

	public ShopAssistant(Client client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		if(c.getRights().isRainbow()) {
			c.sendMessage("as an rainbow donator u cant use shops");
			return;
		}
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true
						&& PlayerHandler.players[i].myShopId == c.myShopId
						&& i != c.index) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		synchronized (c) {
			int TotalItems = 0;
			for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0) {
					TotalItems++;
				}
			}
			if (TotalItems > ShopHandler.MaxShopItems) {
				TotalItems = ShopHandler.MaxShopItems;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(3900);
			c.getOutStream().writeWord(TotalItems);
			int TotalCount = 0;
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0
						|| i <= ShopHandler.ShopItemsStandard[ShopID]) {
					if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(
								ShopHandler.ShopItemsN[ShopID][i]);
					} else {
						c.getOutStream().writeByte(
								ShopHandler.ShopItemsN[ShopID][i]);
					}
					if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT
							|| ShopHandler.ShopItems[ShopID][i] < 0) {
						ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
					}
					c.getOutStream().writeWordBigEndianA(
							ShopHandler.ShopItems[ShopID][i]);
					TotalCount++;
				}
				if (TotalCount > TotalItems) {
					break;
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double TotPrice = 0;
		//int price = getSpecialItemValue(ItemID);
		 int price = ItemCacheDefinition.forID(ItemID).getvalue();
		ShopValue = price;
		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		if(c.myShopId == 256) {
			return TotPrice*10;
		}
		return TotPrice;
	}
	public static int getItemShopValue(int itemId) { // TODO
		switch (itemId) {
		case 4745:
		case 4746:
		case 4747:
		case 4748:
		case 4749:
		case 4750:
		case 4751:
		case 4752:
		case 4753:
		case 4754:
		case 4755:
		case 4756:
		case 4757:
		case 4758:
		case 4759:
		case 4760:
		case 4708:
		case 4709:
		case 4710:
		case 4711:
		case 4712:
		case 4713:
		case 4714:
		case 4715:
		case 4716:
		case 4717:
		case 4718:
		case 4719:
		case 4720:
		case 4721:
		case 4722:
		case 4723:
		case 4724:
		case 4725:
		case 4726:
		case 4727:
		case 4728:
		case 4729:
		case 4730:
		case 4731:
		case 4732:
		case 4733:
		case 4734:
		case 4735:
		case 4736:
		case 4737:
		case 4738:
		case 4739:
			return 300;
		case 4151:
		case 12773:
			return 250;
		case 12006:
			return 500;
		case 6585:
		case 11840:
			return 200;
		case 10334:
		case 10330:
		case 10332:
		case 10336:
			return 250;
		case 12357:
			return 200;
		case 12422:
		case 12424:
		case 12426:
		case 12437:
		case 10350:
		case 10348:
		case 10346:
		case 10352:
		case 10342:
		case 10344:
		case 10338:
		case 10340:
			return 350;
		case 13173:
			return 250;
		case 13174:
			return 200;
		case 11863:
			return 345;
		case 11862:
			return 340;
		case 11847:
			return 300;
		case 962:
			return 150;
		case 1419:
			return 110;
		case 4084:
		case 12848:
			return 110;
		case 6199:
			return 50;
		case 13239:
		case 13237:
		case 13235:
			return 1000;
		}
		/** Donator items **/
		if (itemId == 11889 || itemId == 11824) {
			return 80;
		}
		if (itemId == 11905 || itemId == 11906 || itemId == 11907 || itemId == 11908) {
			return 250;
		}
		if (itemId == 12817 || itemId == 12818 || itemId == 12819 || itemId == 12820 || itemId == 12821
				|| itemId == 12822 || itemId == 12824 || itemId == 12825) {
			return 800;
		}

		if (itemId == 1038 || itemId == 1038 || itemId == 1039 || itemId == 1040 || itemId == 1041 || itemId == 1042
				|| itemId == 1043 || itemId == 1044 || itemId == 1045 || itemId == 1046 || itemId == 1047
				|| itemId == 1048 || itemId == 1049 || itemId == 1050 || itemId == 1051 || itemId == 1053
				|| itemId == 1054 || itemId == 1055 || itemId == 1056 || itemId == 1057 || itemId == 1058) {
			return 150;
		}

		if (itemId == 961 || itemId == 963) {
			return 150;
		}

		if (itemId == 1037) {
			return 70;
		}

		/** PK ITEMS **/
		if (itemId == 11802) {
			return 1200;
		}
		if (itemId == 13652 || itemId == 14484) {
			return 1400;
		}
		if (itemId == 13265) {
			return 600;
		}
		if (itemId == 13263) {
			return 1350;
		}
		if (itemId == 13576) {
			return 1300;
		}
		if (itemId == 11804 || itemId == 11806 || itemId == 11808) {
			return 300;
		}
		if (itemId == 11785) {
			return 750;
		}
		if (itemId == 11926) {
			return 500;
		}
		if (itemId == 2577 || itemId == 2581) {
			return 40;
		}
		if (itemId == 6733) {
			return 3;
		}
		if (itemId == 12006 || itemId == 11838) {
			return 100;
		}
		if (itemId == 11283 || itemId == 11284 || itemId == 11285) {
			return 600;
		}
		if (itemId == 13215) {
			return 600;
		}
		if (itemId == 11771 || itemId == 11773 || itemId == 11772 || itemId == 11770) {
			return 50;
		}
		if (itemId == 6918 || itemId == 6916 || itemId == 6917 || itemId == 6918 || itemId == 6919 || itemId == 6920
				|| itemId == 6921 || itemId == 6922 || itemId == 6923 || itemId == 6924 || itemId == 6925) {
			return 25;
		}
		if (itemId == 13213) {
			return 600;
		}
		if (itemId == 12929 || itemId == 12927 || itemId == 12928 || itemId == 12930 || itemId == 12931) {
			return 500;
		}
		if (itemId == 6737) {
			return 3;
		}
		if (itemId == 11924) {
			return 500;
		}
		if (itemId == 6914 || itemId == 6914 || itemId == 6889 || itemId == 6890) {
			return 80;
		}
		if (itemId == 12002) {
			return 250;
		}
		if (itemId == 6735 || itemId == 6731) {
			return 3;
		}
		if (itemId == 12904 || itemId == 12902 || itemId == 12900) {
			return 650;
		}
		if (itemId == 11791) {
			return 600;
		}
		if (itemId == 13211) {
			return 600;
		}
		if (itemId == 11826 || itemId == 11828 || itemId == 11830) {
			return 400;
		}
		if (itemId == 12596) {
			return 300;
		}
		if (itemId == 11834 || itemId == 11832) {
			return 800;
		}
		if (itemId == 12924 || itemId == 12926) {
			return 2200;
		}
		return 1;
	}
	public int getSpecialItemValue(int id) {
		switch (id) {

		case 6018:
			return 5;
		case 6762:
			return 500;
		case 2:
			return 1;// cannonball
		case 6:
		case 8:
		case 10:
		case 12:
			return 125;// cannon
		case 2950: // feather
		case 775:
			return 65;
		case 8950:
			return 50;
		case 2946: // tinderbox
		case 2949: // hammer
			return 35;
		case 3204:
			return 100;
		case 245:
		case 246:
			return 10;
		case 1381:
		case 1383:
		case 1385:
		case 1387:
			return 100;
		case 2643:
		case 12321:
		case 12325:
		case 12323:
		case 2639:
		case 5509:
			return 25;
		case 10499:
		case 12453:
			return 15;
		case 12499:
			return 20;
		case 4097:
		case 4107:
		case 4117:
			return 10;
		case 10828:
			return 30;
		case 5510:
			return 100;
		case 5512:
			return 250;
		case 5514:
		case 626:
		case 628:
		case 2904:
		case 2914:
		case 2924:
		case 2934:
			return 1000;
		case 3061:
		case 1837:
			return 1500;
		case 7114:
		case 9005:
		case 9073:
			return 2500;
		case 10836:
		case 10837:
		case 10838:
		case 10839:
		case 6858:
			return 2000;
		case 6860:
		case 10420:
		case 10424:
		case 10428:
		case 10432:
		case 10436:
			return 2500;
		case 6859:
		case 10394:
		case 12373:
		case 10396:
			return 3000;
		case 2633:
		case 2635:
		case 2637:
		case 12375:
		case 12377:
		case 12379:
			return 5000;
		case 1144:
		case 1210:
			return 1;
		case 1182:
		case 1300:
		case 1160:
		case 1739:
			return 2;
		case 1344:
		case 1370:
		case 1198:
		case 1316:
		case 1146:
		case 1212:
		case 1072:
		case 1086:
			return 3;
		case 1184:
		case 1302:
		case 1162:
		case 1074:
		case 1092:
		case 7461:
			return 6;
		case 1346:
		case 1372:
		case 1200:
		case 1318:
			return 9;
		case 1122:
		case 1148:
		case 1214:
		case 1052:
		case 10010:
		case 11260:
		case 11261:
			return 5;
		case 1186:
		case 1304:
		case 1164:
			return 10;
		case 1202:
		case 1348:
		case 1374:
		case 1320:
		case 1124:
		case 1080:
		case 1094:
			return 15;
		case 1128:
		case 11259:
			return 25;

		case 840:
		case 846:
		case 848:
		case 852:
			return 5;
		case 856:
			return 7;
		case 860:
			return 8;
		case 1655:
		case 1693:
		case 1636:
		case 1170:
		case 1064:
			return 2;
		case 1659:
		case 1697:
		case 1640:
			return 9;
		case 1657:
		case 1695:
		case 1638:
			return 5;
		case 1661:
		case 1699:
		case 1642:
			return 18;
		case 1663:
		case 1701:
		case 1644:
			return 34;
		case 1665:
		case 1703:
		case 1646:
			return 65;
		case 3145:
			return 10;
		case 7947:
			return 3;
		case 380:
		case 379:
			return 1;
		case 5641:
			return 3;
		case 373:
		case 374:
			return 2;
		case 386:
		case 385:
			return 5;
		case 140:
			return 6;
		case 3043:
			return 10;
		case 6688:
			return 15;
		case 3027:
			return 8;
		case 170:
			return 12;
		case 146:
			return 4;
		case 158:
			return 6;
		case 164:
			return 8;
		case 12235:
			return 25;
		case 6528:
			return 200;
		case 1712:
			return 30;
		case 2579:
			return 5;
		case 4675:
			return 35;
		case 3842:
			return 10;
		case 4153:
		case 4587:
			return 50;
		case 4089:
		case 4099:
		case 4109:
		case 5698:
			return 20;
		case 4091:
		case 4101:
		case 4111:
			return 40;
		case 4093:
		case 4103:
		case 4113:
			return 30;
		case 8008:
		case 8007:
		case 8009:
		case 8010:
		case 8011:
		case 8012:
			return 1;
		case 811:
		case 1077:
		case 1125:
		case 1195:
		case 1159:
		case 4127:
		case 9144:
		case 868:
		case 8013:
		case 19613:
		case 19615:
		case 19627:
		case 19629:
			return 2;
		case 561:
		case 1165:
		case 810:
		case 4125:
		case 892:
		case 884:
		case 565:
		case 560:
		case 566:
		case 562:
		case 563:
		case 564:
		case 9075:
			return 1;
		case 554:
		case 555:
		case 556:
		case 557:
		case 558:
		case 559:
			return 0;
		case 1121:
		case 1071:
		case 1197:
		case 861:
		case 6109:
			return 4;
		case 9244:
			return 5;
		case 1161:
		case 4129:
		case 1725:
			return 6;
		case 1073:
		case 1123:
		case 1199:
		case 2489:
		case 2493:
			return 12;
		case 1135:
		case 2495:
		case 2491:
		case 6107:
			return 16;
		case 1099:
		case 2487:
		case 6108:
			return 8;
		case 2499:
		case 2497:
		case 12383:
		case 3755:
		case 3753:
			return 20;
		case 2501:
			return 24;
		case 2503:
			return 28;
		case 1065:
		case 2550:
		case 7458:
			return 4;
		case 1163:
		case 12241:
			return 10;
		case 1127:
		case 4131:
		case 12225:
			return 25;
		case 1079:
		case 3749:
		case 3751:
		case 9185:
		case 12237:
			return 20;
		case 1201:
			return 15;
		case 9672:
			return 50;
		case 9674:
			return 125;
		case 9676:
			return 100;
		case 3105:
			return 5;
		case 11840:
			return 5000;
		case 13572:
			return 40;
		case 9747:
		case 9748:
		case 9751:
		case 9754:
		case 9757:
		case 9760:
		case 9763:
		case 9766:
		case 9769:
		case 9772:
		case 9775:
		case 9778:
		case 9781:
		case 9784:
		case 9787:
		case 9790:
		case 9793:
		case 9796:
		case 9799:
		case 9802:
		case 9805:
		case 9811:
		case 9949:
		case 9753:
		case 9750:
		case 9768:
		case 9756:
		case 9759:
		case 9762:
		case 9801:
		case 9807:
		case 9783:
		case 9798:
		case 9804:
		case 9780:
		case 9795:
		case 9792:
		case 9774:
		case 9771:
		case 9777:
		case 9786:
		case 9810:
		case 9765:
			return 1000;
		case 12695:
		case 12696:
			return 25;
		case 247:
		case 248:
			return 12;
		case 12913:
		case 12914:
			return 20;
		case 11937:
		case 11936:
			return 10;
		case 13235:
		case 13237:
		case 13239:
			return 25000;
		case 11234:
			return 5;
		case 12773:
			return 3000;
		case 12774:
			return 500;
		case 4151:
			return 2000;
		case 11889:
			return 20000;
		case 11283:
			return 8000;
		case 11926:
		case 11924:
			return 10000;
		case 1592:
		case 1595:
		case 1597:
		case 1619:
		case 1620:
			return 20;
		case 1617:
		case 1618:
			return 40;
		case 1631:
		case 1632:
			return 80;
		case 11128:
			if (c.myShopId == 29)
				return 10000;
			else
				return 500;
		case 6585:
			if (c.myShopId == 29)
				return 15000;
			else
				return 5000;
		case 6571:// onyx
			return 12000;
		case 20035:
		case 20038:
		case 20041:
		case 20044:
		case 20047:	
			return 2500;
		case 1621:// emerald
		case 1622:
			return 10;
		case 1607:
		case 1608:
			return 4;
		case 1605:
		case 1606:
			return 8;
		case 1603:
		case 1604:
			return 16;
		case 1601:
		case 1602:
			return 32;
		case 1615:
		case 1616:
			return 64;
		case 6573:
			return 128;
		case 1436:
		case 1437:
			return 1;
		case 227:
		case 228:
			return 4;
		case 211:
			return 8;
		case 12938:
		case 1623:// sapphire
		case 1624:
		case 4155:
		case 1523:
		case 1935:
		case 5603:
		case 1925:
		case 1923:
		case 1887:
		case 590:
		case 2347:
		case 952:
		case 946:
		case 1755:
		case 1265:
		case 1351:
		case 954:
		case 225:
		case 226:
		case 5341:
		case 5343:
		case 5340:
		case 6032:
		case 5298:
			return 5;
		case 239:
		case 240:
		case 5299:
			return 6;
		case 1778:
			return 4;
		case 1275:
		case 1359:
			return 15;
		case 1540:
			return 25;
		case 303:
		case 305:
		case 307:
		case 309:
		case 311:
		case 310:
		case 3157:
			return 20;
		case 231:
		case 232:
		case 5295:
		case 5296:
		case 5297:
			return 4;
		case 301:
		case 243:
		case 244:
			return 7;
		case 6049:
		case 6050:
		case 5301:
			return 8;
		case 3138:
		case 3139:
		case 5302:
			return 9;
		case 5300:
			return 10;
		case 6693:
		case 6694:
			return 11;
		case 5303:
			return 12;
		case 5304:
			return 15;
		case 235:
		case 236:
		case 1526:
		case 5293:
		case 5294:
			return 2;
		case 313:
			return 0;
		case 11940:
		case 223:
		case 224:
		case 9736:
		case 9737:
			return 3;
		case 2363:
		case 2361:
		case 2359:
		case 2357:
		case 2355:
		case 2353:
		case 2351:
		case 2349:
		case 383:
		case 371:
		case 363:
		case 359:
		case 345:
		case 377:
		case 335:
		case 331:
		case 327:
		case 321:
		case 317:
		case 314:
		case 221:
		case 222:
		case 5291:
		case 5292:
			return 1;
		case 13124:
			return 25;
		case 12357:
			return 50;
		case 6737:
		case 6733:
		case 6731:
		case 6735:
		case 2990:
		case 2991:
		case 2992:
		case 2993:
		case 2994:
		case 2995:
		case 12845:
		case 9920:
		case 12359:
		case 2651:
		case 8928:
		case 12412:
		case 12432:
		case 12434:
		case 2641:
		case 11280:
		case 430:
		case 12335:
		case 12514:
		case 6665:
		case 6666:
			return 3;
		case 12813:
		case 12814:
		case 12815:
		case 12810:
		case 12811:
		case 12812:
		case 12887:
		case 12888:
		case 12890:
		case 12891:
		case 12892:
		case 12893:
		case 12894:
		case 12895:
		case 12896:
		case 11919:
		case 12956:
		case 12957:
		case 12958:
		case 12959:
		case 12889:
			return 5;
		case 13140:
		case 13107:
		case 13155:
		case 13120:
		case 13132:
		case 13103:
		case 13136:
		case 13128:
		case 13111:
		case 13144:
		case 13115:
			return 10;
		case 15098:
			return 25;
		case 299:
			return 5;
		case 12954:
			if (c.myShopId == 77 || c.myShopId == 115)
				return 10;
		case 11864:
			return 250;
		case 11865:
			return 150;
		case 11738:
			return 1;
		case 6570:
			if (c.myShopId == 77 || c.myShopId == 115)
				return 3;
			else
				return 2500;
		case 13226:
			return 30;
		case 13635:
		case 13637:
		case 13636:
		case 13634:
		case 13633:
		case 13632:
		case 13631:
		case 13630:
		case 13629:
		case 13638:
		case 13627:
			if (c.myShopId == 77 || c.myShopId == 115)
				return 10;
		case 7462:
		case 10551:
			if (c.myShopId == 77 || c.myShopId == 115)
				return 4;
			else
				return 2500;
		case 12637:
		case 12638:
		case 12639:
		case 12537:
			return 100;

		/*
		 * Thieving Stalls Cake-Wine
		 */
		case 1893:
			return 3;
		case 6814:
			return 4;
		case 1806:
			return 5;
		case 1613:
			return 6;
		case 1993:
			return 7;
		/*
		 * PK STORE
		 */
		case 2379:
			return 1;
		case 13066:
			return 1;
		case 7582:
			return 50;
		case 1249:
			return 300;
		case 2839:
			return 350;
		case 4202:
			return 200;
		case 6720:
			return 300;
		case 4081:
			return 150;
		case 3481:
		case 3483:
		case 3485:
		case 3486:
		case 3488:
		case 6856:
		case 6857:
		case 6861:
		case 6862:
		case 6863:
			return 10;
		case 11212:
			return 5;
		case 4333:
		case 4353:
		case 4373:
		case 4393:
		case 4413:
			return 2;
		case 2996:
		case 1464:
			return 1;
		case 10887:
			if (c.myShopId == 9)
				return 20;
			else
				return 200;
		case 8849:
			return 25;
		case 8848:
			return 20;
		case 8850:
		case 7398:
		case 7399:
		case 7400:
			if (c.myShopId == 77)
				return 10;
			else
				return 40;
		case 8845:
			return 10;
		case 12751:
			return 500;
		/*
		 * case 7462: if (c.myShopId == 12 || c.myShopId == 49 || c.myShopId ==
		 * 50) return 75; else return 15;
		 */
		case 2437:
		case 2441:
		case 2443:
			return 4;
		case 7460:
			if (c.myShopId == 12 || c.myShopId == 49 || c.myShopId == 50)
				return 60;
			else
				return 6;
		case 7459:
			if (c.myShopId == 12 || c.myShopId == 49 || c.myShopId == 50)
				return 50;
			else
				return 5;
		case 12000:
			return 20;
		case 11144:
			if (c.myShopId == 9)
				return 30;
			else
				return 500;
		case 2677:
			return 15;
		case 2572:
			if (c.myShopId == 77)
				return 10;
			else if (c.myShopId == 78)
				return 40;
			else if (c.myShopId == 44)
				return 200;
			else
				return 20;
		case 13887:
		case 13893:
			if (c.myShopId == 9)
				return 50;
			else
				return 280;
		case 13899:
			return 120;
		case 13902:
			return 110;
		case 13905:
			return 100;
		case 14484:
			return 400;
		case 13896:
		case 13884:
		case 13890:
			if (c.myShopId == 9)
				return 40;
			else
				return 250;
		case 13858:
		case 13861:
		case 13864:
			if (c.myShopId == 9)
				return 20;
			else
				return 130;
		case 13870:
		case 13873:
		case 13876:
			if (c.myShopId == 9)
				return 30;
			else
				return 130;
			// case 10551:
		case 10548:
			if (c.myShopId == 77 || c.myShopId == 115)
				return 2;
			else
				return 2000;
		case 7509:
			return 1;
		case 4168:
		case 4166:
		case 4551:
		case 4164:
		case 4170:
			return 10;
		case 12006:
			return 10000;

		/*
		 * case 6731: case 6735: case 6733: case 6737: if (c.myShopId == 77)
		 * return 3; else return 15;
		 */
		case 6916:
		case 6918:
		case 6920:
		case 6922:
		case 6924:
			return 500;
		case 12002:
			return 3000;
		case 13265:
			return 40000;
		case 2417:
		case 2415:
		case 2416:
			if (c.myShopId == 77)
				return 10;
			else if (c.myShopId == 9)
				return 10;
			else
				return 50;
		case 11771:
		case 11773:
		case 11772:
		case 11778:
		case 11770:
			return 50;
		/*
		 * case 6570: if (c.myShopId == 77) return 10; else return 120;
		 */
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11235:
			return 2500;
		case 11785:
			return 17500;

		case 11791:
			return 15000;

		case 11061:
			if (c.myShopId == 9)
				return 50;
			else
				return 1000;
		case 11907:
			if (c.myShopId == 9)
				return 10000;
			else
				return 500;
		case 8839:
		case 8840:
		case 8842:
			return 10000;
		case 11663:
		case 11664:
		case 11665:
			return 15000;
		case 6889:
			return 1000;
		case 6914:
			return 1000;
		case 4732:
		case 4734:
		case 4736:
		case 4738:
			if (c.myShopId == 77)
				return 4;
			else
				return 12;
		case 4716:
		case 4718:
		case 4720:
		case 4722:
			if (c.myShopId == 77)
				return 2;
			else
				return 20;
		case 4712:
		case 4710:
		case 4714:
		case 4708:
			if (c.myShopId == 77)
				return 2;
			else
				return 14;
		case 4724:
		case 4726:
		case 4728:
		case 4730:
		case 4745:
		case 4747:
		case 4749:
		case 4751:
		case 4753:
		case 4755:
		case 4757:
		case 4759:
			return 12;
		case 10338:
		case 10342:
		case 10340:
		case 1989:
			return 500;
		case 11838:
		case 1961:
			return 100;
		case 10352:
		case 10350:
		case 10348:
		case 10346:
			return 1000;
		case 11826:
			return 400;
		case 11828:
			return 400;
		case 11830:
			return 400;
		case 12596:
			return 1000;
		case 12806:
		case 12807:
			return 500;
		case 12877:
		case 12873:
		case 12875:
		case 12879:
		case 12881:
		case 12883:
			return 15;
		case 1959:
		case 9703:
			if (c.myShopId == 9)
				return 40;
			else
				return 600;
		case 11802:
			if (c.myShopId == 9)
				return 60000;
			else
				return 60000;
		case 2581:
			return 500;
		case 12449:
			return 15;
		case 2577:
			return 1000;
		/*
		 * case 12954: return 100;
		 */
		case 13213:
		case 13211:
		case 13215:
			return 20000;
		case 12934:
			return 0;
		case 11832:
		case 11834:
			if (c.myShopId == 9)
				return 25000;
			else
				return 800;
		case 12929:
			c.sendMessage("You must do zulrah for Zulrah Scales to charge this item.");
			return 25000;
		case 12924:
			return 65000;
		case 11804:
			return 10000;
		case 11808:
			return 15000;
		case 11806:
			return 25000;
		// DONATOR
		case 1042:
		case 1048:
		case 1038:
		case 1046:
		case 1044:
		case 1040:
			return 11999;
		case 1053:
		case 1055:
		case 1057:
			return 11000;
		case 1419:
			return 40;
		case 4566:
			return 40;
		case 4084:
			return 60;
		case 1037:
			return 149;
		case 6199:
			if (c.myShopId == 77)
				return 20;
			else if (c.myShopId == 78)
				return 30;
			else
				return 1449;
			// VOTE
			/*
			 * case 9920: return 10;
			 */
		case 3057:
		case 3058:
		case 3059:
		case 6654:
		case 6655:
		case 6656:
		case 6180:
		case 6181:
		case 6182:
			return 5;
		/*
		 * case 6666: return 20;
		 */
		case 1050:
			return 80;
		case 13072:
			return 2000;
		case 13073:
			return 2000;
		case 12791:
			return 10;
		case 11941:
			if (c.myShopId == 70)
				return 7500;
			else
				return 5000;
		}
		return 0;
	}

	public int getDonotorPrice(int id) {
		switch (id) {
		
		}
		return 21470000;
	}
	public int getVotePrice(int id) {
switch (id) {
case 1038:
case 1040:
case 1042:
case 1044:
case 1046:
case 1048:
case 1050:
case 1053:
case 1055:
case 1057:
case 11040:
return 30;
case 773:
	return 50;
case 23910:
	return 100;
case 23797:
case 23798:
case 23799:
case 23800:
case 23801:
case 23802:
case 23803:
case 23489:
	return 300;
case 4151:
case 23964:
	return 100;
}
return 21470000;
}


	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0,
				removeSlot));
		ShopValue *= 1.15;
		String ShopAdd = "";
		if (c.myShopId == 196 || c.myShopId == 197 || c.myShopId == 198) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + ShopValue + " tokkul" + ShopAdd);
			return;
		}
		if (c.myShopId == 250) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getDonotorPrice(removeId)
					+ " donator points.");
			return;
		}
		if (c.myShopId == 251 || c.myShopId == 252 || c.myShopId == 253 || c.myShopId == 254 || c.myShopId == 255) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getVotePrice(removeId)
					+ " Vote points.");
			return;
		}

	if (ShopValue >= 1000 && ShopValue < 1000000) {
		ShopAdd = " (" + (ShopValue / 1000) + "K)";
	} else if (ShopValue >= 1000000) {
		ShopAdd = " (" + (ShopValue / 1000000) + " million)";
	}
	c.sendMessage(
			c.getItems().getItemName(removeId) + ": currently costs " + ShopValue + " Coins." + ShopAdd);
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		if(c.myShopId == 250) {
			return;
		}
		if(c.myShopId == 251) {
			return;
		}
		if(c.myShopId == 252) {
			return;
		}
		if(c.myShopId == 253) {
			return;
		}
		if(c.myShopId == 254) {
			return;
		}
		if(c.myShopId == 255) {
			return;
		}
		for (int i : Config.ITEM_SELLABLE) {
			if (i == removeId) {
				c.sendMessage("You can't sell "
						+ c.getItems().getItemName(removeId).toLowerCase()
						+ ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell "
					+ c.getItems().getItemName(removeId).toLowerCase()
					+ " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1,
					removeSlot));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			c.sendMessage(c.getItems().getItemName(removeId)
					+ ": shop will buy for " + ShopValue + " coins" + ShopAdd);
		}
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (c.myShopId == 250 || c.myShopId == 140)
			return false;
		if (c.myShopId == 251 || c.myShopId == 252 || c.myShopId == 253 || c.myShopId == 254 || c.myShopId == 255)
			return false;
		for (int i : Config.ITEM_SELLABLE) {
			if (i == itemID) {
				c.sendMessage("You can't sell "
						+ c.getItems().getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if(c.getRights().isAdministrator() && !Config.ADMIN_CAN_SELL_ITEMS) {
			c.sendMessage("Selling items as an admin has been disabled.");
			return false;
		}
		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage("You can't sell "
							+ c.getItems().getItemName(itemID).toLowerCase()
							+ " to this store.");
					return false;
				}
			}
			if (amount > c.playerItemsN[fromSlot] && (ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isNoted()
					|| ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isStackable())) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID)
					&& !ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isNoted()
					&& !ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isStackable()) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			int TotPrice3 = 0;
			// int Overstock;
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1,
						fromSlot));
				if (c.getItems().freeSlots() > 0
						|| c.getItems().playerHasItem(995)) {
					if (!ItemDefinition.forId(itemID).isNoted()) {
						c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
					} else {
						c.getItems().deleteItem(itemID, fromSlot, 1);
					}
					c.getItems().addItem(995, TotPrice2);
					addShopItem(itemID, 1);
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					break;
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (ItemDefinition.forId(itemID).isNoted()) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}


	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c))
			return false;
		if (!shopSellsItem(itemID) && !(c.myShopId == 140)) {
			c.sendMessage("Stop trying to cheat!");
			return false;
		}
		if (c.myShopId == 140) {
			skillBuy(itemID);
			return false;
		}
		if (itemID != itemID) {
			return false;
		}
		if (!shopSellsItem(itemID)) {
			c.sendMessage("Stop trying to cheat!");
			return false;
		}
		if (amount > 0) {
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;// Tokkul
			if(c.myShopId == 250) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if(c.myShopId == 251) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if(c.myShopId == 252) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if(c.myShopId == 253) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if(c.myShopId == 254) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if(c.myShopId == 255) {
				handleOtherShop(itemID, amount);
				return false;
			}
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0,
						fromSlot));
				Slot = c.getItems().getItemSlot(995);
				Slot1 = c.getItems().getItemSlot(6529);
				if (Slot == -1 && c.myShopId != 196 && c.myShopId != 197 && c.myShopId != 250 && c.myShopId != 198 && c.myShopId != 251&& c.myShopId != 252&& c.myShopId != 253&& c.myShopId != 254&& c.myShopId != 255) {
					c.sendMessage("You don't have enough Coins.");
					break;
				}
				if (Slot1 == -1 && c.myShopId == 196 && c.myShopId == 197 && c.myShopId == 198) {
					c.sendMessage("You don't have enough tokkul.");
					break;
				}
				if(c.myShopId == 250) {
					c.sendMessage("You don't have enough Donator Points.");
					break;
				}
				if(c.myShopId == 251) {
					c.sendMessage("You don't have enough Vote Points.");
					break;
				}
				if(c.myShopId == 252) {
					c.sendMessage("You don't have enough Vote Points.");
					break;
				}
				if(c.myShopId == 253) {
					c.sendMessage("You don't have enough Vote Points.");
					break;
				}
				if(c.myShopId == 254) {
					c.sendMessage("You don't have enough Vote Points.");
					break;
				}
				if(c.myShopId == 255) {
					c.sendMessage("You don't have enough Vote Points.");
					break;
				}
				if (TotPrice2 <= 1) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0,
							fromSlot));
					TotPrice2 *= 1.66;
				}
				if (c.myShopId != 197 & c.myShopId != 196 && c.myShopId != 198 && c.myShopId != 250 && c.myShopId != 251 && c.myShopId != 252&& c.myShopId != 253&& c.myShopId != 254&& c.myShopId != 255) {
					if (c.playerItemsN[Slot] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995),
									TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough Coins.");
						break;
					}
				}
				if (c.myShopId == 196) {
					if (c.playerItemsN[Slot1] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
				if (c.myShopId == 197) {
					if (c.playerItemsN[Slot1] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
				if (c.myShopId == 198) {
					if (c.playerItemsN[Slot1] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
			}


			
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
}
		return false;
	}
	public void handleOtherShop(int itemID, int amount) {
		if (!shopSellsItem(itemID)) {
			c.sendMessage("Stop trying to cheat!");
			return;
		}
		if (amount <= 0) {
			c.sendMessage("You need to buy atleast one or more of this item.");
			return;
		}
		if (!c.getItems().isStackable(itemID)) {
			if (amount > c.getItems().freeSlots()) {
				amount = c.getItems().freeSlots();
			}
			 if (c.myShopId == 250) {
				if (c.DonatorPoints >= getDonotorPrice(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.DonatorPoints -= getDonotorPrice(itemID) * amount;
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
					}
				} else {
					c.sendMessage("You do not have enough donator points to buy this item.");
				}
			}
			 if (c.myShopId == 251) {
				if (c.votePoints >= getVotePrice(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.votePoints -= getVotePrice(itemID) * amount;
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
					}
				} else {
					c.sendMessage("You do not have enough Vote points to buy this item.");
				}
			}
			 if (c.myShopId == 252) {
				if (c.votePoints >= getVotePrice(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.votePoints -= getVotePrice(itemID) * amount;
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
					}
				} else {
					c.sendMessage("You do not have enough Vote points to buy this item.");
				}
			}
			 if (c.myShopId == 253) {
				if (c.votePoints >= getVotePrice(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.votePoints -= getVotePrice(itemID) * amount;
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
					}
				} else {
					c.sendMessage("You do not have enough Vote points to buy this item.");
				}
			}
			if (c.myShopId == 254) {
				if (c.votePoints >= getVotePrice(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.votePoints -= getVotePrice(itemID) * amount;
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
					}
				} else {
					c.sendMessage("You do not have enough Vote points to buy this item.");
				}
			}
						 if (c.myShopId == 255) {
				if (c.votePoints >= getVotePrice(itemID) * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.votePoints -= getVotePrice(itemID) * amount;
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
					}
				} else {
					c.sendMessage("You do not have enough Vote points to buy this item.");
				}
			}

		}
	}
	public void handleOtherShop(int itemID) {
		if (c.myShopId == 17) {
			if (c.magePoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.magePoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.");
			}
		} else if (c.myShopId == 18) {
			if (c.pcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.pcPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 140;
		setupSkillCapes(capes, get99Count());
	}

	/*
	 * public int[][] skillCapes =
	 * {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680
	 * ,4359,2682},{3,2701,4341,2703
	 * },{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
	 * {7,2737,4325,2733
	 * },{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730
	 * },{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727},
	 * {14,2722,4345
	 * ,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361,
	 * 2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};
	 */
	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795,
			9792, 9774, 9771, 9777, 9786, 9810, 9765, 9789, 9948 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < c.playerLevel.length-2; j++) {
			if (Client.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes, int capes2) {
		//c.getPA().sendFrame171(1, 28050);
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 140;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		for (int i = 0; i <= 22; i++) {
			if (Client.getLevelForXP(c.playerXP[i]) < 99)
				continue;
			if (skillCapes[i] == -1)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (Client.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99,000 coins to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}


	public void openVoid() {
	}

	public void buyVoid(int item) {
	}



}
