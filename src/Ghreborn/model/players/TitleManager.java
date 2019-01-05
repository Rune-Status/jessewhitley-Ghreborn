package Ghreborn.model.players;

import Ghreborn.Config;

public class TitleManager {
	
	/**
	 * @author Deku
	 * @notes: This is a ROUGH base, sorry.
	 * @param id - titles base id, don't bother touching
	 * @param unlockId
	 * 		-special unlock requirement:  	-1
	 * 		-automatically unlocked: 		id
	 * @param requirement - set a requirement for unlocking the title
	 * 
	 * - If you add a new title in the client, don't forget to register it into the enum.
	 */
	public enum Title {
		//Id, unlockId, requirement
		TITLE0(0, 0, 0),
		TITLE1(1, 1, 250_000),
		TITLE2(2, 2, 300_000),
		TITLE3(3, 3, 0),
		TITLE4(4, 4, 0),
		TITLE5(5, 5, 0),
		TITLE6(6, 6, 0),
		TITLE7(7, 7, 0),
		TITLE8(8, 8, 0),
		TITLE9(9, 9, 0),
		TITLE10(10, 10, 0),
		TITLE11(11, 11, 0),
		TITLE12(12, 12, 0),
		TITLE13(13, 13, 0),
		TITLE14(14, 14, 0),
		TITLE15(15, 15, 0),
		TITLE16(16, 15, 0),
		TITLE17(17, 17, 0),
		TITLE18(18, 18, 0),
		TITLE19(19, 19, 0),
		TITLE20(20, 20, 0),
		TITLE21(21, 21, 0),
		TITLE22(22, 22, 0),
		TITLE23(23, 23, 0),
		TITLE24(24, 24, 0),
		TITLE25(25, 25, 0),
		TITLE26(26, -1, 0),
		TITLE27(27, -1, 0),
		TITLE28(28, -1, 0),
		TITLE29(29, -1, 0),
		TITLE30(30, -1, 0),
		TITLE31(31, -1, 0),
		TITLE32(32, -1, 0),
		TITLE33(33, -1, 0),
		TITLE34(34, -1, 0),
		TITLE35(35, -1, 0),
		TITLE36(36, -1, 0),
		TITLE37(37, -1, 0),
		TITLE38(38, -1, 0),
		TITLE39(39, -1, 0),
		TITLE40(40, -1, 0),
		TITLE41(41, -1, 0),
		TITLE42(42, -1, 0),
		TITLE43(43, -1, 0),
		TITLE44(44, -1, 0),
		TITLE45(45, -1, 0),
		TITLE46(46, -1, 0),
		TITLE47(47, -1, 0),
		TITLE48(48, -1, 0),
		TITLE49(49, -1, 0),
		TITLE50(50, -1, 0),
		TITLE51(51, -1, 0),
		TITLE52(52, -1, 0),
		TITLE53(53, -1, 0),
		TITLE54(54, -1, 0),
		TITLE55(55, -1, 0),
		TITLE56(56, -1, 0),
		TITLE57(57, -1, 0),
		TITLE58(58, -1, 0),
		TITLE59(59, -1, 0),
		TITLE60(60, -1, 0),
		TITLE61(61, -1, 0),
		TITLE62(62, -1, 0),
		TITLE63(63, -1, 0),
		TITLE64(64, -1, 0),
		TITLE65(65, -1, 0),
		TITLE66(66, -1, 0),
		TITLE67(67, -1, 0),
		TITLE68(68, -1, 0),
		TITLE69(69, -1, 0),
		TITLE70(70, -1, 0),
		TITLE71(71, -1, 0),
		TITLE72(72, -1, 0),
		TITLE73(73, -1, 0),
		TITLE74(74, -1, 0),
		TITLE75(75, -1, 0),
		TITLE76(76, -1, 0),
		TITLE77(77, -1, 0),
		TITLE78(78, -1, 0),
		TITLE79(79, -1, 0),
		TITLE80(80, -1, 0),
		TITLE81(81, -1, 0),
		TITLE82(82, -1, 0),
		TITLE83(83, -1, 0),
		TITLE84(84, -1, 0),
		TITLE85(85, -1, 0),
		TITLE86(86, -1, 0),
		TITLE87(87, -1, 0),
		TITLE88(88, -1, 0),
		TITLE89(89, -1, 0),
		TITLE90(90, -1, 0),
		TITLE91(91, -1, 0),
		TITLE92(92, -1, 0),
		TITLE93(93, -1, 0),
		TITLE94(94, -1, 0),
		TITLE95(95, -1, 0),
		TITLE96(96, -1, 0),
		TITLE97(97, -1, 0),
		TITLE98(98, -1, 0),
		TITLE99(99, -1, 0),
		TITLE100(100, -1, 0),
		TITLE101(101, -1, 0),
		TITLE102(102, -1, 0),
		TITLE103(103, -1, 0),
		TITLE104(104, -1, 0),
		TITLE105(105, -1, 0),
		TITLE106(106, -1, 0),
		TITLE107(107, -1, 0),
		TITLE108(108, -1, 0),
		TITLE109(109, -1, 0),
		TITLE110(110, -1, 0),
		TITLE111(111, -1, 0),
		TITLE112(112, -1, 0),
		TITLE113(113, -1, 0),
		TITLE114(114, -1, 0),
		TITLE115(115, -1, 0),
		TITLE116(116, -1, 0),
		TITLE117(117, -1, 0),
		TITLE118(118, -1, 0),
		TITLE119(119, -1, 0),
		TITLE120(120, -1, 0),
		TITLE121(121, -1, 0),
		TITLE122(122, -1, 0),
		TITLE123(123, -1, 0),
		TITLE124(124, -1, 0),
		TITLE125(125, -1, 0),
		TITLE126(126, -1, 0),
		TITLE127(127, -1, 0),
		TITLE128(128, -1, 0),
		TITLE129(129, -1, 0),
		TITLE130(130, -1, 0),
		TITLE131(131, -1, 0),
		TITLE132(132, -1, 0),
		TITLE133(133, -1, 0),
		TITLE134(134, -1, 0),
		TITLE135(135, -1, 0),
		TITLE136(136, -1, 0),
		TITLE137(137, -1, 0),
		TITLE138(138, -1, 0),
		TITLE139(139, -1, 0),
		TITLE140(140, -1, 0),
		TITLE141(141, -1, 0),
		TITLE142(142, -1, 0),
		TITLE143(143, -1, 0),
		TITLE144(144, -1, 0),
		TITLE145(145, -1, 0),
		TITLE146(146, -1, 0),
		TITLE147(147, -1, 0),
		TITLE148(148, -1, 0),
		TITLE149(149, -1, 0),
		TITLE150(150, -1, 0),
		TITLE151(151, -1, 0),
		TITLE152(152, -1, 0),
		TITLE153(153, -1, 0),
		TITLE154(154, -1, 0),
		TITLE155(155, -1, 0),
		TITLE156(156, -1, 0),
		TITLE157(157, -1, 0),
		TITLE158(158, -1, 0),
		TITLE159(159, -1, 0),
		TITLE160(160, -1, 0),
		TITLE161(161, -1, 0),
		TITLE162(162, -1, 0),
		TITLE163(163, -1, 0),
		TITLE164(164, -1, 0),
		TITLE165(165, -1, 0),
		TITLE166(166, -1, 0),
		TITLE167(167, -1, 0),
		TITLE168(168, -1, 0),
		TITLE169(169, -1, 0),
		TITLE170(170, -1, 0),
		TITLE171(171, -1, 0),
		TITLE172(172, -1, 0),
		TITLE173(173, -1, 0),
		TITLE174(174, -1, 0),
		TITLE175(175, -1, 0),
		TITLE176(176, -1, 0),
		TITLE177(177, -1, 0),
		TITLE178(178, -1, 0),
		TITLE179(179, -1, 0),
		TITLE180(180, -1, 0),
		TITLE181(181, -1, 0),
		TITLE182(182, -1, 0),
		TITLE183(183, -1, 0),
		TITLE184(184, -1, 0),
		TITLE185(185, -1, 0),
		TITLE186(186, -1, 0),
		TITLE187(187, -1, 0),
		TITLE188(188, -1, 0),
		TITLE189(189, -1, 0),
		TITLE190(190, -1, 0),
		TITLE191(191, -1, 0),
		TITLE192(192, -1, 0),
		TITLE193(193, -1, 0),
		TITLE194(194, -1, 0),
		TITLE195(195, -1, 0),
		TITLE196(196, -1, 0),
		TITLE197(197, -1, 0),
		TITLE198(198, -1, 0),
		TITLE199(199, -1, 0),
		TITLE200(200, -1, 0),
		TITLE201(201, -1, 0),
		TITLE202(202, -1, 0),
		TITLE203(203, -1, 0),
		TITLE204(204, -1, 0),
		TITLE205(205, -1, 0),
		TITLE206(206, -1, 0),
		TITLE207(207, -1, 0),
		TITLE208(208, -1, 0),
		TITLE209(209, -1, 0),
		TITLE210(210, -1, 0),
		TITLE211(211, -1, 0),
		TITLE212(212, -1, 0),
		TITLE213(213, -1, 0),
		TITLE214(214, -1, 0),
		TITLE215(215, -1, 0),
		TITLE216(216, -1, 0),
		TITLE217(217, -1, 0),
		TITLE218(218, -1, 0),
		TITLE219(219, -1, 0),
		TITLE220(220, -1, 0),
		TITLE221(221, -1, 0),
		TITLE222(222, -1, 0),
		TITLE223(223, -1, 0),
		TITLE224(224, -1, 0),
		TITLE225(225, -1, 0),
		TITLE226(226, -1, 0),
		TITLE227(227, -1, 0),
		TITLE228(228, -1, 0),
		TITLE229(229, -1, 0),
		TITLE230(230, -1, 0),
		TITLE231(231, -1, 0),
		TITLE232(232, -1, 0),
		TITLE233(233, -1, 0),
		TITLE234(234, -1, 0),
		TITLE235(235, -1, 0),
		TITLE236(236, -1, 0),
		TITLE237(237, -1, 0),
		TITLE238(238, -1, 0),
		TITLE239(239, -1, 0),
		TITLE240(240, -1, 0),
		TITLE241(241, -1, 0),
		TITLE242(242, -1, 0),
		TITLE243(243, -1, 0),
		TITLE244(244, -1, 0),
		TITLE245(245, -1, 0),
		TITLE246(246, -1, 0),
		TITLE247(247, -1, 0),
		TITLE248(248, -1, 0),
		TITLE249(249, -1, 0),
		TITLE250(250, -1, 0),
		TITLE251(251, -1, 0),
		TITLE252(252, -1, 0),
		TITLE253(253, -1, 0),
		TITLE254(254, -1, 0),
		TITLE255(255, -1, 0),
		TITLE256(256, -1, 0),
		TITLE257(257, -1, 0),
		TITLE258(258, -1, 0),
		TITLE259(259, -1, 0),
		TITLE260(260, -1, 0),
		TITLE261(261, -1, 0),
		TITLE262(262, -1, 0),
		TITLE263(263, -1, 0),
		TITLE264(264, -1, 0),
		TITLE265(265, -1, 0),
		TITLE266(266, -1, 0),
		TITLE267(267, -1, 0),
		TITLE268(268, -1, 0),
		TITLE269(269, -1, 0),
		TITLE270(270, -1, 0),
		TITLE271(271, -1, 0),
		TITLE272(272, -1, 0),
		TITLE273(273, -1, 0),
		TITLE274(274, -1, 0),
		TITLE275(275, -1, 0),
		TITLE276(276, -1, 0),
		TITLE277(277, -1, 0),
		TITLE278(278, -1, 0),
		TITLE279(279, -1, 0),
		TITLE280(280, -1, 0),
		TITLE281(281, -1, 0),
		TITLE282(282, -1, 0),
		TITLE283(283, -1, 0),
		TITLE284(284, -1, 0),
		TITLE285(285, -1, 0),
		TITLE286(286, -1, 0),
		TITLE287(287, -1, 0),
		TITLE288(288, -1, 0),
		TITLE289(289, -1, 0),
		TITLE290(290, -1, 0),
		TITLE291(291, -1, 0),
		TITLE292(292, -1, 0),
		TITLE293(293, -1, 0),
		TITLE294(294, -1, 0),
		TITLE295(295, -1, 0),
		TITLE296(296, -1, 0),
		TITLE297(297, -1, 0),
		TITLE298(298, -1, 0),
		TITLE299(299, -1, 0),
		TITLE300(300, -1, 0),
		TITLE301(301, -1, 0),
		TITLE302(302, -1, 0),
		TITLE303(303, -1, 0),
		TITLE304(304, -1, 0),
		TITLE305(305, -1, 0),
		TITLE306(306, -1, 0),
		TITLE307(307, -1, 0),
		TITLE308(308, -1, 0),
		TITLE309(309, -1, 0),
		TITLE310(310, -1, 0),
		TITLE311(311, -1, 0),
		TITLE312(312, -1, 0),
		TITLE313(313, -1, 0),
		TITLE314(314, -1, 0),
		TITLE315(315, -1, 0),
		TITLE316(316, -1, 0),
		TITLE317(317, -1, 0),
		TITLE318(318, -1, 0),
		TITLE319(319, -1, 0),
		TITLE320(320, -1, 0),
		TITLE321(321, -1, 0),
		TITLE322(322, -1, 0),
		TITLE323(323, -1, 0),
		TITLE324(324, -1, 0);
		
		public int id;
		public int unlockId;
		public Object requirement;
		
		public static Title forId(int id) {
			for(Title title : Title.values()) {
				if(title.getTitleId() == id) {
					return title;
				}
			}
			return null;
		}
		
		Title(int id, int unlockId, Object requirement) {
			this.id = id;
			this.unlockId = unlockId;
			this.requirement = requirement;
		}
		
		public int getTitleId() {
			return id;
		}
		
		public int getUnlockId() {
			return unlockId;
		}
		
		public Object getRequirement() {
			return requirement;
		}
		
	}
	
	/**
	 * 
	 * @param player - player instance
	 * @param title - title enum
	 */
	public static void unlockTitle(final Client player, final Title title) {
		if(title != null) {
			if(player.unlocked.contains(title))
				return;
			
			player.unlocked.add(title);
			player.sendMessage("You have earned a special title!");
		}
	}
	
	/**
	 * Check if the title is unlocked by default or earned.
	 * @param player - player instance
	 */
	public static void loadUnlockedTitles(final Player player) {
		for(int i = Title.TITLE0.ordinal(); i < Title.TITLE324.ordinal() + 1; i++) {
			Title title = Title.values()[i];
			if(title != null) {
				if(title.getUnlockId() == -1) 
					return;
				
				if(title.getUnlockId() == i) {
					if(player.unlocked.contains(title))
						return;
					
					player.unlocked.add(title);
				}
			}
		}
	}

	/**
	 * Check special arguments for unlocking and setting a title.
	 * @param player - player instance
	 * @param id - action id in the interface, used to get the index of the title id
	 * @param archived - total amount of titles loaded from the client
	 * @return if the parameters have met the arguments unlock the title
	 */
	public static boolean checkUnlockParam(final Client player, int id, int archived) {
		Title title = Title.forId(id - 14007);
		if(title == null)
			return false;
		
		if(!player.unlocked.contains(title)) {
			if (Config.SERVER_DEBUG) {
				System.out.println("title clicked: " + title);
				System.out.println("list: " + player.unlocked);
			}
			/** 
			 * Add requirement objects here 
			 * */
			if (!player.getItems().playerHasItem(995, Integer.parseInt(String.valueOf(title.getRequirement())))) {
				player.sendMessage("<col=128>You don't have enough coins to unlock this title.");
				return true;
			}
			
			/** 
			 * Set the title 
			 * */
			if(player.getTitleId() != id) {
				player.getItems().removeItem(995, Integer.parseInt(String.valueOf(title.getRequirement())));
				player.sendMessage("<col=128>You have unlocked this title.");
			}
			
			/** 
			 * Add to the list.
			 * TODO: save 'unlocked' to the player file
			 *  */
			if(player.unlocked.contains(title))
				return false;
			
			player.unlocked.add(title);
			return true;
		} else {
			if(player.getTitleId() == title.getTitleId()) {
				player.sendMessage("<col=ff0000>You are already using this title!");
				return true;
			} else {
				if(player.getTitleId() != id) {
					if (!player.getItems().playerHasItem(995, Integer.parseInt(String.valueOf(title.getRequirement())))) {
						player.sendMessage("<col=128>You don't have enough coins to apply this title.");
						return true;
					}
					player.setTitleId(id - 14007);
					player.getItems().removeItem(995, Integer.parseInt(String.valueOf(title.getRequirement())));
					return true;
				}
			}
			return true;
		}
	
	}
	
}