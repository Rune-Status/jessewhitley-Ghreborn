package Ghreborn.model.players.packets;

import java.util.Arrays;
import java.util.Optional;

import Ghreborn.clip.Region;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.Location;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.items.Item2;
import Ghreborn.model.objects.Doors;
import Ghreborn.model.players.*;
import Ghreborn.model.players.skills.farming.Farming;
import Ghreborn.model.players.skills.hunter.impling.PuroPuro;
import Ghreborn.util.Misc;
import Ghreborn.world.WorldObject;
import Ghreborn.net.Packet;

public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132;
	public static final int SECOND_CLICK = 252;
	public static final int THIRD_CLICK = 70;
	public static final int FOURTH_CLICK = 234;
	public static final int FIFTH_CLICK = 228;


	public ClickObject() {
	}
	@Override
	public void processPacket(final Client client, Packet packet) {
		client.clickObjectType = client.objectX = client.objectId = client.objectY = 0;
		//client.objectYOffset = //client.objectXOffset = 0;
		client.getPA().resetFollow();
		if (!client.canUsePackets) {
			return;
		}
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			client.setClickX(packet.getLEShortA());
			client.setClickId(packet.getUnsignedShort());
			client.setClickY(packet.getUnsignedShortA());
			client.setClickZ(client.getPosition().getZ());
			client.objectDistance = 1;
				if (!goodPath(client)) {
					return;
				}
				 if (!Region.isWorldObject(client.getClickId(), client.getClickX(), client.getClickY(), client.getClickZ())) {
				 client.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
				 client.sendMessage("incorrect, please contact a staff member to have this resolved.");
				 return;
				 }
				if (Math.abs(client.getX() - client.getClickX()) > 25 || Math.abs(client.getY() - client.getClickY()) > 25) {
					client.resetWalkingQueue();
					break;
				}
				client.setWalkInteractionTask(() -> client.getActions().firstClickObject(client.getClickId(), client.getClickX(), client.getClickY()));
				switch (client.getClickId()) {
				case 25016:
				case 25017:
				case 25018:
				case 25029:
					PuroPuro.magicalWheat(client);
					break;
				case 31990:
					if(client.getVorkath().getVorkathInstance() == null) {
						client.getVorkath().jump();
					}
					break;
				case 9398:// deposit
					client.getPA().sendFrame126("The Bank of Ghreborn - Deposit Box", 7421);
					client.getPA().sendFrame248(4465, 197);// 197 just because you can't
														// see it =\
					client.getItems().resetItems(7423);
					break;
				case 410:
					if (client.playerMagicBook == 0) {
						client.setSidebarInterface(6, 29999);
						client.playerMagicBook = 2;
						client.autocasting = false;
						client.sendMessage("An ancient wisdomin fills your mind.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
					} else {
						client.setSidebarInterface(6, 1151);
						client.playerMagicBook = 0;
						client.autocasting = false;
						client.sendMessage("You feel a drain on your memory.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
					}
					break;

				case 1733:
					client.objectYOffset = 2;
					break;

				case 3044:
					client.objectDistance = 3;
					break;

				case 245:
					client.objectYOffset = -1;
					client.objectDistance = 0;
					break;

				case 272:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 273:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 246:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 4493:
				case 4494:
				case 4495:
				case 4496:
					client.objectDistance = 5;
					break;

				case 6522:
				case 10229:
				case 26709:
				case 2123:
					client.objectDistance = 2;
					break;

				case 8959:
					client.objectYOffset = 1;
					break;

				case 4417:
					if (client.objectX == 2425 && client.objectY == 3074) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.objectYOffset = 2;
					}
					}
					break;
				case 29728:
					if (client.objectX == 3076 && client.objectY == 3463) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3158, 4280, 3);
					}
					}
					break;

				case 29729:
					if (client.objectX == 3159 && client.objectY == 4280) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3076, 3462, 0);
						}
					}
					break;

				case 29671:
					if (client.objectX == 3171 && client.objectY == 4272) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3174, 4273, 2);
					}
					}
					break;

				case 29672:
					if (client.objectX == 3171 && client.objectY == 4273) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3171, 4271, 3);
					}
					}
					break;

				case 29663:
					if (client.objectX == 3157 && client.objectY == 4245) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3160, 4246, 1);
					}
					}
					break;


				case 29667:
					if (client.objectX == 3157 && client.objectY == 4250) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3160, 4249, 1);
					}
					}
					break;

				case 29659:
					if (client.objectX == 3149 && client.objectY == 4250) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3146, 4249, 1);
					}
					}
					break;
				case 29655:
					if (client.objectX == 3149 && client.objectY == 4245) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3146, 4246, 1);
					}
					}
					break;
				case 29668:
					if (client.objectX == 3157 && client.objectY == 4249) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 3)) {
						client.getPA().movePlayer(3157, 4251, 2);
					}
					}
					break;

				case 29664:
					if (client.objectX == 3157 && client.objectY == 4246) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3157, 4246, 2);
					}
					}
					break;

				case 29656:
					if (client.objectX == 3147 && client.objectY == 4246) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3149, 4244, 2);
					}
					}
					break;

				case 29660:
					if (client.objectX == 3147 && client.objectY == 4249) {
						if(client.goodDistance(client.getX(), client.getY(), client.objectX, client.objectY, 1)) {
						client.getPA().movePlayer(3149, 4251, 2);
					}
					}
					break;			
				case 4420:
					if (client.getX() >= 2383 && client.getX() <= 2385) {
						client.objectYOffset = 1;
					} else {
						client.objectYOffset = -2;
					}
					// fall through

				case 409:
				case 6552:
					client.objectDistance = 2;
					break;

				case 2878:
				case 2879:
					client.objectDistance = 3;
					break;

				case 2558:
					client.objectDistance = 0;
					if (client.absX > client.objectX && client.objectX == 3044) {
						client.objectXOffset = 1;
					}
					if (client.absY > client.objectY) {
						client.objectYOffset = 1;
					}
					if (client.absX < client.objectX && client.objectX == 3038) {
						client.objectXOffset = -1;
					}
					break;

				case 9356:
					client.objectDistance = 2;
					break;

				case 1815:
				case 1816:
				case 5959:
				case 5960:
					client.objectDistance = 0;
					break;

				case 9293:
					client.objectDistance = 2;
					break;

				case 4418:
					if (client.objectX == 2374 && client.objectY == 3131) {
						client.objectYOffset = -2;
					} else if (client.objectX == 2369 && client.objectY == 3126) {
						client.objectXOffset = 2;
					} else if (client.objectX == 2380 && client.objectY == 3127) {
						client.objectYOffset = 2;
					} else if (client.objectX == 2369 && client.objectY == 3126) {
						client.objectXOffset = 2;
					} else if (client.objectX == 2374 && client.objectY == 3131) {
						client.objectYOffset = -2;
					}
					break;

				case 9706:
					client.objectDistance = 0;
					client.objectXOffset = 1;
					break;

				case 9707:
					client.objectDistance = 0;
					client.objectYOffset = -1;
					break;

				case 4419:
				case 6707:
					client.objectYOffset = 3;
					break;

				case 6823:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6706:
					client.objectXOffset = 2;
					break;

				case 6772:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6705:
					client.objectYOffset = -1;
					break;

				case 6822:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6704:
					client.objectYOffset = -1;
					break;

				case 6773:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 6703:
					client.objectXOffset = -1;
					break;

				case 6771:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 6702:
					client.objectXOffset = -1;
					break;

				case 6821:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 1276:
				case 1278:
				case 1281:
				case 1306:
				case 1307:
				case 1308:
				case 1309:
					client.objectDistance = 3;
					break;
				case 19691:
					client.objectDistance = 2;
					break;
				default:
					client.objectDistance = 1;
					client.objectXOffset = 0;
					client.objectYOffset = 0;
					break;
				}
			break;

		case SECOND_CLICK:
			client.objectId = packet.getLEShortA(); //getUnsignedShortA
			client.objectY = packet.getLEShort();
			client.objectX = packet.getUnsignedShortA();
			//client.objectDistance = 1;
			client.face(client.objectY, client.objectY);
			if (!goodPath(client)) {
				return;
			}
			 if (!Region.isWorldObject(client.objectId, client.objectX, client.objectY, client.heightLevel)) {
			 client.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
			 client.sendMessage("incorrect, please contact a staff member to have this resolved.");
			 return;
			 }
			 client.setWalkInteractionTask(() -> client.getActions().secondClickObject(client.objectId, client.objectX, client.objectY));
			if (true) {
				break;
			}
			if (PlayerCannon.CannonPart.isObjCannon(client.objectId)) {
				return;
			}
				
			switch (client.objectId) {
			case 6162:
			case 6163:
			case 6164:
			case 6165:
			case 6166:
				//client.objectDistance = 2;
				break;
				
			default:
				//client.objectDistance = 1;
				//client.objectXOffset = 0;
				//client.objectYOffset = 0;
				break;
			}
			break;

		case 70: // 'F'
			client.objectX = packet.getLEShort();
			client.objectY = packet.getUnsignedShort();
			client.objectId = packet.getUnsignedShortA();
			//client.objectDistance = 1;
			//client.face(client.objectX, client.objectY);
			if (!goodPath(client)) {
				return;
			}
			client.setWalkInteractionTask(() -> client.getActions().thirdClickObject(client.objectId, client.objectX, client.objectY));
			if (true) {
				break;
			}

			switch (client.objectId) {
			default:
				//client.objectDistance = 1;
				break;
			}
			break;
			
	case FOURTH_CLICK:
		client.objectX = client.getInStream().readSignedWordBigEndian();
		client.objectY = client.getInStream().readUnsignedWord();
		client.objectId = client.getInStream().readUnsignedWordBigEndianA();
		//client.face(client.objectX, client.objectY);
		//client.objectDistance = 1;
		if (!goodPath(client)) {
			return;
		}
		 if (!Region.isWorldObject(client.objectId, client.objectX, client.objectY, client.heightLevel)) {
		 client.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
		 client.sendMessage("incorrect, please contact a staff member to have this resolved.");
		 return;
		 }
		 client.setWalkInteractionTask(() -> client.getActions().fourthClickObject(client.objectId, client.objectX, client.objectY));
		if (true) {
			break;
		}

		switch (client.objectId) {
                    //case JUNGLEID:... ( might not be needed)
		default:
			//client.objectDistance = 1;
			//client.objectXOffset = 0;
			//client.objectYOffset = 0;
			break;
		}
		break;
	
case FIFTH_CLICK:
	client.objectId = packet.getUnsignedShortA();
	client.objectY = packet.getUnsignedShortA();
	client.objectX = packet.getUnsignedShort();
	//client.face(client.objectX, client.objectY);
	//client.objectDistance = 1;
	if (!goodPath(client)) {
		return;
	}
	 if (!Region.isWorldObject(client.objectId, client.objectX, client.objectY, client.heightLevel)) {
	 client.sendMessage("Warning: The object could not be verified by the server. If you feel this is");
	 client.sendMessage("incorrect, please contact a staff member to have this resolved.");
	 return;
	 }
 client.getActions().fifthClickObject(client.objectId, client.objectX, client.objectY);
	if (true) {
		break;
	}

	switch (client.objectId) {
                //case JUNGLEID:... ( might not be needed)
	default:
		//client.objectDistance = 1;
		//client.objectXOffset = 0;
		//client.objectYOffset = 0;
		break;
	}
	break;
		}
}

	public static boolean goodPath(Client player) {
		if (ignorePath(player.objectId)) {
			return true;
		}

		Position destination = player.getWalkingDestination();
		int x = destination.getX();
		int y = destination.getY();

		if (x > player.objectX) {
			x--;
		} else if (x < player.objectX) {
			x++;
		}

		if (y > player.objectY) {
			y--;
		} else if (y < player.objectY) {
			y++;
		}
		
		boolean notBlocked = true;
		
		Optional<WorldObject> optional = Region.getWorldObject(player.objectId, player.objectX, player.objectY,
				player.heightLevel);
		
		if (optional.isPresent()) {
			WorldObject object = optional.get();
			if (object.type == 0) {
				return Math.max(Math.abs(player.objectX - x), Math.abs(player.objectY - y)) < 2;
			}

			ObjectDef def = ObjectDef.getObjectDef(object.getId());
			if (def == null) {
				return false;
			}

			int farX = player.objectX;
			int farY = player.objectY;
			if (object.getFace() != 1 && object.getFace() != 3) {
				farX += def.tileSizeY;
				farY += def.tileSizeX;
			} else {
				farX += def.tileSizeX;
				farY += def.tileSizeY;
			}
			
			notBlocked = x >= player.objectX && x <= farX && y >= player.objectY && y <= farY; 
		}

		return notBlocked && !Region.isBlockedPath(destination.getX(), destination.getY(), x, y, destination.getZ());
	}

	private static final int[] ignorePathObjects = { 10777 };

	public static boolean ignorePath(int objectId) {
		if (Arrays.binarySearch(ignorePathObjects, objectId) < 0) {
			return false;
		}
		return true;
	}

	public void handleSpecialCase(Client client, int i, int j, int k) {
	}
}
