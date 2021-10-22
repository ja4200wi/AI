package KI_Bohnenspiel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Main {

	static String server = "http://bohnenspiel.informatik.uni-mannheim.de";
	static String name = "mr.bean";

	static int p1 = 0;
	static int p2 = 0;

	public static void main(String[] args) throws Exception {
		//System.out.println(load(server));
		createGame();
		/*String [] openGames = openGames();
		if(openGames.length>0) {
			joinGame(openGames[0]);
		}*/
		//joinGame("2479");
	}

	static void createGame() throws Exception {
		String url = server + "/api/creategame/" + name;
		String gameID = load(url);
		System.out.println("Spiel erstellt. ID: " + gameID);

		url = server + "/api/check/" + gameID + "/" + name;
		while (true) {
			Thread.sleep(3000);
			String state = load(url);
			System.out.print("." + " (" + state + ")");
			if (state.equals("0") || state.equals("-1")) {
				break;
			} else if (state.equals("-2")) {
				System.out.println("time out");
				return;
			}
		}
		play(gameID, 0);
	}

	static String[] openGames() throws Exception {
		String url = server + "/api/opengames";
		String[] opengames = load(url).split(";");
		for (int i = 0; i < opengames.length; i++) {
			System.out.println(opengames[i]);
		} return opengames;
	}

	static void joinGame(String gameID) throws Exception {
		String url = server + "/api/joingame/" + gameID + "/" + name;
		String state = load(url);
		System.out.println("Join-Game-State: " + state);
		if (state.equals("1")) {
			play(gameID, 6);
		} else if (state.equals("0")) {
			System.out.println("error (join game)");
		}
	}

	static void play(String gameID, int offset) throws Exception {
		String checkURL = server + "/api/check/" + gameID + "/" + name;
		String statesMsgURL = server + "/api/statemsg/" + gameID;
		String stateIdURL = server + "/api/state/" + gameID;
		int[] board = { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 }; // position 1-12
		int start, end;
		if (offset == 0) {
			start = 7;
			end = 12;
		} else {
			start = 1;
			end = 6;
		}

		while (true) {
			Thread.sleep(1000);
			int moveState = Integer.parseInt(load(checkURL));
			int stateID = Integer.parseInt(load(stateIdURL));
			if (stateID != 2 && ((start <= moveState && moveState <= end) || moveState == -1)) {
				if (moveState != -1) {
					int selectedField = moveState - 1;
					board = updateBoard(board, selectedField);
					System.out.println("Gegner wï¿½hlte: " + moveState + " /\t" + p1 + " - " + p2);
					System.out.println(printBoard(board) + "\n");
				}
				Knoten k = new Knoten();
				boolean myColor = offset==0 ? true : false; // muss eigtl andersrum sein
				State.setiAmStarting(myColor);
				State state = k.letKIroll(new State(board, myColor, p1, p2));
				System.out.println("State hat Schatzkisten: Blau - " + state.getScoreBlue() + " Rot - " + state.getScoreRed() + " " + state.heuristic007());
				board = updateBoard(board, state.lastMoveOnField);
				move(gameID, state.lastMoveOnField + 1);
			} else if (moveState == -2 || stateID == 2) {
				System.out.println("GAME Finished");
				checkURL = server + "/api/statemsg/" + gameID;
				System.out.println(load(checkURL));
				return;
			} else {
				System.out.println("- " + moveState + "\t\t" + load(statesMsgURL));
			}

		}
	}

	static int[] updateBoard(int[] board, int field) {
		int startField = field;

		int value = board[field];
		board[field] = 0;
		while (value > 0) {
			field = (++field) % 12;
			board[field]++;
			value--;
		}

		if (board[field] == 2 || board[field] == 4 || board[field] == 6) {
			do {
				if (startField < 6) {
					p1 += board[field];
				} else {
					p2 += board[field];
				}
				board[field] = 0;
				field = (field == 0) ? field = 11 : --field;
			} while (board[field] == 2 || board[field] == 4 || board[field] == 6);
		}
		return board;
	}

	static String printBoard(int[] board) {
		String s = "";
		for (int i = 11; i >= 6; i--) {
			if (i != 6) {
				s += board[i] + "; ";
			} else {
				s += board[i];
			}
		}

		s += "\n";
		for (int i = 0; i <= 5; i++) {
			if (i != 5) {
				s += board[i] + "; ";
			} else {
				s += board[i];
			}
		}

		return s;
	}

	static void move(String gameID, int fieldID) throws Exception {
		String url = server + "/api/move/" + gameID + "/" + name + "/" + fieldID;
		System.out.println(load(url));
	}

	static String load(String url) throws Exception {
		URI uri = new URI(url.replace(" ", ""));
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(uri.toURL().openConnection().getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		bufferedReader.close();
		return (sb.toString());
	}
	
}

