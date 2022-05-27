import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) throws Exception {
		int port = 8989;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is working...");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				try (
						PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

					BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
					final String word = in.readLine();
					String result = engine.listToJson(engine.search(word).stream().collect(Collectors.toList()));
					out.println(result);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
